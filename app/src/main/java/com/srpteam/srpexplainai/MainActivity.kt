package com.srpteam.srpexplainai

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
            background = Background,
            surface = CardColor,
            onBackground = Color.White,
            onSurface = Color.White
        )

        setContent {
            MaterialTheme(colorScheme = colors) {
                SRPApp()
            }
        }
    }
}

@Composable
fun SRPApp() {
    var script by remember { mutableStateOf("") }
    var duration by remember { mutableIntStateOf(5) }
    var language by remember { mutableStateOf("বাংলা") }
    var scenes by remember { mutableStateOf(listOf<StoryScene>()) }
    var showStoryboard by remember { mutableStateOf(false) }

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
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 22.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(22.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("▶", color = Purple, fontSize = 35.sp)
            Spacer(Modifier.width(10.dp))
            Column {
                Text("SRP", fontSize = 21.sp, fontWeight = FontWeight.Bold)
                Text("Explain AI", color = Purple, fontSize = 18.sp,
                    fontWeight = FontWeight.Bold)
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Create Something", fontSize = 30.sp,
                fontWeight = FontWeight.Bold)
            Text("Extraordinary.", fontSize = 30.sp,
                fontWeight = FontWeight.Bold, color = Purple)
            Text(
                "Turn your script into stunning 3D explainer videos.",
                color = Muted, fontSize = 15.sp
            )
        }

        Card(
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = CardColor)
        ) {
            Column(Modifier.padding(18.dp)) {
                Text("▶  Script to Video", fontSize = 20.sp,
                    fontWeight = FontWeight.Bold)
                Text("Create realistic 3D CGI videos",
                    color = Muted, fontSize = 13.sp)
            }
        }

        Text("Your Script", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)

        OutlinedTextField(
            value = script,
            onValueChange = onScriptChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Paste your script here...", color = Muted) },
            minLines = 4,
            maxLines = 7,
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Purple,
                unfocusedBorderColor = Color(0xFF383344),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = Purple
            )
        )

        Text("Video Duration", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)

        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(1, 5, 10, 15, 20, 25, 30).forEach {
                FilterChip(
                    selected = duration == it,
                    onClick = { onDurationChange(it) },
                    label = { Text("${it}m") }
                )
            }
        }

        Text("Video Language", fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold)

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("বাংলা", "English", "Hindi").forEach {
                FilterChip(
                    selected = language == it,
                    onClick = { onLanguageChange(it) },
                    label = { Text(it) }
                )
            }
        }

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
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Purple)
        ) {
            Text("Create Storyboard  →", fontSize = 16.sp,
                fontWeight = FontWeight.Bold)
        }

        Text("Your Projects", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = CardColor)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth().height(100.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("No projects yet", color = Muted)
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
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = onBack) {
                Text("← Back", color = Purple)
            }
            Spacer(Modifier.weight(1f))
            Text("SRP Explain AI", color = Purple,
                fontWeight = FontWeight.Bold)
        }

        Text("Your Storyboard", fontSize = 28.sp,
            fontWeight = FontWeight.Bold)

        Text(
            "${scenes.size} scenes created · Review before video generation",
            color = Muted, fontSize = 13.sp
        )

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CardColor)
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("VISUAL STYLE", color = Purple,
                    fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Spacer(Modifier.height(8.dp))
                Text("Realistic Cinematic 3D CGI",
                    fontSize = 17.sp, fontWeight = FontWeight.Bold)
                Text("High-detail lighting · Realistic materials · Cinematic camera",
                    color = Muted, fontSize = 12.sp)
            }
        }

        scenes.forEachIndexed { index, scene ->
            var description by remember(scene.id) {
                mutableStateOf(scene.description)
            }

            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = CardColor)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "SCENE ${index + 1}",
                            color = Purple,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.weight(1f))
                        Text("${scene.duration}s", color = Muted)
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(115.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Card(
                            modifier = Modifier.fillMaxSize(),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFF242033)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("3D Scene Preview", color = Muted)
                            }
                        }
                    }

                    Text(scene.title, fontSize = 18.sp,
                        fontWeight = FontWeight.Bold)

                    Text("Scene Description", color = Muted, fontSize = 12.sp)

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Purple,
                            unfocusedBorderColor = Color(0xFF383344),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )

                    Text(
                        "Camera: ${scene.camera}",
                        color = Muted, fontSize = 12.sp
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(
                            onClick = {
                                onScenesChange(
                                    scenes.map {
                                        if (it.id == scene.id) {
                                            it.copy(description = description)
                                        } else it
                                    }
                                )
                                Toast.makeText(
                                    context, "Scene saved",
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
                                    scenes.filterNot { it.id == scene.id }
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

        Button(
            onClick = {
                Toast.makeText(
                    context,
                    "AI video generation will be connected in a later step.",
                    Toast.LENGTH_LONG
                ).show()
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Purple),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Continue to Video Generation →",
                fontWeight = FontWeight.Bold)
        }
    }
}
