package com.tistory.pflower.epd.sprites;

import org.andengine.entity.sprite.TiledSprite;

import com.tistory.pflower.epd.ResourceManager;
import com.tistory.pflower.epd.BaseActivity;

import java.util.Random;

public class Tile extends  TiledSprite{

    public int locationX;
    public int locationY;

    public float screenX;
    public float screenY;

    public final int TILE_WIDTH = 128;
    public final int TILE_HEIGHT = 128;
    public final int TILE_HEIGHT_HALF = 128;

    public final int TILE_MAX_IDX = 40;

    Random rnd = new Random(System.currentTimeMillis());

    public Tile() {

        super(0, 0, ResourceManager.getSharedInstance().getTiledResourceByName("tile"), BaseActivity.getSharedInstance().getVertexBufferObjectManager());

        setWidth(TILE_WIDTH);
        setHeight(TILE_HEIGHT);

        setCullingEnabled(true);
        setCurrentTileIndex(0); // rnd.nextInt(TILE_MAX_IDX)
    }

    public void clean() {
        clearEntityModifiers();
        clearUpdateHandlers();
    }

    public void init(int locX, int locY) {
        setCurrentTileIndex(0);
        setLocation(locX, locY);
    }

    public void purge(){
        setLocation(-1, -1);
    }

    public void setLocation(int locX, int locY) {

        int TILE_WIDTH_HALF = TILE_WIDTH / 2;
        int TILE_HEIGHT_QUAT = TILE_HEIGHT_HALF / 4;

        screenX = locX * TILE_WIDTH_HALF - locY * TILE_WIDTH_HALF;
        screenY = locX * TILE_HEIGHT_QUAT + locY * TILE_HEIGHT_QUAT;

        locationX = locX;
        locationY = locY;

        setX(screenX);
        setY(screenY);
    }
}
