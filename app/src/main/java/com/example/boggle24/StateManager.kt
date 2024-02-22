package com.example.boggle24


import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import bogglegame.BoggleBoard
import bogglegame.BoggleStats
import bogglegame.BoggleWordHandler
import bogglegame.WordScoreHandler
import com.example.boggle24.ui.theme.Header
import java.util.Locale

class StateManager(
    stats: BoggleStats,
    val saveStats: (BoggleStats) -> Unit
) {

    val boggleWordHandler = WordScoreHandler(
        updateStatus = {gameStatus(it)},
        updateScore = {score(it)},
        updateNumWordsFound = {NumWordsFound(it)},
        updateWordsFound = {foundWords(it)}
    )
    private val boardMaker = BoggleBoard(stats, updateStats = {
        saveStats(it)
    }, gameOver = { gameOver(it) },
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
    var pressed = mutableStateOf(ArrayList<Int>())
    var board = mutableStateOf(boardMaker.board)
    private val numWordsFound = mutableIntStateOf(0)
    private val foundWords = mutableStateOf("")
    private var wordsOnBoard = mutableStateOf(ArrayList<String>())
    var status = mutableStateOf("")
    var score = mutableIntStateOf(0)
    private var isHighScore = mutableStateOf(boardMaker.isHighScore())


    @Composable
    fun stateManager() {

        Header(
            timeleft = timeLeft.value,
            currentWord = current.value,
            newGame = { startNewGame() }
        )

        if (!gameover.value) {
            GameDisplay(
                board = board.value,
                pressed = pressed.value,
                score = score.intValue,
                status = status.value,
                isHighScore = isHighScore.value,
                wordsOnBoard = wordsOnBoard.value,
                numWordsFound = numWordsFound.intValue,
                pressLetter = { index, type ->
                    boardMaker.letterPress(index, type)
                },
                clearCurrentWord = { boardMaker.clearCurrentWord() },
                useHighScoreBoards = { boardMaker.useHighScoreBoards() },
                submitWord =  ::submitWord )
        } else {
            GameOverDisplay(
                numWords = numWordsFound.intValue.toString(),
                score = score.intValue,
                highScore = stats.value.highScore,
                longestWord = stats.value.longestWord,
                totalGames = stats.value.total4Games,
                foundWords = foundWords.value,
                wordsOnBoard = wordsOnBoard.value
            )
        }

    }

    private fun setTimeLeft(time: String) {
        timeLeft.value = time
    }

    private fun gameOver(over: Boolean) {
        gameover.value = over
    }

    fun currentWord(currentWord: String) {
        current.value = currentWord
    }

    fun PressedDice(pressedDice: ArrayList<Int>) {
        pressed.value = pressedDice
    }

    fun BoardArray(boardArray: Array<String>) {
        board.value = boardArray
    }

    fun NumWordsFound(numWordsFound: Int) {
        this.numWordsFound.intValue = numWordsFound
    }

    fun foundWords(foundWords: String) {
        this.foundWords.value = foundWords
    }

    fun wordsOnBoard(wordsOnBoard: List<String>) {
        this.wordsOnBoard.value = wordsOnBoard as ArrayList<String>
    }

    fun gameStatus(status: String) {
        this.status.value = status
    }

    fun score(score: Int) {
        this.score.intValue = score
    }

    fun isHighScoreMode(mode: Boolean) {
        isHighScore.value = mode
    }

    fun startNewGame() {
        boardMaker.startNewGame()
        boggleWordHandler.clearUserWords()
        boggleWordHandler.setBoardLayout(board.value)
        wordsOnBoard(boggleWordHandler.wordsOnBoard)
        stats.value.add4Score(score.intValue)
        saveStats(stats.value)
    }

    fun submitWord() {
        boggleWordHandler.submit(current.value)
        boardMaker.clearCurrentWord()
    }

}

