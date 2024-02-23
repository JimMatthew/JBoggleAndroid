package bogglegame

import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.Locale

class WordScoreHandler (
    var updateStatus: (String) -> Unit,
    var updateScore: (Int) -> Unit,
    var updateNumWordsFound: (Int) -> Unit,
    var updateWordsFound: (String) -> Unit,
    var allWordsOnBoard: (List<String>) -> Unit
){
    private val wordSet: HashSet<String> = HashSet()
    private val userWordSet: MutableSet<String>
    private var wordsOnBoard: List<String> = ArrayList()
    private val tsolver: BoggleTrieSolver
    private var score = 0
    init {
        userWordSet = HashSet()
        tsolver = BoggleTrieSolver()
        loadWordList()
    }

    private fun loadWordList() {
        try {
            val `is` = javaClass.getResourceAsStream("/enable1.txt")
            val isr = InputStreamReader(`is`)
            val br = BufferedReader(isr)
            var lined: String
            while (br.readLine().also { lined = it } != null) {
                wordSet.add(lined)
            }
            br.close()
            isr.close()
            `is`?.close()
            tsolver.loadWordList(wordSet)
        } catch (e: Exception) {
            println("Dictionary not loaded!")
        }
    }

    private fun userWordsString(): String {
        return userWordSet.joinToString(separator = "\n")
    }

    fun setBoardLayout(board: Array<String>) {
        wordsOnBoard = tsolver.solve(board)
        allWordsOnBoard(wordsOnBoard)
    }



    private fun testIfWordExists(word: String): Boolean {
        return wordSet.contains(word.lowercase(Locale.getDefault()))
    }

    fun submit(word: String) :Boolean{
        return if (!testIfWordExists(word)) {
            updateStatus("${word.uppercase(Locale.ROOT)}  Is Not a Word!")
            false
        } else if (testIfWordAlreadyFound(word)) {
            updateStatus("${word.uppercase(Locale.ROOT)}  Was Already Found!")
            false
        } else {
            userWordSet.add(word.lowercase(Locale.getDefault()))
            updateStatus("${word.uppercase(Locale.ROOT)} Was Found!")
            score += computeScore(word)
            updateScore(score)
            updateNumWordsFound(userWordSet.size)
            updateWordsFound(userWordsString())
            true
        }
    }

    private fun testIfWordAlreadyFound(word: String): Boolean {
        return userWordSet.contains(word.lowercase(Locale.getDefault()))
    }

    fun clearUserWords() {
        userWordSet.clear()
        score = 0
        updateScore(score)
        updateWordsFound(userWordsString())
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
}
