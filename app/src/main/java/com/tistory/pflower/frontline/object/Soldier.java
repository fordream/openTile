package com.tistory.pflower.frontline.object;


import java.util.Iterator;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.shape.IShape;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.util.color.Color;

import com.tistory.pflower.frontline.BaseActivity;
import com.tistory.pflower.frontline.CoolDown;
import com.tistory.pflower.frontline.ResourceManager;
import com.tistory.pflower.frontline.Tile.Tile;
import com.tistory.pflower.frontline.Tile.TileLayer;
import com.tistory.pflower.frontline.Tile.TileLine;
import com.tistory.pflower.frontline.hud.HUDLayer;
import com.tistory.pflower.frontline.object.Leg.Direction;
import com.tistory.pflower.frontline.scene.GameScene;

import android.util.Log;

public class Soldier extends Rectangle{

	protected int MAX_HEALTH = 2000;
	public Weapon[] myWeapons;
	public Torso torso;
	public Leg legs;
	public Arm arms;
    public Rectangle hitBox;
	
	protected final float SPEED = 60f / BaseActivity.FPS_LIMITS;

	
	public static Soldier instance;
    public static Sprite reload = null;

	Camera mCamera;
	boolean moveable;
	public boolean isOnFire;
	protected int hp;
	private boolean isFacingRight;
	private HUDLayer hud;
	public int money;
	public int killCnt;
	public boolean isDead;
	
	private Rectangle hpBar;
	protected Rectangle remainBar;
	public Rectangle head;

    private static boolean isReloadGlow;
    private static int glowingCnt;


    public static Soldier getSharedInstance() {
		if (instance == null) {
            instance = new Soldier();
            reload = new Sprite(0, 0, ResourceManager.getSharedInstance().getResourceByName("reload"), BaseActivity.getSharedInstance().getVertexBufferObjectManager());
            reload.setPosition(instance.getWidth() / 2 - reload.getWidth() / 2 + 8f, -20f);
            reload.setVisible(false);
            reload.setIgnoreUpdate(true);
            Soldier.isReloadGlow = false;
            instance.attachChild(reload);
        }
        return instance;
	}

	public static void clear()
	{
		
	}

	public Soldier() {
		super(0, 0, 30, 30, BaseActivity.getSharedInstance().getVertexBufferObjectManager());
		setColor(0.f, 0.f, 0.f, 0.f);
		hp = MAX_HEALTH;

		
		setFacingRight(true);
		hud = null;
		myWeapons = Weapon.obtainNewWeaponSet();

        hitBox = new Rectangle(32, 0, 16, 30, BaseActivity.getSharedInstance().getVertexBufferObjectManager());
        //hitBox.setScaleCenterX(15);

		// ������ ������
		torso = new Torso(0 + 16, 0, ResourceManager.getSharedInstance().getTiledResourceByName("Torso"), BaseActivity.getSharedInstance()
				.getVertexBufferObjectManager(), this);
		torso.setCullingEnabled(true);
		// ����� �� ��ü�� �߽� 23
		setScaleCenterX(23 + 23);



		// �Ӹ� Ÿ����
		head = new Rectangle(19 + 16, 1, 11, 11,  BaseActivity.getSharedInstance()
				.getVertexBufferObjectManager());
        head.setCullingEnabled(true);

		// �ٸ� ������ �� (23, 20) �ٸ� (14, 7)
		legs = new Leg(23 - 14 + 16, 20 - 7, ResourceManager.getSharedInstance().getTiledResourceByName("Legs"), BaseActivity.getSharedInstance()
				.getVertexBufferObjectManager(), this);
        legs.setCullingEnabled(true);
		// �ٸ� �߽� 14
		legs.setScaleCenterX(14 + 16);
		
        
		// �� ������ ��(23, 12) ��(10, 12)
		arms = new Arm(23 - 10 + 16, 12 - 12, ResourceManager.getSharedInstance().getTiledResourceByName("Arms"), BaseActivity.getSharedInstance()
				.getVertexBufferObjectManager(), myWeapons);
        arms.setCullingEnabled(true);
		// �� �� �߾� (15, 22)
		arms.setScaleCenter(15 + 16, 12);


		
		mCamera = BaseActivity.getSharedInstance().mCamera;


        attachChild(hitBox);

		attachChild(legs);
		attachChild(torso);
		attachChild(arms);
		
		setPosition(mCamera.getWidth() / 2 - getWidth() / 2,	mCamera.getHeight() - getHeight());

		
		hpBar = new Rectangle(0 + 16, 0, 25, 3, BaseActivity.getSharedInstance().getVertexBufferObjectManager());
		hpBar.setPosition(23 - hpBar.getWidth() / 2 + 16, 0);
		hpBar.setColor(Color.RED);
		attachChild(hpBar);
		
		remainBar = new Rectangle(0 + 16, 0, 25, 3, BaseActivity.getSharedInstance().getVertexBufferObjectManager());
		remainBar.setPosition(23 - hpBar.getWidth() / 2 + 16, 0);
		remainBar.setColor(Color.GREEN);
		remainBar.setRotationCenterX(16);
		attachChild(remainBar);
		
		moveable = true;
		isOnFire = false;
		
		//legs.setDesending(Direction.PLAIN);
		// instance = this;
		
		setCullingEnabled(true);
	}

