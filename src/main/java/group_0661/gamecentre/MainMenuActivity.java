package group_0661.gamecentre;

import group_0661.gamecentre.gameSystem.Game;
import group_0661.gamecentre.user.UserManager;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * The initial activity for the sliding puzzle tile game.
 */
public class MainMenuActivity extends AppCompatActivity implements ServiceConnection {
    /**
     * Request code for account registry/login.
     */
    private final int ACCOUNT_REQUEST = 1;
    /**    @Override
    public int getScore() {
        return board.getScore();
    }

     * Instance of userManager service.
     */
    private UserManager userManager;

    /**
     * Initializes the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // Adds button listeners
        addStartButtonListener();
        addLoadButtonListener();
        addLoginButtonListener();
        addChangeGameButtonListener();
        addScoreButtonListener();

        // Starts running service in the background to be accessed by other activities
        startService(new Intent(MainMenuActivity.this, UserManager.class));
        }

    /**
     * Binds UserManager service to MainMenuActivity when said activity starts
     */
    @Override
    protected void onStart() {
        super.onStart();
        bindService(new Intent(MainMenuActivity.this, UserManager.class), this, Context.BIND_AUTO_CREATE);
    }

    /**
     * Unbinds UserManager service to MainMenuActivity when said activity stops
     */
    @Override
    protected void onStop() {
        super.onStop();
        unbindService(this);
    }

