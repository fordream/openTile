package com.tistory.pflower.frontline.Tile;


import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Random;

import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.util.math.MathUtils;

import com.tistory.pflower.frontline.pools.BulletPool;
import com.tistory.pflower.frontline.pools.TileLinePool;
import android.util.Log;

import com.tistory.pflower.frontline.BaseActivity;
import com.tistory.pflower.frontline.Bullet;

public class TileLayer extends Entity {

	public LinkedHashMap<Integer, TileLine> lines;
	public static TileLayer instance;
	public int seed;

	public static final int BEGIN_LOC = 32;
	public static int oldHeight;
	public int nextLoc;
	public int lastLoc;
	Random gen;

	public static TileLayer getSharedInstance() {
		return instance;
	}

	public static boolean isEmpty() {
		if (instance.lines.size() == 0)
			return true;
		return false;
	}

	public static Iterator<TileLine> getIterator() {
		return instance.lines.values().iterator();
	}

	public void purge() {

		detachChildren();
		for (TileLine l : instance.lines.values()) {
			l.purge();
			TileLinePool.sharedTilePool().recyclePoolItem(l);
		}
		lines.clear();
		
	}

	public TileLayer(int x) {
		lines = new LinkedHashMap<Integer, TileLine>();
		instance = this;
		gen = new Random();
		oldHeight = 0;
	}

	public void restart() {
		Log.v("jimvaders", "TileLayer restarted");
		nextLoc = 0;
		lastLoc = 0;
		lines.clear();
		clearEntityModifiers();
		clearUpdateHandlers();

		setVisible(true);
		setPosition(0, 0);
		oldHeight = 0;
		
	}
	
	public void processLine(int maximumX) {
		int wantedMin = (maximumX - BaseActivity.CAMERA_WIDTH / 2) / 32;
		int wantedMax = (maximumX + BaseActivity.CAMERA_WIDTH / 2) / 32 + 1;
		
		int to = wantedMax - nextLoc + 1;
		for(int i = 0; i < to; i ++)
			createNextLine();
		to = wantedMin - lastLoc;
		for(int i = 0; i < to; i ++)
			deleteFirstLine();
	}
	
	public void createNextLine()
	{
		int height = 0;
		if(nextLoc < BEGIN_LOC)
			height  = 0;
		else
		{
			height = oldHeight + MathUtils.random(-1, 1);
			if(height < 0) height = 0;
			if(height > TileLine.MAX_HEIGHT) height = TileLine.MAX_HEIGHT;
			oldHeight = height;
		}

		TileLine t = TileLinePool.sharedTilePool().obtainPoolItem();
		t.restart(nextLoc, height);
		
		attachChild(t);
		lines.put(nextLoc, t);
		++nextLoc;
	}
	
	public void deleteFirstLine()
	{
		if(lines.isEmpty())
			return;
		++lastLoc;
		
		TileLine first = lines.values().iterator().next();
		while(first.location < lastLoc)
		{
		
			lines.remove(first.location);	
			first.detachSelf();				
			TileLinePool.sharedTilePool().recyclePoolItem(first);
			
			
			if(!lines.isEmpty())
				first = lines.values().iterator().next();
			else 
				break;
		}
		//heights.removeFirst();
		
	}
	
	public static void purgeAndRestart() {
		Log.v("Jimvaders", "EnemyLayer PurgeAndRestart()");
		instance.purge();
		instance.restart();
	}

	@Override
	public void onDetached() {
		purge();
		clearUpdateHandlers();
		super.onDetached();
	}
/*
	public static int isCollsionVertical(Rectangle e) {
		// �� ���鿡 �浹
		// Location�� ��ġ�µ�, X�� ũ���� Height �κ��� ������
		
		for (TileLine l : instance.lines.values()) {
			
			if(l.collidesWith(e))
			{
				Iterator<Tile> line = l.getIterator();
				
				while(line.hasNext())
				{
					Tile t = line.next();
					
					if(t.collidesWith(e))
					{
						t.gotHit();
						BulletPool.sharedBulletPool().recyclePoolItem((Bullet)e);
					}
				}

			}
			
			
		}
		return 0;
	}
    */

	public static boolean isCollsionHorizontal(Rectangle e) {
		// �� �κп� �浹
		
		for (TileLine l : instance.lines.values()) {
			if( ( e.getX() / 32 >=  l.location) && ( (e.getX() + e.getWidth()) / 32 <= l.location + 1)) // X�� ���Ե�
			{
				if( (e.getY() + e.getHeight()) / 14 < BaseActivity.CAMERA_HEIGHT / 32 - l.height ) // �� �κ��� �ɸ�
				{
					return true; // �Ϲ� �浹
				}
			}
		}
		return false;
	}
	
	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		// TODO Auto-generated method stub
		super.onManagedUpdate(pSecondsElapsed);
		
	}


}