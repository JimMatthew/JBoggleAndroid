package com.example.boggle24

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
    pressLetter: (Int, Enum: BoggleBoard.InputType) -> Unit,
    clearCurrentWord: () -> Unit,
    useHighScoreBoards: () -> Unit,
    submitWord: () -> Unit
) {

    BoardDisplay(board = board, pressed = pressed) { index, type ->
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