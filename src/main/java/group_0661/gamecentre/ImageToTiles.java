package group_0661.gamecentre;


import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Locale;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorRes;

/**
 * A module that cuts an input bitmap into n^2 pieces. Allows for custom backgrounds in sliding tiles
 */
public class ImageToTiles {
    /**
     * The new width (in pixels) of the input bitmap after adjustment
     */
    private final int RESIZE_WIDTH = 1800;
    /**
     * The new height (in pixels) of the input bitmap after adjustment
     */
    private final int RESIZE_HEIGHT = 2400;
    /**
     * File path of the saved Bitmap
     */
    private String path;
    /**
     * The bitmap image
     */
    private Bitmap image;
    /**
     * The number of rows that will make up the sliced up image
     */
    private int row;
    private int col;
    /**
     * An arrayList containing the sliced up Bitmap image
     */
    private ArrayList<Bitmap> tiles;

    /**
     * Constructs the image slicer module
     */
    public ImageToTiles(Bitmap image, int col, int row) {
        this.image = android.graphics.Bitmap.createScaledBitmap(image,RESIZE_WIDTH, RESIZE_HEIGHT,true);
        this.col = col;
        this.row = row;
        this.tiles = new ArrayList<>();
    }

    /**
     * Performs the Bitmap slicing and puts the resulting sliced images into ArrayList tiles
     */
    public boolean createTiles() {
        int x = 0;
        int y = 0;

        int num_tiles = col * row;
        int tileWidth = RESIZE_WIDTH / col;
        int tileHeight = RESIZE_HEIGHT / row;

        if (row == col) {
            for (int i = 0; i < num_tiles - 1; i++) {
                Bitmap crop = android.graphics.Bitmap.createBitmap(this.image, x, y, tileWidth, tileHeight);
                this.tiles.add(crop);
                x += tileWidth;

                if (x == RESIZE_WIDTH) {
                    x = 0;
                    y += tileHeight;
                }
            }
            tiles.add(createBlankTile());
        } else {
            for (int i = 0; i < num_tiles; i++) {
                Bitmap crop = android.graphics.Bitmap.createBitmap(this.image, x, y, tileWidth, tileHeight);
                this.tiles.add(crop);
                x += tileWidth;

                if (x == RESIZE_WIDTH) {
                    x = 0;
                    y += tileHeight;
                }
            }
            tiles.add(createBlankTile());
        }

        return true;
    }


    /**
     * Creates a blank Bitmap
     *
     * @return a blank white Bitmap the size of the tiles contained in ArrayList tiles
     */
    private Bitmap createBlankTile() {
        Bitmap blankTile = android.graphics.Bitmap.createBitmap(this.image, 0, 0, RESIZE_WIDTH / 2, RESIZE_HEIGHT / 2);
        blankTile.eraseColor(Color.WHITE);
        Canvas canvas = new Canvas(blankTile);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(15);
        paint.setAntiAlias(true);
        Rect rect = new Rect(
                15 / 2, 15 / 2, canvas.getWidth() - 15/2, canvas.getHeight() - 15/2
        );
        canvas.drawRect(rect,paint);
        return blankTile;
    }

    /**
     * A getter for the save path of the bitmap
     */
    public String getSavePath() {
        return path;
    }

    /**
     * Saves the sliced images to a directory within the internal files of the application
     *
     * @return true iff the save is a success
     */
    public boolean saveTiles(Context c) {
        File dir = new File(c.getFilesDir(), "/tiles");
        if (dir.exists()) { dir.delete(); }
        dir.mkdir();
        path = dir.getAbsolutePath();

        int i = 1;
        for (Bitmap img : tiles) {
            File newImage = new File(dir,  String.format(Locale.CANADA, "tile_%d.png", i));
            try {
                FileOutputStream out = new FileOutputStream(newImage);
                img.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, out);
                out.close();
                i++;
            }
            catch (IOException e) {
                return false;
            }
        }
        return true;
    }
}
