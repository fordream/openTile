package com.tistory.pflower.frontline.Tile;


import org.andengine.entity.sprite.TiledSprite;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;

import com.tistory.pflower.frontline.BaseActivity;
import com.tistory.pflower.frontline.ResourceManager;

public class Tile extends  TiledSprite{
	public ITextureRegion pTextureRegion;
	public int destructionLevel;
	// the max health for each enemy
	protected final int MAX_LEVEL = 4;
	public int location;
	public int height;

	public Tile() {

        super(0, 0, ResourceManager.getSharedInstance().getTiledResourceByName("tile"), BaseActivity.getSharedInstance().getVertexBufferObjectManager());
                //ResourceManager.getSharedInstance().getSharedVertexByTextureRegion(ResourceManager.getSharedInstance().getTiledResourceByName("tile")) );
		location = -1;
		height = -1;
        setCullingEnabled(true);
	}

	public void clean() {
		clearEntityModifiers();
		clearUpdateHandlers();
	}

    // method for applying hit and checking if enemy died or not
	// returns false if enemy died
	public boolean gotHit() {
		synchronized (this) {
            if (++destructionLevel >= 16)
                destructionLevel = 15;

			if( (destructionLevel - 1) / 4 != destructionLevel / 4) {
                setCurrentTileIndex( destructionLevel / 4 );
            }
		}
        return true;
	}

	public void init() {
		destructionLevel = 0;
        setCurrentTileIndex(0);
		location = -1;
		height = -1;
	}
	
	public void setLocation(int _loc, int _height) {
		destructionLevel = 0;
		
		location = _loc;
		height = _height;
	}
}
