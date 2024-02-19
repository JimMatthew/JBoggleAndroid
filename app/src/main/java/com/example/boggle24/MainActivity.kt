package com.example.boggle24
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bogglegame.BoggleBoard
import com.example.boggle24.ui.theme.Boggle24Theme
import com.example.boggle24.ui.theme.Header

class MainActivity : ComponentActivity() {

    private var timeleft = mutableStateOf("")
    private var gameover = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val board = BoggleBoard(this)
        //this.timeleft.value = board.time.toString()
        setContent {
            Boggle24Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {

                        Launcher(
                            startGame = {
                                stateManager(boardMaker = board, timeleft.value)
                        },
                            startSettings = { /*TODO*/ }) {

                        }
                        //stateManager(boardMaker = board, timeleft.value)
                    }
                }
            }
        }
    }

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
        var totalGames by remember { mutableIntStateOf(boardMaker.totalGames) }
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
            currentWord = input) {
            boardMaker.startNewGame()
            update()
            newGame()
        }


        if (!gameover.value) {
            BoardDisplay(board = board, pressed = pressed) {index, type ->
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
        Button(
            modifier = Modifier.padding(5.dp),
            onClick = {

            }) {
            Text("Settings")
        }
    }

    fun setTimeLeft(time: Int) {
        timeleft.value = time.toString()
    }

    fun gameOver(over: Boolean) {
        gameover.value = over
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    var board = BoggleBoard()
    Boggle24Theme {
        Greeting(board, "Android")
    }
}

@Composable
fun Greeting(board: BoggleBoard, s: String) {

}
