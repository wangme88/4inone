package group_0661.gamecentre.gameSystem;

import android.support.v4.util.Pair;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class BoardTest {
    private Board testBoard;
    private Integer[][] initialState;

    @Before
    public void setUp() {
        testBoard = new Board(5);
        initialState = new Integer[5][5];
        for (int i = 0; i < 25; i++) {
            initialState[i / 5][i % 5] = i + 1;
        }
        testBoard.setBoard(initialState);
    }

    @Test
    public void getTileTrue() {
        assertEquals(25,testBoard.getTile(4,4));
        assertNotEquals(25, testBoard.getTile(0,0));
    }

    @Test
    public void makeMove() {
        assertTrue(testBoard.makeMove(4,3,false));
        assertTrue(testBoard.makeMove(3,3,false));
        assertTrue(testBoard.makeMove(3,4,false));
        assertTrue(testBoard.makeMove(4,4,false));
        assertFalse(testBoard.makeMove(4,4,false));
        assertFalse(testBoard.makeMove(0,0,false));
    }

    @Test
    public void getPreviousMoves() {
        int[] expPrevMove = {4, 4};
        int noPrevMove = -1;

        testBoard.makeMove(4,3,false);
        int[] actPrevMove = testBoard.getPreviousMoves();
        assertEquals(expPrevMove[0], actPrevMove[0]);
        assertEquals(expPrevMove[1], actPrevMove[1]);

        testBoard.makeMove(4,4,false);
        actPrevMove = testBoard.getPreviousMoves();
        assertNotEquals(expPrevMove[1], actPrevMove[1]);

        actPrevMove = testBoard.getPreviousMoves();
        assertEquals(noPrevMove, actPrevMove[0]);
        assertEquals(noPrevMove, actPrevMove[1]);
    }

    @Test
    public void getState() {
        Integer[][] nonEqualState = new Integer[5][5];
        for (int i = 0; i < 25; i++) { initialState[i / 5][i % 5] = 25 - i; }

        assertArrayEquals(initialState, testBoard.getState());
        assertNotEquals(nonEqualState, testBoard.getState());
    }

    @Test
    public void getSize() {
        assertEquals(5, testBoard.getSize());
        assertNotEquals(2, testBoard.getSize());
    }

    @Test
    public void getMoves_made() {
        assertNotEquals(4, testBoard.getMoves_made());
        testBoard.makeMove(4,3,false);
        testBoard.makeMove(3,3,false);
        testBoard.makeMove(3,4,false);
        testBoard.makeMove(4,4,false);
        assertEquals(4, testBoard.getMoves_made());
    }

    @Test
    public void puzzleSolvedFalse() {
        assertTrue(testBoard.puzzleSolved());
        testBoard.makeMove(4,3,false);
        assertFalse(testBoard.puzzleSolved());
    }
}