package group_0661.gamecentre.gameSystem;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import java.lang.reflect.Array;
import java.util.Observable;
import java.lang.Math;

import android.util.Log;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * The sliding tiles board.
 */
public class Board extends Observable implements Serializable{

    /**
     * The number of rows.
     */
    private int NUM_ROWS;

    /**
     * The number of columns.
     */
    private int NUM_COLS;

    /**
     * The list of previous moves
     */
    private int[] previousMoves;

    /**
     * The tiles on the board in row-major order.
     */
    private Integer[][] tiles;

    /**
     * The number of moves made.
     */
    int moves_made = 0;

    /**
     * A new board of tiles in row-major order.
     * Precondition: len(tiles) == NUM_ROWS * NUM_COLS
     *
     * @param dimension the size of the board
     */
    public Board(int dimension) {
        NUM_ROWS = dimension;
        NUM_COLS = dimension;
        previousMoves = new int[] {-1,- 1};
        tiles = new Integer[NUM_ROWS][NUM_COLS];
        ArrayList<Integer> tilesList = generateSolvableGame(dimension);
        for (int i = 0; i < dimension * dimension; i++) {
            tiles[i / dimension][i % dimension] = tilesList.get(i);
        }

    }

    /**
     * Generates a board that is always solvable
     *
     * See https://www.cs.bham.ac.uk/~mdr/teaching/modules04/java2/TilesSolvability.html for the strategy used
     * @param size of the board
     */
    private ArrayList<Integer> generateSolvableGame(int size) {
        ArrayList<Integer> tilesList = new ArrayList<>();
        int sum;
        // true iff number of rows/columns is odd
        boolean odd = size % 2 == 1;
        // true iff the blank tile falls on an odd row (counting from the bottom). Initialized in do-while loop
        boolean tileOdd = false;
        int lowest = 1;

        // Generates the tiles in order
        for (int i = 0; i < size * size; i++) {
            tilesList.add(new Integer(i + 1));
        }

        do {
            sum = 0;
            Collections.shuffle(tilesList);
            ArrayList<Integer> counted = new ArrayList<>();
            for (int i = 0; i < tilesList.get(i); i++) {
                // If the current tile is the lowest accounted for, the sum is unchanged
                if ( tilesList.get(i) == lowest) {
                    counted.add(tilesList.get(i));
                    lowest++;
                } else if (tilesList.get(i) < tilesList.size()){
                    sum +=  tilesList.get(i) - getInversions(counted, tilesList.get(i));
                } else {
                    tileOdd = (size - (i / size)) % 2 == 1;
                }
            }
            // The game is solvable if:
            // 1. size is odd and sum is even
            // 2. size is even, the blank tile is on an odd row (counting from the bottom), and the sum is even
            // 3. size is even, the blank tile is on an even row, and sum is odd
        } while ((odd & sum % 2 == 0) | (!odd & tileOdd & sum % 2 == 0) | (!odd & !tileOdd & sum % 2 == 1));

        return tilesList;
    }

    /**
     * Counts the number of values in ArrayList counted that are less than num
     *
     * @param counted all tile numbers already accounted for
     * @param num the number of the tile in question
     */
    private int getInversions(ArrayList<Integer> counted, int num) {
        int sum = 0;
        for (Integer i : counted) {
            if (i < num) { sum += 1; }
        }
        return sum;
    }

    /**
     * Return the tile at (row, col)
     *
     * @param row the tile row
     * @param col the tile column
     * @return the tile at (row, col)
     */
    int getTile(int row, int col) {
        return tiles[row][col];
    }

    /**
     * Swap the tiles at (row1, col1) and (row2, col2)
     *
     * @param row1 the first tile row
     * @param col1 the first tile col
     * @param row2 the second tile row
     * @param col2 the second tile col
     */
    private void swapTiles(int row1, int col1, int row2, int col2) {
        int temp = tiles[row1][col1];
        tiles[row1][col1] = tiles[row2][col2];
        tiles[row2][col2] = temp;
        setChanged();
        notifyObservers();
    }

    /**
     * Check if valid move then swap the tiles. If not an undo call, add row and col of
     * the last location of the blank tile to previousMoves.
     *
     * @param row the tile row
     * @param column the tile column
     * @param undo true if an undo call, false if not
     * @return true if tiles are successfully swapped
     */
    public boolean makeMove(int row, int column, boolean undo) {
        List<Pair<Integer, Integer>> toCheck = getTilesToCheck(row, column);
        for (Pair<Integer, Integer> indices: toCheck) {
            if (tiles[indices.first][indices.second] == NUM_COLS * NUM_ROWS) {
                swapTiles(row, column, indices.first, indices.second);
                if (!undo) {
                    previousMoves[0] = indices.first;
                    previousMoves[1] = indices.second;
                }
                moves_made += 1;
                return true;
            }
        }
        return false;
    }

    public void setBoard(Integer[][] newBoard) {
        tiles = newBoard;
    }

    /**
     * Return (row, col) of the last location of the blank tile.
     *
     * @return (row, col) of the last location of the blank tile.
     */
    public int[] getPreviousMoves() {
        int[] temp = new int[2];
        temp[0] = previousMoves[0];
        temp[1] = previousMoves[1];
        previousMoves[0] = -1;
        previousMoves[1] = -1;
        return temp;
    }

    /**
     * Return list of (row, col) of tiles that are candidates for swapping.
     *
     * @param row the current tile row
     * @param column the current tile column
     * @return list of (row, col) of tiles that are candidates for swapping
     */
    private List<Pair<Integer, Integer>> getTilesToCheck(int row, int column) {
        List<Pair<Integer, Integer>> indices = new ArrayList<>();
        if (column != 0) {
            Pair<Integer, Integer> toAdd = new Pair<>(row, column - 1);
            indices.add(toAdd);
        }
        if (column != NUM_COLS - 1) {
            Pair<Integer, Integer> toAdd = new Pair<>(row, column + 1);
            indices.add(toAdd);
        }
        if (row != 0) {
            Pair<Integer, Integer> toAdd = new Pair<>(row - 1, column);
            indices.add(toAdd);
        }
        if (row != NUM_ROWS - 1) {
            Pair<Integer, Integer> toAdd = new Pair<>(row + 1, column);
            indices.add(toAdd);
        }

        return indices;
    }

    /**
     * Return tiles on board in row-major order.
     *
     * @return tiles on board in row-major order
     */
    Integer[][] getState() {return tiles;}

    /**
     * Return number of columns in the board.
     *
     * @return number of columns in the board
     */
    public int getSize() {return NUM_COLS;}

    /**
     * Return number of moves made.
     *
     * @return number of moves made
     */
    public int getMoves_made() {return moves_made;}

    /**
     * Check if the tiles slidingtiles is solved.
     *
     * @return true if the slidingtiles is solved, false if the slidingtiles is not solved.
     */
    boolean puzzleSolved() {
        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLS; j++) {
                if (getTile(i, j) != (i * NUM_ROWS) + j + 1) {
                    return false;
                }
            }
        }
        return true;
    }
}
