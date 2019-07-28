package group_0661.gamecentre.knightsTour;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class KnightsTourGameTest {
    private KnightsTourGame game;
    private int knightPos;

    @Before
    public void setUp() {
        game = new KnightsTourGame(8);
        knightPos = game.getKnight();
    }

    @Test
    public void touchMove() {
        if (knightPos - 6 > 0) {
            assertTrue(game.touchMove(knightPos - 6));
        } else {
            assertTrue(game.touchMove(knightPos + 6));
        }
        assertFalse(game.touchMove(65));
    }

    @Test
    public void undo() {
        if (knightPos - 6 > 0) {
            game.touchMove(knightPos - 6);
            game.undo();
            assertEquals(knightPos, game.getKnight());
        } else {
            game.touchMove(knightPos + 6);
            game.undo();
            assertEquals(knightPos, game.getKnight());
        }
    }
}