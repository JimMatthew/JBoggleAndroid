package com.example.boggle24

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import bogglegame.BoggleBoard

@Composable
fun GameDisplay(
    board: Array<String>,
    pressed: ArrayList<Int>,
    score: Int,
    status: String,
    isHighScore: Boolean,
    wordsOnBoard: List<String?>,
    numWordsFound: Int,
    isRotated: Boolean,
    pressLetter: (Int, Enum: BoggleBoard.InputType) -> Unit,
    clearCurrentWord: () -> Unit,
    useHighScoreBoards: () -> Unit,
    submitWord: () -> Unit
) {

    if (isRotated) {
        Row (horizontalArrangement = Arrangement.SpaceEvenly){
            BoardDisplay(board = board, isRotated = isRotated, pressed = pressed) { index, type ->
                pressLetter(index, type)
            }
            Controls(
                numWords = numWordsFound,
                score = score,
                wordsOnBoard = wordsOnBoard,
                status = status,
                isHS = isHighScore,
                submit = { submitWord() },
                toggleHS = { useHighScoreBoards() },
                cancel = { clearCurrentWord() }
            )
        }
    } else {
        BoardDisplay(board = board, isRotated = isRotated, pressed = pressed) { index, type ->
            pressLetter(index, type)
        }
        Controls(
            numWords = numWordsFound,
            score = score,
            wordsOnBoard = wordsOnBoard,
            status = status,
            isHS = isHighScore,
            submit = { submitWord() },
            toggleHS = { useHighScoreBoards() },
            cancel = { clearCurrentWord() }
        )
    }

}