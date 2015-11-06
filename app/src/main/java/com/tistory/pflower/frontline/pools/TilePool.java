//source: https://c0deattack.wordpress.com/2011/01/06/andengine-using-the-object-pool/
package com.tistory.pflower.frontline.pools;

import java.util.LinkedList;

import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.util.adt.pool.GenericPool;

import com.tistory.pflower.frontline.BaseActivity;
import com.tistory.pflower.frontline.ResourceManager;
import com.tistory.pflower.frontline.Tile.Tile;
import com.tistory.pflower.frontline.object.Enemy;

import android.util.Log;

public class TilePool extends GenericPool<Tile> {

	public static TilePool instance;
	public static LinkedList<Tile> list;

	public static TilePool sharedTilePool() {

		if (instance == null)
			instance = new TilePool();
		return instance;

	}

	

	private TilePool() {
		super();
		list = new LinkedList<Tile>();
		
		
	}

	@Override
	protected Tile onAllocatePoolItem() {
        Log.v("Jimvaders", "TilePool onAllocatePoolItem()");
		Tile t = new Tile();
		list.add(t);
		return t;
	}

	@Override
	protected void onHandleObtainItem(Tile pItem) {
		pItem.init();
	}

	protected void onHandleRecycleItem(final Tile e) {
		e.setVisible(false);
		e.detachSelf();
		e.clean();
		Log.v("Jimvaders", "TilePool onHandleRecycleItem()");
	}

	public static void clear() {
		for (Tile e : list) {
			if(!e.equals(null))
				TilePool.sharedTilePool().recyclePoolItem(e);
		}
		list.clear();
	}
}