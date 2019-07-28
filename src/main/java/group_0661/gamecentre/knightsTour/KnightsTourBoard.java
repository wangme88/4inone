package group_0661.gamecentre.knightsTour;

import android.support.v4.util.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Observable;

/**
 * The knight's tour board.
 */
public class KnightsTourBoard extends Observable implements Serializable{

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
    private Integer[][] tiles, isPressed;

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
    KnightsTourBoard(int dimension) {
        NUM_ROWS = dimension;
        NUM_COLS = dimension;
        previousMoves = new int[] {-1,- 1};

        ArrayList<Integer> tilesList = new ArrayList<>();
        tilesList.add(4);
        for (int i = 0; i < dimension * dimension - 1; i++) {
            tilesList.add(1);
        }
        Collections.shuffle(tilesList);
        initBoard(dimension, tilesList);

        isPressed = new Integer[dimension][dimension];
        for (int i = 0; i < dimension * dimension; i++) { isPressed[i / dimension][i % dimension] = 0;}

    }
    private void initBoard(int dimension, ArrayList<Integer> tList) {
        tiles = new Integer[dimension][dimension];

        for (int i = 0; i < dimension * dimension; i++) {
            boolean check = tList.get(i) != 4;
            if ((dimension % 2 == 1) & (i % 2 == 0) & check) {
                tiles[i / dimension][i % dimension] = 2;
            } else if ((dimension % 2 == 0) & check) {
                if ((i / dimension) % 2 == 0 &  (i % 2 == 0)) {
                    tiles[i / dimension][i % dimension] = 2;
                } else if ((i / dimension) % 2 == 1 &  (i % 2 == 1)) {
                    tiles[i / dimension][i % dimension] = 2;
                } else {
                    tiles[i / dimension][i % dimension] = tList.get(i);
                }
            } else {
                tiles[i / dimension][i % dimension] = tList.get(i);
            }
        }
    }

    private void refreshBoard() {
        for (int i = 0; i < NUM_ROWS * NUM_COLS; i++) {
            boolean check = isPressed[i / NUM_COLS][i % NUM_ROWS] == 0 & !(i == getKnightPosition());
            if ((NUM_COLS % 2 == 1) & (i % 2 == 0) & check) {
                tiles[i / NUM_COLS][i % NUM_ROWS] = 2;
            } else if ((NUM_COLS % 2 == 0) & check) {
                if ((i / NUM_ROWS) % 2 == 0 &  (i % 2 == 0)) {
                    tiles[i / NUM_ROWS][i % NUM_COLS] = 2;
                } else if ((i / NUM_ROWS) % 2 == 1 &  (i % 2 == 1)) {
                    tiles[i / NUM_ROWS][i % NUM_COLS] = 2;
                }
            }
            if ( isPressed[i / NUM_COLS][i % NUM_ROWS] == 1 & !(i == getKnightPosition())) {
                tiles[i / NUM_COLS][i % NUM_ROWS] = 3;
            }
        }
    }

    int getKnightPosition() {
        for (int i = 0; i < NUM_COLS * NUM_ROWS; i++) {
            if (this.tiles[i / NUM_ROWS][i % NUM_COLS] == 4) { return i; }
        }
        return 0;
    }

    private boolean isValid(int row, int col) {
        int knightRow = getKnightPosition() / NUM_ROWS;
        int knightCol = getKnightPosition() % NUM_COLS;
        if (!(row - 1 == knightRow | row + 1 == knightRow | col - 1 == knightCol | col + 1 == knightCol)) {
            return false;
        }
        if (!(row - 2 == knightRow | row + 2 == knightRow | col - 2 == knightCol | col + 2 == knightCol)) {
            return false;
        }
        return true;
    }



    /**
     * Check if valid move then move the knight. If not an undo call, add row and col of
     * the last location of the knight to previousMoves.
     *
     * @param row the tile row
     * @param column the tile column
     * @param undo true if an undo call, false if not
     * @return true if tiles are successfully swapped
     */
    boolean makeMove(int row, int column, boolean undo) {
        if (isValid(row, column)) {
            int knightRow = getKnightPosition() / NUM_ROWS;
            int knightCol = getKnightPosition() % NUM_COLS;

            tiles[knightRow][knightCol] = 1;
            tiles[row][column] = 4;


            if (isPressed[row][column] == 1) {
                isPressed[row][column] = 0;
            } else {
                isPressed[row][column] = 1;
            }
            if (!undo) {
                previousMoves[0] = knightRow;
                previousMoves[1] = knightCol;
                }
                moves_made += 1;
                refreshBoard();
                setChanged();
                notifyObservers();
                return true;
        }
        return false;
    }

    public void setBoard(Integer[][] newBoard, Integer[][] newPressed) {
        tiles = newBoard;
        isPressed = newPressed;
    }

    /**
     * Return (row, col) of the last location of the knight.
     *
     * @return (row, col) of the last location of the knight.
     */
    int[] getPreviousMoves() {
        int[] temp = new int[2];
        temp[0] = previousMoves[0];
        temp[1] = previousMoves[1];
        previousMoves[0] = -1;
        previousMoves[1] = -1;
        return temp;
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
    int getMoves_made() {return moves_made;}

    /**
     * Check if the game is solved.
     *
     * @return true if the game is solved, false if the game is not solved.
     */
    boolean puzzleSolved() {
        for (int i = 0; i < NUM_ROWS * NUM_COLS; i++) {
            if(isPressed[i / NUM_ROWS][i % NUM_COLS] != 1) {
                return false;
            }
        }
        return true;
    }

    /**
     * Return the knightsTour state.
     *
     * @return the board with the tiles it contains
     */
    @Override
    public String toString() {
        return "Board{" +
                "tiles=" + Arrays.toString(tiles) +
                '}';
    }

}
