package group_0661.gamecentre.gameSystem;

/**
 * The interface for any slidingtiles

 */
public interface IGame {

    /**
     * Get the score of the slidingtiles
     *
     * @return the recorded score
     */
    String getGameTitle();

    int getScore();

    /**
     * Get the board of the slidingtiles
     *
     * @return the board
     */
    Integer[][] getBoard();

    /**
     * Get the type of slidingtiles
     *
     * @return type of the slidingtiles
     */
    String getType();
}
