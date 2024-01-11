package bbrz.Frameworks;

/*
  Author: Moritz Wenger
  Date: 22.12.2021
  Version: 0.2
  Description:
            Includes Game-Logic and Calculations
 */

import java.io.File;
import java.util.*;

public class Framework {
    //#################################################################################################################
    // Variables which need to be altered by your game logic:
    //#################################################################################################################

    // imported list of words
    static ArrayList<String> wordList = new ArrayList<>();

    // mistake counter
    static int mistakes = 0;

    // typing speed calc
    static double totalInputTime = 0;
    static int inputChars = 0;

    // variables to measure time since start
    static long START_TIME = 0;
    static long NEW_TIME = 0;
    static long NANOSECONDS_SINCE_START = 0;
    static double SECONDS_SINCE_START = 0;

    //#################################################################################################################
    // The following variables are CONSTANTS which should be read but not altered:
    //#################################################################################################################

    static final char[] ALPHABET = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    static final int GAME_BOARD_COLUMNS = 40;

    static final double PERCENTAGE_PER_SUCCESSFUL_INPUT = 100.0 / 3.0;

    // static final String HIGH_SCORE_SEPARATOR = " - ";
    static long NANOSECONDS_PASSED = 0;


    //#################################################################################################################
    // The following fuctions are part of the framework and should be used but not altered.
    //#################################################################################################################

    /**
     * Reads the userinput while updating SECONDS_PASSED and printing prompt to standard output.
     */
    public static String timed_input(String prompt, long startTime) {
        Scanner userInput = new Scanner(System.in);
        System.out.println(prompt);
        String userInputString = userInput.nextLine();
        long currentTime = System.nanoTime();
        NANOSECONDS_PASSED = currentTime - startTime;

        // add input-time to total time for CPS-calculation
        totalInputTime += NANOSECONDS_PASSED;

        // System.out.println("Execution time in nanoseconds: " + SECONDS_PASSED);
        System.out.println("Execution time in milliseconds: " + NANOSECONDS_PASSED / 1000000);
        return userInputString;
    }

    //#################################################################################################################
    // The following fuctions are part of the framework and should be used and altered
    //#################################################################################################################

    /**
     * Calculates time since game start.
     *
     * @return Returns time since game start as seconds.
     */
    public static double calcTotalTimeElapsed(){
        NEW_TIME = System.nanoTime();
        NANOSECONDS_SINCE_START = NEW_TIME - START_TIME;
        SECONDS_SINCE_START = (double) NANOSECONDS_SINCE_START / 1000000000;

        return SECONDS_SINCE_START;
    }

    public static double calcCPS(){
        return totalInputTime / inputChars / 1000000000;
    }

    /**
     * Returns the corresponding letter from ALPHABET for a given word
     *
     * @param key Index number for alphabet char-array.
     * @return Alphabet letter corresponding to given index as String.
     */
    public static String getWordFromLetter(String key) {
        String word = TypingGame.board.get(key).get(1);

        return word;
    }

    /**
     * returns true if a given word has >= 100% progress
     *
     * @param board  HashMap representing the playing field.
     * @param rowKey HashMap key of to the row being altered.
     * @return boolean
     */
    public static boolean getCompletionStatus(HashMap<String, ArrayList<String>> board, String rowKey) {
        double compStatus = (Double.parseDouble(board.get(rowKey).get(2)));
        if (compStatus == 100) {
            if (board.get(rowKey).get(3).equals("0")) {
                NEW_TIME = System.nanoTime();
                board.get(rowKey).set(3, String.valueOf(calcTotalTimeElapsed()));
            }
            return true;
        }
        return false;
    }

