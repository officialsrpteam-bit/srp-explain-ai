
package com.srpteam.srpexplainai

import android.media.MediaPlayer
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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

    val manager = remember {
        VoiceoverManager(
            context = context,
            onReady = { success, message ->
                ready = success
                status = message
            },
            onComplete = { file ->
                audioFile = file
                status = "Voiceover created successfully"
            },
            onError = { message ->
                status = message
            }
        )
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

            Button(
                onClick = {
                    audioFile = null
                    status = "Generating voiceover..."
                    manager.generate(script, language)
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
                            newPlayer.prepare()
                            newPlayer.start()
                            player = newPlayer

                            status = "Playing voiceover"
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
