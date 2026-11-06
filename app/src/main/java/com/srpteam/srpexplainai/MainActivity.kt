package com.srpteam.srpexplainai

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Shared colors — VoiceoverPanel.kt also uses these
val Background = Color(0xFF08080D)
val CardColor = Color(0xFF15141E)
val Purple = Color(0xFF9B35F5)
val Muted = Color(0xFF9995AA)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val colors = darkColorScheme(
            primary = Purple,
            onPrimary = Color.White,
            background = Background,
            surface = CardColor,
            onBackground = Color.White,
            onSurface = Color.White,
            secondary = Purple
        )

        setContent {
            MaterialTheme(colorScheme = colors) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Background,
                    contentColor = Color.White
                ) {
                    SRPApp()
                }
            }
        }
    }
}

@Composable
fun SRPApp() {
    var script by remember { mutableStateOf("") }
    var language by remember { mutableStateOf("বাংলা") }
    var aspectRatio by remember { mutableStateOf("9:16") }
    var isGenerating by remember { mutableStateOf(false) }

    if (isGenerating) {
        GenerationScreen(
            aspectRatio = aspectRatio,
            onBack = { isGenerating = false }
        )
    } else {
        HomeScreen(
            script = script,
            onScriptChange = { script = it },
            language = language,
            onLanguageChange = { language = it },
            aspectRatio = aspectRatio,
            onAspectRatioChange = { aspectRatio = it },
            onGenerate = { isGenerating = true }
        )
    }
}

