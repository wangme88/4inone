package group_0661.gamecentre;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.WindowManager;

/**
 * Initializes an activity as a pop-up window. Extend this class to create such an activity.
 */
public class PopUpActivity extends AppCompatActivity {

    /**
     * Adjusts the dimensions of the popUpActivity
     *
     * @param widthModifier pop-up width = widthModifier * screenWidth
     * @param heightModifier pop-up height = heightModifier * screenHeight
     */
    public void configurePopUp(double widthModifier, double heightModifier) {
        // Retrieves the height and width of the system
        int width = Resources.getSystem().getDisplayMetrics().widthPixels;
        int height = Resources.getSystem().getDisplayMetrics().heightPixels;

        // Adjusts the pop up with the modifier inputs
        getWindow().setLayout((int)(width*widthModifier), (int)(height*heightModifier));

        // Sets the position of the pop up within the phone display
        WindowManager.LayoutParams par = getWindow().getAttributes();
        par.gravity = Gravity.CENTER_VERTICAL;
        par.y = -30;
        getWindow().setAttributes(par);
    }
}
