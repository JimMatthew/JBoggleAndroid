package bogglegame

import androidx.compose.ui.text.toUpperCase
import com.example.boggle24.MainActivity
import java.util.Locale
import java.util.Random
import java.util.Timer
import java.util.TimerTask
import kotlin.math.abs

class BoggleBoard {
    var currentWord = ""
    var die = arrayOf(
        "aaeegn", "elrtty", "abbjoo", "abbkoo", "ehrtvw", "cimotu", "distty", "eiosst", "achops",
        "himnqu", "eeinsu", "eeghnnw", "affkps", "hlnnrz", "deilrx", "delrvy"
    )
    lateinit var board: Array<String?>
    private var wordHandler: BoggleWordHandler
    private var highScoreHandler: BoggleWordHandler
    var status = ""
    private var SIZE = 4
    var time = 0
    private var playTime = 80
    var score = 0
    var timer: Timer? = null
    var activity: MainActivity? = null
    var stats: BoggleStats?
    private var isGameOver = true
    private var isRandom = true
    private val genScore = 200
    private var isRunning = false
    private var HSIndex = 0
    private val gameBoardList: MutableList<Array<String?>> = ArrayList()
    private val gameBoardWordList: MutableList<List<String>> = ArrayList()
    private var fileHelper: FileHelper? = null
    var pressed = ArrayList<Int>()

    enum class InputType {
        TAP,
        DRAG
    }
    constructor() {
        wordHandler = BoggleWordHandler()
        highScoreHandler = BoggleWordHandler()
        pressed = ArrayList()
        stats = BoggleStats()
        startNewGame()
        makeHighScoreBoards2()
    }

    constructor(activity: MainActivity?) {
        wordHandler = BoggleWordHandler()
        highScoreHandler = BoggleWordHandler()
        pressed = ArrayList()
        this.activity = activity
        fileHelper = FileHelper(activity!!)
        stats = fileHelper!!.readStatFromFile("bog.dat")
        if (stats == null) stats = BoggleStats()
        startNewGame()
        makeHighScoreBoards2()
        score = 0
        status = ""
    }

    fun startNewGame() {
        if (timer != null) timer!!.cancel()
        isGameOver = false
        activity!!.gameOver(false)
        board = if (isRandom) {
            randomizeBoardToArray()
        } else {
            if (HSIndex + 5 >= gameBoardList.size) {
                makeHighScoreBoards2()
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
        fileHelper!!.writeStatToFile(stats!!, "bog.dat")
        startTimer()
    }


    private fun startTimer() {
        time = playTime
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
            activity!!.setTimeLeft(time)
        } else {
            isGameOver = true
            activity!!.gameOver(true)
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

    fun letterPress(button: Int) {
        if (button < 0 || button >= SIZE * SIZE) return
        if (isGameOver) return
        if (pressed.contains(button)) {
            //setNewPosition(pressed.indexOf(button))
            currentWord = currentWord.substring(0, pressed.size)
        } else if (isNextTo(button)) {
            currentWord += board[button]
            pressed.add(button)
        }
        status = ""
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

    private fun makeHighScoreBoards2() {
        if (!isRunning) {
            isRunning = true
            Thread {
                val size = gameBoardList.size + 10
                while (gameBoardList.size < size) {
                    val b = randomizeBoardToArray()
                    val words = highScoreHandler.solveBoard(b)
                    if (words.size > genScore) {
                        gameBoardList.add(b.clone())
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
        get()= wordHandler.wordsOnBoard

    val foundWords: String
        get() = wordHandler.foundwords
    val numWordsOnBoard: Int
        get() = wordHandler.numOfWordsOnBoard
    val numWordsFound: Int
        get() = wordHandler.numFoundWords

    private fun shuffleArr(array: Array<String>): Array<String> {
        val rnd = Random()
        val shuffledArray = array.copyOf()
        for (i in shuffledArray.size - 1 downTo 1) {
            val index = rnd.nextInt(i + 1)
            val temp = shuffledArray[i]
            shuffledArray[i] = shuffledArray[index]
            shuffledArray[index] = temp
        }
        return shuffledArray
    }

    /*
        Check if a button is next to the last pressed button
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

    private fun computeScore2(word: String): Int {
        val len = word.length
        if (len == 3 || len == 4) return 1
        if (len == 5) return 2
        if (len == 6) return 3
        if (len == 7) return 5
        return if (len > 7) 11 else 0
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

    fun randomizeBoardToArray(): Array<String?> {
        val die = die
        val rand = Random()
        val b = arrayOfNulls<String>(SIZE * SIZE)
        shuffleArr(die)
        for (j in 0 until SIZE * SIZE) {
            b[j] = die[j][rand.nextInt(6)].toString()
        }
        return b
    }

    companion object {
        private fun <T> shuffleArray(array: Array<T>) {
            val n = array.size
            val random = Random()
            for (i in n - 1 downTo 1) {
                val randIndex = random.nextInt(i + 1)
                val temp = array[i]
                array[i] = array[randIndex]
                array[randIndex] = temp
            }
        }
    }
}
