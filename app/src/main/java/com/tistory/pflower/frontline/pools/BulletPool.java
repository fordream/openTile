//source: https://c0deattack.wordpress.com/2011/01/06/andengine-using-the-object-pool/
package com.tistory.pflower.frontline.pools;

import org.andengine.util.adt.pool.GenericPool;

import com.tistory.pflower.frontline.Bullet;

import android.util.Log;

public class BulletPool extends GenericPool<Bullet> {

	public static BulletPool instance;

	public static BulletPool sharedBulletPool() {
		if (instance == null)
			instance = new BulletPool();
		return instance;

	}

	private BulletPool() {
		super();
	}

	@Override
	protected Bullet onAllocatePoolItem() {
		Log.v("Jimvaders", "BulletPool onAllocatePoolItem()");
		return new Bullet();
	}

	protected void onHandleRecycleItem(final Bullet b) {
		b.isExpired = false;
		b.setDead(false);
		b.clearEntityModifiers();
		b.clearUpdateHandlers();
		b.setVisible(false);
		b.detachSelf();
		Log.v("Jimvaders", "BulletPool onHandleRecycleItem()");
	}
}