//source: https://c0deattack.wordpress.com/2011/01/06/andengine-using-the-object-pool/
package com.tistory.pflower.frontline.pools;

import java.util.LinkedList;

import org.andengine.util.adt.pool.GenericPool;

import com.tistory.pflower.frontline.Projectile;
import com.tistory.pflower.frontline.Tile.Tile;
import com.tistory.pflower.frontline.Tile.TileLine;

import android.util.Log;

public class TileLinePool extends GenericPool<TileLine> {

	public static TileLinePool instance;
	private static LinkedList<TileLine> list;

	public static TileLinePool sharedTilePool() {

		if (instance == null)
			instance = new TileLinePool();
		return instance;

	}

	private TileLinePool() {
		super();
		list = new LinkedList<TileLine>();
	}

	@Override
	protected TileLine onAllocatePoolItem() {
		return new TileLine();
	}

	@Override
	protected void onHandleObtainItem(TileLine pItem) {
		pItem.init();
	}

	protected void onHandleRecycleItem(final TileLine e) {
		e.setVisible(false);
		e.detachSelf();
		e.clean();
		Log.v("Jimvaders", "TileLinePool onHandleRecycleItem()");
	}

	public static void clear() {
		for (TileLine e : list) {
			if(!e.equals(null))
				TileLinePool.sharedTilePool().recyclePoolItem(e);
		}
		list.clear();
	}
}