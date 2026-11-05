
package com.srpteam.srpexplainai

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Background = Color(0xFF08080D)
private val CardColor = Color(0xFF15141E)
private val Purple = Color(0xFF9B35F5)
private val Muted = Color(0xFF9995AA)

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val colors = darkColorScheme(
                primary = Purple,
                background = Background,
                surface = CardColor,
                onBackground = Color.White,
                onSurface = Color.White
            )

            MaterialTheme(colorScheme = colors) {
                SRPHomeScreen()
            }
        }
    }
}

@Composable
fun SRPHomeScreen() {

    val context = LocalContext.current

    var script by remember { mutableStateOf("") }
    var selectedDuration by remember { mutableIntStateOf(5) }
    var selectedLanguage by remember { mutableStateOf("বাংলা") }
    var selectedTab by remember { mutableIntStateOf(0) }

    val durations = listOf(1, 5, 10, 15, 20, 25, 30)
    val languages = listOf("বাংলা", "English", "Hindi")

    Scaffold(
        containerColor = Background,

        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF111019)
            ) {
                val tabs = listOf("Home", "Projects", "Settings")
                val icons = listOf("⌂", "▣", "⚙")

                tabs.forEachIndexed { index, tab ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        icon = {
                            Text(
                                icons[index],
                                fontSize = 23.sp
                            )
                        },
                        label = {
                            Text(tab)
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Purple,
                            selectedTextColor = Purple,
                            unselectedIconColor = Muted,
                            unselectedTextColor = Muted,
                            indicatorColor = Color(0xFF29143A)
                        )
                    )
                }
            }
        }

    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 22.dp)
                .padding(top = 20.dp, bottom = 24.dp),

            verticalArrangement = Arrangement.spacedBy(24.dp)
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
                    color = Muted,
                    fontSize = 25.sp
                )
            }

            // HERO SECTION

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Text(
                    "Create Something",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    "Extraordinary.",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Purple
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
                        fontSize = 36.sp
                    )

                    Spacer(Modifier.width(18.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            "Script to Video",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            "Create realistic 3D CGI videos",
                            color = Muted,
                            fontSize = 12.sp
                        )
                    }

                    Text(
                        "→",
                        color = Purple,
                        fontSize = 30.sp
                    )
                }
            }

            // SCRIPT INPUT

            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                Text(
                    "Your Script",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )

                OutlinedTextField(
                    value = script,
                    onValueChange = { script = it },
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

                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    ),

                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Purple,
                        unfocusedBorderColor = Color(0xFF383344),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Purple
                    )
                )
            }

            // VIDEO DURATION

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Text(
                    "Video Duration",
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

                    horizontalArrangement = Arrangement.spacedBy(9.dp)
                ) {

                    durations.forEach { duration ->

                        FilterChip(
                            selected = selectedDuration == duration,

                            onClick = {
                                selectedDuration = duration
                            },

                            label = {
                                Text("${duration}m")
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
                    "Selected: $selectedDuration Minutes",
                    color = Purple,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // VIDEO LANGUAGE

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Text(
                    "Video Language",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Row(
                    modifier = Modifier.horizontalScroll(
                        rememberScrollState()
                    ),

                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    languages.forEach { language ->

                        FilterChip(
                            selected = selectedLanguage == language,

                            onClick = {
                                selectedLanguage = language
                            },

                            label = {
                                Text(language)
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

            // CREATE STORYBOARD BUTTON

            Button(
                onClick = {

                    if (script.isBlank()) {

                        Toast.makeText(
                            context,
                            "Please enter your script first.",
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {

                        Toast.makeText(
                            context,
                            "Storyboard generation will be connected next.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),

                shape = RoundedCornerShape(16.dp),

                colors = ButtonDefaults.buttonColors(
                    containerColor = Purple
                )
            ) {

                Text(
                    "Create Storyboard  →",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // YOUR PROJECTS

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        "Your Projects",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.weight(1f))

                    Text(
                        "See All →",
                        color = Purple,
                        fontSize = 13.sp
                    )
                }

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
}
