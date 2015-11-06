//source: https://c0deattack.wordpress.com/2011/01/06/andengine-using-the-object-pool/
package com.tistory.pflower.frontline.pools;

import java.util.LinkedList;

import org.andengine.util.adt.pool.GenericPool;

import com.tistory.pflower.frontline.Tile.Tile;
import com.tistory.pflower.frontline.object.Enemy;

import android.util.Log;

public class EnemyPool extends GenericPool<Enemy> {

	public static EnemyPool instance;
	public static LinkedList<Enemy> list;
	public static EnemyPool sharedEnemyPool() {

		if (instance == null)
			instance = new EnemyPool();
		return instance;

	}

	private EnemyPool() {
		super();
		list = new LinkedList<Enemy>();
	}

	@Override
	protected Enemy onAllocatePoolItem() {
		Enemy e = new Enemy();
		list.add(e);
		return e;
	}

	@Override
	protected void onHandleObtainItem(Enemy pItem) {
		pItem.init();
	}

	protected void onHandleRecycleItem(final Enemy e) {
		e.setVisible(false);
		e.detachSelf();
		e.clean();
		Log.v("Jimvaders", "EnemyPool onHandleRecycleItem()");
	}

	public static void clear() {
		for (Enemy e : list) {
			if(!e.equals(null))
				EnemyPool.sharedEnemyPool().recyclePoolItem(e);
		}
		list.clear();
	}
}