package group_0661.gamecentre.user;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import group_0661.gamecentre.gameSystem.Game;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.app.Service;
import android.os.Binder;
import android.graphics.Bitmap;

/**
 * User manager class.
 */
public class UserManager extends Service implements IUserManager {
    /**
     * List of (user, password).
     */
    private List<String[]> usersList;

    /**
     * File path
     */
    private final String USERS_FILE = "users.ser";

    /**
     * Logged in indicator: 1 being logged in, 0 being not logged in.
     */
    private static int loggedIn = 0;

    /**
     * Logged in user account.
     */
    private static User user;
    private final IBinder binder = new UserBinder();

    @Override
    public IBinder onBind(Intent intent) {
        fetchUserList();
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        fetchUserList();
        return super.onStartCommand(intent, flags, startId);
    }

    public class UserBinder extends Binder {
        public UserManager getService() {
            return UserManager.this;
        }
    }

    /**
     * Return true if sign is successful given user name and password
     * If successful, change loggedIn to 1.
     *
     * @param username string
     * @param password string
     * @return true if sign in is successful
     */
    public boolean signIn(String username, String password) {
        for(int i = 0; i < usersList.size(); i++) {
            if (username.equals(usersList.get(i)[0])){
                if (password.equals(usersList.get(i)[1])){
                    loggedIn = 1;
                    //open user file
                    loadUser("save_file"+ username +".ser");
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Return true if sign up is successful given username and password.
     * If successful, change loggedIn to 1.
     *
     * @param username string
     * @param password string
     * @return true if sign up is successful
     */
    public boolean signUp(String username, String password) {
        if (usersList == null) {
            usersList = new ArrayList<>();
        }
        for(int i = 0; i < usersList.size(); i++) {
            if (username.equals(usersList.get(i)[0])) {
                return false;
            }
        }
        String[] element = {username, password};
        this.usersList.add(element);
        user = new User(username);
        loggedIn = 1;
        saveUser("save_file"+ username +".ser");
        saveUsersList();
        return true;
    }

    /**
     * Return true if signed out.
     * @return true if signed out
     */
    public boolean signOut() {
        if (getStatus()) {
            loggedIn = 0;
            user = null;
            return true;
        }
        return false;
    }

    /**
     * Return account username.
     *
     * @return account username
     */
    public String getName(){
        return user.getUserName();
    }

    /**
     * Return true if logged in.
     *
     * @return true if logged in
     */
    public boolean getStatus(){
        return loggedIn == 1;
    }

    /**
     * Save slidingtiles and background path, then update user file.
     *
     * @param game given slidingtiles state
     * @param path given background path
     */
    public void saveGame(Game game, String path){
        user.setSavedGame(game);
        user.setBackgroundPath(game, path);
        saveUserImage(game, loadUserImage(game,true), false );
        saveUser("save_file"+ user.getUserName() +".ser");
    }

    /**
     * Return background path.
     * @return background path
     */
    public String getBackgroundPath(String gameName){
        return user.getBackgroundPath(gameName);
    }

    /**
     * Return saved slidingtiles.
     * @return saved slidingtiles
     */
    public Game getSavedGame(String gameName){
        return user.getSavedGame(gameName);
    }

    /**
     * Drop saved slidingtiles.
     */
    public void dropSavedGame(Game game){
        user.dropSavedGame(game);
    }

    /**
     * Load user file through deserialization for access to user saved slidingtiles.
     *
     * @param fileName user file name
     */
    private void loadUser(String fileName) {
        try {
            InputStream inputStream = this.openFileInput(fileName);
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                user = (User) input.readObject();
                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        } catch (ClassNotFoundException e) {
            Log.e("login activity", "File contained unexpected data type: " + e.toString());
        }
    }

    /**
     * Save user fields to fileName using serialization.
     *
     * @param fileName user file name
     */
    private void saveUser(String fileName) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    this.openFileOutput(fileName, Context.MODE_PRIVATE));
            outputStream.writeObject(user);
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private String getPrefix(boolean temp, String game) {
        String prefix = "";
        if (temp) {
            prefix += "temp_";
        } if (game.equals("Sliding Tiles")) {
            prefix += "slidingtiles";
        } else if (game.equals("Matching Tiles")) {
            prefix += "matchingtiles";
        } else if (game.equals("Knight's Tour")) {
            prefix += "knightstour";
        } else {
            prefix += "snake";
        }
        return prefix;
    }


    /**
     * Save background image for saved slidingtiles.
     *
     * @param image background image
     * @param temp true if temporary file
     */
    public boolean saveUserImage(Game game, Bitmap image, boolean temp) {
        String prefix = getPrefix(temp, game.getGameTitle());

        File path = new File(this.getFilesDir(), prefix + "_save_img"+ user.getUserName() +".png");
        try {
            FileOutputStream out = new FileOutputStream(path);
            image.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, out);
            out.close();
            return true;
        }
        catch (IOException e) {
            Log.e("Save Image Error:", "File write failed: " + e.toString());
            return false;
        }
    }

    /**
     * Return background image for saved slidingtiles.
     *
     * @param temp true if temporary file
     * @return background image for saved slidingtiles
     */
    public Bitmap loadUserImage(Game game, boolean temp) {
        String prefix = getPrefix(temp, game.getGameTitle());

        return BitmapFactory.decodeFile(this.getFilesDir() + "/" + prefix + "_save_img"+ user.getUserName() +".png");
    }

    /**
     * Deserialize usersList.
     */
    private void fetchUserList() {
        File dir = new File(this.getFilesDir(), "/" + USERS_FILE);
        if (dir.exists()) {
            try {
                InputStream inputStream = this.openFileInput(USERS_FILE);
                if (inputStream != null) {
                    ObjectInputStream input = new ObjectInputStream(inputStream);
                    usersList = (ArrayList) input.readObject();
                    inputStream.close();
                }
            } catch (FileNotFoundException e) {
                Log.e("login activity", "File not found: " + e.toString());
            } catch (IOException e) {
                Log.e("login activity", "Can not read file: " + e.toString());
            } catch (ClassNotFoundException e) {
                Log.e("login activity", "File contained unexpected data type: " + e.toString());
            }
        }
        else {
            usersList = new ArrayList<>();
            saveUsersList();
        }
    }

    /**
     * Serialize usersList.
     */
    private void saveUsersList() {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    this.openFileOutput(USERS_FILE, Context.MODE_PRIVATE));
            outputStream.writeObject(usersList);
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }


}

