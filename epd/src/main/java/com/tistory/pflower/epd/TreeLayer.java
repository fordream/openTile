package com.tistory.pflower.epd;

import android.util.Log;

import org.andengine.entity.Entity;
import org.andengine.entity.sprite.batch.SpriteBatch;
import org.andengine.entity.sprite.batch.SpriteGroup;

public class TreeLayer extends Entity {

    private static int MAX_ENEMY = 20;

    public static TreeLayer instance = null;

    public static int remains = 0;

    public static TreeLayer getSharedInstance() {
        return instance;
    }

    public void purge() {

        detachChildren();
        remains = 0;
    }

    private TreeLayer() {
        instance = this;

        initSprite();
    }

    private void initSprite() {

    }

    public void restart() {
        Log.v("jimvaders", "EnemyLayer restarted");
        clearEntityModifiers();
        clearUpdateHandlers();

        startLevel();

        setVisible(true);
    }


    public void startLevel()
    {
    }

    public static void purgeAndRestart() {
        Log.v("Jimvaders", "EnemyLayer PurgeAndRestart()");
        instance.purge();
        instance.restart();
    }

    @Override
    public void onDetached() {
        purge();
        clearUpdateHandlers();
        super.onDetached();
    }

}