package com.tistory.pflower.frontline.object;

import java.io.IOException;
import java.util.Random;

import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.tistory.pflower.frontline.BaseActivity;

import com.tistory.pflower.frontline.pools.EnemyPool;
import android.util.Log;


public class Torso extends AnimatedSprite {
	
	final int ANIMATE_DURATION = 100;

	Sound reloadS;
	Sound reloadE;
	
	private int currentTorso = 0;
	public boolean isOnReload;
	
	private long[] reloadFrameDurration = {100, 100, 100, 100, 100, 100};
	private long[] shieldFrameDurration = {50, 50, 50, 50, 50, 50};
	private int[] reloadframes = {0, 5};

	IAnimationListener reloadListener = new IAnimationListener()
	{

		@Override
		public void onAnimationFinished(AnimatedSprite arg0) {
			// TODO Auto-generated method stub
			isOnReload = false;
			reloadS.play();
			parent.arms.setVisible(true);
			parent.arms.setShootAble(true);
			setDefault();
		}

		@Override
		public void onAnimationFrameChanged(AnimatedSprite arg0, int arg1,
				int arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onAnimationLoopFinished(AnimatedSprite arg0, int arg1,
				int arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onAnimationStarted(AnimatedSprite arg0, int arg1) {
			// TODO Auto-generated method stub
			parent.arms.setVisible(false);
		}
	};
	IAnimationListener shieldListener = new IAnimationListener()
	{

		@Override
		public void onAnimationFinished(AnimatedSprite arg0) {
			setShieldOn(true);
			parent.legs.setVisible(false);
			parent.moveable = false;
		}

		@Override
		public void onAnimationFrameChanged(AnimatedSprite arg0, int arg1,
				int arg2) {
			if(arg2 == reloadFrameDurration.length - 1)
				parent.legs.setVisible(false);
		}

		@Override
		public void onAnimationLoopFinished(AnimatedSprite arg0, int arg1,
				int arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onAnimationStarted(AnimatedSprite arg0, int arg1) {
			// TODO Auto-generated method stub
		}
		
	};
	
	private long[] deathFrameDurration = {100, 100, 100, 100, 100, 100, 100, 100, 100};
	private int[] deathFrames = {0, 8};
	
	IAnimationListener deathListener = new IAnimationListener()
	{
		@Override
		public void onAnimationFinished(AnimatedSprite arg0) {
			// TODO Auto-generated method stub
			isDeadFinished = true;

		}

		@Override
		public void onAnimationFrameChanged(AnimatedSprite arg0, int arg1,
				int arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onAnimationLoopFinished(AnimatedSprite arg0, int arg1,
				int arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onAnimationStarted(AnimatedSprite arg0, int arg1) {
			parent.legs.setVisible(false);
			parent.moveable = false;
		}
	};
	
	
	long[] frameDurration = {100, 100};
    int[] frames = {0, 1};

	private boolean isShieldOn;
	private Soldier parent;
	public boolean isDeadFinished;
	
	public Torso(int i, int j, TiledTextureRegion _Torso,
			VertexBufferObjectManager vertexBufferObjectManager, Soldier character) {
		super(i, j, _Torso, vertexBufferObjectManager);
		
		currentTorso = 0;
		isDeadFinished = false;
		isOnReload = false;
		setShieldOn(false);
		parent = character;
		
		// �ݵ�� �ý��� ���̺귯�� �ε� �Ŀ� ȣ��
		Log.d("Torso", "reload sound");
		SoundFactory.setAssetBasePath("sound/weapons/");
		BaseActivity activity = BaseActivity.getSharedInstance();

		try {
			reloadE = SoundFactory.createSoundFromAsset(activity.getSoundManager(), activity, "ReloadE"+ ".wav");
			reloadS = SoundFactory.createSoundFromAsset(activity.getSoundManager(), activity, "ReloadS"+ ".wav");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		reloadE.setLooping(false);
		reloadS.setLooping(false);
		
		animate(frameDurration, frames[0], frames[1], false);
		stopAnimation();
		setScaleCenterX(getWidth() / 2);
		
		
		setCullingEnabled(true);
	}
	public void setDefault()
	{
		if(currentTorso == 1)
			setPosition(getX() - 5, getY());
		currentTorso = 0;
		parent.legs.setVisible(true);
		parent.moveable = true;
		isDeadFinished = false;
		animate(frameDurration, frames[0], frames[1], false);
		stopAnimation(0);
	}
	public void shieldUp()
	{
		currentTorso = 1;
		setPosition(getX() + 5, getY());
		animate(shieldFrameDurration, reloadframes[0] + 9, reloadframes[1] + 9, false, shieldListener);
	}
	
	public void reload(int weapon_id)
	{
		isOnReload = true;
		currentTorso = 2 + weapon_id;
		reloadE.play();
		animate(reloadFrameDurration, reloadframes[0] + 9 * currentTorso, reloadframes[1] + 9 * currentTorso, false, reloadListener);
	}
	
	public void die()
	{
		Random rnd = new Random();
		currentTorso = 6 + rnd.nextInt(3);
		animate(deathFrameDurration, deathFrames[0] + 9 * currentTorso, deathFrames[1] + 9 * currentTorso, false, deathListener);
	}
	
	public boolean isShieldOn() {
		return isShieldOn;
	}
	public void setShieldOn(boolean isShieldOn) {
		this.isShieldOn = isShieldOn;
	}
	
}
