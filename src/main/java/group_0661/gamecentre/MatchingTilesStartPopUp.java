package group_0661.gamecentre;

import group_0661.gamecentre.matchingtiles.MatchingTileGame;
import group_0661.gamecentre.user.UserManager;

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
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;


/**
 * Manager for the matching tiles option selection layout.
 */
public class MatchingTilesStartPopUp extends PopUpActivity implements ServiceConnection {

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
        setContentView(R.layout.activity_matchingtiles_pop_up);

        configurePopUp(0.85, 0.85);

        addStartButtonListener();
    }

    /**
     * Binds UserManager service to MatchingTilesStartPopUp when said activity starts
     */
    @Override
    protected void onStart() {
        super.onStart();
        bindService(new Intent(MatchingTilesStartPopUp.this, UserManager.class), this, Context.BIND_AUTO_CREATE);
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
        RadioGroup boardSelect = findViewById(R.id.mboard_select);
        if (boardSelect.getCheckedRadioButtonId() == R.id.match_easy) {
            width = 3;
            Toast.makeText(MatchingTilesStartPopUp.this, "Game Start: Easy", Toast.LENGTH_SHORT).show();
            return true;
        } else if (boardSelect.getCheckedRadioButtonId() == R.id.match_casual) {
            width = 4;
            Toast.makeText(MatchingTilesStartPopUp.this, "Game Start: Normal", Toast.LENGTH_SHORT).show();
            return true;
        } else if (boardSelect.getCheckedRadioButtonId() == R.id.match_hard) {
            width = 5;
            Toast.makeText(MatchingTilesStartPopUp.this, "Game Start: Hard", Toast.LENGTH_SHORT).show();
            return true;
        }
        Toast.makeText(MatchingTilesStartPopUp.this, "Please Select a Difficulty Level", Toast.LENGTH_SHORT).show();
        return false;
    }

    /**
     * Slices up the selected image into a number of tiles depending on the selected board size
     */
    private void setBackground(Bitmap background) {
        ImageToTiles initBoard = new ImageToTiles(background, this.width, this.width+1);
        initBoard.createTiles();
        initBoard.saveTiles(MatchingTilesStartPopUp.this);
        backgroundPath = initBoard.getSavePath();
    }

    /**
     * Fetches default backgrounds from the R.drawable folder
     *
     * @return a Bitmap of the default boards (containing only numbers)
     */
    private void setDefaultBoard() {
        if (this.width == 3) {
            background = BitmapFactory.decodeResource(MatchingTilesStartPopUp.this.getResources(), R.drawable.m_easy);
        }
        else if (this.width == 4) {
            background =  BitmapFactory.decodeResource(MatchingTilesStartPopUp.this.getResources(), R.drawable.m_medium);
        } else {
            background = BitmapFactory.decodeResource(MatchingTilesStartPopUp.this.getResources(), R.drawable.m_hard);
        }
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
                    startActivity(initNewGame());
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
        Intent startGame = new Intent(MatchingTilesStartPopUp.this, MatchingTilesActivity.class);
        MatchingTileGame game = new MatchingTileGame(width);
        if (userManager != null & userManager.getStatus() ) { userManager.saveUserImage(game, background, true); }
        startGame.putExtra("Matching Tiles", game);
        startGame.putExtra("background_path", backgroundPath);

        return startGame;
    }
}
