package group_0661.gamecentre.scoreboard;

import org.junit.Test;

import static org.junit.Assert.*;

public class ScoreFieldTest {

    @Test
    public void compareTo() {
        ScoreField user1 = new ScoreField("user1", 90210);
        ScoreField user2 = new ScoreField("user2", 1776);
        ScoreField user3 = new ScoreField("user3", 255);
        ScoreField user4 = new ScoreField("user4", 90210);

        assertEquals(-1, user1.compareTo(user2));
        assertEquals(1, user3.compareTo(user2));
        assertEquals(-3, user1.compareTo(user4));
    }
}