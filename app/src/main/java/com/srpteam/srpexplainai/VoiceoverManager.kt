package com.srpteam.srpexplainai

import android.content.Context
import android.os.Handler
import android.os.Looper
import java.io.File

class VoiceoverManager(
    context: Context,
    private val onReady: (Boolean, String) -> Unit,
    private val onComplete: (File) -> Unit,
    private val onError: (String) -> Unit
) {
    private val appContext = context.applicationContext
    private val engine = SherpaVoiceEngine(appContext)
    private val installer = VoiceModelInstaller(appContext)
    @Volatile private var readyLanguage: String? = null

    fun isInstalled(language: String) = installer.isInstalled(language)

    fun install(language: String) {
        onReady(false, "Downloading $language neural voice model...")
        installer.install(language) { success, message ->
            if (!success) {
                onReady(false, message)
                return@install
            }
            val model = installer.modelFor(language) ?: return@install
            val directory = File(appContext.filesDir, "voice-models/${model.folder}")
            engine.loadVitsModel(directory, model.modelFile, model.requiresEspeak) { loaded, detail ->
                readyLanguage = if (loaded) language else null
                onReady(loaded, if (loaded) "$message. Ready to generate." else detail)
            }
        }
    }

    fun generate(text: String, language: String, speed: Float = 1.0f, speakerId: Int = 0) {
        if (text.isBlank()) {
            onError("Enter your script")
            return
        }
        if (readyLanguage != language) {
            onError("Download and load the $language voice model first")
            return
        }
        val model = installer.modelFor(language)
        if (model == null) {
            onError("A verified Bengali neural voice model is not available yet")
            return
        }
        val folder = File(appContext.getExternalFilesDir(null), "SRPVoiceovers")
        if (!folder.exists() && !folder.mkdirs()) {
            onError("Cannot create voice folder")
            return
        }
        val outputFile = File(folder, "neural_voiceover_${System.currentTimeMillis()}.wav")
        engine.generate(text, outputFile, speakerId = speakerId, speed = speed) { success, file, message ->
            if (success && file != null) onComplete(file) else onError(message)
        }
    }

    fun shutdown() {
        readyLanguage = null
        installer.shutdown()
        engine.shutdown()
    }
}
