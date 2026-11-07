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

    fun isModelInstalled(modelDirectory: File, modelFileName: String, requiresEspeak: Boolean): Boolean {
        return File(modelDirectory, modelFileName).isFile && File(modelDirectory, "tokens.txt").isFile && (!requiresEspeak || File(modelDirectory, "espeak-ng-data").isDirectory)
    }

    fun loadVitsModel(
        modelDirectory: File,
        modelFileName: String,
        requiresEspeak: Boolean,
        onResult: (Boolean, String) -> Unit
    ) {
        worker.execute {
            try {
                require(isModelInstalled(modelDirectory, modelFileName, requiresEspeak)) { "Voice model files are incomplete for $modelFileName" }
                val onnxModel = File(modelDirectory, modelFileName)
                tts?.release()
                val config = OfflineTtsConfig(
                    model = OfflineTtsModelConfig(
                        vits = OfflineTtsVitsModelConfig(
                            model = onnxModel.absolutePath,
                            tokens = File(modelDirectory, "tokens.txt").absolutePath,
                            dataDir = if (requiresEspeak) File(modelDirectory, "espeak-ng-data").absolutePath else ""
                        ),
                        numThreads = 2,
                        debug = true,
                        provider = "cpu"
                    )
                )
                val loadedEngine = OfflineTts(config = config)
                val sampleRate = loadedEngine.sampleRate()
                require(sampleRate > 0) { "Voice model reported an invalid sample rate" }
                tts = loadedEngine
                mainHandler.post { onResult(true, "Neural voice engine ready (${loadedEngine.numSpeakers()} speaker(s), ${sampleRate} Hz)") }
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
                // Some native builds may return a zero sampleRate in GeneratedAudio
                // even though the loaded OfflineTts reports a valid output rate.
                // Use the engine's authoritative rate as a safe fallback.
                val outputSampleRate = audio.sampleRate.takeIf { it > 0 } ?: engine.sampleRate()
                check(outputSampleRate > 0) {
                    "TTS engine has no valid output sample rate (audio=${audio.sampleRate}, engine=${engine.sampleRate()})"
                }
                check(audio.samples.size > 800) { "The voice model returned empty or too-short audio (${audio.samples.size} samples)" }
                check(audio.samples.any { kotlin.math.abs(it) > 0.00001f }) { "The voice model returned silent audio" }
                val audioToSave = if (audio.sampleRate == outputSampleRate) audio
                    else com.k2fsa.sherpa.onnx.GeneratedAudio(audio.samples, outputSampleRate)
                check(audioToSave.save(outputFile.absolutePath)) { "Could not save generated WAV audio" }
                check(outputFile.isFile && outputFile.length() > 44L) { "Generated WAV file is missing or empty" }
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
