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

import group_0661.gamecentre.knightsTour.KnightsTourGame;
import group_0661.gamecentre.user.UserManager;


/**
 * Manager for the matching tiles option selection layout.
 */
public class KnightsTourPopUp extends PopUpActivity implements ServiceConnection {

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
    private int size = 8;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(group_0661.gamecentre.R.layout.activity_knightstour_pop_up);

        configurePopUp(0.85, 0.85);

        addStartButtonListener();
    }

    /**
     * Binds UserManager service to KnightsTourStartPopUp when said activity starts
     */
    @Override
    protected void onStart() {
        super.onStart();
        bindService(new Intent(KnightsTourPopUp.this, UserManager.class), this, Context.BIND_AUTO_CREATE);
    }

    /**
     * Unbinds UserManager service to KnightsTourPopUp when said activity stops
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
        RadioGroup boardSelect = findViewById(group_0661.gamecentre.R.id.kboard_select);
        if (boardSelect.getCheckedRadioButtonId() == group_0661.gamecentre.R.id.knight_easy) {
            this.size = 5;
            Toast.makeText(KnightsTourPopUp.this, "Game Start: Easy", Toast.LENGTH_SHORT).show();
            return true;
        } else if (boardSelect.getCheckedRadioButtonId() == group_0661.gamecentre.R.id.knight_classic) {
            this.size = 8;
            Toast.makeText(KnightsTourPopUp.this, "Game Start: Classic", Toast.LENGTH_SHORT).show();
            return true;
        }
        Toast.makeText(KnightsTourPopUp.this, "Please Select a Difficulty Level", Toast.LENGTH_SHORT).show();
        return false;
    }

    /**
     * Slices up the selected image into a number of tiles depending on the selected board size
     */
    private void setBackground(Bitmap background) {
        ImageToTiles initBoard = new ImageToTiles(background, 2,2);
        initBoard.createTiles();
        initBoard.saveTiles(KnightsTourPopUp.this);
        backgroundPath = initBoard.getSavePath();
    }

    /**
     * Fetches default backgrounds from the R.drawable folder
     *
     * @return a Bitmap of the default boards (containing only numbers)
     */
    private void setDefaultBoard() {
        background = BitmapFactory.decodeResource(KnightsTourPopUp.this.getResources(), R.drawable.knight);
    }
    /**
     * Nullifies UserManager once the UserManager service is disconnected
     */
    @Override
    public void onServiceDisconnected(ComponentName name) {
        userManager = null;
    }

    public void addStartButtonListener() {
        Button startButton = (Button) findViewById(group_0661.gamecentre.R.id.start_kgame);
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
     * Initialises the new KnightsTour
     *
     * @return an intent with new KnightsTour data
     */
    private Intent initNewGame() {
        Intent startGame = new Intent(KnightsTourPopUp.this, KnightsTourActivity.class);
        KnightsTourGame game = new KnightsTourGame(this.size);
        if (userManager != null & userManager.getStatus() ) { userManager.saveUserImage(game, background, true); }
        startGame.putExtra("Knight's Tour", game);
        startGame.putExtra("background_path", backgroundPath);

        return startGame;
    }
}


