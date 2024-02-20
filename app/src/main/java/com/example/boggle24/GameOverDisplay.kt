package com.example.boggle24

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

@Composable
fun GameOverDisplay(
    numWords: String,
    score: Int,
    highScore: Int,
    longestWord: String,
    totalGames: Int,
    foundWords: String,
    wordsOnBoard: List<String>
) {
    var showStats by remember { mutableStateOf(false) }
    Row {
        Button(
            modifier = Modifier.padding(5.dp),
            onClick = {
                showStats = !showStats
            }) {
            Text("Show Stats")
        }
    }
    Row {
        if (showStats) {
            StatCard(highScore, longestWord, totalGames)
        }
    }
    Row {
        Column {
            Text(
                text = "Game Over! \n\nWords Found: $numWords \nScore: $score",
                color = Color.Black, // Adjust text color as needed
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }
        TextField(
            value = foundWords.uppercase(Locale.ROOT),
            onValueChange = { },
            label = { Text("Words Found") },
            maxLines = 12,
            textStyle = TextStyle(
                fontSize = 20.sp,
                color = Color.Blue,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(20.dp),
            readOnly = true
        )
    }
    Row {
        val size = wordsOnBoard.size
        TextField(
            value = wordsOnBoard.joinToString(separator = "\n").uppercase(Locale.ROOT),
            onValueChange = { },
            label = { Text("$size Words on Board") },
            maxLines = 40,
            textStyle = TextStyle(
                fontSize = 20.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(20.dp),
            readOnly = true
        )
    }
}