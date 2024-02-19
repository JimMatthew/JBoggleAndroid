package com.example.boggle24

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
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
fun SettingsDisplay(
    setPlayTime: (String) -> Unit
){

    var time by remember { mutableStateOf("120") }
    Column {
        Row {
            Text(text = "Settings",modifier = Modifier.padding(5.dp),fontSize = 23.sp)
        }
        Row {

            TextField(
                value = time,
                onValueChange = {
                    time = it
                    setPlayTime(time)},
                label = { Text("Play Time") }
            )
        }

    }

}