package group_0661.gamecentre;

import group_0661.gamecentre.gameSystem.Game;
import group_0661.gamecentre.user.UserManager;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.content.Intent;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.graphics.Bitmap;
import android.provider.MediaStore.Images.Media;

/**
 * Manager for the slidingtiles option selection layout.
 */
public class SlidingTilesStartPopUp extends PopUpActivity implements ServiceConnection {

    /**
     * The size of the board to be initialized
     */
    private int size = 4;

    /**
     * The current background image, 'null' if the default number tile is used
     */
    private Bitmap background = null;

    /**
     * The file path for the background
     */
    private String backgroundPath;

    /**
     * The instantiation of the UserManager service
     */
    private UserManager userManager;

    /**
     * Check if there is an unlimited number of undo
     */
    private boolean unlimited = false;

    /**
     * Retrieves the image from android gallery
     */
    private final int  GALLERY_REQUEST = 2;

    /**
     * Initialize the slidingtiles options page
     *
     * @param savedInstanceState load the saved slidingtiles bundle if available
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slidingtiles_pop_up);

        configurePopUp(0.85,0.85);

        addStartButtonListener();
        addBackgroundButtonListener();
        addCheckboxListener();
    }

    /**
     * Binds UserManager service to SlidingTilesStartPopUp when said activity starts
     */
    @Override
    protected void onStart() {
        super.onStart();
        bindService(new Intent(SlidingTilesStartPopUp.this, UserManager.class), this, Context.BIND_AUTO_CREATE);
    }

    /**
     * Unbinds UserManager service to SlidingTilesStartPopUp when said activity stops
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
     * Nullifies UserManager once the UserManager service is disconnected
     */
    @Override
    public void onServiceDisconnected(ComponentName name) {
        userManager = null;
    }

    public void addCheckboxListener() {
        CheckBox checkbox = (CheckBox) findViewById(R.id.checkBox);
        checkbox.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    unlimited = true;
                }
            }
        });
    }

    /**
     * Sets variable background to an image selected from the gallery activity (called by the
     * AddBackgroundButton)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent returnData) {
        super.onActivityResult(requestCode,resultCode,returnData);
        if (requestCode == GALLERY_REQUEST) {
            if (resultCode == RESULT_OK) {
                try {
                        background = Media.getBitmap(this.getContentResolver(), returnData.getData());
                        Toast.makeText(SlidingTilesStartPopUp.this,"Background Added", Toast.LENGTH_SHORT).show();
                } catch (Exception ex) {
                    Toast.makeText(SlidingTilesStartPopUp.this,"Could not Retrieve Image", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    /**
     * Sets start button behavior.
     */
    private void addStartButtonListener() {
        Button startButton = findViewById(R.id.start_game);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioGroupListener()) {
                    // Sets background to default if no custom image was selected from the gallery
                    if (background == null) { background = getDefaultBoard(); }
                    setBackground(background);

                    startActivity(initNewGame());
                    finish();
                }
            }
        });

    }

    /**
     * Sets addBackground button behavior. Links user to gallery to choose an image, which is
     * returned to this activity via the above OnActivityResult method
     */
    private void addBackgroundButtonListener() {
        Button startButton = findViewById(R.id.add_image);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent accessToGallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(accessToGallery, GALLERY_REQUEST);
                }
                catch (Exception e) {
                    Toast.makeText(SlidingTilesStartPopUp.this,"Sliding Tiles Does Not Have Permission to Access Gallery", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Slices up the selected image into a number of tiles depending on the selected board size
     */
    private void setBackground(Bitmap bitmap) {
        ImageToTiles initBoard = new ImageToTiles(bitmap, this.size, this.size);
        initBoard.createTiles();
        initBoard.saveTiles(SlidingTilesStartPopUp.this);
        backgroundPath = initBoard.getSavePath();
    }

    /**
     * Fetches default backgrounds from the R.drawable folder
     *
     * @return a Bitmap of the default boards (containing only numbers)
     */
    private Bitmap getDefaultBoard() {
        if (this.size == 3) {
            return BitmapFactory.decodeResource(SlidingTilesStartPopUp.this.getResources(), R.drawable.easy);
        }
        else if (this.size == 4) {
            return BitmapFactory.decodeResource(SlidingTilesStartPopUp.this.getResources(), R.drawable.normal);
        }
        return BitmapFactory.decodeResource(SlidingTilesStartPopUp.this.getResources(), R.drawable.hard);
    }

    /**
     * Initializes a listener for the radio buttons regarding the board size selection.
     *
     * @return true iff one of the radioButtons in this radioGroup is selected
     */
    private boolean radioGroupListener() {
        RadioGroup boardSelect = findViewById(R.id.board_select);
        if (boardSelect.getCheckedRadioButtonId() == R.id.easy) {
            size = 3;
            Toast.makeText(SlidingTilesStartPopUp.this, "Game Start: Easy", Toast.LENGTH_SHORT).show();
            return true;
        } else if (boardSelect.getCheckedRadioButtonId() == R.id.medium) {
            size = 4;
            Toast.makeText(SlidingTilesStartPopUp.this, "Game Start: Normal", Toast.LENGTH_SHORT).show();
            return true;
        } else if (boardSelect.getCheckedRadioButtonId() == R.id.hard) {
            size = 5;
            Toast.makeText(SlidingTilesStartPopUp.this, "Game Start: Hard", Toast.LENGTH_SHORT).show();
            return true;
        }
        Toast.makeText(SlidingTilesStartPopUp.this, "Please Select a Board Size", Toast.LENGTH_SHORT).show();
        return false;
    }

    /**
     * Initialises the new slidingtiles
     *
     * @return an intent with new slidingtiles data
     */
    private Intent initNewGame() {
        Intent startGame = new Intent(SlidingTilesStartPopUp.this, SlidingTilesActivity.class);
        Game game = new Game(size, Integer.valueOf(((
                EditText)findViewById(R.id.UndoInput)).getText().toString()), unlimited);
        if (userManager != null & userManager.getStatus() ) { userManager.saveUserImage(game, background, true); }
        startGame.putExtra("Sliding Tiles", game);
        startGame.putExtra("background_path", backgroundPath);

        return startGame;
    }
}

