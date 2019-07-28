package group_0661.gamecentre.scoreboard;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import android.content.Context;

import java.util.List;

import group_0661.gamecentre.gameSystem.Game;
import group_0661.gamecentre.scoreboard.ScoreField;

import static org.junit.Assert.*;

public class ScoreboardTest {
    private Scoreboard testScoreBoard;
    private Game testGame;

    @Mock
    private Context testContext;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        testScoreBoard = Mockito.spy(new Scoreboard(testContext));
        testGame = new Game(5, 3, false);
    }

    @Test
    public void getGlobalHighScores() {
        assertNull(testScoreBoard.getGlobalHighScores("Sliding Tiles - 5 x 5"));
        testScoreBoard.addGame(testGame, "testUser");
        testScoreBoard.addGame(testGame, "AnotherTestUser");
        testScoreBoard.addGame(testGame, "YetAnotherTestUser");
        List<ScoreField> scoreFields = testScoreBoard.getGlobalHighScores( "Sliding Tiles - 5 x 5");
        for (ScoreField i : scoreFields) {
            System.out.println(i.getUserName());
        }
        assertEquals(3, scoreFields.size());
    }

    @Test
    public void getUserHighScores() {
        assertNull(testScoreBoard.getUserHighScores("testUser", "Sliding Tiles - 5 x 5"));
        testScoreBoard.addGame(testGame, "testUser");
        testScoreBoard.addGame(testGame, "AnotherTestUser");
        testScoreBoard.addGame(testGame, "YetAnotherTestUser");
        List<ScoreField> scoreFields = testScoreBoard.getUserHighScores("testUser", "Sliding Tiles - 5 x 5");
        assertEquals(1, scoreFields.size());
    }

    @Test
    public void addGame() {
        testScoreBoard.addGame(testGame, "testUser");
        assertNotNull(testScoreBoard.getUserHighScores("testUser", "Sliding Tiles - 5 x 5"));
    }
}