package bogglegame

import com.example.boggle24.MainActivity
import java.util.Locale
import java.util.Random
import java.util.Timer
import java.util.TimerTask
import kotlin.math.abs


class BoggleBoard
    (
     private val gameOver: (Boolean) -> Unit,
     private val wordInput: (String) -> Unit,
     private val pressedDice: (ArrayList<Int>) -> Unit,
     private val boardArray: (Array<String>) -> Unit,
     private val updateStatus: (String) -> Unit,
     private val isHighScoreMode: (Boolean) -> Unit,
     private val updateTime: (String) -> Unit) {
    var currentWord = ""
    val die = arrayOf(
        "aaeegn", "elrtty", "abbjoo", "abbkoo", "ehrtvw", "cimotu", "distty", "eiosst", "achops",
        "himnqu", "eeinsu", "eeghnnw", "affkps", "hlnnrz", "deilrx", "delrvy"
    )
    var board = Array(16) { "" }
    private var highScoreHandler: BoggleWordHandler = BoggleWordHandler()
    private var SIZE = 4
    var time = 0
    private var playTime = 100
    var timer: Timer? = null
    private var isGameOver = true
    private var isRandom = true
    private val genScore = 200
    private var isRunning = false
    private var HSIndex = 0
    private val gameBoardList: MutableList<Array<String>> = ArrayList()
    private val gameBoardWordList: MutableList<List<String>> = ArrayList()
    var pressed = ArrayList<Int>()
    enum class InputType {
        TAP,
        DRAG
    }

    fun startNewGame() {
        if (timer != null) timer!!.cancel()
        isGameOver = false
        gameOver(false)
        board = if (isRandom) {
            rollDice(die)
        } else {
            if (HSIndex + 5 >= gameBoardList.size) {
                makeHighScoreBoards()
            }
            gameBoardList[HSIndex++]
        }
        clearCurrentWord()
        pressed = ArrayList()
        pressedDice(pressed)
        updateStatus("")
        boardArray(board)
        startTimer()
    }

    private fun startTimer() {
        time = playTime
        updateTime(time.toString())
        val task: TimerTask = object : TimerTask() {
            override fun run() {
                incrementTime()
            }
        }
        timer = Timer("Timer")
        timer!!.scheduleAtFixedRate(task, 1000, 1000)
    }

    private fun timerStop() {
        timer!!.cancel()
    }

    fun incrementTime() {
        if (time-- > 0) {
            updateTime(time.toString())
        } else {
            isGameOver = true
            gameOver(true)
            updateStatus("")
            timerStop()
        }
    }

    fun getPressed(): List<Int> {
        return ArrayList(pressed)
    }

    fun clearCurrentWord() {
        pressed = ArrayList()
        currentWord = ""
        pressedDice(pressed)
        wordInput(currentWord)
    }

    fun useHighScoreBoards() {
        isRandom = !isRandom
        isHighScoreMode(!isRandom)
    }

    fun letterPress(button: Int, type: InputType) {
        if (button < 0 || button >= SIZE * SIZE) return
        if (isGameOver) return
        if (pressed.contains(button)) {
            setNewPosition(pressed.indexOf(button), type)
            currentWord = currentWord.substring(0, pressed.size)
        } else if (isNextTo(button)) {
            currentWord += board[button]
            pressed.add(button)
        }
        wordInput(currentWord)
        pressedDice(pressed)
        boardArray(board)
        updateStatus("")
    }

    fun isHighScore(): Boolean {
        return !isRandom
    }

    private fun makeHighScoreBoards() {
        if (!isRunning) {
            isRunning = true
            Thread {
                val size = gameBoardList.size + 10
                while (gameBoardList.size < size) {
                    val b = rollDice(die)
                    val words = highScoreHandler.solveBoard(b)
                    if (words.size > genScore) {
                        gameBoardList.add(b)
                        gameBoardWordList.add(words)
                    }
                }
                isRunning = false
            }.start()
        }
    }

    private fun setNewPosition(position: Int, type: InputType) {
        val newPosition = if (type == InputType.DRAG) position + 1 else position
        pressed = pressed.take(newPosition).toMutableList() as ArrayList<Int>
    }

    /*
        Check if a button is next to the last pressed button
        If there are no pressed buttons, return true
     */
    private fun isNextTo(button: Int): Boolean {
        if (pressed.size == 0) return true
        val last = pressed[pressed.size - 1]
        val Xbtt = button / SIZE
        val Ybtt = button % SIZE
        val Xblp = last / SIZE
        val Yblp = last % SIZE
        return abs(Xbtt - Xblp) <= 1 && abs(Ybtt - Yblp) <= 1
    }

    /*
        We need to select a Random letter from each die, then shuffle the order
        of those randomly selected letters
     */
    private fun rollDice(die: Array<String>): Array<String> {
        val random = Random()
        return die.map { it[random.nextInt(it.length)].toString() }.shuffled().toTypedArray()
    }

    init {
        pressed = ArrayList()
        makeHighScoreBoards()
    }
}
