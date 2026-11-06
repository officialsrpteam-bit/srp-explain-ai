# SRP Explain AI — Voice Engine Implementation

## Goal

Build a high-quality, on-device neural narration engine for Bengali, English, and Hindi, with selectable male and female voices where a suitable licensed model is available. The target style is polished cinematic explainer narration; it must not imitate a named creator or real speaker.

## Runtime

Use the official Sherpa-ONNX Android runtime. It supports offline TTS on Android and exposes a Kotlin `OfflineTts` API. The native Android libraries must match the app ABI; do not add an unverified third-party wrapper or guess a Maven coordinate.

Official references:
- https://k2-fsa.github.io/sherpa/onnx/android/build-sherpa-onnx.html
- https://github.com/k2-fsa/sherpa-onnx/blob/master/sherpa-onnx/kotlin-api/Tts.kt
- https://github.com/k2-fsa/sherpa-onnx/tree/master/android/SherpaOnnxTts

## Model candidates and compatibility

### Bengali

Candidate: `demoqqq/banglatts-eleven-new`
- Single-speaker Bengali VITS ONNX model, about 114 MB, 22.05 kHz.
- Model card describes a male Bangladeshi Bengali news-presenter voice.
- It is a single-speaker model, so it cannot provide a genuine female voice or speaker switching.
- The model card notes that the voice is a real person's voice; use must be responsible and must not impersonate that person.
- Verify the exact model-file license and attribution before shipping or distributing it.

### Bengali / Hindi expressive multi-speaker research candidate

Candidate: `MatiasLin/sherpa-onnx-vits-rasa-13`
- CC-BY-4.0 model card; multilingual Indic VITS export with 1024 speakers and 32 emotion IDs.
- Includes Bengali and Hindi; the underlying Rasa data includes male and female speakers.
- **Not currently compatible with stock Sherpa-ONNX:** its ONNX graph requires an additional `emotion_id` input. The model card explicitly says a Sherpa build with emotion-input support is required.
- Do not integrate until the app can use a runtime build that supports that input and speaker/language IDs are verified from the model's documentation.

### English and Hindi

Select separate Sherpa-compatible neural models only after checking sample audio, speaker gender, language/accent, model license, model size, and Android compatibility. Do not assume a model supports both genders just because its language is supported.

## App architecture

1. `VoiceModelCatalog`: explicit metadata for each verified model (language, voice label, model/token filenames, download URL, license/attribution, expected size and checksum).
2. `VoiceModelInstaller`: download selected model files with progress, cancellation, temporary files, checksum verification, and atomic installation into app-private storage.
3. `SherpaVoiceEngine`: load only an installed, verified model; synthesize on a background dispatcher; save WAV output.
4. `VoiceoverPanel`: expose only voices that are actually available for the selected language; show download/ready/generating/error states.
5. Audio preview and export remain separate from video generation.

## Device constraints

The app targets Android 8.0+ and should start with ARM64 support. Models are downloaded on demand rather than bundled into the APK. Check available storage before download and avoid loading multiple large models into memory simultaneously.

## Acceptance tests

- Clean install does not crash when no model is installed.
- Model download can be paused/cancelled or safely retried.
- SHA-256 is verified before installation.
- Each advertised voice generates a non-empty WAV file with the expected sample rate.
- Bengali, English, and Hindi are tested with native-script sample sentences.
- Voice selection changes the actual model/speaker, not merely pitch.
- The engine releases native resources when no longer in use.
- Test on a physical ARM64 Android phone before claiming production readiness.

## Current repository status

The existing app still uses Android system `TextToSpeech`. Sherpa-ONNX has not yet been integrated, and no neural voice model is currently shipped or downloaded by the app. This document records verified integration constraints so the implementation does not expose non-working gender or language options.
