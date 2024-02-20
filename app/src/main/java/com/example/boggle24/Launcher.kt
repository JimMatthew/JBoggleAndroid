package com.example.boggle24

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bogglegame.BoggleStats


@Composable
fun Launcher(
    startGame: () -> Unit,
    stats: BoggleStats
) {
    val showSettings by remember { mutableStateOf(false) }
    var showStats by remember { mutableStateOf(false) }
    var ptime by remember { mutableStateOf("") }
    fun setTime(time: String) {
        ptime = time
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            fontFamily = FontFamily.Monospace,
            text = "JBoggle",
            fontWeight = FontWeight.Bold,
            fontSize = 35.sp,
            modifier = Modifier.padding(20.dp, 150.dp)
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                modifier = Modifier.padding(10.dp),
                onClick = startGame
            ) {
                Text("Start Game")
            }
            Button(
                modifier = Modifier.padding(10.dp),
                onClick = { showStats = !showStats }
            ) {
                Text("Statistics")
            }
        }
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            if (showSettings) {
                SettingsDisplay(::setTime)
            }
        }
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            if (showStats) {
                StatCard(
                    highscore = stats.highScore,
                    longestWord = stats.longestWord,
                    gamesPlayed = stats.totalGamesPlayed
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "2024 James Lindstrom",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(20.dp, 50.dp)
            )
        }
    }
}