    /**
     * Increments the progress of a given word by value of PERCENTAGE_PER_SUCCESSFUL_INPUT
     *
     * @param board     HashMap representing the playing field.
     * @param rowKey    HashMap key of to the row being altered.
     * @param boardSize Size of the playing field as int value.
     */
    public static void increment_progress(HashMap<String, ArrayList<String>> board, String rowKey, int boardSize) {

        // calculate maximum amount of dots in given line, rounding up
        double maxDotLength = boardSize - board.get(rowKey).get(1).length();
        double dotCalc = Math.round(maxDotLength / 3);
        int dotIncrement = (int) dotCalc;

        // increment completion% and add dots
        String newDots = board.get(rowKey).get(0).concat(".".repeat(dotIncrement));
        board.get(rowKey).set(0, newDots);
        double newCompletion = Double.parseDouble(board.get(rowKey).get(2)) + PERCENTAGE_PER_SUCCESSFUL_INPUT;
        board.get(rowKey).set(2, String.valueOf(newCompletion));
    }

    /**
     * Resets the progress of a given word to 0.0
     *
     * @param board  HashMap representing the playing field.
     * @param rowKey HashMap key of to the row being altered.
     */
    public static void reset_progress(HashMap<String, ArrayList<String>> board, String rowKey) {
        board.get(rowKey).set(0, "");
        board.get(rowKey).set(2, "0");
        mistakes += 1;
        System.out.println("Wrong input. Starting again from the beginning!");
    }

    /**
     * returns false if a single word hasn't been completed yet. True if all words are complete.
     *
     * @param board HashMap representing the playing field.
     */
    public static boolean getWinStatus(HashMap<String, ArrayList<String>> board) {

        for (Map.Entry<String, ArrayList<String>> entry : board.entrySet()) {
            String key = entry.getKey();
            if (!getCompletionStatus(board, key)) {
                return false;
            }
        }
        TypingGame.printBoard();
        NEW_TIME = System.nanoTime();
        System.out.println("Contratulations! You won in " + calcTotalTimeElapsed() + " seconds with " + mistakes +  " mistakes. Your typing speed is " + calcCPS() + " characters per second (CPS).");
        return true;
    }

    /**
     * Creates a Random-object to then create a random number between 0 and the size of the given HashMap.
     * Uses random number to access random character from global Alphabet array to use as key.
     * Returns the key.
     *
     * @param board HashMap representing the playing field.
     * @return Returns random row.
     */
    public static String getRandomRow(HashMap<String, ArrayList<String>> board) {
        Random rng = new Random();
        int randomIndex = 0;
        String randomKey = "";

        do {
            randomIndex = rng.nextInt(board.size());
            randomKey = String.valueOf(ALPHABET[randomIndex]);
        } while (getCompletionStatus(board, randomKey));

        return randomKey;
    }

    /**
     * Reads a user input and updates CHARACTERS_TYPED_BY_USER and CPS based on this input.
     * <p>
     * This function should call timed_input(prompt) instead of scanner.nextLine() to automatically update SECONDS_PASSED.
     * timed_input should be called with the proper prompt as described in the assignment description.
     * <p>
     * The function receives word (String) and returns the user input (String).
     */
    public static String getInput(String key, long startTime) {
        String prompt = "Input: " + key;
        String userInput = "";
        try {
            userInput = timed_input(prompt, startTime);
        } catch (Exception e) {
            System.out.println(e);
        }
        return userInput;
    }

    /**
     * Reads user input by calling getInput and checks whether this input is correct.
     * <p>
     * In case the input is correct, the progress should be updated by calling increment_progress for each word.
     * In case the input is incorrect, the progress should be reset by calling reset_progress for each word and
     * MISTAKES_MADE should be incremented.
     */
    public static void handleInput(HashMap<String, ArrayList<String>> board, String key, long startTime, int boardSize) {

        // add word length of word to character count for CPS-calculation
        inputChars += getWordFromLetter(key).length();

        if (getInput(key, startTime).equals(getWordFromLetter(key))) {
            increment_progress(board, key, boardSize);
            System.out.println("Correct input!");
        } else {
            reset_progress(board, key);
        }


    }

    /**
     * Creates an object of class ReadFiles and checks if the file exists or not.
     * If it exists, Strings from a given file (see File Objects) are added into a given list (see ArraysLists)
     */
    public static void getWordsFromList() {

        File words = new File("words.txt");

        ReadFiles test = new ReadFiles();

        test.saveToContainer(words, wordList);

    }

    public static void main(String[] args) {

    }
}

