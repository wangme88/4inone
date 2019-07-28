package group_0661.gamecentre;

import java.util.ArrayList;

import java.util.Observable;
import java.util.Observer;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.content.Context;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.view.MenuItem;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Bitmap;


import group_0661.gamecentre.gameSystem.Game;
import group_0661.gamecentre.gestures.GestureDetectGridView;
import group_0661.gamecentre.snake.SnakeGame;
import group_0661.gamecentre.user.UserManager;
import group_0661.gamecentre.gestures.CustomAdapter;

/**
 * The slidingtiles activity.
 */
public class SnakeActivity extends ActionBarActivity implements Observer, ServiceConnection {
    /**
     * The buttons to display.
     */
    private ArrayList<Button> tileButtons;

    /**
     * The pre-loaded sprites
     */
    private BitmapDrawable[] tileSprites;

    /**
     * The game instance.
     */
    private SnakeGame game;
    /**
     * The module that detects gestures within the tiles.
     */
    private GestureDetectGridView gesture;
    /**
     * Instance of userManager service;
     */
    private UserManager userManager;
    /**
     * The size of the board.
     */
    private int size;
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
        setContentView(R.layout.activity_snake);

        // Sets title for action bar
        configureActionBar("Snake");

        addUndoButtonListener();

        // Tick the clock
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
                                updateGame();
                                updateTileButtons();
                                updateScore();
                                gameOver();
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
     * Activates the undo button.
     */
    private void addUndoButtonListener() {
        Button undoButton = findViewById(R.id.undo_button);
        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.undo();
            }
        });
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
        bindService(new Intent(SnakeActivity.this, UserManager.class), this, Context.BIND_AUTO_CREATE);
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
                Toast.makeText(SnakeActivity.this, "Game Saved", Toast.LENGTH_SHORT).show();
            } else { Toast.makeText(SnakeActivity.this, "You Must Login to Save", Toast.LENGTH_SHORT).show(); }
            return true;
        }
        else if (R.id.action_revert == item.getItemId()) {
            gesture.undo(SnakeActivity.this);
            return true;
        }
        return false;
    }

    /**
     *  Updates the display. Also initiates an autosave depending on autosaveCounter.
     */
    public void display() {
        // Updates UI
        updateTileButtons();
        updateScore();

        // Auto-save check
        if (userManager != null && userManager.getStatus() && autosaveCounter % 10 == 0) {
            userManager.saveGame(this.game, getIntent().getStringExtra("background_path"));
        }
        autosaveCounter++;

        gesture.setAdapter(new CustomAdapter(tileButtons, columnWidth, columnHeight));
    }

    /**
     * Called when the observer detects change.
     */
    @Override
    public void update(Observable o, Object arg) {
        display();
        gameOver();
    }

    private void loadImages() {
        String path = getIntent().getStringExtra("background_path");

        int nSprites = 4;
        tileSprites = new BitmapDrawable[nSprites];
        for (int i = 0; i < nSprites; i++) {
            Bitmap btmp = BitmapFactory.decodeFile(String.format(path + "/tile_%d.png", i+1));
            BitmapDrawable drawable = new BitmapDrawable(getResources(), btmp);
            tileSprites[i] = drawable;
        }
    }

    /**
     * Initializes the gridView (Taken from A2).
     */
    private void initGrid() {
        gesture = findViewById(R.id.grid);
        gesture.setNumColumns(this.size);
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

                        columnWidth = displayWidth / size;
                        columnHeight = displayHeight / size;

                        display();
                    }
                });
    }

    /**
     * Creates buttons according to the grid size and the number of tiles within the slidingtiles. Assigns
     * tile backgrounds to each button.
     */
    private void createTileButtons(Context context) {
        loadImages();

        Integer[][] tiles = game.getBoard();
        tileButtons = new ArrayList<>();
        for (Integer[] row : tiles) {
            for (Integer element: row) {
                Button button = new Button(context);
                button.setBackground(tileSprites[element]);
                this.tileButtons.add(button);
            }
        }
    }

    /**
     * Updates the backgrounds on each button according to changes within the board.
     */
    private void updateTileButtons() {
        Integer[][] tiles = game.getBoard();
        int i = 0;
        for (Integer[] row : tiles) {
            for (Integer element : row) {
                Button button = (this.tileButtons.get(i));
                button.setBackground(tileSprites[element]);
                tileButtons.set(i, button);
                i++;
            }
        }
    }

    /**
     * Updates the Score TextView according to changes within the board.
     */
    private void updateScore() {
        ((TextView) findViewById(R.id.score_text)).setText(Integer.toString(game.getScore()));
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
     * Calls the game update function
     */
    private void updateGame() {
        game.update();
    }

    /**
     * Initializes the slidingtiles.
     */
    private void initGame() {
        this.game = (SnakeGame) getIntent().getSerializableExtra("Snake");
        this.size = this.game.getBoard().length;

        createTileButtons(SnakeActivity.this);
        initGrid();
    }

    /**
     * Checks if the slidingtiles is over. If so, LeaderboardActivity is initiated.
     */
    private void gameOver() {
        if (game.isWon()) {
            Intent scoreboard = new Intent(SnakeActivity.this, LeaderBoardActivity.class);
            if (userManager != null && userManager.getStatus()) {
                this.userManager.dropSavedGame(game);
                scoreboard.putExtra("game", this.game);
                scoreboard.putExtra("user", userManager.getName());
            }
            scoreboard.putExtra("game_title", "Snake");
            startActivity(scoreboard);
            finish();
        }
    }
}