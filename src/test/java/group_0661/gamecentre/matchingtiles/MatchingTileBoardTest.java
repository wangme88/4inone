package group_0661.gamecentre.matchingtiles;


import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.junit.Assert.*;

public class MatchingTileBoardTest {
    private MatchingTileBoard testMatchingTileBoard;

    @Before
    public void setUpMatchingTileBoard() {
        testMatchingTileBoard = new MatchingTileBoard(4);
        Integer[][] almostFinishedBoard = new Integer[5][4];
        List<Integer> flipped = new ArrayList<>();
        for (int i = 0; i < 20; i ++) {
            almostFinishedBoard[i / 4][i % 4]  = i + 1;
            flipped.add(i+1);
        }
        almostFinishedBoard[0][0] = 21;
        almostFinishedBoard[0][1] = 21;
        testMatchingTileBoard.setBoard(almostFinishedBoard);
        testMatchingTileBoard.setFlipped(flipped);
    }


    @Test
    public void testNumTiles() {
        assertEquals(21, testMatchingTileBoard.numTiles());
    }

    @Test
    public void testGetTile() {
        assertEquals(7, testMatchingTileBoard.getTile(1,2));
    }

    @Test
    public void testMakeMove() {
        assertTrue(testMatchingTileBoard.makeMove(0,0));
        assertTrue(testMatchingTileBoard.makeMove(0,1));
    }

    @Test
    public void testMakeMoveWithFirstTileRevealed() {
        testMatchingTileBoard.makeMove(0,0);
        testMatchingTileBoard.makeMove(0, 1);
        assertTrue(testMatchingTileBoard.puzzleSolved());
    }
    /**
    public void testMakeMoveOnce() {
        testMatchingTileBoard.makeMove(0,0);
        assertTrue(testMatchingTileBoard.getState()[0][0]);
    }
     **/

    @Test
    public void testTwoTilesMatch() {
        testMatchingTileBoard.makeMove(0,0);
        assertTrue(testMatchingTileBoard.twoTilesMatch(2));
    }

    @Test
    public void getState() {
        Integer[][] state = testMatchingTileBoard.getState();
        assertTrue(testMatchingTileBoard.getState().length == 5);
        assertTrue(testMatchingTileBoard.getState()[1][1] == 6);
    }

    @Test
    public void testGetSize() {
        assertEquals(4, testMatchingTileBoard.getSize());
    }

    @Test
    public void testGetMoves_made() {
        testMatchingTileBoard.makeMove(0,1);
        testMatchingTileBoard.makeMove(0,0);
        assertEquals(2, testMatchingTileBoard.getMoves_made());
    }

    @Test
    public void testPuzzleSolved() {
        assertFalse(testMatchingTileBoard.puzzleSolved());
    }

    @Test
    public void testToString() {
        assertEquals(
                "Board{tiles=[[21, 21, 3, 4], [5, 6, 7, 8], " +
                        "[9, 10, 11, 12], [13, 14, 15, 16]," +
                        " [17, 18, 19, 20]]}", testMatchingTileBoard.toString());
    }


}