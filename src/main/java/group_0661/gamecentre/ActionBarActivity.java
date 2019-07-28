package group_0661.gamecentre;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Initializes an activity with an action bar. Extend this class to create such an activity.
 */
public class ActionBarActivity extends AppCompatActivity {

    /**
     * Adjusts the dimensions of the ActionBarActivity
     *
     * @param title the text you want to appear next to the home button on the action bar
     */
    public void configureActionBar(String title) {
        // Initializes the action bar as a tool bar
        Toolbar gameToolBar = findViewById(R.id.action_bar);
        setSupportActionBar(gameToolBar);

        // Sets the title of the actionBar
        getSupportActionBar().setTitle(title);

        // Initializes the back button
        ActionBar gameActionBar = getSupportActionBar();
        gameActionBar.setDisplayHomeAsUpEnabled(true);
    }

}