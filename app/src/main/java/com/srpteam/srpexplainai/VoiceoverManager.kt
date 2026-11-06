
package com.srpteam.srpexplainai

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import java.io.File
import java.util.Locale

class VoiceoverManager(
    context: Context,
    private val onReady: (Boolean, String) -> Unit,
    private val onComplete: (File) -> Unit,
    private val onError: (String) -> Unit
) {
    private val appContext = context.applicationContext
    private var tts: TextToSpeech? = null
    private var initialized = false

    init {
        tts = TextToSpeech(appContext) { status ->
            initialized = status == TextToSpeech.SUCCESS
            onReady(initialized, if (initialized) "Voice engine ready"
                else "Voice engine initialization failed")
        }
    }

    fun generate(text: String, language: String) {
        if (!initialized || text.isBlank()) {
            onError(if (text.isBlank()) "Enter your script" else "Voice engine is not ready")
            return
        }

        val locale = when (language) {
            "বাংলা" -> Locale.forLanguageTag("bn-BD")
            "English" -> Locale.US
            "Hindi" -> Locale.forLanguageTag("hi-IN")
            else -> Locale.US
        }

        val languageResult = tts?.setLanguage(locale)
        if (languageResult == TextToSpeech.LANG_MISSING_DATA ||
            languageResult == TextToSpeech.LANG_NOT_SUPPORTED
        ) {
            onError("$language voice is not supported")
            return
        }

        val folder = File(appContext.getExternalFilesDir(null), "SRPVoiceovers")
        if (!folder.exists() && !folder.mkdirs()) {
            onError("Cannot create voice folder")
            return
        }

        val outputFile = File(folder, "voiceover_${System.currentTimeMillis()}.wav")
        val utteranceId = outputFile.name

        tts?.setSpeechRate(1.0f)
        tts?.setPitch(1.0f)

        tts?.setOnUtteranceProgressListener(
            object : UtteranceProgressListener() {
                override fun onStart(id: String) {}

                override fun onDone(id: String) {
                    if (id == utteranceId && outputFile.exists()) {
                        this@VoiceoverManager.onComplete(outputFile)
                    }
                }

                override fun onError(id: String) {
                    this@VoiceoverManager.onError("Voice generation failed")
                }
            }
        )

        val result = tts?.synthesizeToFile(
            text,
            Bundle(),
            outputFile,
            utteranceId
        )

        if (result != TextToSpeech.SUCCESS) {
            onError("Could not start voice generation")
        }
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
        initialized = false
    }
}
