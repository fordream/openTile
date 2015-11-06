package com.tistory.pflower.epd.layer;

/**
 * Created by 2012105064 on 2015-11-06.
 */

import android.util.Log;

import com.tistory.pflower.epd.sprites.Tile;

import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Rectangle;


import java.util.Random;

public class TileLayer extends Entity {

    public final int TILE_CNT = 16;
    public final int WIDTH_CNT = 4;
    Random rnd = new Random(System.currentTimeMillis());

    public Tile[] tiles;

    public TileLayer() {

        tiles = new Tile[TILE_CNT];

        for(int i = 0; i < WIDTH_CNT; i ++) {
            for (int j = 0; j < WIDTH_CNT; j++) {
                Tile t = new Tile();
                attachChild(t);

                tiles[i * WIDTH_CNT + j] = t;
                t.setLocation(j, i);
            }
        }
    }

    public void purge() {

        detachChildren();
        for (Tile t : tiles) {
            t.purge();
        }

    }

    public void restart() {
        Log.d("tilelayer", "TileLayer restarted");
        clearEntityModifiers();
        clearUpdateHandlers();

        setVisible(true);
        setPosition(0, 0);
    }


    public void purgeAndRestart() {
        Log.v("Jimvaders", "EnemyLayer PurgeAndRestart()");
        purge();
        restart();
    }

    @Override
    public void onDetached() {
        purge();
        clearUpdateHandlers();
        super.onDetached();
    }

    public boolean isCollsionHorizontal(Rectangle e) {
        return false;
    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        // TODO Auto-generated method stub
        super.onManagedUpdate(pSecondsElapsed);

    }


}