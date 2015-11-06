package com.tistory.pflower.epd.sprites;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.sprite.TiledSprite;

import com.tistory.pflower.epd.ResourceManager;
import com.tistory.pflower.epd.BaseActivity;

import java.util.Random;

public class Tile extends Cube{

    public final int TILE_MAX_IDX = 40;

    Random rnd;

    public Tile(int seed) {
        super("tile");

        rnd = new Random(seed);

        setWidth(WIDTH);
        setHeight(HEIGHT);

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
        setTileModifier();
    }

    public void purge(){
        setLocation(-1, -1);
        setTileModifier();
    }

    public void setTileModifier()
    {
        final Tile _this = this;
        registerEntityModifier(new LoopEntityModifier( new SequenceEntityModifier(
                new MoveModifier(0.25f, getX(), screenX + (rnd.nextInt(TILE_MARGIN) - TILE_MARGIN / 2),
                        getY(), screenY + (rnd.nextInt(TILE_MARGIN) - TILE_MARGIN / 2))
                {
                    @Override
                    protected void onModifierFinished(IEntity pItem) {
                        super.onModifierFinished(pItem);
                        this.reset(0.25f, _this.getX(), screenX + (rnd.nextInt(TILE_MARGIN) - TILE_MARGIN / 2), _this.getY(), screenY + (rnd.nextInt(TILE_MARGIN) - TILE_MARGIN / 2));
                    }
                }
        )
        ));
    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed);
    }
}
