
package com.srpteam.srpexplainai

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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

private val Background = Color(0xFF08080D)
private val CardColor = Color(0xFF15141E)
private val Purple = Color(0xFF9B35F5)
private val Muted = Color(0xFF9995AA)

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
    var duration by remember { mutableIntStateOf(5) }
    var isGenerating by remember { mutableStateOf(false) }

    if (isGenerating) {
        GenerationScreen(
            onBack = { isGenerating = false }
        )
    } else {
        HomeScreen(
            script = script,
            onScriptChange = { script = it },
            language = language,
            onLanguageChange = { language = it },
            duration = duration,
            onDurationChange = { duration = it },
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
    duration: Int,
    onDurationChange: (Int) -> Unit,
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

        // MAIN FEATURE CARD

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

        // DURATION

        Column(
            verticalArrangement = Arrangement.spacedBy(11.dp)
        ) {
            Text(
                "Target Video Duration",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                "This is a target length. Final timing will follow the generated voiceover.",
                color = Muted,
                fontSize = 12.sp,
                lineHeight = 18.sp
            )

            Row(
                modifier = Modifier.horizontalScroll(
                    rememberScrollState()
                ),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(1, 5, 10, 15, 20, 25, 30).forEach { item ->
                    FilterChip(
                        selected = duration == item,
                        onClick = { onDurationChange(item) },
                        label = { Text("${item}m") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Purple,
                            selectedLabelColor = Color.White,
                            containerColor = CardColor,
                            labelColor = Muted
                        )
                    )
                }
            }

            Text(
                "Selected: $duration minutes",
                color = Purple,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
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
