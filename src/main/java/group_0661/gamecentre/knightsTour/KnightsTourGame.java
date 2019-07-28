package group_0661.gamecentre.knightsTour;

import java.io.Serializable;
import java.util.Observable;

import group_0661.gamecentre.gestures.Undo;
import group_0661.gamecentre.gameSystem.Game;

/**
 * The game class.
 */
public class KnightsTourGame extends Game implements Serializable {

    /**
     * The board for game.
     */
    private KnightsTourBoard board;

    /**
     * Initialize a new undo system for this game.
     */
    private Undo undoSys;

    /**
     * A new game with given dimensions.
     *
     * @param dimensions size of the board

     */
    public KnightsTourGame(int dimensions){
        super(dimensions);
        board = new KnightsTourBoard(dimensions);
        this.undoSys = new Undo(5, true);
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
        if (getBoard().length == 5) {
            return getGameTitle() + " - " + "Easy";
        } return getGameTitle() + " - " + "Classic";
    }

    public String getGameTitle() { return "Knight's Tour"; }

    /**
     * Return score using moves made and time elapsed.
     *
     * @return score using moves made and time elapsed.
     */
    public int getScore() {
        int score = 10000 - 10*board.getMoves_made();
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
     * Return position of knight.
     *
     * @return position of knight
     */
    public int getKnight() { return board.getKnightPosition(); }

    /**
     * Return true if puzzle is solved.
     *
     * @return true if puzzle is solved
     */
    public boolean isWon() {
        return board.puzzleSolved();
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
