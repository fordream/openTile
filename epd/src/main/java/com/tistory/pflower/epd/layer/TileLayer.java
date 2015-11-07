package com.tistory.pflower.epd.layer;

/**
 * Created by 2012105064 on 2015-11-06.
 */

import android.util.Log;

import com.tistory.pflower.epd.BaseActivity;
import com.tistory.pflower.epd.ResourceManager;
import com.tistory.pflower.epd.gameLogic.TileManager;
import com.tistory.pflower.epd.sprites.Cube;
import com.tistory.pflower.epd.sprites.Hero;
import com.tistory.pflower.epd.sprites.Tile;

import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.batch.SpriteBatch;
import org.andengine.opengl.texture.region.TiledTextureRegion;


import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

public class TileLayer extends Entity {

    public static final int TILE_CNT = 16;
    public static final int WIDTH_CNT = 4;
    private static final int BATCH_SPRITE_CNT = 16;


    public static TileLayer instance = null;

    Random rnd = new Random(System.currentTimeMillis());

    private SpriteBatch batchBack;
    private SpriteBatch batchFore;

    public Tile[] tiles;

    public Hero hero;

    public int backLeftTilePosition[] =
            {
                    -1, 0,
                    -1, 1,
                    -1, 2,
                    -1, 3,

                    -2, -1,
                    -2, 0,
                    -2, 1,
                    -2, 2,
            };

    public static TileLayer getInstance()
    {
        return instance;
    }

    public TileLayer() {
        instance = this;


        tiles = new Tile[TILE_CNT];


        for(int i = 0; i < WIDTH_CNT; i ++) {
            for (int j = 0; j < WIDTH_CNT; j++) {
                Tile t = new Tile(rnd.nextInt());
                attachChild(t);

                tiles[i * WIDTH_CNT + j] = t;
                t.setLocation(j, i);
                t.setTileModifier();
            }
        }

        setBatchSprites();

        hero = new Hero();

        attachChild(hero);
    }

    public void setBatchSprites()
    {
        TiledTextureRegion region = ResourceManager.getSharedInstance().getTiledResourceByName("tile");

        final int width = (int) region.getWidth();
        final int height = (int) region.getHeight();

        batchBack = new SpriteBatch(region.getTexture(), BATCH_SPRITE_CNT, BaseActivity.getSharedInstance().getVertexBufferObjectManager());
        batchBack.setBlendFunction(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);

        int TILE_WIDTH_HALF = Cube.WIDTH / 2 + Cube.TILE_MARGIN;
        int TILE_HEIGHT_QUAT = Cube.HEIGHT_HALF / 2 + Cube.TILE_MARGIN;

        for(int i = 0; i < BATCH_SPRITE_CNT / 2; i++) {
            int locX = backLeftTilePosition[i * 2 + 1];
            int locY = backLeftTilePosition[i * 2];

            int screenX = locX * TILE_WIDTH_HALF - locY * TILE_WIDTH_HALF + BaseActivity.CAMERA_WIDTH / 2 - TILE_WIDTH_HALF;
            int screenY = locX * TILE_HEIGHT_QUAT + locY * TILE_HEIGHT_QUAT + TILE_HEIGHT_QUAT;

            batchBack.draw(region.getTextureRegion(2), screenX, screenY, width, height, 1f, 1f, 1f, 1f);
        }

        for(int i = 0; i < BATCH_SPRITE_CNT / 2; i++) {
            int locX = backLeftTilePosition[i * 2];
            int locY = backLeftTilePosition[i * 2 + 1];


            int screenX = locX * TILE_WIDTH_HALF - locY * TILE_WIDTH_HALF + BaseActivity.CAMERA_WIDTH / 2 - TILE_WIDTH_HALF;
            int screenY = locX * TILE_HEIGHT_QUAT + locY * TILE_HEIGHT_QUAT + TILE_HEIGHT_QUAT;

            batchBack.draw(region.getTextureRegion(2), screenX, screenY, width, height, 1f, 1f, 1f, 1f);
        }

        batchBack.submit();
        attachChild(batchBack);
        batchBack.setZIndex(-1);

        batchFore = new SpriteBatch(region.getTexture(), BATCH_SPRITE_CNT, BaseActivity.getSharedInstance().getVertexBufferObjectManager());
        batchFore.setBlendFunction(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);

        for(int i = 0; i < BATCH_SPRITE_CNT / 2; i++) {
            int locX = backLeftTilePosition[i * 2] * -1 + (WIDTH_CNT - 1);
            int locY = backLeftTilePosition[i * 2 + 1] ;

            int screenX = locX * TILE_WIDTH_HALF - locY * TILE_WIDTH_HALF + BaseActivity.CAMERA_WIDTH / 2 - TILE_WIDTH_HALF;
            int screenY = locX * TILE_HEIGHT_QUAT + locY * TILE_HEIGHT_QUAT + TILE_HEIGHT_QUAT;

            batchFore.draw(region.getTextureRegion(2), screenX, screenY, width, height, 1f, 1f, 1f, 1f);
        }

        for(int i = 0; i < BATCH_SPRITE_CNT / 2; i++) {
            int locX = backLeftTilePosition[i * 2 + 1];
            int locY = backLeftTilePosition[i * 2] * -1 + (WIDTH_CNT - 1);

            int screenX = locX * TILE_WIDTH_HALF - locY * TILE_WIDTH_HALF + BaseActivity.CAMERA_WIDTH / 2 - TILE_WIDTH_HALF;
            int screenY = locX * TILE_HEIGHT_QUAT + locY * TILE_HEIGHT_QUAT + TILE_HEIGHT_QUAT;

            batchFore.draw(region.getTextureRegion(2), screenX, screenY, width, height, 1f, 1f, 1f, 1f);
        }

        batchFore.submit();
        attachChild(batchFore);

        sortChildren();
    }

    public void purge() {

        detachChildren();
        for (Tile t : tiles) {
            t.purge();
        }
        hero.purge();
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

    public Tile At(int idx) {
        return tiles[idx];
    }

}