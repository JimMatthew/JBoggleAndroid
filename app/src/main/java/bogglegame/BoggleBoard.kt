package bogglegame

import com.example.boggle24.MainActivity
import java.util.Locale
import java.util.Random
import java.util.Timer
import java.util.TimerTask
import kotlin.math.abs


class BoggleBoard
    (stats: BoggleStats,
     private val updateStats: (BoggleStats) -> Unit,
     private val gameOver: (Boolean) -> Unit,
     private val updateTime: (String) -> Unit) {
    var currentWord = ""
    val die = arrayOf(
        "aaeegn", "elrtty", "abbjoo", "abbkoo", "ehrtvw", "cimotu", "distty", "eiosst", "achops",
        "himnqu", "eeinsu", "eeghnnw", "affkps", "hlnnrz", "deilrx", "delrvy"
    )
    var board = Array(16) { "" }
    private var wordHandler: BoggleWordHandler = BoggleWordHandler()
    private var highScoreHandler: BoggleWordHandler = BoggleWordHandler()
    var status = ""
    private var SIZE = 4
    var time = 0
    private var playTime = 20
    var score = 0
    var timer: Timer? = null
    var stats: BoggleStats? = stats
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
        wordHandler.clearUserWords()
        wordHandler.setBoardLayout(board)
        if (score != 0) stats!!.add4Score(score)
        score = 0
        status = ""
        stats!!.add4GamePlayed()
        updateStats(stats!!)
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
            status = ""
            timerStop()
        }
    }

    fun getPressed(): List<Int> {
        return ArrayList(pressed)
    }

    fun clearCurrentWord() {
        pressed = ArrayList()
        currentWord = ""
    }

    fun useHighScoreBoards() {
        isRandom = !isRandom
    }

    val totalGames: Int
        get() = stats!!.total4Games
    val highScore: Int
        get() = stats!!.highScore
    val longestWord: String
        get() = stats!!.longestWordFour

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
        status = ""
    }

    fun isHighScore(): Boolean {
        return !isRandom
    }

    fun submitWordPressed() {
        if (wordHandler.testIfWordAlreadyFound(currentWord)) {
            status = "${currentWord.uppercase(Locale.ROOT)}  Was Already Found!"
        } else if (wordHandler.submitWord(currentWord)) {
            score += computeScore(currentWord)
            status = "${currentWord.uppercase(Locale.ROOT)} Was Found!"
            stats!!.isWordLongestFour(currentWord)
        } else {
            status = "${currentWord.uppercase(Locale.ROOT)}  Is Not a Word!"
        }
        clearCurrentWord()
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

    val wordsOnBoard: List<String>
        get() = wordHandler.wordsOnBoard

    val foundWords: String
        get() = wordHandler.foundwords
    val numWordsOnBoard: Int
        get() = wordHandler.numOfWordsOnBoard
    val numWordsFound: Int
        get() = wordHandler.numFoundWords


    /*
        Check if a button is next to the last pressed button
        If there are no pressed buttons, return true
     */
    private fun isNextTo2(button: Int): Boolean {
        if (pressed.size == 0) return true
        val last = pressed[pressed.size - 1]
        return abs((button/SIZE)-(last/SIZE)) <=1 && abs((button%SIZE)-(last%SIZE)) <=1

    }

    private fun isNextTo(button: Int): Boolean {
        if (pressed.size == 0) return true
        val last = pressed[pressed.size - 1]
        val Xbtt = button / SIZE
        val Ybtt = button % SIZE
        val Xblp = last / SIZE
        val Yblp = last % SIZE
        return abs(Xbtt - Xblp) <= 1 && abs(Ybtt - Yblp) <= 1
    }

    private fun computeScore(word: String): Int {
        return when (val len = word.length) {
            3, 4 -> 1
            5 -> 2
            6 -> 3
            7 -> 5
            else -> if (len > 7) 11 else 0
        }
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
        score = 0
        status = ""
    }
}
