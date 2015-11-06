package com.tistory.pflower.epd.sprites;

import android.util.FloatMath;
import android.util.Log;

import com.tistory.pflower.epd.BaseActivity;
import com.tistory.pflower.epd.ResourceManager;
import com.tistory.pflower.epd.layer.TileLayer;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.PathModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.util.modifier.ease.EaseBackOut;
import org.andengine.util.modifier.ease.EaseBounceInOut;
import org.andengine.util.modifier.ease.EaseLinear;

import java.util.Random;

/**
 * Created by 2012105064 on 2015-11-06.
 */
public class Hero extends Cube {

    public Hero() {
        super("slime");

        locationX = 1;
        locationY = 2;

        isMoving = false;

        setWidth(WIDTH);
        setHeight(HEIGHT);

        setCullingEnabled(true);
        setCurrentTileIndex(0); // rnd.nextInt(TILE_MAX_IDX)

        setScaleCenter(WIDTH / 2, HEIGHT);

        setScale(0.5f);

        registerEntityModifier(new LoopEntityModifier(new SequenceEntityModifier(
                new ScaleModifier(1f, 0.5f, 0.7f, EaseBackOut.getInstance()),
                new ScaleModifier(0.7f, 0.7f, 0.5f, EaseLinear.getInstance())
        )));

        setLocation(locationX, locationY);

        instance = this;
    }

    private boolean isMoving;

    private static Hero instance = null;

    public static Hero getSharedInstance() {
        return instance;
    }

    public void setMoving(Boolean isMoving)
    {
        this.isMoving = isMoving;
    }

    public void move(Direction dir)
    {
        Log.d("Hero", "move : " + isMoving);
        if(isMoving)
            return;

        int TILE_WIDTH_HALF = WIDTH / 2 + TILE_MARGIN;
        int TILE_HEIGHT_QUAT = HEIGHT_HALF / 2 + TILE_MARGIN;
        //int locX = locationX, locY = locationY;



        switch (dir) {
            case UP_RIGHT:
                Log.d("Hero", "ur --locY : " + locationY);
                if(locationY - 1 < 0) return;
                --locationY;
                setCurrentTileIndex(2);
                break;
            case UP_LEFT:
                Log.d("Hero", "ur --locX : " + locationX);
                if(locationX - 1 < 0) return;
                --locationX;
                setCurrentTileIndex(3);
                break;
            case DOWN_LEFT:
                Log.d("Hero", "ur ++locY : " + locationY);
                if(locationY + 1 >= TileLayer.WIDTH_CNT) return;
                ++locationY;
                setCurrentTileIndex(0);
                break;
            case DOWN_RIGHT:
                Log.d("Hero", "ur ++locX : " + locationX);
                if(locationX + 1 >= TileLayer.WIDTH_CNT) return;
                ++locationX;
                setCurrentTileIndex(1);
                break;
        }
        isMoving = true;

        final Hero _me = this;

        final float initialY = getY();

        final float new_screenX = locationX * TILE_WIDTH_HALF - locationY * TILE_WIDTH_HALF + BaseActivity.CAMERA_WIDTH / 2 - WIDTH / 2 - TILE_MARGIN ;
        final float new_screenY = locationX * TILE_HEIGHT_QUAT + locationY * TILE_HEIGHT_QUAT + TILE_HEIGHT_QUAT - WIDTH / 2;

        registerEntityModifier(
                new MoveXModifier(0.08f, getX(), new_screenX) {
                    @Override
                    protected void onManagedUpdate(float pSecondsElapsed, IEntity pItem) {
                        super.onManagedUpdate(pSecondsElapsed, pItem);
                        _me.setY(initialY * (1 - getSecondsElapsed() / getDuration()) + new_screenY * getSecondsElapsed() / getDuration() - HEIGHT * FloatMath.sin((float) (getSecondsElapsed() / getDuration() * Math.PI)));
                    }

                    @Override
                    protected void onModifierFinished(IEntity pItem) {
                        super.onModifierFinished(pItem);
                        _me.setMoving(false);
                        _me.setY(new_screenY);
                    }
                }
        );
    }

    @Override
    public void setLocation(int locX, int locY) {
        super.setLocation(locX, locY);
        setPosition(getX(), getY() - WIDTH / 2);   // offset
    }

    public void purge() {
    }
}
