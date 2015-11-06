package com.tistory.pflower.frontline.object;

import org.andengine.util.math.MathUtils;

import com.tistory.pflower.frontline.EnemyLayer;
import com.tistory.pflower.frontline.pools.EnemyPool;

import android.util.Log;

import com.tistory.pflower.frontline.BaseActivity;
import com.tistory.pflower.frontline.CoolDown;
import com.tistory.pflower.frontline.scene.GameScene;

public class Enemy extends Soldier {

	enum SimpleAction
	{
		Shoot,
		Wait,
		Move,
	}
	
	CoolDown cooldown;
	private float timeRemain;
	private SimpleAction action;
	
	public Enemy()
	{
		super();
		cooldown = new CoolDown();
		arms.changeWeapon(MathUtils.random(0, 3));
		timeRemain = 0f;
		action = SimpleAction.Move;
		
		setCullingEnabled(true);
	}
	public void init() {
		MAX_HEALTH = 30;
		torso.isDeadFinished = false;
		isDead = false;
		hp = MAX_HEALTH;
		for (Weapon w : myWeapons) {
			w.ammo = w.MAX_AMMO / 3;
			w.magagine = 10;
		}
		arms.changeWeapon(MathUtils.random(0, 3));
		
		legs.setColor(1.0f, 1.0f, 0.0f);
		torso.setColor(1.0f, 1.0f, 0.0f);
		arms.setColor(1.0f, 1.0f, 0.0f);
		
		action = SimpleAction.Move;
	}

	public void clean() {
		super.clean();
	}
	
	@Override
	protected void onManagedUpdate(float pSecondsElapsed) 
	{
		super.onManagedUpdate(pSecondsElapsed);
		
		//if(isDead && torso.isDeadFinished)
		if(torso.isDeadFinished && (GameScene.maximumX - BaseActivity.CAMERA_WIDTH / 2 - getWidth() > getX()))
		{
			Log.d("del e", "ee");
			final Enemy e = this;		
			BaseActivity.getSharedInstance().getEngine().runOnUpdateThread(new Runnable() {
		        @Override
		        public void run() {
                    EnemyLayer.remains--;
		        	EnemyPool.sharedEnemyPool().recyclePoolItem(e);
		        }
		    });
		}
	};
	
	public void moveCharacter(Soldier character, float pSecondsElapsed) {
		
		timeRemain -= pSecondsElapsed;
		if(timeRemain < 0.0f)
		{
			
			switch(MathUtils.random(0, 2))
			{
			// 3�ʰ� �߻�
			case 0:
				timeRemain = 3.0f;
				action = SimpleAction.Shoot;
				break;
			// 1�ʰ� ���
			case 1:
				timeRemain = 1.0f;
				action = SimpleAction.Wait;
				break;
			// 2�ʰ� �̵�
			case 2:
				timeRemain = 2.0f;
				action = SimpleAction.Move;
				break;
			}
		}
		
		if(getX() < character.getX() - BaseActivity.CAMERA_WIDTH / 2)
		{
			setX(character.getX() - BaseActivity.CAMERA_WIDTH / 2);
		}
		setAim(character.getX() + character.getWidth() / 2, character.getY() + character.getHeight() / 2);
		
		switch (action) {
		case Shoot:
			moveCharacter(0);
			
			if(!torso.isOnReload)
			{
				if(myWeapons[arms.currentWeapon].ammo == 0)
					reload();
				else if(cooldown.checkValidity(arms.getDelay() * 2))
					arms.shoot();
			}
			
			break;
		case Wait:
			moveCharacter(0);
			break;
		case Move:
			if(Math.abs(character.getX() - this.getX()) > 50)
				if(character.getX() > this.getX())
					moveCharacter(SPEED);
				else
					moveCharacter(-SPEED);
			break;
		}

	}

}
