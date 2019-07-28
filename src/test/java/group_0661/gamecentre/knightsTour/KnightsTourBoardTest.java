package group_0661.gamecentre.knightsTour;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class KnightsTourBoardTest {
    private KnightsTourBoard testBoard;
    private Integer[][] isPressed, board;

    @Before
    public void setUp() {
        testBoard  = new KnightsTourBoard(8);
        board = new Integer[8][8];
        isPressed = new Integer[8][8];
        board[0][0] = 1;
        isPressed[0][0] = 0;
        for (int i = 1; i < 64; i++) {
            board[i / 8][i % 8] = 3;
            isPressed[i / 8][i % 8] = 1;
        }
        board[1][2] = 4;
        testBoard.setBoard(board, isPressed);
    }

    @Test
    public void makeMove() {
        assertTrue(testBoard.makeMove(0,0,false));
        assertFalse(testBoard.makeMove(0,0,false));
        assertFalse(testBoard.makeMove(7,7,false));
    }

    @Test
    public void puzzleSolved() {
        assertFalse(testBoard.puzzleSolved());
        testBoard.makeMove(0,0,false);
        assertTrue(testBoard.puzzleSolved());
    }
}