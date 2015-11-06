package com.tistory.pflower.frontline.pools;

import java.util.Iterator;
import java.util.LinkedList;

import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.util.adt.pool.GenericPool;

import com.tistory.pflower.frontline.BaseActivity;
import com.tistory.pflower.frontline.Projectile;
import com.tistory.pflower.frontline.Tile.TileLine;
import com.tistory.pflower.frontline.object.Enemy;

import android.util.Log;

public class ProjectilePool extends GenericPool<Projectile> {

	public static ProjectilePool instance;
	public static TextureRegion mTextureRegion;
	private static LinkedList<Projectile> list;
	
	public enum ProjectType
	{
		FRAG,
		SHOT_ROUND,
		GUN_ROUND,
	};

	
	public static ProjectilePool sharedProjPool() {
		if (instance == null)
			instance = new ProjectilePool();
		return instance;

	}

	private ProjectilePool() {
		super();
		list = new LinkedList<Projectile>();
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("game/");
		BuildableBitmapTextureAtlas mBitmapTextureAtlas = new BuildableBitmapTextureAtlas(BaseActivity.getSharedInstance().getTextureManager(), 16, 16, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        mTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBitmapTextureAtlas, BaseActivity.getSharedInstance(), "flag.png");
        
        mBitmapTextureAtlas.load();
	}

	@Override
	protected Projectile onAllocatePoolItem() {
		return new Projectile();
	}

	protected void onHandleRecycleItem(final Projectile b) {
		b.clearEntityModifiers();
		b.clearUpdateHandlers();
		b.clean();
		b.setVisible(false);
		b.detachSelf();
		Log.v("Jimvaders", "BulletPool onHandleRecycleItem()");
	}

	public static void clear() {
		for (Projectile e : list) {
			if(!e.equals(null))
				ProjectilePool.sharedProjPool().recyclePoolItem(e);
		}
		list.clear();
	}

}