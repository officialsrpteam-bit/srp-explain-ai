package com.srpteam.srpexplainai

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import java.io.File
import java.util.Locale
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Uses the Android device's installed Text-to-Speech engine.
 * No separate voice model download is required.
 */
class VoiceoverManager(
    context: Context,
    private val onReady: (Boolean, String) -> Unit,
    private val onComplete: (File) -> Unit,
    private val onError: (String) -> Unit
) {
    private val appContext = context.applicationContext
    @Volatile private var tts: TextToSpeech? = null
    @Volatile private var initialized = false
    @Volatile private var selectedLanguage: String? = null
    private val released = AtomicBoolean(false)

    init {
        tts = TextToSpeech(appContext) { result ->
            initialized = result == TextToSpeech.SUCCESS
            onReady(
                initialized,
                if (initialized) "Device voice engine ready. No model download needed."
                else "Android text-to-speech engine could not initialize."
            )
        }
    }

    // Retain the panel's existing language-selection flow without downloading files.
    fun install(language: String) {
        selectedLanguage = language
        if (initialized) onReady(true, "Ready. Using your phone's installed voice for $language.")
        else onReady(false, "Initializing your phone's voice engine...")
    }

    fun generate(text: String, language: String, speed: Float = 1.0f, speakerId: Int = 0) {
        if (text.isBlank()) {
            onError("Enter your script")
            return
        }
        val engine = tts
        if (!initialized || engine == null || released.get()) {
            onError("Phone voice engine is not ready yet")
            return
        }

        val locale = when (language) {
            "বাংলা" -> Locale.forLanguageTag("bn-BD")
            "English" -> Locale.US
            "Hindi" -> Locale.forLanguageTag("hi-IN")
            else -> Locale.US
        }

        val languageResult = engine.setLanguage(locale)
        if (languageResult == TextToSpeech.LANG_MISSING_DATA ||
            languageResult == TextToSpeech.LANG_NOT_SUPPORTED
        ) {
            onError("Your phone's installed TTS engine does not support $language. Install its voice data in Android TTS settings.")
            return
        }

        val folder = File(appContext.getExternalFilesDir(null), "SRPVoiceovers")
        if (!folder.exists() && !folder.mkdirs()) {
            onError("Cannot create voice folder")
            return
        }

        val outputFile = File(folder, "voiceover_${System.currentTimeMillis()}.wav")
        val utteranceId = outputFile.name
        engine.setSpeechRate(speed.coerceIn(0.75f, 1.25f))
        engine.setPitch(0.92f)

        engine.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(id: String) = Unit

            override fun onDone(id: String) {
                if (id != utteranceId) return
                if (outputFile.isFile && outputFile.length() > 44L) {
                    onComplete(outputFile)
                } else {
                    onError("The phone TTS engine returned an empty audio file.")
                }
            }

            override fun onError(id: String) {
                if (id == utteranceId) onError("Voice generation failed on the phone's TTS engine.")
            }

            override fun onError(id: String, errorCode: Int) {
                if (id == utteranceId) onError("Voice generation failed (TTS error $errorCode).")
            }
        })

        val result = engine.synthesizeToFile(text, Bundle(), outputFile, utteranceId)
        if (result != TextToSpeech.SUCCESS) onError("Could not start voice generation.")
    }

    fun shutdown() {
        if (!released.compareAndSet(false, true)) return
        initialized = false
        tts?.stop()
        tts?.shutdown()
        tts = null
    }
}
