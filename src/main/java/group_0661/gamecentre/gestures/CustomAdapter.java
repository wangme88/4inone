package group_0661.gamecentre.gestures;


/*
Taken from:
https://github.com/DaveNOTDavid/sample-puzzle/blob/master/app/src/main/java/com/davenotdavid/samplepuzzle/CustomAdapter.java

This Class is an overwrite of the Base Adapter class
It is designed to aid setting the button sizes and positions in the GridView
 */

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import java.util.ArrayList;

/**
 * Custom adapter for setting up view for GridView
 */
public class CustomAdapter extends BaseAdapter {

    /**
     * The list of buttons displayed on board
     */
    private ArrayList<Button> mButtons = null;

    /**
     * The height and width of the display
     */
    private int mColumnWidth, mColumnHeight;

    /**
     * Initialize a new custom adapter
     *
     * @param buttons the list of buttons to be displayed
     * @param columnWidth the width of the display
     * @param columnHeight the height of the display
     */
    public CustomAdapter(ArrayList<Button> buttons, int columnWidth, int columnHeight) {
        mButtons = buttons;
        mColumnWidth = columnWidth;
        mColumnHeight = columnHeight;
    }

    /**
     * Get the number of buttons in GridView
     *
     * @return the number of buttons
     */
    @Override
    public int getCount() {
        return mButtons.size();
    }

    /**
     * Fetch the item at position
     *
     * @param position the position to fetch object from
     * @return the object at position
     */
    @Override
    public Object getItem(int position) {
        return mButtons.get(position);
    }

    /**
     * Get the item id at position
     *
     * @param position the position of the object
     * @return the position of the item corresponding to the id
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Generates layout for GridView
     *
     * @param position the position to fetch object from
     * @param convertView reuses old view if possible
     * @param parent inflate the view for layout parameter
     * @return the view object for GridView
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Button button;

        if (convertView == null) {
            button = mButtons.get(position);
        } else {
            button = (Button) convertView;
        }

        android.widget.AbsListView.LayoutParams params =
                new android.widget.AbsListView.LayoutParams(mColumnWidth, mColumnHeight);
        button.setLayoutParams(params);

        return button;
    }
}

