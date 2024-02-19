package bogglegame;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BoggleWordHandler {
    private final HashSet<String> wordSet;
    private final Set<String> userWordSet;
    private List<String> wordOnBoard = new ArrayList<>();
    private final BoggleTrieSolver tsolver;

    public BoggleWordHandler() {
        wordSet = new HashSet<>();
        userWordSet = new HashSet<>();
        tsolver = new BoggleTrieSolver();
        loadWordList();
    }

    private void loadWordList() {
        try {
            InputStream is = getClass().getResourceAsStream("/enable1.txt");
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String lined;
            while ((lined = br.readLine()) != null) {
                wordSet.add(lined);
            }
            br.close();
            isr.close();
            if (is != null) {
                is.close();
            }
            tsolver.loadWordList(wordSet);
        } catch (Exception e) {
            System.out.println("Dictionary not loaded!");
        }
    }

    public void setBoardLayout(String[] board) {
        wordOnBoard = tsolver.solve(board);
    }

    public boolean testIfWordOnBoard(String word) {
        return wordOnBoard.contains(word.toLowerCase());
    }

    public boolean testIfWordExists(String word) {
        return wordSet.contains(word.toLowerCase());
    }

    public List<String> getWordsOnBoard() {
        return wordOnBoard;
    }

    public int getNumOfWordsOnBoard() {
    	return wordOnBoard.size();
    }

    public int getNumFoundWords() {
        return userWordSet.size();
    }

    public boolean submitWord(String word) {
        boolean wordExists = testIfWordExists(word);
        if (wordExists) {
            userWordSet.add(word.toLowerCase());
        }
        return wordExists;
    }

    public boolean testIfWordAlreadyFound(String word) {
        return userWordSet.contains(word.toLowerCase());
    }

    public List<String> solveBoard(String[] board) {
        return tsolver.solve(board);
    }

    public String getFoundwords() {
        StringBuilder words = new StringBuilder();
        for (String s : userWordSet) {
            words.append(s).append("\n");
        }
        return words.toString();
    }

    public void clearUserWords() {
        userWordSet.clear();
    }
}
