package bogglegame;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import java.io.Serial;
import java.io.Serializable;

/*
 * The purpose of this class is to hold stat info for player
 */
public class BoggleStats implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;
	private int totalGamesPlayed;
	private int totalWordsFound;
	private String longestWord;
	private String longestWordFour;
	private String longestWordFive;
	private final int highScore = 0;
	private int totalScore;
	private int totalTimePlayed;
	private int total4GamesPlayed;
	private int total5GamesPlayed;
	private int total4WordsFound;
	private int total5WordsFound;
	private int total4Score;
	private int total5Score;
	private int high4Score;
	private int high5Score;

	private Context context;

	public BoggleStats() {
		clearStats();
	}

	public void clearStats() {
		totalGamesPlayed = 0;
		totalWordsFound = 0;
		longestWord = "";
		longestWordFour = "";
		longestWordFive = "";
		totalScore = 0;
		totalTimePlayed = 0;
		total4GamesPlayed = 0;
		total5GamesPlayed = 0;
		total4WordsFound = 0;
		total5WordsFound = 0;
		total4Score = 0;
		total5Score = 0;
		high4Score = 0;
		high5Score = 0;
	}
	public void add4GamePlayed() {
		total4GamesPlayed++;
	}


	public void add4Score(int score) {
		total4Score = total4Score + score;
		if (score > high4Score) {
			high4Score = score;
		}
		total4GamesPlayed++;
	}

	public void add5Score(int score) {
		total5Score = total5Score + score;
		if (score > high5Score) {
			high5Score = score;
		}
	}

	private void isWordLongest(String word) {
		if (word.length() > longestWord.length()) {
			longestWord = word;
		}
	}

	public void isWordLongestFour(String word4) {
		total4WordsFound++;
		if (word4.length() > longestWordFour.length()) {
			longestWordFour = word4.toUpperCase();
			isWordLongest(word4.toUpperCase());
		}
	}

	public void isWordLongestFive(String word5) {
		total5WordsFound++;
		if (word5.length() > longestWordFive.length()) {
			longestWordFive = word5;
			isWordLongest(word5);
		}
	}

	public void IncrementTimePlayed() {
		totalTimePlayed++;
	}

	public int getTotal4Games() {
		return total4GamesPlayed;
	}

	public int GetTotal5Games() {
		return total5GamesPlayed;
	}

	public int getTotal4words() {
		return total4WordsFound;
	}

	public int getTotal5Words() {
		return total5WordsFound;
	}

	public int getTotal4Score() {
		return total4Score;
	}

	public int getTotal5Score() {
		return total5Score;
	}

	public int getTotalGamesPlayed() {
		totalGamesPlayed = total4GamesPlayed + total5GamesPlayed;
		return totalGamesPlayed;
	}

	public int getTotalWordsFound() {
		totalWordsFound = total4WordsFound + total5WordsFound;
		return totalWordsFound;
	}

	public int getHighScore() {
		return Math.max(high5Score, high4Score);
	}

	public int getHigh4Score() {
		return high4Score;
	}

	public int getHigh5Score() {
		return high5Score;
	}

	public String getLongestWord() {
		return longestWord;
	}

	public String getLongestWordFour() {
		return longestWordFour;
	}

	public String getLongestWordFive() {
		return longestWordFive;
	}

	public int getTotalTimePlayed() {
		return totalTimePlayed;
	}

	public int getTotalScore() {
		totalScore = total4Score + total5Score;
		return totalScore;
	}
}
