package com.tistory.pflower.epd.sprites;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.DelayModifier;

import java.util.Random;

/**
 * Created by 2012105064 on 2015-11-07.
 */
public class EffectCube extends Cube{


    EffectCountDown text;

    public EffectCube() {
        super("sprite");

        text = new EffectCountDown();

        setWidth(WIDTH);
        setHeight(HEIGHT);

        setCullingEnabled(true);
        setCurrentTileIndex(0); // rnd.nextInt(TILE_MAX_IDX)
    }




    public void setExplosion(int insturID, float sec) {
        text.init();
        DelayModifier mod = new DelayModifier(3f)
        {
            @Override
            protected void onManagedUpdate(float pSecondsElapsed, IEntity pEntity) {
                super.onManagedUpdate(pSecondsElapsed, pEntity);

                // EXPLODE!
                pEntity.registerEntityModifier(new AlphaModifier(0.25f, 1f, 0f));
            }
        };

        registerEntityModifier(mod);
    }
}
