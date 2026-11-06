package com.srpteam.srpexplainai

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.k2fsa.sherpa.onnx.OfflineTts
import com.k2fsa.sherpa.onnx.OfflineTtsConfig
import com.k2fsa.sherpa.onnx.OfflineTtsModelConfig
import com.k2fsa.sherpa.onnx.OfflineTtsVitsModelConfig
import java.io.File
import java.util.concurrent.Executors

/**
 * Offline neural TTS adapter backed by the official Sherpa-ONNX Android runtime.
 *
 * A voice model is deliberately not bundled. The app must install a model whose
 * license, provenance, language and speaker characteristics have been verified.
 */
class SherpaVoiceEngine(context: Context) {
    private val appContext = context.applicationContext
    private val worker = Executors.newSingleThreadExecutor()
    private val mainHandler = Handler(Looper.getMainLooper())
    @Volatile private var tts: OfflineTts? = null

    fun isModelInstalled(modelDirectory: File): Boolean {
        return File(modelDirectory, "model.onnx").isFile &&
            File(modelDirectory, "tokens.txt").isFile
    }

    fun loadVitsModel(
        modelDirectory: File,
        onResult: (Boolean, String) -> Unit
    ) {
        worker.execute {
            try {
                require(isModelInstalled(modelDirectory)) {
                    "Voice model files are missing"
                }
                tts?.release()
                val config = OfflineTtsConfig(
                    model = OfflineTtsModelConfig(
                        vits = OfflineTtsVitsModelConfig(
                            model = File(modelDirectory, "model.onnx").absolutePath,
                            tokens = File(modelDirectory, "tokens.txt").absolutePath,
                            dataDir = File(modelDirectory, "espeak-ng-data").absolutePath
                        ),
                        numThreads = 2,
                        provider = "cpu"
                    )
                )
                tts = OfflineTts(config = config)
                mainHandler.post { onResult(true, "Neural voice engine ready") }
            } catch (error: Exception) {
                tts?.release()
                tts = null
                mainHandler.post {
                    onResult(false, error.message ?: "Could not load neural voice model")
                }
            }
        }
    }

    fun generate(
        text: String,
        outputFile: File,
        speakerId: Int = 0,
        speed: Float = 1.0f,
        onResult: (Boolean, File?, String) -> Unit
    ) {
        if (text.isBlank()) {
            onResult(false, null, "Enter your script")
            return
        }
        worker.execute {
            try {
                val engine = requireNotNull(tts) { "Install and load a voice model first" }
                outputFile.parentFile?.mkdirs()
                val audio = engine.generate(text = text, sid = speakerId, speed = speed)
                check(audio.samples.isNotEmpty()) { "The voice model returned empty audio" }
                check(audio.save(outputFile.absolutePath)) { "Could not save generated WAV audio" }
                mainHandler.post { onResult(true, outputFile, "Neural voiceover created") }
            } catch (error: Exception) {
                mainHandler.post {
                    onResult(false, null, error.message ?: "Neural voice generation failed")
                }
            }
        }
    }

    fun shutdown() {
        worker.execute {
            tts?.release()
            tts = null
        }
        worker.shutdown()
    }
}
