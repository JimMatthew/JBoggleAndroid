package com.example.boggle24

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import bogglegame.BoggleBoard
import bogglegame.BoggleStats
import bogglegame.FileHelper
import com.example.boggle24.ui.theme.Boggle24Theme
import com.example.boggle24.ui.theme.Header
import kotlinx.coroutines.MainScope

class MainActivity : ComponentActivity() {

    private var timeleft = mutableStateOf("0")
    private var gameover = mutableStateOf(false)
    private var islaunched = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        val fileHelper = FileHelper(this)
        val boggleStats = fileHelper.readStatFromFile("bog.dat")
        super.onCreate(savedInstanceState)

        val board = BoggleBoard(boggleStats, updateStats = {
            fileHelper.writeStatToFile(it, "bog.dat")
        }, gameOver = { gameOver(it) },
            updateTime = { timeleft.value = it })
        setContent {
            Boggle24Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {

                        if (!islaunched.value) {
                            Launcher(
                                stats = boggleStats,
                                startGame = { islaunched.value = true }
                            )
                        } else {
                            stateManager(boardMaker = board, timeleft.value)
                        }

                    }
                }
            }
        }
    }

    @SuppressLint("MutableCollectionMutableState")//pressed is always being replaced
    @Composable
    fun stateManager(boardMaker: BoggleBoard, timeLeft: String) {
        var input by remember { mutableStateOf(boardMaker.currentWord) }
        var board by remember { mutableStateOf(boardMaker.board) }
        var pressed by remember { mutableStateOf(boardMaker.pressed) }
        var numWords by remember { mutableIntStateOf(boardMaker.numWordsFound) }
        var wordsFound by remember { mutableStateOf(boardMaker.foundWords) }
        var wordsOnBoard by remember { mutableStateOf(boardMaker.wordsOnBoard) }
        var status by remember { mutableStateOf(boardMaker.status) }
        var score by remember { mutableIntStateOf(boardMaker.score) }
        val totalGames by remember { mutableIntStateOf(boardMaker.totalGames) }
        var isHighScore by remember { mutableStateOf(boardMaker.isHighScore()) }
        val currentLocalConfig = LocalConfiguration.current
        val screenWidth = currentLocalConfig.screenWidthDp

        fun update() {
            board = boardMaker.board
            input = boardMaker.currentWord
            pressed = boardMaker.pressed
            status = boardMaker.status
            score = boardMaker.score
            isHighScore = boardMaker.isHighScore()
            numWords = boardMaker.numWordsFound
            wordsFound = boardMaker.foundWords
        }

        fun newGame() {
            board = boardMaker.board
            wordsOnBoard = boardMaker.wordsOnBoard
        }

        Header(
            timeleft = timeLeft,
            currentWord = input
        ) {
            boardMaker.startNewGame()
            update()
            newGame()
        }


        if (!gameover.value) {
            BoardDisplay(board = board, pressed = pressed) { index, type ->
                boardMaker.letterPress(index, type)
                update()
            }
            Controls(
                numWords = numWords,
                score = score,
                wordsOnBoard = wordsOnBoard,
                status = status,
                isHS = isHighScore,
                submit = {
                    boardMaker.submitWordPressed()
                    update()
                },
                toggleHS = {
                    boardMaker.useHighScoreBoards()
                    update()
                },
                cancel = {
                    boardMaker.clearCurrentWord()
                    update()
                }
            )
        }
        if (gameover.value) {
            GameOverDisplay(
                numWords = numWords.toString(),
                score = score,
                highScore = boardMaker.highScore,
                longestWord = boardMaker.longestWord,
                totalGames = totalGames,
                foundWords = wordsFound,
                wordsOnBoard = wordsOnBoard
            )
        }
    }

    fun setTimeLeft(time: Int) {
        timeleft.value = time.toString()
    }

    private fun gameOver(over: Boolean) {
        gameover.value = over
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    //val board = BoggleBoard(MainActivity(),null, null)
    Boggle24Theme {
        //Greeting(board, "Android")
    }
}

@Composable
fun Greeting(board: BoggleBoard, s: String) {

}
