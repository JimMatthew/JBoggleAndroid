package bogglegame

import java.util.stream.Collectors
import kotlin.math.sqrt

/*
 * This class uses a Trie to recursively search a boggle board to find all valid words
 * Instantiate with BoggleTrieDfsSolver(). You must provide the dictionary as either HashSet<String> or List<String>
 * A boggle Board is to be represented as a 16 item long String Array, containing 1 letter per String
 * Call the solve method by passing a single board as a String[], or you can pass multiple boards 
 * as a List<String[]>. 
 * For single boards, the solve method will return a List<String>, a list of Strings, one String for each word found
 * For multiple boards, a List<List<String>> is returned, a List<String> for each board solved. 
 * NOTE: If no words are found, an empty List<String> is return
 * 
 * The function will begin recursively searching through all possible combinations of dice. 
 * Since this uses a trie, we can at each step check whether the branch we are on is a prefix to a known word
 * If it is not, than we can stop going any further down that path, as there is no possible word to be found
 * This substantially improves performance over a basic recursive DFS search because it eliminates exploring branches
 * that will never contain a valid word. This allows it to solve thousands of boards per second
 */
class BoggleTrieSolver {
    private var wordsFound: MutableList<String?> = ArrayList()
    private val board = Array(5) { arrayOfNulls<String>(5) }
    private val MaxWordLength = 16
    private var size = 4

    constructor() {
        root = TrieNodee()
    }

    constructor(dictionary: HashSet<String>) {
        root = TrieNodee()
        loadWordList(dictionary)
    }

    constructor(dictionary: List<String>) {
        root = TrieNodee()
        loadWordList(dictionary)
    }

    class TrieNodee {
        val children = arrayOfNulls<TrieNodee>(ALPHABET_SIZE)
        var isEndOfWord = false
    }

    fun loadWordList(set: HashSet<String>) {
        set.forEach(::insert)
    }

    fun loadWordList(list: List<String>) {
        list.forEach(::insert)
    }

    fun getWordsFound(): List<String?> {
        return wordsFound
    }

    fun solve(board: Array<String>): List<String?> {
        size = sqrt(board.size.toDouble()).toInt()
        setBoard(board)
        val visited = Array(5) { arrayOfNulls<Boolean>(5) }
        resetVisited(visited)
        for (i in 0 until size) {
            for (j in 0 until size) {
                solver(visited, "", i, j)
            }
        }
        wordsFound = wordsFound.stream().distinct().collect(Collectors.toList())
        wordsFound.sortWith(StringSort())
        return wordsFound
    }

    //This method will accept a list of boards, and return a list of the words found for each board
    //List<String[]> boards is a List, where each String[] represents a board 
    //Returns a List, containing a List of Strings of words found for each board
    //Words in each List are deduplicated and sorted largest first. 
    fun solveList(boards: List<Array<String>>): List<List<String?>> {
        val lls: MutableList<List<String?>> = ArrayList()
        for (sa in boards) {
            size = sqrt(sa.size.toDouble()).toInt()
            setBoard(sa)
            val visited = Array(5) { arrayOfNulls<Boolean>(5) }
            resetVisited(visited)
            for (i in 0 until size) {
                for (j in 0 until size) {
                    solver(visited, "", i, j)
                }
            }
            wordsFound = wordsFound.stream().distinct().collect(Collectors.toList())
            //Collections.sort(wordsFound, new StringSort());
            lls.add(wordsFound)
        }
        return lls
    }

    //Recursive method used to find all words on a boggle board
    //This will perform a depth first search for all possible strings from starting pos, (row,col)
    //Since this is using a Trie, we can test our current string 'current' to see if it is a valid prefix
    //If it is not a valid prefix to any word, meaning there is nothing else on this branch, we can return and quit
    //searching as there is no valid word to be found from this position
    private fun solver(visited: Array<Array<Boolean?>>, current: String?, row: Int, col: Int) {
        var current = current
        visited[row][col] = true
        current += board[row][col]
        val length = current!!.length
        var index: Int
        var pCrawl: TrieNodee? = root
        var level: Int = 0
        while (level < length) {
            index = current[level].code - 'a'.code
            if (pCrawl!!.children[index] == null) { //no more valid words on this branch
                visited[row][col] = false
                return
            }
            pCrawl = pCrawl.children[index]
            level++
        }
        if (pCrawl!!.isEndOfWord && current.length > 2) {
            wordsFound.add(current)
        }
        if (current.length == MaxWordLength) {
            visited[row][col] = false
            return
        }
        val rows = intArrayOf(-1, 1, 0, 0, -1, 1, -1, 1)
        val cols = intArrayOf(0, 0, -1, 1, -1, 1, 1, -1)
        for (i in 0..7) {
            val newRow = row + rows[i]
            val newCol = col + cols[i]
            if (isValidCell(newRow, newCol) && !visited[newRow][newCol]!!) {
                solver(visited, current, newRow, newCol)
            }
        }
        visited[row][col] = false
    }

    private fun setBoard(s: Array<String>) {
        wordsFound = ArrayList()
        var c = 0
        for (i in 0 until size) {
            for (j in 0 until size) {
                board[i][j] = s[c++]
            }
        }
    }

    private fun isValidCell(row: Int, col: Int): Boolean {
        return row in 0..<size && col >= 0 && col < size
    }

    private fun isValidCell(row: Int, col: Int, s: Int): Boolean {
        return row in 0..<s && col >= 0 && col < s
    }

    private fun resetVisited(b: Array<Array<Boolean?>>) {
        for (i in 0 until size) {
            for (j in 0 until size) {
                b[i][j] = false
            }
        }
    }

    private class StringSort : Comparator<String?> {


        override fun compare(p0: String?, p1: String?): Int {
            if (p0!!.length < p1!!.length) {
                return 1
            }
            // if size the same sort alphabetically
            return if (p0.length == p1.length) {
                    p0.compareTo(p1)
                } else -1
        }
    }

    companion object {
        private lateinit var root: TrieNodee
        const val ALPHABET_SIZE = 26
        fun isEmpty(root: TrieNodee): Boolean {
            for (i in 0 until ALPHABET_SIZE) if (root.children[i] != null) return false
            return true
        }

        private fun insert(key: String) {
            val length = key.length
            var index: Int
            var pCrawl: TrieNodee? = root
            var level: Int = 0
            while (level < length) {
                index = key[level].code - 'a'.code
                if (pCrawl!!.children[index] == null) {
                    pCrawl.children[index] = TrieNodee()
                }
                pCrawl = pCrawl.children[index]
                level++
            }
            pCrawl!!.isEndOfWord = true
        }
    }
}
