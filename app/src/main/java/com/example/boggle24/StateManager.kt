package com.example.boggle24

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import bogglegame.BoggleBoard
import bogglegame.BoggleStats
import bogglegame.WordScoreHandler
import com.example.boggle24.ui.theme.Header
import com.google.firebase.firestore.FirebaseFirestore

class StateManager(
    stats: BoggleStats,
    var isRotated: Boolean,
    val saveStats: (BoggleStats) -> Unit
) {

    private val boggleWordHandler = WordScoreHandler(
        updateStatus = { gameStatus(it) },
        updateScore = { score(it) },
        updateNumWordsFound = { NumWordsFound(it) },
        updateWordsFound = { foundWords(it) },
        allWordsOnBoard = { wordsOnBoard(it) }
    )
    private val boardMaker = BoggleBoard(
        gameOver = { gameOver(it) },
        wordInput = { currentWord(it) },
        pressedDice = { PressedDice(it) },
        boardArray = { BoardArray(it) },
        updateStatus = { gameStatus(it) },
        isHighScoreMode = { isHighScoreMode(it) },
        updateTime = { setTimeLeft(it) }
    )

    private var timeLeft = mutableStateOf("0")
    private var gameover = mutableStateOf(false)
    private var current = mutableStateOf(" ")
    private var stats = mutableStateOf(stats)
    private var pressed = mutableStateOf(ArrayList<Int>())
    var board = mutableStateOf(boardMaker.board)
    private val numWordsFound = mutableIntStateOf(0)
    private val foundWords = mutableStateOf("")
    private var wordsOnBoard = mutableStateOf(ArrayList<String>())
    private var status = mutableStateOf("")
    private var score = mutableIntStateOf(0)
    private var isHighScore = mutableStateOf(boardMaker.isHighScore())

    @Composable
    fun stateManager() {

        if (isRotated) {
            Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                BoardDisplay(
                    board = board.value,
                    isRotated = isRotated,
                    pressed = pressed.value
                ) { index, type ->
                    boardMaker.letterPress(index, type)
                }
                Column {
                    Header(
                        timeleft = timeLeft.value,
                        currentWord = current.value,
                        newGame = { startNewGame() }
                    )
                    if (!gameover.value) {
                        Controls(
                            numWords = numWordsFound.intValue,
                            score = score.intValue,
                            wordsOnBoard = wordsOnBoard.value,
                            status = status.value,
                            isHS = isHighScore.value,
                            submit = { submitWord() },
                            toggleHS = { isHighScore.value = !isHighScore.value },
                            cancel = { boardMaker.clearCurrentWord() }
                        )
                    } else {
                        GameOverDisplay(
                            numWords = numWordsFound.intValue.toString(),
                            score = score.intValue,
                            highScore = stats.value.highScore,
                            longestWord = stats.value.longestWord,
                            totalGames = stats.value.total4Games,
                            foundWords = foundWords.value,
                            isRotated = isRotated,
                            wordsOnBoard = wordsOnBoard.value
                        )
                    }
                }
            }
        } else {    //Not Rotated
            Header(
                timeleft = timeLeft.value,
                currentWord = current.value,
                newGame = { startNewGame() }
            )
            if (!gameover.value) {
                BoardDisplay(
                    board = board.value,
                    isRotated = isRotated,
                    pressed = pressed.value
                ) { index, type ->
                    boardMaker.letterPress(index, type)
                }
                Controls(
                    numWords = numWordsFound.intValue,
                    score = score.intValue,
                    wordsOnBoard = wordsOnBoard.value,
                    status = status.value,
                    isHS = isHighScore.value,
                    submit = { submitWord() },
                    toggleHS = { boardMaker.useHighScoreBoards() },
                    cancel = { boardMaker.clearCurrentWord() }
                )
            } else {
                GameOverDisplay(
                    numWords = numWordsFound.intValue.toString(),
                    score = score.intValue,
                    highScore = stats.value.highScore,
                    longestWord = stats.value.longestWord,
                    totalGames = stats.value.total4Games,
                    foundWords = foundWords.value,
                    isRotated = isRotated,
                    wordsOnBoard = wordsOnBoard.value
                )
            }
        }
    }




    private fun setTimeLeft(time: String) {
        timeLeft.value = time
    }

    private fun gameOver(over: Boolean) {
        gameover.value = over
    }

    private fun currentWord(currentWord: String) {
        current.value = currentWord
    }

    private fun PressedDice(pressedDice: ArrayList<Int>) {
        pressed.value = pressedDice
    }

    private fun BoardArray(boardArray: Array<String>) {
        board.value = boardArray
    }

    private fun NumWordsFound(numWordsFound: Int) {
        this.numWordsFound.intValue = numWordsFound
    }

    private fun foundWords(foundWords: String) {
        this.foundWords.value = foundWords
    }

    private fun wordsOnBoard(wordsOnBoard: List<String>) {
        this.wordsOnBoard.value = wordsOnBoard as ArrayList<String>
    }

    private fun gameStatus(status: String) {
        this.status.value = status
    }

    fun score(score: Int) {
        this.score.intValue = score
    }

    private fun isHighScoreMode(mode: Boolean) {
        isHighScore.value = mode
    }

    private fun startNewGame() {
        stats.value.add4Score(score.intValue)
        saveStats(stats.value)
        boardMaker.startNewGame()
        boggleWordHandler.clearUserWords()
        boggleWordHandler.setBoardLayout(board.value)
    }

    fun rotate(rotated: Boolean) {
        isRotated = rotated
    }

    private fun submitWord() {
        if (boggleWordHandler.submit(current.value)) {
            stats.value.isWordLongestFour(current.value)
        }
        boardMaker.clearCurrentWord()
    }

}






