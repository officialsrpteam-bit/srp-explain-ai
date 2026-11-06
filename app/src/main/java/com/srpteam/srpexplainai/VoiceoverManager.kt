
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

            if (initialized) {
                onReady(true, "Voice engine ready")
            } else {
                onReady(false, "Voice engine initialization failed")
            }
        }
    }

    fun generate(text: String, language: String) {
        if (!initialized || tts == null) {
            onError("Voice engine is not ready")
            return
        }

        if (text.isBlank()) {
            onError("Please enter your script")
            return
        }

        val locale = when (language) {
            "বাংলা" -> Locale.forLanguageTag("bn-BD")
            "English" -> Locale.US
            "Hindi" -> Locale.forLanguageTag("hi-IN")
            else -> Locale.US
        }

        val result = tts?.setLanguage(locale)

        if (result == TextToSpeech.LANG_MISSING_DATA ||
            result == TextToSpeech.LANG_NOT_SUPPORTED
        ) {
            onError("$language voice is not supported on this device")
            return
        }

        tts?.setSpeechRate(1.0f)
        tts?.setPitch(1.0f)

        val folder = File(
            appContext.getExternalFilesDir(null),
            "SRPVoiceovers"
        )

        if (!folder.exists()) {
            folder.mkdirs()
        }

        val outputFile = File(
            folder,
            "voiceover_${System.currentTimeMillis()}.wav"
        )

        val utteranceId = outputFile.name

        tts?.setOnUtteranceProgressListener(
            object : UtteranceProgressListener() {

                override fun onStart(id: String) {}

                override fun onDone(id: String) {
                    if (id == utteranceId && outputFile.exists()) {
                        onComplete(outputFile)
                    }
                }

                override fun onError(id: String) {
                    onError("Voice generation failed")
                }
            }
        )

        val params = Bundle()

        val resultCode = tts?.synthesizeToFile(
            text,
            params,
            outputFile,
            utteranceId
        )

        if (resultCode != TextToSpeech.SUCCESS) {
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
