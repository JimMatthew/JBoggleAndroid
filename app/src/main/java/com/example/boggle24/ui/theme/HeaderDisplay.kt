package com.example.boggle24.ui.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Header(
    timeleft: String,
    currentWord: String,
    newGame: () -> Unit,
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = "Time : $timeleft",
                modifier = Modifier.padding(15.dp),
                fontSize = 20.sp
            )
            Button(
                modifier = Modifier.padding(15.dp),
                onClick = {
                    newGame()
                }) {
                Text("New Game")
            }
            Text(
                text = currentWord.uppercase(),
                style = TextStyle(
                    color = Color.Blue,
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp
                )
            )
        }
    }
}