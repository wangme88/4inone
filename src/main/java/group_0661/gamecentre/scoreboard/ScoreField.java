package group_0661.gamecentre.scoreboard;

/**
 * A data type representing a slidingtiles score and the accompanying username.
 * Comparable is implemented to order scores from highest to lowest.
 */

public class ScoreField implements Comparable<ScoreField>{
    /**
     * Account username.
     */
    private String userName;

    /**
     * Game score.
     */
    private int score;

    /**
     * A new score field associating slidingtiles score with account username.
     *
     * @param userName account username
     * @param score slidingtiles score
     */
    public ScoreField(String userName, int score) {
        this.userName = userName;
        this.score = score;
    }

    /**
     * Return username.
     * @return username
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Return slidingtiles score.
     * @return slidingtiles score
     */
    public int getScore() {
        return score;
    }

    /**
     * Compare score fields by slidingtiles score.
     *
     * @param other comparable score field
     * @return score field with higher score
     * (or in the case of equal scores, score field with username that comes later in the alphabet)
     */
    public int compareTo(ScoreField other) {
        // scoreFields are ordered based on the scores
        if (score != other.getScore()) {
            return -((Integer) score).compareTo(other.getScore());
        }
        // But if the scores are the same, then we use the user
        return userName.compareTo(other.getUserName());
    }
}
