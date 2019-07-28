package group_0661.gamecentre;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import group_0661.gamecentre.knightsTour.KnightsTourGame;
import group_0661.gamecentre.gestures.CustomAdapter;
import group_0661.gamecentre.gestures.GestureDetectGridView;
import group_0661.gamecentre.user.UserManager;

/**
 * The KnightsTour activity.
 */
public class KnightsTourActivity extends ActionBarActivity implements Observer, ServiceConnection {
    /**
     * The buttons to display.
     */
    private ArrayList<Button> tileButtons;
    /**
     * The KnightsTour instance.
     */
    private KnightsTourGame game;
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
     * Initializes the game.
     *
     * @param savedInstanceState load the saved game bundle if available
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(group_0661.gamecentre.R.layout.activity_knightstour);

        // Sets title for action bar
        configureActionBar("Knight's Tour");
    }

    /**
     * Pauses the timer once the activity enters a pause state
     */
    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * Binds UserManager service to AccountPopUp when said activity starts
     */
    @Override
    protected void onStart() {
        super.onStart();
        bindService(new Intent(KnightsTourActivity.this, UserManager.class), this, Context.BIND_AUTO_CREATE);
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
        getMenuInflater().inflate(group_0661.gamecentre.R.menu.game_menu, menu);
        return true;
    }

    /**
     * Initializes the buttons (minus the back button) on the action bar.
     *
     * @return true if and when one of the items on the action bar is selected.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (group_0661.gamecentre.R.id.action_save == item.getItemId()) {
            if (userManager != null && userManager.getStatus()){
                userManager.saveGame(this.game, getIntent().getStringExtra("background_path"));
                Toast.makeText(KnightsTourActivity.this, "Game Saved", Toast.LENGTH_SHORT).show();
            } else { Toast.makeText(KnightsTourActivity.this, "You Must Login to Save", Toast.LENGTH_SHORT).show(); }
            return true;
        }
        else if (group_0661.gamecentre.R.id.action_revert == item.getItemId()) {
            gesture.undo(KnightsTourActivity.this);
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
        updateMovesMade();

        // Auto-save check
        if (userManager != null && userManager.getStatus() && autosaveCounter % 10 == 0) {
            userManager.saveGame(this.game, getIntent().getStringExtra("background_path"));
        }
        autosaveCounter++;

        gesture.setAdapter(new CustomAdapter(tileButtons, columnWidth, columnHeight));
    }

    /**
     * Called when the KnightsTour observer detects change.
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
        gesture = findViewById(group_0661.gamecentre.R.id.grid);
        gesture.setNumColumns(size);
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
                Bitmap btmp = BitmapFactory.decodeFile(String.format(path + "/tile_%d.png", element));
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
                Bitmap btmp = BitmapFactory.decodeFile(String.format(path + "/tile_%d.png", element));
                BitmapDrawable background = new BitmapDrawable(getResources(), btmp);
                button.setBackground(background);
                tileButtons.set(i, button);
                i++;
            }
        }
    }

    /**
     * Updates the Moves-Made TextView according to changes within the MatchingTiles.
     */
    private void updateMovesMade() {
        ((TextView) findViewById(group_0661.gamecentre.R.id.moves_taken)).setText(new Integer(game.getMoves()).toString());
    }

    /**
     * Initializes the knightsTour.
     */
    private void initGame() {
        this.game = (KnightsTourGame) getIntent().getSerializableExtra("Knight's Tour");
        this.size = this.game.getBoard().length;

        createTileButtons(KnightsTourActivity.this);
        initGrid();
    }

    /**
     * Checks if knightsTour is over. If so, LeaderboardActivity is initiated.
     */
    private void gameOver() {
        if (game.isWon()) {
            Intent scoreboard = new Intent(KnightsTourActivity.this, LeaderBoardActivity.class);
            if (userManager != null && userManager.getStatus()) {
                this.userManager.dropSavedGame(game);
                scoreboard.putExtra("game", this.game);
                scoreboard.putExtra("user", userManager.getName());
            }
            scoreboard.putExtra("game_title", "Knight's Tour");
            startActivity(scoreboard);
            finish();
        }
    }
}