	public void moveCharacter(float accelerometerSpeedX) {
		if (!moveable)
			return;
		// Log.v("Jimvaders",
		// "Ship moveShip() accelerometerSpeedX = "+accellerometerSpeedX);
		if (accelerometerSpeedX != 0) {
			
		
			if(!legs.isAnimationRunning())
				legs.moveFast();	
			if(Math.abs(accelerometerSpeedX) < SPEED)
			{
				legs.stopAnimation();
				return;
			}
			if( Math.abs(accelerometerSpeedX) >= SPEED ) accelerometerSpeedX = accelerometerSpeedX / Math.abs(accelerometerSpeedX) * SPEED;
			
			int lL = 0;
			int rL = 65536;//(int) (mCamera.getWidth() - (int) sprite.getWidth());

			float newX;

			// Calculate New X,Y Coordinates within Limits
			if (getX() >= lL)
				newX = getX() + accelerometerSpeedX;
			else
				newX = lL;
			if (newX <= rL)
				newX = getX() + accelerometerSpeedX;
			else
				newX = rL;

			// Double Check That New X,Y Coordinates are within Limits
			if (newX < lL)
				newX = lL;
			else if (newX > rL)
				newX = rL;

			setPosition(newX, getY());
		}
		else
		{
			legs.stopAnimation();
		}
		
		
	}

	// shoots bullets
	public void shoot() {
		if (myWeapons[arms.currentWeapon].roundType != Weapon.RoundType.GRANADE && (!CoolDown.getSharedInstance().checkValidity(arms.getDelay()) || !isOnFire) )
			return ;
		Log.d("Arm", "fire");
		arms.shoot();
	}
	
	@Override
	protected void onManagedUpdate(float pSecondsElapsed) 
	{
		super.onManagedUpdate(pSecondsElapsed);

        if(this == instance)
        {
            if(!Soldier.isReloadGlow && myWeapons[arms.currentWeapon].ammo < 1)
            {
                reload.setVisible(true);
                Soldier.isReloadGlow = true;
            }

            if(Soldier.isReloadGlow && myWeapons[arms.currentWeapon].ammo >= 1)
            {
                reload.setVisible(false);
                Soldier.isReloadGlow = false;
                glowingCnt = 0;
            }

            if(Soldier.isReloadGlow)
            {
                if(glowingCnt > BaseActivity.FPS_LIMITS / 2) {
                    glowingCnt = 0;
                    reload.setVisible(!reload.isVisible());
                }
                ++glowingCnt;
            }
        }
		float _w = ((float)hp / MAX_HEALTH) * 25;
		if(_w < 0)
			_w = 0f;
		remainBar.setWidth( _w );
		
		
		final int legWidth = 16;
		if(((int)getX()) % 32 < 32 - legWidth)
		{
			TileLine _l = TileLayer.getSharedInstance().lines.get( (int)((getX() + getWidth() / 2 + 32) / 32) );
			if(_l != null)
			{
				legs.setDesending(Leg.Direction.PLAIN);
				setY(BaseActivity.CAMERA_HEIGHT - _l.height * 14 - getHeight());
			}
			else
			{
				legs.setDesending(Leg.Direction.PLAIN);
				setY(BaseActivity.CAMERA_HEIGHT - getHeight());
			}
		}
		else
		{
			TileLine _l = TileLayer.getSharedInstance().lines.get( (int)((getX() - getWidth() / 2 + 32) / 32) );
			TileLine _l2 = TileLayer.getSharedInstance().lines.get( (int)((getX() + getWidth() + 32) / 32) );
			
			if(_l != null && _l2 != null)
			{
				if(_l.location * 32 > getX())
				{
					
					float ratio = Math.abs( ((int)(getX() + 17) / 32) * 32 - getX() ) / legs.getWidth();
					setY(BaseActivity.CAMERA_HEIGHT - _l.height * 14 * (ratio) - _l2.height * 14 * (1-ratio) - getHeight());
					
					//Log.d("Height", "l.height : " + l.height + " l2.height : " + l2.height);
					if(_l.height > _l2.height)
					{
						legs.setDesending(Leg.Direction.DESENDING_RIGHT);
						
						if(isFacingRight())
							legs.setCurrentTileIndex((int) (9 + 9 * ratio));
						else
							legs.setCurrentTileIndex((int) (18 + 9 * ratio));
					}
					else if(_l.height < _l2.height)
					{
						legs.setDesending(Leg.Direction.DESENDING_LEFT);
						
						if(isFacingRight())
							legs.setCurrentTileIndex((int) (18 + 9 * (1 - ratio) ));
						else
							legs.setCurrentTileIndex((int) (9 + 9 * (1 - ratio) ));
					}
					else
					{
						legs.setDesending(Leg.Direction.PLAIN);
					}
				}
			}
			else
			{
				legs.setDesending(Leg.Direction.PLAIN);
			}
		}
		
	}
	public void restart() {
		isDead = false;
		moveable = true;
		isOnFire = false;
		isFacingRight = true;
		
		
		hp = MAX_HEALTH;
		
		Camera mCamera = BaseActivity.getSharedInstance().mCamera;
		MoveXModifier mod = new MoveXModifier(0.1f, getX(),
				mCamera.getWidth() / 2 - getWidth() / 2) {
			@Override
			protected void onModifierFinished(IEntity pItem) {
				super.onModifierFinished(pItem);
				moveable = true;
			}
		};
		registerEntityModifier(mod);

	}

