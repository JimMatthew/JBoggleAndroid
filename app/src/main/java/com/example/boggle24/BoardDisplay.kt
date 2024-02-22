package com.example.boggle24

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bogglegame.BoggleBoard
import com.example.boggle24.ui.theme.pdie

@Composable
fun BoardDisplay(
    pressed: List<Int>,
    board: Array<String>,
    isRotated: Boolean,
    pressLetter: (Int, Enum: BoggleBoard.InputType) -> Unit
) {
    var touchPoint by remember { mutableStateOf(Offset.Zero) }
    var xoff by remember { mutableDoubleStateOf(0.0) }
    var yoff by remember { mutableDoubleStateOf(0.0) }
    var endDragIndex by remember { mutableIntStateOf(-1) }
    val currentLocalConfig = LocalConfiguration.current
    val screenWidth = currentLocalConfig.screenWidthDp
    var boxsize = (screenWidth) / 4
    var swp = with(LocalDensity.current) { currentLocalConfig.screenWidthDp.dp.toPx() }

    if (isRotated) boxsize /= 2
    if (isRotated){
        swp /= 2
    }
    Column {
        for (i in 0..3) {
            Row {
                for (j in 0..3) {
                    val index = (i * 4) + j
                    val c = if (pressed.contains(index)) Color.White else Color.White
                    Button(
                        onClick = {
                        },
                        shape = RectangleShape,
                        modifier = Modifier
                            .size(boxsize.dp) // Adjust size as needed
                            .padding(5.dp), // Adjust padding as needed
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        contentPadding = PaddingValues(0.dp),

                        ) {
                        Box(
                            modifier = Modifier
                                .size(boxsize.dp)
                                //.fillMaxSize()
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onPress = {
                                            try {
                                                touchPoint = it
                                                pressLetter(index, BoggleBoard.InputType.TAP)
                                            } finally {
                                            }
                                        },
                                    )
                                }
                                .pointerInput(Unit) {
                                    detectDragGestures(
                                        onDragStart = {
                                            touchPoint = it
                                            endDragIndex = index
                                            xoff =
                                                j + ((touchPoint.x) / (swp / 4)).toDouble()
                                            yoff =
                                                i + ((touchPoint.y) / (swp / 4)).toDouble()
                                        },
                                        onDrag = { change, dragAmount ->
                                            change.consume()
                                            val a = swp / 4
                                            xoff += dragAmount.x / a
                                            yoff += dragAmount.y / a
                                            val x = endDragIndex % 4
                                            val y = endDragIndex / 4

                                            if (yoff >= 0 && yoff < 4) {
                                                if (xoff.toInt() != x && yoff.toInt() != y) {
                                                    endDragIndex =
                                                        xoff.toInt() + (yoff.toInt() * 4)
                                                    pressLetter(endDragIndex, BoggleBoard.InputType.DRAG)
                                                } else if (xoff.toInt() != x) {
                                                    endDragIndex = xoff.toInt() + y * 4
                                                    pressLetter(endDragIndex, BoggleBoard.InputType.DRAG)
                                                } else if (yoff.toInt() != y) {
                                                    endDragIndex = (yoff.toInt() * 4) + x
                                                    pressLetter(endDragIndex, BoggleBoard.InputType.DRAG)
                                                }
                                            }
                                        },
                                        onDragEnd = {
                                        }
                                    )
                                },
                            contentAlignment = Alignment.Center
                        ) {

                            Image(
                                painter = painterResource(id = R.drawable.blank),
                                contentDescription = null
                            )

                            if (isRotated){
                                Text(
                                    text = board[index].uppercase(),
                                    color = if (pressed.contains(index)) pdie else Color.Black,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 25.sp

                                )
                            } else {
                                Text(
                                    text = board[index].uppercase(),
                                    color = if (pressed.contains(index)) pdie else Color.Black,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 36.sp

                                )
                            }

                            //#Color(0xFF38b01e)
                        }
                    }
                }
            }
        }
    }
}