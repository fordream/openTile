package com.tistory.pflower.frontline.object;


import java.util.Iterator;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.primitive.vbo.IRectangleVertexBufferObject;
import org.andengine.entity.shape.IShape;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.util.algorithm.path.Direction;
import org.andengine.util.color.Color;

import com.tistory.pflower.frontline.BaseActivity;
import com.tistory.pflower.frontline.Bullet;
import com.tistory.pflower.frontline.CoolDown;
import com.tistory.pflower.frontline.ResourceManager;
import com.tistory.pflower.frontline.Tile.Tile;
import com.tistory.pflower.frontline.Tile.TileLayer;
import com.tistory.pflower.frontline.Tile.TileLine;
import com.tistory.pflower.frontline.hud.HUDLayer;
import android.util.Log;

public class Chopper extends Rectangle{


	static Chopper instance = null;
	
	protected final int shotCooldown = 10;
	int currentFrame = 0;
	
	public TiledSprite cockpit;
	public AnimatedSprite tail;
	
	public Sprite body;
	public Sprite rotor;
	
	public Arm arm;

	public Weapon[] weaponSet;
	
	private boolean isInBounding = false;
	private boolean isInBound = false;
	private CoolDown cooldown;
	private boolean isFacingRight;
	
	public static Chopper getSharedInstance()
	{
		if(instance == null)
			instance = new Chopper();
		return instance;
	}
	
	private Chopper() {
		super(0, 100, 30, 30, BaseActivity.getSharedInstance().getVertexBufferObjectManager());
		setColor(0.f, 0.f, 0.f, 0.f);
		
		weaponSet = Weapon.obtainNewWeaponSet();
		
		cockpit = new TiledSprite(19, 83, ResourceManager.getSharedInstance().getTiledResourceByName("cockpit"), BaseActivity.getSharedInstance().getVertexBufferObjectManager());
		cockpit.setVisible(false);
		cockpit.setRotationCenterX(cockpit.getWidth() / 2);
		
		tail = new AnimatedSprite(185, 13, ResourceManager.getSharedInstance().getTiledResourceByName("tail"), BaseActivity.getSharedInstance().getVertexBufferObjectManager());
		tail.setRotationCenterX(tail.getWidth() / 2);
		tail.animate(10);
		
		body = new Sprite(0, 0, ResourceManager.getSharedInstance().getResourceByName("chopperBody"), BaseActivity.getSharedInstance().getVertexBufferObjectManager());
		body.setRotationCenterX(body.getWidth() / 2);
		this.setHeight(body.getHeight());
		this.setWidth(body.getWidth());
		
		rotor = new Sprite(-21, 21, ResourceManager.getSharedInstance().getResourceByName("rotor"), BaseActivity.getSharedInstance().getVertexBufferObjectManager());
        rotor.setRotationCenterX(body.getWidth() / 2);
		
		arm = new Arm(6, 136, ResourceManager.getSharedInstance().getTiledResourceByName("Arms"), BaseActivity.getSharedInstance()
				.getVertexBufferObjectManager(), weaponSet);
		arm.setRotationCenterX(arm.getWidth() / 2);
		arm.setVisible(false);
		arm.setShootAble(true);
		
		cooldown = new CoolDown();
		
		arm.currentWeapon = 5;
		
		attachChild(cockpit);
		attachChild(rotor);
		attachChild(tail);
		attachChild(body);
		
		attachChild(arm);
		
		setCullingEnabled(true);
	}
	
	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		// TODO Auto-generated method stub
		super.onManagedUpdate(pSecondsElapsed);
		rotor.setVisible(!rotor.isVisible());
	}

	// shoots bullets
	public void shoot() {
		if (currentFrame < shotCooldown)
		{
			++currentFrame;
			return ;
		}
		currentFrame = 0;
		
		Log.d("Heri", "fire");
		
		arm.shoot();
	}
	
	public void moveCharacter(Soldier character, float pSecondsElapsed) {

		this.arm.setAim(character.getX() + character.getWidth() / 2, character.getY() + character.getHeight() / 2);
		
		//Log.d("Heri", "" + isInBounding + " " + isInBound);
		
		if(!isInBounding && isInBound)
		{

			if(weaponSet[arm.currentWeapon].ammo == 0)
				leave();
			else if(cooldown.checkValidity(arm.getDelay()))
			{
				Log.d("Heri", "" + arm.getDelay());
				arm.shoot();
			}
		}
	}
	
	public void restart() {

	}

	public void die()
	{
		
	}

	public void clean() {
	}


	public void inbound(float xPos, float yPos, boolean backEnemy) {
		// TODO Auto-generated method stub
		ResourceManager.getSharedInstance().getSoundByName("Heri").setLooping(true);
		ResourceManager.getSharedInstance().getSoundByName("Heri").play();
		this.arm.reload();
		isFacingRight = backEnemy;
		
		isInBounding = true;
		MoveModifier mod1 = new MoveModifier(2f, xPos, xPos + (backEnemy ? 100 : -100) ,	yPos, yPos)
		{
			@Override
			protected void onModifierFinished(IEntity pItem) {
				super.onModifierFinished(pItem);
				((Chopper)pItem).isInBounding = false;
				((Chopper)pItem).isInBound = true;
			}
		};
		registerEntityModifier(mod1);
		
	}

	private void leave() {
		ResourceManager.getSharedInstance().getSoundByName("Heri").stop();
		MoveModifier mod1 = new MoveModifier(2f, getX(), getX() + (isFacingRight ? -200 :  200),	getY(), getY())
		{
			@Override
			protected void onModifierFinished(IEntity pItem) {
				super.onModifierFinished(pItem);
				((Chopper)pItem).isInBound = false;
				setVisible(false);
				//detachSelf();
			}
		};
		registerEntityModifier(mod1);
		
	}

}
