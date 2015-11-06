package com.tistory.pflower.frontline.scene;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.util.Log;

import com.tistory.pflower.frontline.BaseActivity;
import com.tistory.pflower.frontline.Bullet;
import com.tistory.pflower.frontline.EnemyLayer;
import com.tistory.pflower.frontline.GameLoopUpdateHandler;
import com.tistory.pflower.frontline.Projectile;
import com.tistory.pflower.frontline.ResourceManager;
import com.tistory.pflower.frontline.SensorListener;
import com.tistory.pflower.frontline.Tile.TileLayer;
import com.tistory.pflower.frontline.Tile.TileLine;
import com.tistory.pflower.frontline.hud.HUDLayer;
import com.tistory.pflower.frontline.object.Chopper;
import com.tistory.pflower.frontline.object.Enemy;
import com.tistory.pflower.frontline.object.Medibox;
import com.tistory.pflower.frontline.object.Soldier;
import com.tistory.pflower.frontline.pools.BulletPool;
import com.tistory.pflower.frontline.pools.ProjectilePool;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.ui.activity.BaseGameActivity;

import java.util.Iterator;

public class GameScene extends Scene implements IOnSceneTouchListener {
	public Soldier character;
	Camera mCamera;
	public float accelerometerSpeedX;
	static SensorManager sensorManager = null;
	
	public int bulletCount;
	public int missCount;

    public int hitCnt;
    public int hitRateChar;

    public int accDam;
    public int hitRateEnemies;
	
	public static float maximumX = 0.0f;
	private HUDLayer hud;
	private AutoParallaxBackground background;
	private int nextBox;
	private boolean isPlayDanger;


	public GameScene() {
		
		BaseActivity activity = BaseActivity.getSharedInstance();		
		
		character = Soldier.getSharedInstance();
		character.detachSelf();

		mCamera = BaseActivity.getSharedInstance().mCamera;
		
		BaseActivity.getSharedInstance().setCurrentScene(this);
		if(sensorManager == null)
		{
			sensorManager = (SensorManager) BaseActivity.getSharedInstance()
					.getSystemService(BaseGameActivity.SENSOR_SERVICE);
			SensorListener.getSharedInstance();
			sensorManager.registerListener(SensorListener.getSharedInstance(),
					sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
					SensorManager.SENSOR_DELAY_GAME);
		}
		setOnSceneTouchListener(this);

		
		attachChild(new TileLayer(24));



		attachChild(new EnemyLayer());
        attachChild(character);
		
		
		Sprite _spr = new Sprite(0,0, ResourceManager.getSharedInstance().getResourceByName("bg"), activity.getVertexBufferObjectManager());
		Sprite _spr2 = new Sprite(0,0, ResourceManager.getSharedInstance().getResourceByName("sky"), activity.getVertexBufferObjectManager());
		
		background = new AutoParallaxBackground(0, 0, 0, 0);
		background.attachParallaxEntity(new ParallaxEntity(-100f, _spr2));
		background.attachParallaxEntity(new ParallaxEntity(10f, _spr));
		
		
	    setBackground(background);
	    
		hud = new HUDLayer(character, this);
		character.bind(hud);
		//attachChild(ship.sprite);
		
		resetValues();
		activity.mCamera.setHUD(hud);		
		
	}


	// method to reset values and restart the game
	public void resetValues() {
		isPlayDanger = false;
		bulletCount = 0;
		maximumX = 0.0f;
		nextBox = 40;
        hitCnt = 0;
        hitRateChar = 0;
        hitRateEnemies = 0;

		character.restart();
		EnemyLayer.purgeAndRestart();
		TileLayer.purgeAndRestart();
		
		clearChildScene();
		registerUpdateHandler(new GameLoopUpdateHandler());

	}

	
	public void moveCharacter(float pSecondsElapsed) {
		if(character.isOnFire)
			character.shoot();
		
		accelerometerSpeedX = SensorListener.getSharedInstance().getAccelerometerSpeedX();
		
		maximumX = Math.max(maximumX, character.getX() + character.getWidth() / 2);
		//Log.d("adfasd", "" + accelerometerSpeedX);
		if(accelerometerSpeedX > 0 || maximumX - mCamera.getWidth() / 2 < character.getX())
			character.moveCharacter(accelerometerSpeedX);
		background.setParallaxChangePerSecond(accelerometerSpeedX / 14);
			
		mCamera.setCenter(maximumX + 16, BaseActivity.CAMERA_HEIGHT / 2 + HUDLayer.HUD_HEIGHT);
		synchronized (this) {
			TileLayer layer = TileLayer.getSharedInstance();	
			layer.processLine((int)maximumX);
			Iterator<com.tistory.pflower.frontline.object.Enemy> ite = EnemyLayer.getIterator();
			while(ite.hasNext())
			{
				Enemy e = ite.next();
				if(!e.isDead)
					e.moveCharacter(character, pSecondsElapsed);
			}	
			if(Chopper.getSharedInstance().hasParent())
				Chopper.getSharedInstance().moveCharacter(character, pSecondsElapsed);
		}
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		synchronized (this) {
			
			if(character.isDead)
				return true;
			if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP)
			{
				character.isOnFire = false;
				character.arms.SetAim();
				return true;
			}
			else
			{
				character.isOnFire = true;
				character.setAim(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
			}
			character.shoot();

		}
		return true;
	}
	

