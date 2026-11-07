package com.srpteam.srpexplainai

import android.content.Context
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream
import java.io.BufferedInputStream
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors

/**
 * Installs official Sherpa-ONNX VITS/Piper bundles into app-private storage.
 */
class VoiceModelInstaller(context: Context) {
    private val app = context.applicationContext
    private val worker = Executors.newSingleThreadExecutor()

    data class Model(val language: String, val archive: String, val folder: String)

    fun modelFor(language: String): Model? = when (language) {
        "English" -> Model(
            language,
            "https://github.com/k2-fsa/sherpa-onnx/releases/download/tts-models/vits-piper-en_US-libritts_r-medium.tar.bz2",
            "vits-piper-en_US-libritts_r-medium"
        )
        "Hindi" -> Model(
            language,
            "https://github.com/k2-fsa/sherpa-onnx/releases/download/tts-models/vits-piper-hi_IN-rohan-medium.tar.bz2",
            "vits-piper-hi_IN-rohan-medium"
        )
        "বাংলা" -> Model(
            language,
            "https://github.com/k2-fsa/sherpa-onnx/releases/download/tts-models/vits-coqui-bn-custom_female.tar.bz2",
            "vits-coqui-bn-custom_female"
        )
        else -> null
    }

    fun isInstalled(language: String): Boolean {
        val model = modelFor(language) ?: return false
        val dir = File(app.filesDir, "voice-models/${model.folder}")
        return File(dir, "tokens.txt").isFile &&
            dir.listFiles()?.any { it.extension == "onnx" } == true &&
            File(dir, "espeak-ng-data").isDirectory
    }

    fun install(language: String, onResult: (Boolean, String) -> Unit) {
        val model = modelFor(language)
        if (model == null) {
            onResult(false, "A verified Bengali neural model is not available yet.")
            return
        }
        worker.execute {
            val root = File(app.filesDir, "voice-models")
            val temp = File(root, model.folder + ".download")
            val target = File(root, model.folder)
            try {
                root.mkdirs()
                temp.deleteRecursively()
                temp.mkdirs()
                val connection = (URL(model.archive).openConnection() as HttpURLConnection).apply {
                    connectTimeout = 20000
                    readTimeout = 60000
                    instanceFollowRedirects = true
                    setRequestProperty("User-Agent", "SRP-Explain-AI")
                }
                connection.connect()
                check(connection.responseCode in 200..299) { "Model download failed: HTTP ${connection.responseCode}" }
                connection.inputStream.use { raw ->
                    TarArchiveInputStream(BZip2CompressorInputStream(BufferedInputStream(raw))).use { tar ->
                        var entry = tar.nextTarEntry
                        while (entry != null) {
                            val out = File(temp, entry.name).canonicalFile
                            check(out.path.startsWith(temp.canonicalPath + File.separator) || out == temp) {
                                "Invalid model archive path"
                            }
                            if (entry.isDirectory) {
                                out.mkdirs()
                            } else {
                                out.parentFile?.mkdirs()
                                out.outputStream().buffered().use { sink -> tar.copyTo(sink) }
                            }
                            entry = tar.nextTarEntry
                        }
                    }
                }
                // Release archives may contain a top-level model directory.
                // Locate the actual Piper model root instead of assuming files are at archive root.
                val modelRoot = temp.walkTopDown().firstOrNull { it.isFile && it.name == "tokens.txt" }
                    ?.parentFile ?: error("Downloaded model is incomplete (tokens missing)")
                check(modelRoot.listFiles()?.any { it.isFile && it.extension == "onnx" } == true) {
                    "Downloaded model is incomplete (ONNX missing)"
                }
                check(File(modelRoot, "espeak-ng-data").isDirectory) {
                    "Downloaded model is incomplete (phonemizer data missing)"
                }
                target.deleteRecursively()
                check(modelRoot.renameTo(target)) { "Could not install downloaded model" }
                temp.deleteRecursively()
                onResult(true, "${model.language} voice model installed")
            } catch (error: Exception) {
                temp.deleteRecursively()
                onResult(false, error.message ?: "Voice model installation failed")
            }
        }
    }

    fun shutdown() {
        worker.shutdown()
    }
}
