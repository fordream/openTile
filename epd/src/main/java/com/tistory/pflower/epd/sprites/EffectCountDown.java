package com.tistory.pflower.epd.sprites;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;

import java.util.LinkedList;
import java.util.Random;

/**
 * Created by 2012105064 on 2015-11-07.
 */
public class EffectCountDown extends Cube{

    public float timer;
    public boolean isFinished;

    LinkedList<Float> arrival;

    public EffectCountDown() {
        super("num");

        arrival = new LinkedList<>();

        setWidth(WIDTH);
        setHeight(HEIGHT);

        setCullingEnabled(true);
        setCurrentTileIndex(3); // nothing

        init();

        isFinished = true;

        timer = 0.0f;
    }

    public void init() {

        arrival.add(timer + 3.0f);

        this.setVisible(true);
        isFinished = false;
    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed);

        timer += pSecondsElapsed;

        if(arrival.peek() != null) {

            if (arrival.peek() - timer > 0.0f) {
                setCurrentTileIndex((int) (arrival.peek() - timer) < 3 ?  (int)(arrival.peek() - timer) : 3 );
            } else {
                arrival.poll();
            }
        }
        else {
            setCurrentTileIndex(3);
            this.setVisible(false);
            isFinished = true;
        }

    }
}
