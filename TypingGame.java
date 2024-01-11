package bbrz.Frameworks;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Author: hemmer
 * Date: 2023-02-02
 * Version: 0.1
 */
public class TypingGame {

    // empty HashMap for later use as playing board
    static HashMap<String, ArrayList<String>> board = new HashMap<>();

    /**
     * Initialises the board, filling the 2D ArrayList with empty values and row headers depending on the number of words in given wordList.
     *
     * @param wordList ArrayList containing words from imported txtfile.
     */
    public static void initialiseBoard(ArrayList<String> wordList, char[] alphabet) {
        for (int i = 0; i < wordList.size(); i++) {

            // create strings
            String position = String.valueOf(alphabet[i]);
            String dotFiller = "";
            String word = wordList.get(i);
            String completion = "0";
            String compDuration = "0";

            // create list to put into 2D arraylist
            ArrayList<String> alph = new ArrayList<>();
            alph.add(dotFiller);
            alph.add(word);
            alph.add(completion);
            alph.add(compDuration);

            board.put(position, alph);
        }
    }

    /**
     * Prints the state of the board.
     */
    public static void printBoard() {

        String border = "  " + "-".repeat(Framework.GAME_BOARD_COLUMNS + 4);

        System.out.println(border);

        printLines();

        System.out.println(border);
    }

    /**
     * Print HashMap contents into board.
     */
    private static void printLines() {
        board.forEach((key, array) -> {

            // variables for filling print statement
            String position = key;
            String dotFiller = array.get(0);
            String word = array.get(1);
            String spaceFiller = "";
            double completion = Double.parseDouble(array.get(2));

            // length of word and leading dots
            int lengthCheck = (dotFiller + word).length();

            // if lengthcheck exceeds bounds of board, reassign with appropriate number of "."
            if (lengthCheck > Framework.GAME_BOARD_COLUMNS) {
                dotFiller = ".".repeat(Framework.GAME_BOARD_COLUMNS - word.length());

                // pads remaining space to border of board, if necessary
            } else if (lengthCheck < Framework.GAME_BOARD_COLUMNS) {
                spaceFiller = " ".repeat(Framework.GAME_BOARD_COLUMNS - lengthCheck);
            }
            if (Framework.getCompletionStatus(board, position)) {
                double compDuration = Double.parseDouble(array.get(3));
                System.out.printf("%s | %s%s%s | %.0f%% %.2fs %n", position, dotFiller, word, spaceFiller, completion, compDuration);
            } else {
                System.out.printf("%s | %s%s%s | %.2f%% %n", position, dotFiller, word, spaceFiller, completion);
            }
        });
    }

    public static void main(String[] args) {

        // import words from file
        Framework.getWordsFromList();

        // fill board with words
        initialiseBoard(Framework.wordList, Framework.ALPHABET);

        // start the clock
        Framework.START_TIME = System.nanoTime();

        // game time
        while (!Framework.getWinStatus(board)) {

            printBoard();

            Framework.handleInput(board, Framework.getRandomRow(board), System.nanoTime(), Framework.GAME_BOARD_COLUMNS);
        }

    }
}