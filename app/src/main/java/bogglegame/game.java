package bogglegame;

import java.util.Random;

public class game {

    String[] die;
    String[] board;
    int SIZE = 4;
    public game() {
        setDice();
    }

    public void startNewGame() {

    }

    public String[] getBoard() {
        return board;
    }


        private void setDice() {
        die = new String[]{"aaeegn", "elrtty", "abbjoo", "abbkoo", "ehrtvw", "cimotu", "distty", "eiosst", "achops",
                "himnqu", "eeinsu", "eeghnnw", "affkps", "hlnnrz", "deilrx", "delrvy",};
    }



    private String[] shuffleArr(String[] ar) {
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            String a = ar[i];
            ar[i] = ar[index];
            ar[index] = a;
        }
        return ar;
    }

    private static <T> void shuffleArray(T[] array) {
        int n = array.length;
        Random random = new Random();

        for (int i = n - 1; i > 0; i--) {
            int randIndex = random.nextInt(i + 1);
            T temp = array[i];
            array[i] = array[randIndex];
            array[randIndex] = temp;
        }
    }


    public String[] randomizeBoardToArray() {
        String[] cdie = die;
        Random rand = new Random();
        String[] b = new String[SIZE * SIZE];
        shuffleArr(cdie);
        for (int j = 0; j < SIZE * SIZE; j++) {
            b[j] = Character.toString(cdie[j].charAt(rand.nextInt(6)));
        }
        return b;
    }

}