	public boolean gotHit(int Damage) {

		ResourceManager.getSharedInstance().getSoundByName("Hit4").play();
		synchronized (this) {
			hp -= Damage;
            if(!(this instanceof Enemy))
                ((GameScene) BaseActivity.getSharedInstance().mCurrentScene).doHit(false, Damage);
            if (hp <= 0) {
                if(this instanceof Enemy)
                    ((GameScene) BaseActivity.getSharedInstance().mCurrentScene).doHit(true, Damage);
                return false;
            }
            else
				return true;
		}
}

	public void setFlippedHorizontal(boolean b) {
		float scale = 1.0f;
		if(b) scale *= -1; 
		torso.setScaleX(scale);
		legs.setScaleX(scale);
		arms.setScaleX(scale);
	}

	public void bind(HUDLayer hud) {
		this.hud = hud;
		
	}

	public void reload() {
		if(myWeapons[arms.currentWeapon].IsInfinity || myWeapons[arms.currentWeapon].magagine > 0 )
		{
			arms.setVisible(false);
			arms.setShootAble(false);
			arms.reload();
			torso.reload(arms.currentWeapon);
		}
	}

	public void buyItem(int i) {
		synchronized(this)
		{
			if(money >= myWeapons[i].price ) // �� �� ����
			{
				money -= myWeapons[i].price;
				myWeapons[i].magagine++;
			}
		}
	}
	
	public void die()
	{
		isDead = true;
		moveable = false;
		arms.setVisible(false);
		arms.setShootAble(false);
		legs.setVisible(false);
		torso.die();
	}

	public void clean() {
		moveable = true;
		arms.setVisible(true);
		arms.setShootAble(true);
		legs.setVisible(true);
		torso.setDefault();
	}

	public boolean isFacingRight() {
		return isFacingRight;
	}

	public void setFacingRight(boolean isFacingRight) {
		this.isFacingRight = isFacingRight;
	}
	
	public void setAim(float x, float y) {
		
		arms.setAim(x, y);
		
		double rad = arms.getAim();
		
		if( rad > 0.5 * Math.PI || rad < -0.5 * Math.PI) // ������ ������
		{
			setFacingRight(false);
			setScaleX(-1.0f);
		
			arms.setRotation( -(float)Math.toDegrees(rad) + 180.0f);
			
		}
		else if( rad < 0.5 * Math.PI && rad > -0.5 * Math.PI) // �������� ������
		{
			setFacingRight(true);
			setScaleX(1.0f);
			
			arms.setRotation((float)Math.toDegrees(rad));
		}
	}

	public void setHealth(int i) {
		this.hp = i;
	}

	public boolean isEndanger() {
		if(hp <= 50)
			return true;
		return false;
	}
}
