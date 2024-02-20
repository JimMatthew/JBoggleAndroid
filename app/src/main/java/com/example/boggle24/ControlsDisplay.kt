package com.example.boggle24

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Controls(
    numWords: Int,
    score: Int,
    wordsOnBoard: List<String>,
    status: String,
    isHS: Boolean,
    submit: () -> Unit,
    toggleHS: () -> Unit,
    cancel: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Button(
            modifier = Modifier
                .padding(5.dp)
                .weight(1f),
            onClick = {
                submit()
            }) {
            Text("Submit")
        }
        Button(
            modifier = Modifier
                .padding(5.dp)
                .weight(1f),
            onClick = {
                cancel()
            }) {
            Text("Clear")
        }
    }
    Column {
        Row(horizontalArrangement = Arrangement.Center) {
            Text(
                text = status,
                fontWeight = FontWeight.Bold,
                fontSize = 23.sp,
                modifier = Modifier.padding(20.dp)
            )
        }
        Text(text = "Words Found: $numWords",modifier = Modifier.padding(5.dp))
        Text(text = "Score: $score",modifier = Modifier.padding(5.dp))
        Text(text = "Words on Board: " + wordsOnBoard.size,modifier = Modifier.padding(5.dp))
        Row(modifier = Modifier.padding(5.dp)) { Toggle(text = "HS Board", value = isHS, onValueChanged = { toggleHS() }) }

    }
}

@Composable
fun Toggle(
    text: String,
    value: Boolean,
    onValueChanged: (Boolean) -> Unit
) {
    Row {
        Text(text)
        Checkbox(checked = value, onCheckedChange = onValueChanged)
    }
}