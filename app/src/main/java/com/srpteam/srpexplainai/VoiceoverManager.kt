package com.srpteam.srpexplainai

import android.content.Context
import android.os.Handler
import android.os.Looper
import java.io.File

/**
 * App-facing manager for the Sherpa-ONNX offline neural voice engine.
 * It never silently falls back to Android's basic system TTS.
 */
class VoiceoverManager(
    context: Context,
    private val onReady: (Boolean, String) -> Unit,
    private val onComplete: (File) -> Unit,
    private val onError: (String) -> Unit
) {
    private val appContext = context.applicationContext
    private val engine = SherpaVoiceEngine(appContext)
    private val mainHandler = Handler(Looper.getMainLooper())
    private val modelDirectory = File(appContext.filesDir, "voice-models/bn-male")
    @Volatile private var ready = false

    init {
        if (engine.isModelInstalled(modelDirectory)) {
            engine.loadVitsModel(modelDirectory) { success, message ->
                ready = success
                onReady(success, message)
            }
        } else {
            onReady(
                false,
                "Neural runtime connected. A verified Bengali voice model is still required."
            )
        }
    }

    fun generate(text: String, language: String) {
        if (text.isBlank()) {
            onError("Enter your script")
            return
        }
        if (!ready) {
            onError("A verified neural voice model is not installed yet")
            return
        }
        if (language != "বাংলা") {
            onError("This installed model is Bengali only")
            return
        }

        val folder = File(appContext.getExternalFilesDir(null), "SRPVoiceovers")
        if (!folder.exists() && !folder.mkdirs()) {
            onError("Cannot create voice folder")
            return
        }
        val outputFile = File(folder, "neural_voiceover_${System.currentTimeMillis()}.wav")
        engine.generate(text, outputFile) { success, file, message ->
            if (success && file != null) {
                onComplete(file)
            } else {
                onError(message)
            }
        }
    }

    fun shutdown() {
        ready = false
        engine.shutdown()
    }
}
