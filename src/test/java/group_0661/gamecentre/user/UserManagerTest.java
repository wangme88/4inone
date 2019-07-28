package group_0661.gamecentre.user;

import android.content.Context;
import android.graphics.Bitmap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import group_0661.gamecentre.gameSystem.Game;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class UserManagerTest {
    private UserManager testUserManager;
    private Game testGame;
    private final String USER1 = "user1";
    private final String PASS1 = "pass1";
    private final String USER2 = "user2";
    private final String PASS2 = "pass2";
    private final String TEST_PATH = "path";

    @Mock
    private Bitmap testImage;

    @Mock
    private Context context;

    @Before
    public void setUp() {
        testUserManager = Robolectric.buildService(UserManager.class).create().get();
        testGame = new Game(5, 3, false);

    }

    @Test
    public void signIn() {
        testUserManager.signUp(USER1, PASS1);
        testUserManager.signOut();
        assertTrue(testUserManager.signIn(USER1, PASS1));
        testUserManager.signOut();
        assertFalse(testUserManager.signIn(USER1, PASS2));
        assertFalse(testUserManager.signIn(USER2, PASS2));
    }

    @Test
    public void signUp() {
        assertTrue(testUserManager.signUp(USER1, PASS1));
        assertFalse(testUserManager.signUp(USER1, PASS1));
        assertFalse(testUserManager.signUp(USER1, PASS2));
    }

    @Test
    public void signOut() {
        testUserManager.signUp(USER1, PASS1);
        assertTrue(testUserManager.signOut());
        assertFalse(testUserManager.signOut());
    }

    @Test
    public void saveGame() {
        testUserManager.signUp(USER1, PASS1);
        testUserManager.saveGame(testGame, TEST_PATH);
        assertArrayEquals(testGame.getBoard(), testUserManager.getSavedGame("Sliding Tiles").getBoard());
    }

    @Test
    public void dropSavedGame() {
        testUserManager.signUp(USER1, PASS1);
        testUserManager.saveGame(testGame, "Sliding Tiles");
        testUserManager.dropSavedGame(testGame);
        assertNull(testUserManager.getSavedGame("Sliding Tiles"));
    }
}