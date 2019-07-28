package group_0661.gamecentre.user;

import group_0661.gamecentre.gameSystem.Game;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;

/**
 * User manager interface.
 */
public interface IUserManager {

    /**
     * List of (user, password).
     */
    List<String[]> usersList = new ArrayList<String[]>();

    /**
     * Return true if sign is successful given username and password.
     *
     * @param username string
     * @param password string
     * @return true if sign in is successful
     */
    boolean signIn(String username, String password);

    /**
     * Return true if sign up is successful given username and password.
     * @param username string
     * @param password string
     * @return true if sign up is successul
     */
    boolean signUp(String username, String password);

    /**
     * Return username.
     *
     * @return username
     */
    String getName();

    /**
     * Return true if logged in.
     *
     * @return true if logged in
     */
    boolean getStatus();

    /**
     * Return savedGame in User.
     *
     * @return savedGame in User
     */
    Game getSavedGame(String gameName);

    /**
     * Set savedGame to null.
     */
    void dropSavedGame(Game game);
}
