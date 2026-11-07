
package com.srpteam.srpexplainai

import android.media.MediaPlayer
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import android.os.Handler
import android.os.Looper
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.io.File

@Composable
fun VoiceoverPanel(script: String, language: String) {
    val context = LocalContext.current

    var ready by remember { mutableStateOf(false) }
    var status by remember { mutableStateOf("Initializing voice engine...") }
    var audioFile by remember { mutableStateOf<File?>(null) }
    var player by remember { mutableStateOf<MediaPlayer?>(null) }
    var speed by remember { mutableFloatStateOf(1.0f) }
    var gender by remember { mutableStateOf("Male") }

    val manager = remember {
        VoiceoverManager(
            context = context,
            onReady = { success, message ->
                Handler(Looper.getMainLooper()).post {
                    ready = success
                    status = message
                }
            },
            onComplete = { file ->
                Handler(Looper.getMainLooper()).post {
                    audioFile = file
                    status = "Voiceover created successfully"
                }
            },
            onError = { message ->
                Handler(Looper.getMainLooper()).post { status = message }
            }
        )
    }

    LaunchedEffect(manager, language) {
        ready = false
        audioFile = null
        manager.install(language)
    }

    DisposableEffect(manager) {
        onDispose {
            manager.shutdown()
            player?.release()
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = CardColor
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "VOICEOVER GENERATOR",
                color = Purple,
                style = MaterialTheme.typography.labelLarge
            )

            Text(
                status,
                color = Muted
            )

            Text("Voice type", color = androidx.compose.ui.graphics.Color.White)
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                FilterChip(
                    selected = gender == "Male",
                    onClick = { gender = "Male" },
                    label = { Text("Male") }
                )
                FilterChip(
                    selected = gender == "Female",
                    onClick = { gender = "Female" },
                    label = { Text("Female") }
                )
            }

            Text("Narration speed: " + String.format("%.1f", speed) + "x", color = androidx.compose.ui.graphics.Color.White)
            Slider(value = speed, onValueChange = { speed = it }, valueRange = 0.8f..1.2f, steps = 3)

            Button(
                onClick = {
                    audioFile = null
                    status = "Generating voiceover..."
                    manager.generate(script, language, speed = speed, gender = gender)
                },
                enabled = ready && script.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Generate Voiceover")
            }

            if (audioFile != null) {
                Button(
                    onClick = {
                        try {
                            player?.release()

                            val newPlayer = MediaPlayer()
                            newPlayer.setDataSource(audioFile!!.absolutePath)
                            newPlayer.setOnCompletionListener {
                                it.release()
                                player = null
                            }
                            newPlayer.setOnPreparedListener { prepared ->
                                prepared.start()
                                status = "Playing voiceover"
                            }
                            newPlayer.setOnErrorListener { failed, _, _ ->
                                failed.release()
                                player = null
                                status = "Could not play audio"
                                true
                            }
                            player = newPlayer
                            newPlayer.prepareAsync()
                        } catch (e: Exception) {
                            status = "Could not play audio"
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("▶ Play Voiceover")
                }

                Text(
                    "Saved inside SRP Explain AI app storage.",
                    color = Muted,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
