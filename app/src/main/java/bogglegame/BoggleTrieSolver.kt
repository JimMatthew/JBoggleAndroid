package bogglegame

import java.util.Stack
import java.util.stream.Collectors
import kotlin.math.sqrt

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