    /**
     * Creates an instantiation of UserManager service when UserManager establishes a connection with
     * MainMenuActivity. Updates login UI if a user is logged in
     */
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        UserManager.UserBinder binder = (UserManager.UserBinder) service;
        userManager = binder.getService();
        if (userManager.getStatus()) {
            updateLoginUI();
        }
    }

    /**
     * Resets UserManager upon disconnect with service UserManager
     */
    @Override
    public void onServiceDisconnected(ComponentName name) {
        userManager = null;
    }

    /**
     * Returns the result of AccountPopUp (Login/User Details).
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent returnData) {
        super.onActivityResult(requestCode,resultCode,returnData);
        if (requestCode == ACCOUNT_REQUEST) {
            if (resultCode == RESULT_OK) {
                try {
                    updateLoginUI();
                } catch(NullPointerException e) {
                    Log.e("Login Attempt", "Null Serializable");
                }
            }
        }
    }

    /**
     * Changes the Login button to a logout button when the user logs in. Creates a new listener for
     * the aforementioned button that logs out the user when pressed. Also adds a welcome at the top right
     * depending on what user is logged in.
     */
    private void updateLoginUI() {
        Button logoutButton = findViewById(R.id.account);
        logoutButton.setText(R.string.logout);
        ((TextView) findViewById(R.id.welcome)).setText("Welcome " + userManager.getName());
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Logs out user, resets UI to its initial state
                userManager.signOut();
                ((TextView) findViewById(R.id.welcome)).setText("");
                addLoginButtonListener();
            }
        });
    }

    /**
     * Activates the start button.
     */
    private void addStartButtonListener() {
        Button startButton = findViewById(R.id.new_game);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gameType = (((TextView) findViewById(R.id.title)).getText()).toString();
                if (gameType.equals("Sliding Tiles")) {
                    Intent popUp = new Intent(MainMenuActivity.this, SlidingTilesStartPopUp.class);
                    startActivity(popUp);
                } else if (gameType.equals("Snake")) {
                    Intent popUp = new Intent(MainMenuActivity.this, SnakePopUp.class);
                    startActivity(popUp);
                } else if (gameType.equals("Knight's Tour")) {
                    Intent popUp = new Intent(MainMenuActivity.this, KnightsTourPopUp.class);
                    startActivity(popUp);
                } else  {
                    Intent popUp = new Intent(MainMenuActivity.this, MatchingTilesStartPopUp.class);
                    startActivity(popUp);
                }
            }
        });
    }
    /**
     * Activates the load button.
     */
    private void addLoadButtonListener() {
        Button loadButton = findViewById(R.id.load_game);
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gameType = (((TextView) findViewById(R.id.title)).getText()).toString();
                if (userManager != null & userManager.getStatus()) {
                    if (userManager.getSavedGame(gameType) != null) {
                        startActivity(initLoadGame(gameType));
                    }
                    else {
                        Toast.makeText(MainMenuActivity.this, "No saved game found", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(MainMenuActivity.this, "You must login to load a previous save", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    /**
     * Activates the Login Button.
     */
    private void addLoginButtonListener() {
        Button loginButton = findViewById(R.id.account);
        loginButton.setText(R.string.login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent popUp = new Intent(MainMenuActivity.this, AccountPopUp.class);
                startActivityForResult(popUp, ACCOUNT_REQUEST);
            }
        });
    }

    /**
     * Activates the ChangeGame Button.
     */
    private void addChangeGameButtonListener() {
        Button loginButton = findViewById(R.id.change_game);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView game = findViewById(R.id.title);
                String currentGame = game.getText().toString();
                if (currentGame.equals("Sliding Tiles")) {
                    game.setText("Snake");
                } else if (currentGame.equals("Snake")) {
                    game.setText("Knight's Tour");
                } else if (currentGame.equals("Knight's Tour")) {
                    game.setText("Matching Tiles");
                } else {
                    game.setText("Sliding Tiles");
                }
            }
        });
    }

    private void addScoreButtonListener() {
        Button loginButton = findViewById(R.id.leaderboards);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView game = findViewById(R.id.title);
                String currentGame = game.getText().toString();
                if (currentGame.equals("Sliding Tiles")) {
                    Intent score = new Intent(MainMenuActivity.this, LeaderBoardActivity.class);
                    score.putExtra("game_title", "Sliding Tiles");
                    startActivity(score);
                } else if (currentGame.equals("Snake")) {
                    Intent score = new Intent(MainMenuActivity.this, LeaderBoardActivity.class);
                    score.putExtra("game_title", "Snake");
                    startActivity(score);
                } else if (currentGame.equals("Knight's Tour")) {
                    Intent score = new Intent(MainMenuActivity.this, LeaderBoardActivity.class);
                    score.putExtra("game_title", "Knight's Tour");
                    startActivity(score);
                } else {
                    Intent score = new Intent(MainMenuActivity.this, LeaderBoardActivity.class);
                    score.putExtra("game_title", "Matching Tiles");
                    startActivity(score);
                }
            }
        });
    }
    /**
     * Initialises the loaded slidingtiles
     *
     * @return an intent with loaded slidingtiles data
     */
    private Intent initLoadGame(String gameType) {
        Intent load;
        if (gameType.equals("Sliding Tiles")) {
            load = new Intent(MainMenuActivity.this, SlidingTilesActivity.class);
            load.putExtra(gameType, userManager.getSavedGame(gameType));
        } else if (gameType.equals("Matching Tiles")) {
            load = new Intent(MainMenuActivity.this, MatchingTilesActivity.class);
            load.putExtra(gameType, userManager.getSavedGame(gameType));
        } else if (gameType.equals("Knight's Tour")) {
            load = new Intent(MainMenuActivity.this, KnightsTourActivity.class);
            load.putExtra(gameType, userManager.getSavedGame(gameType));
        } else {
            load = new Intent(MainMenuActivity.this, SlidingTilesActivity.class);
        }
        int row =  userManager.getSavedGame(gameType).getBoard().length;
        int column = row;
        if (gameType.equals("Knight's Tour")) {
            row = 2;
            column = 2;
        } else if (gameType.equals("Matching Tiles")) {
            column = row - 1;
        }
        // Cuts up saved image to recreate the saved board
        ImageToTiles initBoard = new ImageToTiles(userManager.loadUserImage(userManager.getSavedGame(gameType), false), column, row);
        initBoard.createTiles();
        initBoard.saveTiles(MainMenuActivity.this);

        // Adding extra required data to be passed to SlidingTilesActivity
        load.putExtra("background_path", userManager.getBackgroundPath(gameType));

        return load;
    }

}
