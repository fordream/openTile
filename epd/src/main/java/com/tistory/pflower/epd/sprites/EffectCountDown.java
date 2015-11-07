package com.tistory.pflower.epd.sprites;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;

import java.util.Random;

/**
 * Created by 2012105064 on 2015-11-07.
 */
public class EffectCountDown extends Cube{

    public float timer;
    public boolean isFinished;

    public EffectCountDown() {
        super("num");

        setWidth(WIDTH);
        setHeight(HEIGHT);

        setCullingEnabled(true);
        setCurrentTileIndex(3); // nothing

        init();

        isFinished = true;
    }

    public void init() {

        this.setVisible(true);
        timer = 3.0f;
        isFinished = false;
    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed);

        if(timer > 0.0f) {
            setCurrentTileIndex( (int)timer );
            timer -= pSecondsElapsed;
        }
        else {
            this.setVisible(false);
            isFinished = true;
        }


    }
}
