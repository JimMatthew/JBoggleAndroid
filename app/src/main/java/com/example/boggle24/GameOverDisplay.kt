package com.example.boggle24

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.boggle24.ui.theme.coolblue
import java.util.Locale

@Composable
fun GameOverDisplay(
    numWords: String,
    score: Int,
    highScore: Int,
    longestWord: String,
    totalGames: Int,
    foundWords: String,
    isRotated: Boolean,
    wordsOnBoard: List<String?>
) {
    var showStats by remember { mutableStateOf(false) }
    Column {
        Row(
            horizontalArrangement = Arrangement.Absolute.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = coolblue),
                modifier = Modifier.padding(5.dp),
                onClick = {
                    showStats = !showStats
                }) {
                Text("Show Stats")
            }
        }
        Row(
            horizontalArrangement = Arrangement.Absolute.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            if (showStats) {
                StatCard(highScore, longestWord, totalGames)
            }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Game Over!",
                color = Color.Black, // Adjust text color as needed
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
            Text(
                text = "Words Found: $numWords",
                color = Color.Black, // Adjust text color as needed
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Text(
                text = "Score: $score",
                color = Color.Black, // Adjust text color as needed
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }
        Row(
            horizontalArrangement = Arrangement.Absolute.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
        }
    }

    Row(horizontalArrangement = Arrangement.SpaceEvenly) {
        val size = wordsOnBoard.size
        TextField(
            value = wordsOnBoard.joinToString(separator = "\n").uppercase(Locale.ROOT),
            onValueChange = { },
            label = { Text("$size Words on Board") },
            maxLines = 20,
            textStyle = TextStyle(
                fontSize = 20.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            ),
            //modifier = Modifier.padding(20.dp),
            modifier = Modifier
                .weight(1f)
                .padding(10.dp),
            readOnly = true
        )
        TextField(
            value = foundWords.uppercase(Locale.ROOT),
            onValueChange = { },
            label = { Text("Words Found") },
            maxLines = 20,
            textStyle = TextStyle(
                fontSize = 20.sp,
                color = coolblue,
                fontWeight = FontWeight.Bold
            ),

            modifier = Modifier
                .weight(1f)
                .padding(10.dp),
            readOnly = true
        )
    }

}