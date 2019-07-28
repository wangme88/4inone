package group_0661.gamecentre;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import group_0661.gamecentre.matchingtiles.MatchingTileGame;
import group_0661.gamecentre.snake.SnakeGame;
import group_0661.gamecentre.user.UserManager;


/**
 * Manager for the matching tiles option selection layout.
 */
public class SnakePopUp extends PopUpActivity implements ServiceConnection {

    /**
     * The instantiation of the UserManager service
     */
    private UserManager userManager;

    /**
     * Board background in use.
     */
    private Bitmap background = null;

    /**
     * The file path for the background
     */
    private String backgroundPath;

    /**
     * Board width
     */
    private int width = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snake_pop_up);

        configurePopUp(0.85, 0.85);

        addStartButtonListener();
    }

    /**
     * Binds UserManager service to MatchingTilesStartPopUp when said activity starts
     */
    @Override
    protected void onStart() {
        super.onStart();
        bindService(new Intent(SnakePopUp.this, UserManager.class), this, Context.BIND_AUTO_CREATE);
    }

    /**
     * Unbinds UserManager service to MatchingTilesStartPopUp when said activity stops
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
    }

    /**
     * Initialize a listener for the radio buttons regarding board difficulty.
     */
    private boolean radioGroupListener() {
        setDefaultBoard();

        RadioGroup boardSelect = findViewById(R.id.mboard_select);
        if (boardSelect.getCheckedRadioButtonId() == R.id.snake_8) {
            width = 8;
            Toast.makeText(SnakePopUp.this, "Game Start: 8x8", Toast.LENGTH_SHORT).show();
            return true;
        } else if (boardSelect.getCheckedRadioButtonId() == R.id.snake_12) {
            width = 12;
            Toast.makeText(SnakePopUp.this, "Game Start: 12x12", Toast.LENGTH_SHORT).show();
            return true;
        } else if (boardSelect.getCheckedRadioButtonId() == R.id.snake_16) {
            width = 16;
            Toast.makeText(SnakePopUp.this, "Game Start: 16x16", Toast.LENGTH_SHORT).show();
            return true;
        }
        Toast.makeText(SnakePopUp.this, "Please Select a Board Size", Toast.LENGTH_SHORT).show();
        return false;
    }

    /**
     * Slices up the selected image into a number of tiles depending on the selected board size
     */
    private void setBackground(Bitmap background) {
        // Load the sprites from the snake sprite sheet
        ImageToTiles initBoard = new ImageToTiles(background, 4, 1);
        initBoard.createTiles();
        initBoard.saveTiles(SnakePopUp.this);
        backgroundPath = initBoard.getSavePath();
    }

    /**
     * Fetches default backgrounds from the R.drawable folder
     *
     * @return a Bitmap of the default boards (containing only numbers)
     */
    private void setDefaultBoard() {
        background = BitmapFactory.decodeResource(SnakePopUp.this.getResources(), R.drawable.snake);
    }
    /**
     * Nullifies UserManager once the UserManager service is disconnected
     */
    @Override
    public void onServiceDisconnected(ComponentName name) {
        userManager = null;
    }

    public void addStartButtonListener() {
        Button startButton = (Button) findViewById(R.id.start_mgame);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioGroupListener()) {
                    setDefaultBoard();
                    setBackground(background);
                    Intent newgame = initNewGame();
                    startActivity(newgame);
                    finish();
                }
            }
        });
    }

    /**
     * Initialises the new matchingtiles
     *
     * @return an intent with new matchingtiles data
     */
    private Intent initNewGame() {
        Intent startGame = new Intent(SnakePopUp.this, SnakeActivity.class);
        SnakeGame game = new SnakeGame(width);
        startGame.putExtra("Snake", game);
        startGame.putExtra("background_path", backgroundPath);

        return startGame;
    }
}
