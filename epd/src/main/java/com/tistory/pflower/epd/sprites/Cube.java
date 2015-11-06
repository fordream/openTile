package com.tistory.pflower.epd.sprites;

import com.tistory.pflower.epd.BaseActivity;
import com.tistory.pflower.epd.ResourceManager;

import org.andengine.entity.sprite.TiledSprite;

/**
 * Created by 2012105064 on 2015-11-07.
 */
public class Cube extends TiledSprite {

    public static final int WIDTH = 128;
    public static final int HEIGHT = 128;

    public static final int HEIGHT_HALF = 64;
    public static final int TILE_MARGIN = 5;

    protected int locationX;
    protected int locationY;

    protected int screenX;
    protected int screenY;

    public enum Direction
    {
        UP_RIGHT,
        UP_LEFT,
        DOWN_LEFT,
        DOWN_RIGHT
    };

    public Cube(String fileName) {
        super(0, 0, ResourceManager.getSharedInstance().getTiledResourceByName(fileName), BaseActivity.getSharedInstance().getVertexBufferObjectManager());
    }

    public void setLocation(int locX, int locY) {

        int TILE_WIDTH_HALF = WIDTH / 2 + TILE_MARGIN;
        int TILE_HEIGHT_QUAT = HEIGHT_HALF / 2 + TILE_MARGIN;

        screenX = locX * TILE_WIDTH_HALF - locY * TILE_WIDTH_HALF + BaseActivity.CAMERA_WIDTH / 2 - TILE_WIDTH_HALF;
        screenY = locX * TILE_HEIGHT_QUAT + locY * TILE_HEIGHT_QUAT + TILE_HEIGHT_QUAT;

        locationX = locX;
        locationY = locY;

        setX(screenX);
        setY(screenY);
    }

}
