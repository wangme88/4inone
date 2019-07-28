package group_0661.gamecentre;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.app.Activity;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import group_0661.gamecentre.user.UserManager;
import group_0661.gamecentre.user.UserManager.UserBinder;

/**
 * An activity where users can log in or register a new account
 */
public class AccountPopUp extends PopUpActivity implements ServiceConnection {
    /**
     * The username typed in the username field
     */
    private String user;
    /**
     * The password typed in the password field
     */
    private String pass;
    /**
     * The instantiation of the UserManager service
     */
    private UserManager userManager;

    /**
     * Initializes the Login Screen.
     *
     * @param savedInstanceState load the saved slidingtiles bundle if available
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_popup);

        // Sets the size of the pop-up window
        configurePopUp(0.85,0.85);

        //Configures button listeners
        addRegisterButtonListener();
        addLoginButtonListener();
    }

    /**
     * Binds UserManager service to AccountPopUp when said activity starts
     */
    @Override
    protected void onStart() {
        super.onStart();
        bindService(new Intent(AccountPopUp.this, UserManager.class), this, Context.BIND_AUTO_CREATE);
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
        UserBinder binder = (UserBinder) service;
        userManager = binder.getService();
    }

    /**
     * Nullifies UserManager once the UserManager service is disconnected
     */
    @Override
    public void onServiceDisconnected(ComponentName name) {
        userManager = null;
    }

    /**
     * Implements the register button functionality (tries to register the user when pressed)
     */
    private void addRegisterButtonListener() {
        Button startButton = findViewById(R.id.register);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (accessAttempt("register")) {
                    sendToMain();
                } else { Toast.makeText(AccountPopUp.this,String.format("User: \"%s\" Already Exists", user), Toast.LENGTH_SHORT).show(); }
            }
        });
    }

    /**
     * Implements the login button functionality (performs a login attempt when pressed)
     */
    private void addLoginButtonListener() {
        Button startButton = findViewById(R.id.login);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (accessAttempt("login")) {
                    sendToMain();
                }
                else { Toast.makeText(AccountPopUp.this,"Invalid Login", Toast.LENGTH_SHORT).show(); }
            }
        });
    }

    /**
     * Attempts to log the user in or register a new user depending on the intended action
     * of the user
     *
     * @param intent either "login" or "register"
     * @return true iff the attempt was successful
     */
    private boolean accessAttempt(String intent) {
        this.user = ((EditText)findViewById(R.id.username_input)).getText().toString();
        this.pass = ((EditText)findViewById(R.id.password_input)).getText().toString();

        if (userManager != null) {
            if (intent.equals("register")) {
                return userManager.signUp(this.user, this.pass);
            }
            return userManager.signIn(this.user, this.pass);
        }
        return false;
    }

    /**
     * Returns user to the MainMenuActivity upon a successful login or registration
     */
    private void sendToMain() {
        Intent mainMenu = new Intent();
        setResult(Activity.RESULT_OK, mainMenu);
        finish();
    }

}