	public void detach() {
		synchronized (this) {
			Log.v("Jimvaders", "GameScene onDetached()");
			clearUpdateHandlers();
			for (Bullet b : Bullet.bulletSet) {
				BulletPool.sharedBulletPool().recyclePoolItem(b);
			}
			Bullet.bulletSet.clear();

			detachChildren();
			
			Soldier.instance = null;
			
		}

	}

	public void update(float pSecondsElapsed) {
		synchronized (this) {

            if(--hitRateEnemies <= 0) {
                accDam = 0;
            }
            if(--hitRateChar <= 0) {
                hitCnt = 0;
            }

            if(accDam > 30)
                hud.showHeavyDam();


            for (Bullet b : Bullet.bulletSet) {
                b.update(pSecondsElapsed);
            }

			for (Bullet b : Bullet.bulletTrash) {
				BulletPool.sharedBulletPool().recyclePoolItem(b);
				Bullet.bulletSet.remove(b);
			}
			Bullet.bulletTrash.clear();

            Iterator<Projectile> it = Projectile.list.iterator();

            for (Projectile p: Projectile.list) {
                if(p.isExpired) {
                    p.die();
                }
                p.update(pSecondsElapsed);
            }

            for (Projectile p : Projectile.trash) {
                ProjectilePool.sharedProjPool().recyclePoolItem(p);
                Projectile.list.remove(p);
            }
            Projectile.trash.clear();
			
			// ���� �����
			
			boxCheck();
			
			// ĳ���� ü�� ����
			
			if(character.isEndanger() && !isPlayDanger)
			{
				isPlayDanger = true;
				hud.setDangerGlowing(true);
			}
			else if(!character.isEndanger())
			{
				isPlayDanger = false;
				hud.setDangerGlowing(false);
			}
			final GameScene game = this;
			// ĳ���Ͱ� �����
			if (character.isDead) {
				
				registerEntityModifier(new DelayModifier(5.0f)
				{
					@Override
					protected void onModifierFinished(IEntity pItem) 
					{
						clearUpdateHandlers();
						detach();
						BaseActivity.getSharedInstance().mCamera.setHUD(null);
						hud.setDangerGlowing(false);
						BaseActivity.getSharedInstance().setCurrentScene(new ResultScene(mCamera, game));
					};
					
				});
			}
			// �� ���

			Iterator<Enemy> eIt2 = EnemyLayer.getIterator();
			while (eIt2.hasNext()) {
				Enemy e = eIt2.next();				
				
				//if(e.torso.isDeadFinished && (character.getX() - BaseActivity.CAMERA_WIDTH / 2 > e.getX()))
				if(e.isDead) {
                    //EnemyLayer.remains--;
                    eIt2.remove();
                }
					//EnemyPool.sharedEnemyPool().recyclePoolItem(e);

			}
            //Iterator<Bullet> but = BulletPool.sharedBulletPool().geti
			
		}
	}

	private void boxCheck() {
		if(maximumX > nextBox * 32)
		{
			 Medibox box = Medibox.getSharedInstance();	     
             final float xPos = nextBox * 32 - box.getWidth() / 2;
             final float yPos = 0;


             TileLine _l = TileLayer.getSharedInstance().lines.get( nextBox );

             if(_l == null)
                box.targetY = BaseActivity.CAMERA_HEIGHT;
             else
                box.targetY = BaseActivity.CAMERA_HEIGHT - _l.height * 14;

             Log.v("box", "" + (int)(xPos) + "a" + box.targetY);

             box.setPosition(xPos, yPos);
             if(box.hasParent())
            	 box.detachSelf();
             attachChild(box);
             box.isAppear = true;
             box.setVisible(true);
			nextBox += 100;
		}
	}


    public void doHit(boolean isEnemy, int damage) {
        if(!isEnemy) {
            hitRateEnemies = BaseActivity.FPS_LIMITS;
            accDam += damage;
        }
        else {
            hitRateChar = BaseActivity.FPS_LIMITS;
            hitCnt++;
        }
    }

    public void doHeadshot()
    {
        hud.showHeadshot();
    }
}
