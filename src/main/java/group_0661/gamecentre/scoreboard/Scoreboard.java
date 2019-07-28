package group_0661.gamecentre.scoreboard;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import group_0661.gamecentre.gameSystem.Game;

import android.content.Context;

/**
 * The queryable collection of high scores for all games.
 */
public class Scoreboard implements IScoreboard {
    /**
     * The file we save scores in
     */
    private static String saveFile = "scoreboard-global.txt";
    /**
     * The number of high scores saved for each
     * slidingtiles/user for the global/local score sets.
     */
    private static int numScores = 10;

    private Context context;

    /**
     * The sorted lists of scores.
     */
    private Map<String, SortedSet<ScoreField>> globalHighScores; // Type -> Scores
    private Map<String, Map<String, SortedSet<ScoreField>>> userHighScores; // User, Type -> Scores

    /**
     * Initializes a Scoreboard.
     * This will create a save file if none exists
     * and load any existing scores.
     *
     * @param context The application context
     */
    public Scoreboard(Context context) {
        this.context = context;

        globalHighScores = new HashMap<>();
        userHighScores = new HashMap<>();

        createSaveFile();
        loadSaveFile();
    }

    /**
     * Returns the high scores among all players of a slidingtiles type.
     * Note that it returns null if the slidingtiles type has not been saved.
     *
     * @param gameType The slidingtiles type identifier
     * @return A sorted list of ScoreFields corresponding to the query, or null if none exists
     */
    public List<ScoreField> getGlobalHighScores(String gameType) {
        if (!globalHighScores.containsKey(gameType)) return null;
        return new ArrayList<>(globalHighScores.get(gameType));
    }

    /**
     * Returns the high scores of a slidingtiles type for a single user.
     * Note that it returns null if the user has not played this slidingtiles.
     *
     * @param userName The user
     * @param gameType The slidingtiles type identifier
     * @return A sorted list of ScoreFields corresponding to the query, or null if none exists
     */
    public List<ScoreField> getUserHighScores(String userName, String gameType) {
        if (!userHighScores.containsKey(userName)) return null;
        Map<String, SortedSet<ScoreField>> gameUserHighScores = userHighScores.get(userName);

        if (!gameUserHighScores.containsKey(gameType)) return null;
        return new ArrayList<>(gameUserHighScores.get(gameType));
    }

    /**
     * Saves a completed slidingtiles to the data.
     * This method should be called whenever a slidingtiles is finished
     *
     * @param game The completed slidingtiles
     * @param user The user name
     */
    public void addGame(Game game, String user) {
        ScoreField score = new ScoreField(user, game.getScore());
        addScore(game.getType(), score);

        System.out.println("SAVING GAME:" + game.getType() + " " + user);

        saveToFile();
    }

    private void addScore(String type, ScoreField score) {
        // Make sure the correct fields exist
        if (!globalHighScores.containsKey(type)) {
            globalHighScores.put(type, new TreeSet<ScoreField>());
        }
        if (!userHighScores.containsKey(score.getUserName())) {
            userHighScores.put(score.getUserName(), new HashMap<String ,SortedSet<ScoreField>>());
        }

        Map<String, SortedSet<ScoreField>> gameUserScores = userHighScores.get(score.getUserName());

        if (!gameUserScores.containsKey(type)) {
            gameUserScores.put(type, new TreeSet<ScoreField>());
        }

        updateScoreSet(globalHighScores.get(type), score);
        updateScoreSet(userHighScores.get(score.getUserName()).get(type), score);
    }

    private void updateScoreSet(SortedSet<ScoreField> set, ScoreField score) {
        set.add(score);
        // If we have too many, remove the lowest score
        if (set.size() > numScores) {
            set.remove(set.last());
        }
    }

    private void createSaveFile() {
        File file = new File(context.getFilesDir(), saveFile);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadSaveFile() {
        try {
            FileInputStream inputStream = context.openFileInput(saveFile);
            InputStreamReader inputStreamReader = new InputStreamReader (inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            for (String line = bufferedReader.readLine();
                 line != null;
                 line = bufferedReader.readLine()) {

                String[] words = line.split("\\|");
                if (words.length != 3) continue;
                addScore(words[1], new ScoreField(words[0], Integer.parseInt(words[2])));
            }

        } catch (FileNotFoundException e) {
            System.err.println("Scoreboard: Could not find save file.");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveToFile() {
        String contents = "";

        for (String userName : userHighScores.keySet()) {
            Map<String, SortedSet<ScoreField>> gameScores = userHighScores.get(userName);
            for (String gameType : gameScores.keySet()) {
                SortedSet<ScoreField> userScores = gameScores.get(gameType);
                for (ScoreField score : userScores) {
                    contents += userName + "|" + gameType + "|" + Integer.toString(score.getScore()) + "\n";
                }
            }
        }
        try {
            FileOutputStream outputStream = context.openFileOutput(saveFile, Context.MODE_PRIVATE);
            outputStream.write(contents.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
