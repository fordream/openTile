package com.tistory.pflower.frontline;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.util.math.MathUtils;
import org.andengine.util.modifier.IModifier;

import com.tistory.pflower.frontline.Tile.TileLayer;
import com.tistory.pflower.frontline.Tile.TileLine;
import com.tistory.pflower.frontline.object.Chopper;
import com.tistory.pflower.frontline.object.Enemy;
import com.tistory.pflower.frontline.object.Medibox;
import com.tistory.pflower.frontline.scene.GameScene;

import com.tistory.pflower.frontline.pools.EnemyPool;
import android.util.Log;

public class EnemyLayer extends Entity {

    private static int MAX_ENEMY = 20;

	private LinkedList<Enemy> enemies;
	
	private TimerHandler timer;
	private Chopper chop;
	public static EnemyLayer instance;
	
	public static int remains = 0;
	private int nextChopper;
	public int enemyCount;
	private int waitInterval;

	public static EnemyLayer getSharedInstance() {
		return instance;
	}

	public static boolean isEmpty() {
		if (instance.enemies.size() == 0)
			return true;
		return false;
	}

	public static Iterator<Enemy> getIterator() {
		return instance.enemies.iterator();
	}

	public void purge() {

		BaseActivity.getSharedInstance().getEngine().unregisterUpdateHandler(timer);
		detachChildren();
		for (Enemy e : enemies) {
			EnemyPool.sharedEnemyPool().recyclePoolItem(e);
		}
		enemies.clear();
		
		nextChopper = 10;
		waitInterval = 0;
		remains = 0;
	}

	public EnemyLayer() {
		enemies = new LinkedList<Enemy>();
		instance = this;
		
		
		chop = Chopper.getSharedInstance();
		
		
		
		timer = new TimerHandler(1.f, true,new ITimerCallback()
        {   
			private int numTries = 0;
			
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler)
            {
            	if(!(BaseActivity.getSharedInstance().mCurrentScene instanceof GameScene))
            		return;
            	GameScene scene = (GameScene) BaseActivity.getSharedInstance().mCurrentScene;
            	if(scene.character == null)
		    		 return;
            	if(waitInterval > 0)
            	{
            		Log.d("EnemyLayer", "wait");
            		waitInterval -= pTimerHandler.getTimerSecondsElapsed();
            		return;
            	}
            	// ��� ����
            	
            	if(GameScene.maximumX > nextChopper * 32) 
            	{
            			
            		 boolean backEnemy = (MathUtils.random(0, 1) == 0);
            		 final float xPos = (backEnemy ? GameScene.maximumX - BaseActivity.CAMERA_WIDTH / 2: GameScene.maximumX + BaseActivity.CAMERA_WIDTH / 2 );
    	             final float yPos = chop.getHeight() / 2;
    	             
    	             
    	             if(chop.hasParent())
    	            	 chop.detachSelf();
    	             if(backEnemy)
    	            	 chop.setScaleX(-1.0f);
    	             else
    	            	 chop.setScaleX(1.0f);
    	             attachChild(chop);
    	     		 chop.setVisible(true);
    	     		 
    	     		
    	     		
    	             chop.inbound(xPos, yPos, backEnemy);
    	             nextChopper += 100;
            	}
            	 
 		    	 // ������ ������ ���� ������ 1�ʸ��� ��ȯ
		    	 
            	 int squadSize = Math.min((int) (GameScene.maximumX / 320), MAX_ENEMY);
            	 
            	 if(squadSize > remains)
            	 {            		 
            		 waitInterval = 0;
            		 numTries = 0;
            		 // 5% Ȯ���� �ڿ��� ����
    		    	 boolean backEnemy = (MathUtils.random(0, 20) == 0);
    		    	     		    	 
    		    	 final float xPos = (backEnemy ? GameScene.maximumX - BaseActivity.CAMERA_WIDTH / 2 : GameScene.maximumX + BaseActivity.CAMERA_WIDTH / 2 );
    	             final float yPos = BaseActivity.CAMERA_HEIGHT;
    	                       
    	             Enemy e = EnemyPool.sharedEnemyPool().obtainPoolItem();
    	             
    	             
    	             TileLine _l = TileLayer.getSharedInstance().lines.get( (int)((e.getX() + e.getWidth() / 2) / 32));
    	             if(_l != null)
    	            	 e.setPosition(xPos - e.getWidth() / 2, yPos - e.getHeight() - _l.height * 14);
    	             else
    	            	 e.setPosition(xPos - e.getWidth() / 2, yPos - e.getHeight());
    	             
    	             attachChild(e);
    	             e.setVisible(true);
    	 			 enemies.add(e);
    	 			 e.moveCharacter(-0.1f);
    	 			 
    	 			 remains++;
            	 }
            	 else // �̹� ū ��쿡�� 2^(��õ� Ƚ��)���� ��ٸ���.
            	 {
            		 numTries++;
            		 waitInterval = (int)Math.pow(2, numTries);
            	 }
            	
            }
        });
	}

	public void restart() {
		Log.v("jimvaders", "EnemyLayer restarted");
		enemies.clear();
		clearEntityModifiers();
		clearUpdateHandlers();
		
		startLevel();

		setVisible(true);
	}

	
	public void startLevel()
	{
	        // 1�ʸ��� �� ��ȯ�� �õ��Ѵ�.
	        BaseActivity.getSharedInstance().getEngine().registerUpdateHandler(timer);
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

}