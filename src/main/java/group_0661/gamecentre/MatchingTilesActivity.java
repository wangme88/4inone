package group_0661.gamecentre;

import java.util.ArrayList;

import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.content.Context;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.view.MenuItem;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Bitmap;


import group_0661.gamecentre.matchingtiles.MatchingTileGame;
import group_0661.gamecentre.gestures.GestureDetectGridView;
import group_0661.gamecentre.user.UserManager;
import group_0661.gamecentre.gestures.CustomAdapter;

/**
 * The matchingtiles activity.
 */
public class MatchingTilesActivity extends ActionBarActivity implements Observer, ServiceConnection {
    /**
     * The buttons to display.
     */
    private ArrayList<Button> tileButtons;
    /**
     * The slidingtiles instance.
     */
    private MatchingTileGame game;
    /**
     * The module that detects gestures within the tiles.
     */
    private GestureDetectGridView gesture;
    /**
     * Instance of userManager service;
     */
    private UserManager userManager;
    /**
     * The width of the board.
     */
    private int width;
    /**
     * The length of the board.
     */
    private int length;
    /**
     * Timer for resetting buttons.
     */
    private CountDownTimer bTimer;
    /**
     * Parameters for creating the gridView.
     */
    private static int columnWidth, columnHeight;
    /**
     * Counter that initiates autosave in display() iff autosaveCounter is a multiple of 10.
     */
    private int autosaveCounter = 1;
    /**
     * True when the game wants to start the counter; false when the slidingtiles wants to pause the counter
     */
    private boolean incrementTime = true;
    /**
     * Initializes the game.
     *
     * @param savedInstanceState load the saved game bundle if available
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Sets title for action bar
        configureActionBar("Matching Tiles");

        // Configures the in-game timer
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted() && incrementTime) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateTime();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();
    }

    /**
     * Pauses the timer once the activity enters a pause state
     */
    @Override
    protected void onPause() {
        super.onPause();
        this.incrementTime = false;
    }

    /**
     * Binds UserManager service to AccountPopUp when said activity starts
     */
    @Override
    protected void onStart() {
        super.onStart();
        bindService(new Intent(MatchingTilesActivity.this, UserManager.class), this, Context.BIND_AUTO_CREATE);
    }

    /**
     * Unbinds UserManager service when this activity stops
     */
    @Override
    protected void onStop() {
        super.onStop();
        unbindService(this);
    }

