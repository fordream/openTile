package com.tistory.pflower.frontline;

import android.util.Log;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.IEntityFactory;
import org.andengine.entity.particle.ParticleSystem;
import org.andengine.entity.particle.emitter.PointParticleEmitter;
import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.entity.particle.modifier.AlphaParticleModifier;
import org.andengine.entity.particle.modifier.RotationParticleModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.AnimatedSprite.IAnimationListener;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.color.Color;

import com.tistory.pflower.frontline.pools.BulletPool;

import com.tistory.pflower.frontline.Tile.Tile;
import com.tistory.pflower.frontline.Tile.TileLayer;
import com.tistory.pflower.frontline.Tile.TileLine;
import com.tistory.pflower.frontline.object.Enemy;
import com.tistory.pflower.frontline.object.Soldier;
import com.tistory.pflower.frontline.scene.GameScene;

public class Bullet extends Rectangle{

    public enum Direction
	{
		RIGHT,
		LEFT,
		UP, NONE
	}
	
	public boolean isExpired;
	public boolean isEnemy;
	public boolean isPierceShot;
	
	public int Damage;
	
	private int px;
	private int py;
	private boolean isDead;
	

	public static HashSet<Bullet> bulletSet = new HashSet<Bullet>();
	public static HashSet<Bullet> bulletTrash = new HashSet<Bullet>();
	
	private AnimatedSprite debris;
	private Direction hitDir; 
	private int currentFrames;
	private boolean isDeadFinished;
	
	public Bullet() {
		super(0, 0, 4, 4, BaseActivity.getSharedInstance()
				.getVertexBufferObjectManager());
		isExpired = false;
		isEnemy = false;
		isPierceShot = false;
		setDead(false);
		
		Damage = 1;
		setColor(0.59904f, 0.5f, 0.1786f);
		
		setCullingEnabled(true);
		
		isDeadFinished = false;
		debris = new AnimatedSprite(0, 0, ResourceManager.getSharedInstance().getTiledResourceByName("debris"), BaseActivity.getSharedInstance().getVertexBufferObjectManager() );
		debris.setVisible(false);
		
		attachChild(debris);
	}

    public void update(float pSecondsElapsed) {
        if (isExpired) {
            die(Direction.NONE);
            bulletTrash.add(this);
            return;
        }

    }

	public void setIsEnemy(boolean isEnemy) {
		this.isEnemy = isEnemy;
	}

	public void setIsPierce(boolean b) {
		this.isPierceShot = b;
	}
	
	public void setDamage(int b) {
		this.Damage = b;
	}
	
	public void die(Bullet.Direction dir)
	{
		isDeadFinished = false;
		this.currentFrames = 0;
		this.hitDir = dir;
		debris.setVisible(true);
		setDead(true);
		setColor(1f, 0.5f, 0.5f, 0f);

	}
	public void setMoment(int x, int y)
	{
		this.px = x;
		this.py = y;
	}

	
	public float getMomentX() {
		return px;
	}
	public float getMomentY() {
		return py;
	}
	public boolean isDead() {
		return isDead;
	}
	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}

}
