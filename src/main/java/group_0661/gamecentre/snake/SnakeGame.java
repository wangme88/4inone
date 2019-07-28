package group_0661.gamecentre.snake;

import java.io.Serializable;
import java.util.ArrayDeque;

import group_0661.gamecentre.gameSystem.Game;
import group_0661.gamecentre.gameSystem.IGame;
import android.util.Pair;

/**
 * The game class for Snake.
 */
public class SnakeGame extends Game implements Serializable{

    /**
     * The snake board.
     */
    private SnakeBoard board;

    /**
     *  Create a new SnakeGame, with dimensions size * size.
     */
    public SnakeGame(int size) {
        super(2, 0, false);

        board = new SnakeBoard(size, size);
    }

    /**
     *  Return a copy of the board attached to this game.
     *
     * @return a 2-D Integer array of board.
     */
    @Override
    public Integer[][] getBoard() {
        return board.getTiles();
    }

    /**
     *  Updates this game. To be called every frame.
     *
     * @return whether or not this update succeeded. A false value means the game is over.
     */
    public boolean update() {
        boolean success = board.update();
        return success;
    }

    /**
     *  Returns whether the game is finished or not.
     *
     * @return whether the game is finished or not.
     */
    public boolean isWon() {
        return board.isOver();
    }

    /**
     *  Undoes the last n moves.
     *
     * @return whether the undo was successful or not.
     */
    public boolean undo() {
        return board.undo();
    }

    /**
     *  Get the score associated with this game.
     *
     * @return the score of the game.
     */
    public int getScore() {
        return board.getScore();
    }

    /**
     *  Make a given move; change the direction that the snake is moving,.
     *
     * @return whether or not the move succeeded.
     */
    public boolean makeMove(String direction) {
        if (direction.equals("right")) {
            int[] move = {0, 1};
            return board.makeMove(move);
        }
        if (direction.equals("left")) {
            int[] move = {0, -1};
            return board.makeMove(move);
        }
        if (direction.equals("down")) {
            int[] move = {1, 0};
            return board.makeMove(move);
        }
        if (direction.equals("up")) {
            int[] move = {-1, 0};
            return board.makeMove(move);
        }
        return false;
    }
}