@Composable
fun HomeScreen(
    script: String,
    onScriptChange: (String) -> Unit,
    language: String,
    onLanguageChange: (String) -> Unit,
    aspectRatio: String,
    onAspectRatioChange: (String) -> Unit,
    onGenerate: () -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 22.dp)
            .padding(top = 26.dp, bottom = 35.dp),
        verticalArrangement = Arrangement.spacedBy(23.dp)
    ) {

        // HEADER

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "▶",
                color = Purple,
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.width(12.dp))

            Column {
                Text(
                    "SRP",
                    color = Color.White,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    "Explain AI",
                    color = Purple,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.weight(1f))

            Text(
                "AI VIDEO STUDIO",
                color = Muted,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // HERO

        Column(
            verticalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            Text(
                "Bring Your Ideas",
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                "To Life.",
                color = Purple,
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                "Transform your script into realistic cinematic 3D CGI explainer videos.",
                color = Muted,
                fontSize = 14.sp,
                lineHeight = 22.sp
            )
        }

        // MAIN FEATURE

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(
                containerColor = CardColor
            )
        ) {
            Column(
                modifier = Modifier.padding(19.dp),
                verticalArrangement = Arrangement.spacedBy(9.dp)
            ) {
                Text(
                    "✦  SCRIPT TO CINEMATIC VIDEO",
                    color = Purple,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    "Voice First. Visuals Second.",
                    color = Color.White,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    "First generate the voiceover. Then create perfectly timed 3D visuals that follow its narration.",
                    color = Muted,
                    fontSize = 13.sp,
                    lineHeight = 20.sp
                )
            }
        }

        // SCRIPT INPUT

        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                "Your Script",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                "Write or paste your explanation script below.",
                color = Muted,
                fontSize = 12.sp
            )

            OutlinedTextField(
                value = script,
                onValueChange = onScriptChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 155.dp),
                placeholder = {
                    Text(
                        "Paste your script here...",
                        color = Muted
                    )
                },
                minLines = 5,
                maxLines = 9,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Purple,
                    unfocusedBorderColor = Color(0xFF383344),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Purple,
                    focusedContainerColor = CardColor,
                    unfocusedContainerColor = CardColor
                )
            )

            Text(
                "${script.length} characters",
                color = Muted,
                fontSize = 11.sp,
                modifier = Modifier.align(Alignment.End)
            )
        }

        // LANGUAGE

        Column(
            verticalArrangement = Arrangement.spacedBy(11.dp)
        ) {
            Text(
                "Voiceover Language",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                "Choose the language for your narration.",
                color = Muted,
                fontSize = 12.sp
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("বাংলা", "English", "Hindi").forEach { item ->
                    FilterChip(
                        selected = language == item,
                        onClick = { onLanguageChange(item) },
                        label = { Text(item) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Purple,
                            selectedLabelColor = Color.White,
                            containerColor = CardColor,
                            labelColor = Muted
                        )
                    )
                }
            }
        }

        // VOICEOVER GENERATOR

        VoiceoverPanel(
            script = script,
            language = language
        )

        // ASPECT RATIO

        Column(
            verticalArrangement = Arrangement.spacedBy(11.dp)
        ) {
            Text(
                "Video Aspect Ratio",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                "Choose the format for your final video.",
                color = Muted,
                fontSize = 12.sp
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                // LANDSCAPE 16:9

                Card(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onAspectRatioChange("16:9")
                    },
                    shape = RoundedCornerShape(16.dp),
                    border = if (aspectRatio == "16:9") {
                        androidx.compose.foundation.BorderStroke(
                            2.dp,
                            Purple
                        )
                    } else {
                        null
                    },
                    colors = CardDefaults.cardColors(
                        containerColor = if (aspectRatio == "16:9") {
                            Purple.copy(alpha = 0.13f)
                        } else {
                            CardColor
                        }
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(9.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(16f / 9f)
                                .background(
                                    Purple.copy(alpha = 0.20f),
                                    RoundedCornerShape(8.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "16:9",
                                color = Color.White,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Text(
                            "Landscape",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            "YouTube Long Videos",
                            color = Muted,
                            fontSize = 10.sp
                        )

                        if (aspectRatio == "16:9") {
                            Text(
                                "✓ Selected",
                                color = Purple,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // PORTRAIT 9:16

                Card(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onAspectRatioChange("9:16")
                    },
                    shape = RoundedCornerShape(16.dp),
                    border = if (aspectRatio == "9:16") {
                        androidx.compose.foundation.BorderStroke(
                            2.dp,
                            Purple
                        )
                    } else {
                        null
                    },
                    colors = CardDefaults.cardColors(
                        containerColor = if (aspectRatio == "9:16") {
                            Purple.copy(alpha = 0.13f)
                        } else {
                            CardColor
                        }
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(9.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .height(85.dp)
                                .aspectRatio(9f / 16f)
                                .background(
                                    Purple.copy(alpha = 0.20f),
                                    RoundedCornerShape(8.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "9:16",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Text(
                            "Portrait",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            "Shorts • Reels • TikTok",
                            color = Muted,
                            fontSize = 10.sp
                        )

                        if (aspectRatio == "9:16") {
                            Text(
                                "✓ Selected",
                                color = Purple,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // VIDEO LENGTH INFORMATION

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = CardColor
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                Text(
                    "AUTOMATIC VIDEO DURATION",
                    color = Purple,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    "Your voiceover decides the video length.",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    "The final video duration will automatically match the generated narration. No manual duration selection needed.",
                    color = Muted,
                    fontSize = 12.sp,
                    lineHeight = 19.sp
                )
            }
        }

        // VISUAL STYLE

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = CardColor
            )
        ) {
            Column(
                modifier = Modifier.padding(17.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    "VISUAL STYLE",
                    color = Purple,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    "Realistic Cinematic 3D CGI",
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    "Realistic materials • Detailed environments • Cinematic lighting • Dynamic camera movement",
                    color = Muted,
                    fontSize = 12.sp,
                    lineHeight = 19.sp
                )
            }
        }

        // GENERATE BUTTON

        Button(
            onClick = {
                if (script.isBlank()) {
                    Toast.makeText(
                        context,
                        "Please enter your script first.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    onGenerate()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(59.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Purple,
                contentColor = Color.White
            )
        ) {
            Text(
                "Generate Video  →",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Text(
            "Powered by SRP Team",
            color = Muted,
            fontSize = 11.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun GenerationScreen(
    aspectRatio: String,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(rememberScrollState())
            .padding(22.dp),
        verticalArrangement = Arrangement.spacedBy(22.dp)
    ) {

        TextButton(onClick = onBack) {
            Text(
                "← Back to Home",
                color = Purple
            )
        }

        Text(
            "Video Generation",
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            "Your script is ready for the voice-first generation pipeline.",
            color = Muted,
            fontSize = 14.sp,
            lineHeight = 21.sp
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = CardColor
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(13.dp)
            ) {
                Text(
                    "GENERATION SETTINGS",
                    color = Purple,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    "Selected Format: $aspectRatio",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                HorizontalDivider(
                    color = Color(0xFF383344)
                )

                Text(
                    "GENERATION STATUS",
                    color = Purple,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    "Waiting for AI backend",
                    color = Color.White,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    "The generation service has not been connected yet. No video is being generated at this stage.",
                    color = Muted,
                    fontSize = 13.sp,
                    lineHeight = 20.sp
                )

                HorizontalDivider(
                    color = Color(0xFF383344)
                )

                GenerationStep(
                    number = "01",
                    title = "Voiceover Generation",
                    description = "Create narration from your script."
                )

                GenerationStep(
                    number = "02",
                    title = "Voiceover Analysis",
                    description = "Extract meaning, pauses and timestamps."
                )

                GenerationStep(
                    number = "03",
                    title = "3D CGI Generation",
                    description = "Generate visuals matched to narration timing."
                )

                GenerationStep(
                    number = "04",
                    title = "Final Video Assembly",
                    description = "Synchronize voice, visuals and sound."
                )
            }
        }

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Purple
            )
        ) {
            Text("Back to Script")
        }
    }
}

@Composable
fun GenerationStep(
    number: String,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            number,
            color = Purple,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.width(14.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                title,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                description,
                color = Muted,
                fontSize = 12.sp,
                lineHeight = 18.sp
            )

            Text(
                "Not connected",
                color = Color(0xFFB58AD8),
                fontSize = 10.sp
            )
        }
    }
}
