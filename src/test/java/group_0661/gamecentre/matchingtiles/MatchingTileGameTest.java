package group_0661.gamecentre.matchingtiles;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MatchingTileGameTest {
    private MatchingTileGame game;

    @Before
    public void setUpMatchingTileBoard() {
        game = new MatchingTileGame(5);
    }


    @Test
    public void touchMove() {

    }

    @Test
    public void getMoves() {

    }

    @Test
    public void getBoard() {

    }

    @Test
    public void getType() {
        assertEquals("Matching Tiles - Casual", game.getType());
    }

    @Test
    public void isWon() {
    }

    @Test
    public void getGameTitle() {
        assertEquals("Matching Tiles", game.getGameTitle());

    }
}