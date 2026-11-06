
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

data class StoryScene(
    val id: Int,
    val title: String,
    val description: String,
    val duration: Int = 5,
    val camera: String = "Cinematic Close-up"
)

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
    var duration by remember { mutableIntStateOf(5) }
    var language by remember { mutableStateOf("বাংলা") }

    var scenes by remember {
        mutableStateOf(listOf<StoryScene>())
    }

    var showStoryboard by remember {
        mutableStateOf(false)
    }

    if (showStoryboard) {

        StoryboardScreen(
            scenes = scenes,
            onScenesChange = { scenes = it },
            onBack = { showStoryboard = false }
        )

    } else {

        HomeScreen(
            script = script,
            onScriptChange = { script = it },
            duration = duration,
            onDurationChange = { duration = it },
            language = language,
            onLanguageChange = { language = it },
            onCreate = {

                val parts = script
                    .split(Regex("[.!?।\\n]+"))
                    .map { it.trim() }
                    .filter { it.isNotBlank() }

                scenes = parts.mapIndexed { index, part ->

                    StoryScene(
                        id = index + 1,
                        title = "Scene ${index + 1}",
                        description = part
                    )
                }

                showStoryboard = true
            }
        )
    }
}

@Composable
fun HomeScreen(
    script: String,
    onScriptChange: (String) -> Unit,
    duration: Int,
    onDurationChange: (Int) -> Unit,
    language: String,
    onLanguageChange: (String) -> Unit,
    onCreate: () -> Unit
) {

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 22.dp)
            .padding(top = 24.dp, bottom = 30.dp),

        verticalArrangement = Arrangement.spacedBy(22.dp)
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

            Spacer(Modifier.width(10.dp))

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
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.weight(1f))

            Text(
                "●",
                color = Purple,
                fontSize = 18.sp
            )
        }

        // HERO

        Column(
            verticalArrangement = Arrangement.spacedBy(7.dp)
        ) {

            Text(
                "Create Something",
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                "Extraordinary.",
                color = Purple,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                "Turn your script into stunning 3D explainer videos.",
                color = Muted,
                fontSize = 15.sp,
                lineHeight = 23.sp
            )
        }

        // SCRIPT TO VIDEO CARD

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(
                containerColor = CardColor
            )
        ) {

            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    "▶",
                    color = Purple,
                    fontSize = 35.sp
                )

                Spacer(Modifier.width(16.dp))

                Column {

                    Text(
                        "Script to Video",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        "Create realistic 3D CGI videos",
                        color = Muted,
                        fontSize = 12.sp
                    )
                }
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
                fontWeight = FontWeight.SemiBold
            )

            OutlinedTextField(
                value = script,
                onValueChange = onScriptChange,
                modifier = Modifier.fillMaxWidth(),

                placeholder = {
                    Text(
                        "Paste your script here...",
                        color = Muted
                    )
                },

                minLines = 4,
                maxLines = 7,

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
        }

        // VIDEO DURATION

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                "Video Duration",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                "Select your preferred video length",
                color = Muted,
                fontSize = 13.sp
            )

            Row(
                modifier = Modifier.horizontalScroll(
                    rememberScrollState()
                ),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                listOf(1, 5, 10, 15, 20, 25, 30).forEach {

                    FilterChip(
                        selected = duration == it,

                        onClick = {
                            onDurationChange(it)
                        },

                        label = {
                            Text("${it}m")
                        },

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
                "Selected: $duration Minutes",
                color = Purple,
                fontSize = 13.sp
            )
        }

        // LANGUAGE

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                "Video Language",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                listOf("বাংলা", "English", "Hindi").forEach {

                    FilterChip(
                        selected = language == it,

                        onClick = {
                            onLanguageChange(it)
                        },

                        label = {
                            Text(it)
                        },

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

        // CREATE STORYBOARD

        Button(
            onClick = {

                if (script.isBlank()) {

                    Toast.makeText(
                        context,
                        "Please enter your script first.",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {

                    onCreate()
                }
            },

            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),

            shape = RoundedCornerShape(16.dp),

            colors = ButtonDefaults.buttonColors(
                containerColor = Purple,
                contentColor = Color.White
            )
        ) {

            Text(
                "Create Storyboard  →",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // PROJECTS

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                "Your Projects",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = CardColor
                )
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(105.dp),

                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        "No projects yet",
                        color = Muted,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun StoryboardScreen(
    scenes: List<StoryScene>,
    onScenesChange: (List<StoryScene>) -> Unit,
    onBack: () -> Unit
) {

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),

        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {

        // TOP BAR

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            TextButton(onClick = onBack) {
                Text("← Back", color = Purple)
            }

            Spacer(Modifier.weight(1f))

            Text(
                "SRP Explain AI",
                color = Purple,
                fontWeight = FontWeight.Bold
            )
        }

        // TITLE

        Text(
            "Your Storyboard",
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            "${scenes.size} scenes created · Review before video generation",
            color = Muted,
            fontSize = 13.sp
        )

        // VISUAL STYLE

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = CardColor
            )
        ) {

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text(
                    "VISUAL STYLE",
                    color = Purple,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    "Realistic Cinematic 3D CGI",
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    "High-detail lighting · Realistic materials · Cinematic camera",
                    color = Muted,
                    fontSize = 12.sp
                )
            }
        }

        // SCENE LIST

        scenes.forEachIndexed { index, scene ->

            var description by remember(scene.id) {
                mutableStateOf(scene.description)
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = CardColor
                )
            ) {

                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            "SCENE ${index + 1}",
                            color = Purple,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.weight(1f))

                        Text(
                            "${scene.duration}s",
                            color = Muted
                        )
                    }

                    // PREVIEW PLACEHOLDER

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(125.dp)
                            .background(
                                Color(0xFF242033),
                                RoundedCornerShape(12.dp)
                            ),

                        contentAlignment = Alignment.Center
                    ) {

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Text(
                                "▶",
                                color = Purple,
                                fontSize = 32.sp
                            )

                            Text(
                                "3D Scene Preview",
                                color = Muted,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Text(
                        scene.title,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        "Scene Description",
                        color = Muted,
                        fontSize = 12.sp
                    )

                    OutlinedTextField(
                        value = description,
                        onValueChange = {
                            description = it
                        },

                        modifier = Modifier.fillMaxWidth(),

                        minLines = 2,
                        maxLines = 5,

                        shape = RoundedCornerShape(12.dp),

                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Purple,
                            unfocusedBorderColor = Color(0xFF383344),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = Purple,
                            focusedContainerColor = Background,
                            unfocusedContainerColor = Background
                        )
                    )

                    Text(
                        "Camera: ${scene.camera}",
                        color = Muted,
                        fontSize = 12.sp
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        OutlinedButton(
                            onClick = {

                                onScenesChange(
                                    scenes.map {
                                        if (it.id == scene.id) {
                                            it.copy(description = description)
                                        } else {
                                            it
                                        }
                                    }
                                )

                                Toast.makeText(
                                    context,
                                    "Scene saved",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },

                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Save")
                        }

                        OutlinedButton(
                            onClick = {

                                onScenesChange(
                                    scenes.filterNot {
                                        it.id == scene.id
                                    }
                                )
                            },

                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Delete")
                        }
                    }
                }
            }
        }

        // CONTINUE BUTTON

        Button(
            onClick = {

                Toast.makeText(
                    context,
                    "AI video generation will be connected in a later step.",
                    Toast.LENGTH_LONG
                ).show()
            },

            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),

            colors = ButtonDefaults.buttonColors(
                containerColor = Purple,
                contentColor = Color.White
            ),

            shape = RoundedCornerShape(16.dp)
        ) {

            Text(
                "Continue to Video Generation →",
                fontWeight = FontWeight.Bold
            )
        }
    }
}
