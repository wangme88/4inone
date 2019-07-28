package group_0661.gamecentre.scoreboard;

import java.util.List;
import group_0661.gamecentre.gameSystem.Game;


/**
 * Interface for a retrievable list of scores,
 * queryable by slidingtiles type and user.
 */
public interface IScoreboard {

    /**
     * Returns the high scores among all players of a slidingtiles type.
     * Note that it returns null if the slidingtiles type has not been saved.
     *
     * @param gameType The slidingtiles type identifier
     * @return A sorted list of ScoreFields corresponding to the query, or null if none exists
     */
    List<ScoreField> getGlobalHighScores(String gameType);

    /**
     * Returns the high scores of a slidingtiles type for a single user.
     * Note that it returns null if the user has not played this slidingtiles.
     *
     * @param userName The user
     * @param gameType The slidingtiles type identifier
     * @return A sorted list of ScoreFields corresponding to the query, or null if none exists
     */
    List<ScoreField> getUserHighScores(String userName, String gameType);

    /**
     * Saves a completed slidingtiles to the data.
     * This method should be called whenever a slidingtiles is finished
     *
     * @param game The completed slidingtiles
     * @param user The user name
     */
    void addGame(Game game, String user);
}