    /**
     * Creates an instantiation of UserManager service when UserManager establishes a connection with
     * this activity
     */
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        UserManager.UserBinder binder = (UserManager.UserBinder) service;
        userManager = binder.getService();
        initGame();
    }

    /**
     * Nullifies UserManager once the UserManager service is disconnected
     */
    @Override
    public void onServiceDisconnected(ComponentName name) {
        userManager = null;
    }

    /**
     * Initializes the action bar with menu items.
     *
     * @return true when called
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.game_menu, menu);
        return true;
    }

    /**
     * Initializes the buttons (minus the back button) on the action bar.
     *
     * @return true if and when one of the items on the action bar is selected.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (R.id.action_save == item.getItemId()) {
            if (userManager != null && userManager.getStatus()){
                userManager.saveGame(this.game, getIntent().getStringExtra("background_path"));
                Toast.makeText(MatchingTilesActivity.this, "Game Saved", Toast.LENGTH_SHORT).show();
            } else { Toast.makeText(MatchingTilesActivity.this, "You Must Login to Save", Toast.LENGTH_SHORT).show(); }
            return true;
        }
        else if (R.id.action_revert == item.getItemId()) {
            Toast.makeText(MatchingTilesActivity.this, "Undo unavaliable for this game", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    /**
     *  Updates the display. Also initiates an autosave depending on autosaveCounter.
     */
    public void display() {
        // Updates UI in 1s interval
        updateTileButtons();
        startTimer();
        updateMovesMade();

//         Auto-save check
        if (userManager != null && userManager.getStatus() && autosaveCounter % 10 == 0) {
            userManager.saveGame(this.game, getIntent().getStringExtra("background_path"));
        }
        autosaveCounter++;

        gesture.setAdapter(new CustomAdapter(tileButtons, columnWidth, columnHeight));
    }

    /**
     * Called when the MatchingTiles observer detects change.
     */
    @Override
    public void update(Observable o, Object arg) {
        display();
        gameOver();
    }

    /**
     * Initializes the gridView (Taken from A2).
     */
    private void initGrid() {
        gesture = findViewById(R.id.grid);
        gesture.setNumColumns(width);
        gesture.setGame(game);
        game.addObserver(this);
        // Observer sets up desired dimensions as well as calls our display function
        gesture.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        gesture.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        int displayWidth = gesture.getMeasuredWidth();
                        int displayHeight = gesture.getMeasuredHeight();

                        columnWidth = displayWidth / width;
                        columnHeight = displayHeight / length;

                        display();
                    }
                });
    }

    /**
     * Creates buttons according to the grid size and the number of tiles within the MatchingTiles. Assigns
     * tile backgrounds to each button.
     */
    private void createTileButtons(Context context) {
        Integer[][] board = game.getBoard();
        String path = getIntent().getStringExtra("background_path");
        tileButtons = new ArrayList<>();
        for (Integer[] row : board) {
            for (Integer element: row) {
                Button button = new Button(context);
                Bitmap btmp = BitmapFactory.decodeFile(String.format(path + "/tile_%d.png", width * length + 1));
                BitmapDrawable background = new BitmapDrawable(getResources(), btmp);
                button.setBackground(background);
                this.tileButtons.add(button);
            }
        }
    }

    /**
     * Updates the backgrounds on each button according to changes within the board.
     */
    private void updateTileButtons() {
        Integer[][] board = game.getBoard();
        String path = getIntent().getStringExtra("background_path");
        int i = 0;
        for (Integer[] row : board) {
            for (Integer element : row) {
                Button button = (this.tileButtons.get(i));
                Bitmap btmp = null;
                if (element == width * length + 1) {
                    btmp = BitmapFactory.decodeFile(String.format(path + "/tile_%d.png", width * length + 1));
                } else {
                    btmp =BitmapFactory.decodeFile(String.format(path + "/tile_%d.png", element));
                }
                BitmapDrawable background = new BitmapDrawable(getResources(), btmp);
                button.setBackground(background);
                tileButtons.set(i, button);
                i++;
            }
        }
    }

    void startTimer() {
        bTimer = new CountDownTimer(1000, 1000) {
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                updateTileButtons();
            }
        };
        bTimer.start();
    }


    //cancel timer
    void cancelTimer() {
        if(bTimer!=null)
            bTimer.cancel();
    }
    /**
     * Updates the Moves-Made TextView according to changes within the MatchingTiles.
     */
    private void updateMovesMade() {
        ((TextView) findViewById(R.id.moves_taken)).setText(new Integer(game.getMoves()).toString());
    }

    /**
     * Updates the Time-Elapsed TextView according to the timer thread in OnCreate.
     */
    private void updateTime() {
        game.time += 1;
        TextView view = findViewById(R.id.time_elapsed);
        view.setText(Integer.toString(game.time));
    }

    /**
     * Initializes the matchingTiles.
     */
    private void initGame() {
        this.game = (MatchingTileGame) getIntent().getSerializableExtra("Matching Tiles");
        this.length = this.game.getBoard().length;
        this.width = length-1;

        createTileButtons(MatchingTilesActivity.this);
        initGrid();
    }

    /**
     * Checks if the matchingTiles is over. If so, LeaderboardActivity is initiated.
     */
    private void gameOver() {
        if (game.isWon()) {
            Intent scoreboard = new Intent(MatchingTilesActivity.this, LeaderBoardActivity.class);
            if (userManager != null && userManager.getStatus()) {
                this.userManager.dropSavedGame(game);
                scoreboard.putExtra("game", this.game);
                scoreboard.putExtra("user", userManager.getName());
            }
            scoreboard.putExtra("game_title", "Matching Tiles");
            startActivity(scoreboard);
            finish();
        }
    }
}