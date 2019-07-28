package group_0661.gamecentre.snake;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Random;
import java.util.Stack;


public class SnakeBoard implements Serializable{

    /**
     * Static alias for drawing board (empty tile).
     */
    public static int EMPTY = 0;

    /**
     * Static alias for drawing board (wall tile).
     */
    public static int WALL = 1;

    /**
     * Static alias for drawing board (body tile).
     */
    public static int BODY = 2;

    /**
     * Static alias for drawing board (cherry tile).
     */
    public static int CHERRY = 3;

    /**
     * The location of the head as an integer array.
     */
    private int[] head = new int[2];

    /**
     * Deque representing the position of the snake's body.
     */
    private ArrayDeque<int[]> body = new ArrayDeque<>();

    /**
     * Integer array representing the direction the snake is moving.
     */
    private int[] direction = new int[2];

    /**
     * number of rows, and columns.
     */
    private int rows, cols;

    /**
     * Drawable array of Integers representing the states of the tiles to be drawn.
     */
    private Integer[][] tiles;

    /**
     * Random object to be used to set new cherries.
     */
    private Random rand = new Random();

    /**
     * Whether the game is finished or not.
     */
    private boolean finished = false;

    /**
     * Current score of the game.
     */
    private int score = 0;

    // Undo stacks
    /**
     * Stack of tiles to be used in the undo function.
     */
    private Stack<Integer[][]> tileStack = new Stack<>();

    /**
     * Stack of positions of head.
     */
    private Stack<int[]> headStack = new Stack<>();;

    /**
     * Stack of positions of body.
     */
    private Stack<ArrayDeque<int[]>> bodyStack = new Stack<>();

    /**
     * Stack of directions.
     */
    private Stack<int[]> directionStack = new Stack<>();

    /**
     * Stack of scores.
     */
    private Stack<Integer> scoreStack = new Stack<>();

    /**
     * A new board of tiles in row-major order.
     *
     * @param cols the number of cols
     * @param rows the number of rows
     */
    SnakeBoard(int cols, int rows) {
        this.cols = cols;
        this.rows = rows;

        tiles = new Integer[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                tiles[i][j] = EMPTY;
            }
        }

        for (int i = 0; i < rows; i++) {
            tiles[i][0] = WALL;
            tiles[i][cols-1] = WALL;
        }

        for (int i = 0; i < cols; i++) {
            tiles[0][i] = WALL;
            tiles[rows-1][i] = WALL;
        }

        // Initialize the starting snake
        int startLen = 4;

        for (int i = 1; i < startLen+1; i++) {
            tiles[1][i] = BODY;

            head[0] = 1;
            head[1] = i;

            body.addFirst(new int[] {head[0], head[1]});
        }

        // Initialize the cherry
        //resetCherry();
        tiles[1][cols-3] = CHERRY;

        // Start direction is to the right
        direction[0] = 0;
        direction[1] = 1;
    }

    /**
     * Returns the tiles of this board.
     *
     * @return an 2-D array of integers representing tiles.
     */
    public Integer[][] getTiles() {
        return tiles;
    }

    /**
     * Updates the game: moves the snake forward and performs corresponding action.
     * To be called once a frame.
     *
     * @return the success or failure of the update (if false, the game has ended).
     */
    public boolean update() {
        pushState();

        int[] new_head = new int[2];
        new_head[0] = head[0] + direction[0];
        new_head[1] = head[1] + direction[1];

        int targetTile = tiles[new_head[0]][new_head[1]];

        if (targetTile == WALL) {
            finished = true;
            return false;
        }

        head = new_head;
        tiles[head[0]][head[1]] = BODY;
        body.addFirst(new int[] {head[0], head[1]});

        if (targetTile == CHERRY) {
            resetCherry();
            score++;
        } else {
            int[] tail = body.removeLast();
            tiles[tail[0]][tail[1]] = 0;
        }

        return true;
    }

    /**
     * Resets the cherry after it has been consumed.
     */
    private void resetCherry() {
        do {
            int i = rand.nextInt(rows);
            int j = rand.nextInt(cols);
            if (tiles[i][j] == 0) {
                tiles[i][j] = CHERRY;
            }
            return;
        } while (true);
    }

    /**
     * Make a move: i.e. change the direction the snake is facing.
     *
     * @return an 2-D array of integers representing tiles.
     */
    public boolean makeMove(int[] move) {
        if (direction[0] == move[0] && direction[1] == move[1]) {
            return false;
        }
        else {
            direction = move;
            return true;
        }
    }

    /**
     * Returns whether the game is over or not.
     *
     * @return true if the game is over.
     */
    boolean isOver() {
        return finished;
    }

    /**
     * Returns the player's score; the number of cherries they have gotten.
     *
     * @return the score.
     */
    int getScore() {
        return score;
    }

    /**
     * Undoes the last n moves.
     *
     * @return success or failure of the undo.
     */
    public boolean undo() {
        if (finished) return false;

        int nUndo = 3;

        for (int i = 0; i < nUndo; i++) {
            if (tileStack.empty()) return true;

            tiles = tileStack.pop();
            head = headStack.pop();
            body = bodyStack.pop();
            direction = directionStack.pop();
            score = scoreStack.pop();
        }

        return true;
    }

    /**
     * Pushes the current state onto the stack for use in undo.
     */
    private void pushState() {
        Integer[][] saveTiles = new Integer[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                saveTiles[i][j] = tiles[i][j];
            }
        }

        tileStack.push(saveTiles);

        headStack.push(head.clone());
        bodyStack.push(body.clone());
        directionStack.push(direction.clone());
        scoreStack.push(score);
    }
}
