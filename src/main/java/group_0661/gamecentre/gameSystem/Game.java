package group_0661.gamecentre.gameSystem;
import group_0661.gamecentre.gestures.Undo;

import java.io.Serializable;
import java.util.Observable;

/**
 * The game class.
 */
public class Game extends Observable implements Serializable, IGame {

    /**
     * The board for game.
     */
    private Board board;

    /**
     * Initialize a new undo system for this game.
     */
    private Undo undoSys;

    /**
     * Time elapsed since the game began.
     */
    public int time = 0;

    /**
     * A new game with given dimensions and undo limit.
     *
     * @param dimensions size of the board
     * @param undoLimit number of possible undo moves
     * @param unlimited true if unlimited undo moves selected
     */
    public Game(int dimensions, int undoLimit, boolean unlimited){
        board = new Board(dimensions);
        this.undoSys = new Undo(undoLimit, unlimited);
    }

    /**
     * A new game with given dimensions and without undo functionality.
     *
     * @param dimensions size of the board
     */
    public Game(int dimensions){
        board = new Board(dimensions);
    }

    /**
     * Return current state of tiles in board.
     *
     * @return current state of tiles in board
     */
    public Integer[][] getBoard() {return board.getState();}

    /**
     * Return dimensions of the board.
     *
     * @return dimensions of the board
     */
    public String getType() {
        return getGameTitle() + " - " + getBoard().length + " x " + getBoard().length;
    }

    public String getGameTitle() { return "Sliding Tiles"; }

    /**
     * Return score using moves made and time elapsed.
     *
     * @return score using moves made and time elapsed.
     */
    public int getScore() {
        int score = 10000 - 10*board.getMoves_made() - 10*this.getTime();
        if (score > 0){
            return score;
        } else {
            return 0;
        }
    }

    /**
     * Return number of moves made.
     *
     * @return number of moves made
     */
    public int getMoves() { return board.getMoves_made(); }

    /**
     * Return true if puzzle is solved.
     *
     * @return true if puzzle is solved
     */
    public boolean isWon() {
        return board.puzzleSolved();
    }

    /**
     * Return number of seconds that have elapsed.
     *
     * @return number of seconds that have elapsed
     */
    public int getTime() {
        return time;
    }

    /**
     * Touch position on board to move
     *
     * @param position the position on the board being tapped
     * @return true if tapping is valid and recorded
     */
    public boolean touchMove(int position) {
        int size = board.getSize();
        if (position < size * size) {
            board.makeMove(position / size, position % size, false);
            updateUndo();
            setChanged();
            notifyObservers();
            return true;
        }
        return false;
    }

    /**
     * Return the current undo system
     *
     * @return the current undo system
     */
    public Undo getUndo() { return undoSys; }

    /**
     * Adds the last valid move to undo arrayList
     */
    public void updateUndo() {
        int[] prev = board.getPreviousMoves();
        if (prev[0] != -1 & prev[1] != -1) {
            undoSys.push(prev);
        }
    }

    /**
     * Revert the board to before the last move. Notify change to be made to board.
     *
     * @return true if undo is executed
     */
    public boolean undo() {
        int[] temp = undoSys.pop();
        board.makeMove(temp[0], temp[1], true);
        setChanged();
        notifyObservers();
        return true;
    }


}
