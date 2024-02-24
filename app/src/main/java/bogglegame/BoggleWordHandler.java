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
    private final BoggleTrieSolver tsolver;

    public BoggleWordHandler() {
        wordSet = new HashSet<>();
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
    public List<String> solveBoard(String[] board) {
        return tsolver.solve(board);
    }

}
