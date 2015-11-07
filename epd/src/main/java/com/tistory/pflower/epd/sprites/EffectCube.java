package com.tistory.pflower.epd.sprites;

import com.tistory.pflower.epd.gameLogic.EffectEvent;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.DelayModifier;

import java.util.Random;

/**
 * Created by 2012105064 on 2015-11-07.
 */
public class EffectCube extends Cube{


    public EffectCountDown text;

    public EffectCube() {
        super("sprite");

        text = new EffectCountDown();

        //setWidth(WIDTH);
        //setHeight(HEIGHT);

        setCullingEnabled(true);
        setCurrentTileIndex(0); // rnd.nextInt(TILE_MAX_IDX)

        //text.setZIndex(99);
        setVisible(false);
    }




    public void setExplosion(EffectEvent e, float sec) {
        text.init();

        final EffectEvent _e = e;

        setCurrentTileIndex(_e.insturNum % 3);

        DelayModifier mod = new DelayModifier(3f)
        {
            @Override
            protected void onModifierFinished(IEntity pItem) {
                super.onModifierFinished(pItem);

                // EXPLODE!
                pItem.setVisible(true);
                pItem.registerEntityModifier(new AlphaModifier(1f, 1f, 0f) {
                    @Override
                    protected void onModifierFinished(IEntity pItem) {
                        super.onModifierFinished(pItem);
                        pItem.setVisible(false);
                    }
                });
                _e.play();
            }

        };

        registerEntityModifier(mod);
    }
}
