package com.tistory.pflower.frontline.object;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.math.MathUtils;

import com.tistory.pflower.frontline.EnemyLayer;
import com.tistory.pflower.frontline.ResourceManager;
import com.tistory.pflower.frontline.pools.BulletPool;
import com.tistory.pflower.frontline.pools.ProjectilePool;

import com.tistory.pflower.frontline.BaseActivity;
import com.tistory.pflower.frontline.Bullet;
import com.tistory.pflower.frontline.Projectile;
import com.tistory.pflower.frontline.object.Weapon;
import com.tistory.pflower.frontline.scene.GameScene;


public class Arm extends AnimatedSprite {
	
	
	
	private static final int SHOTGUN_MULTYSHOT_AMOUNT = 10;

	final int ANIMATE_DURATION = 100;

	public int currentWeapon = 0;
	private boolean shootAble = true;

	private double rad;

	private Weapon[] myWeapons;
	
	

	
	public Arm(int i, int j, TiledTextureRegion _Arms,
			VertexBufferObjectManager vertexBufferObjectManager, Weapon[] myWeapons) {
		super(i, j, _Arms, vertexBufferObjectManager);
		this.myWeapons = myWeapons;
		
		animate(myWeapons[currentWeapon].frameDurration, myWeapons[currentWeapon].frames[0], myWeapons[currentWeapon].frames[1], false);
		this.stopAnimation(myWeapons[currentWeapon].frames[0]);
		
		setRotationCenter(10, 10);
		
		rad = 0.0;
		
		setScaleCenterX(getWidth() / 2);
		
		setCullingEnabled(true);
	}
	
	public void changeWeapon(int i)
	{
		if((myWeapons[i].roundType != Weapon.RoundType.GRANADE || myWeapons[currentWeapon].roundType != Weapon.RoundType.GRANADE) && !shootAble) return;
		currentWeapon  = i;
		animate(myWeapons[currentWeapon].frameDurration, myWeapons[currentWeapon].frames[0], myWeapons[currentWeapon].frames[1], false);
		this.stopAnimation(myWeapons[currentWeapon].frames[0]);
	}

	
	public void reload()
	{
		//if(!shootAble) return;
		if(myWeapons[currentWeapon].IsInfinity)
		{
			myWeapons[currentWeapon].ammo = myWeapons[currentWeapon].MAX_AMMO;
			return;
		}
		if(myWeapons[currentWeapon].magagine < 1)
			return;
		--myWeapons[currentWeapon].magagine;
		myWeapons[currentWeapon].ammo = myWeapons[currentWeapon].MAX_AMMO;
		
	}
	
	@Override
	public void setVisible(boolean pVisible)
	{
		super.setVisible(pVisible);
	}
	
	public void setShootAble(boolean pVisible)
	{
		shootAble = pVisible;
	}


	
	public void setAim(float dx, float dy)
	{
		float beginX = getParent().getX() + getX() + getWidth() / 2;
		float beginY = getParent().getY() + getY();
		float deltaX = dx - beginX;
		float deltaY = dy - beginY;
		
		rad = Math.atan2(deltaY, deltaX);
		setAim(rad);	
	}
	
	public void setAim(double rad)
	{
		this.rad = rad;
	}
	
	public double getAim()
	{
		return this.rad;
	}

	public void shoot() {
		if(myWeapons[currentWeapon].roundType != Weapon.RoundType.GRANADE && !shootAble) return;
		if(myWeapons[currentWeapon].ammo < 1) {
			//TODO: �Ƹ� ������ �˸�
			return;
		}

		GameScene scene = (GameScene) BaseActivity.getSharedInstance().mCurrentScene;

		float beginX = getParent().getX() + getX() + getWidth() / 2;
		float beginY = getParent().getY() + getY() + getHeight() / 2;
		
		switch (myWeapons[currentWeapon].roundType) {
		case GRANADE :
			Projectile projectile = ProjectilePool.sharedProjPool().obtainPoolItem();
            projectile.detachSelf();
			projectile.init(ProjectilePool.ProjectType.FRAG);
			projectile.setVelocity((float) (myWeapons[currentWeapon].distance * Math.cos(rad)), (float)(myWeapons[currentWeapon].distance * Math.sin(rad)));
			
			projectile.setPosition(beginX, beginY);

            ResourceManager.getSharedInstance().getSoundByName("Case2").play();

			DelayModifier mod3 = new DelayModifier(1.5f)
			{
				@Override
				protected void onModifierFinished(IEntity pItem) {
					super.onModifierFinished(pItem);
					((Projectile)pItem).isExpired = true;
				}
			};
			
			projectile.setVisible(true);

            scene.attachChild(projectile);
			projectile.registerEntityModifier(mod3);
			break;
		case MULTY_SHOT :
			
			// ���� �߻�
			for(int i = 0; i < SHOTGUN_MULTYSHOT_AMOUNT; i ++)
			{
				Bullet b = BulletPool.sharedBulletPool().obtainPoolItem();
				b.setPosition(beginX, beginY);
				

				
				double radOffset = rad + MathUtils.random( (float)(-Math.PI / 90), (float)(Math.PI / 90)) * (1 - myWeapons[currentWeapon].accurate / 100);
				double rangeOffset = 	MathUtils.random(1.6f, 2f);
	
				DelayModifier mod = new DelayModifier(myWeapons[currentWeapon].distance / 1000f)
				{
					@Override
					protected void onModifierFinished(IEntity pItem) {
						super.onModifierFinished(pItem);
						((Bullet)pItem).isExpired = true;
					}
				};
				
				b.isPierceShot = myWeapons[currentWeapon].IsPiece;
				b.setVisible(true);
				b.detachSelf();
				b.setMoment((int)(myWeapons[currentWeapon].distance * Math.cos(radOffset) * rangeOffset), (int)(myWeapons[currentWeapon].distance * Math.sin(radOffset)* rangeOffset) );

				scene.attachChild(b);
				Bullet.bulletSet.add(b);
				b.registerEntityModifier(mod);
				b.setDamage(myWeapons[currentWeapon].damage);
				if(this.getParent() instanceof Enemy)
					b.setIsEnemy(true);
				else
					b.setIsEnemy(false);
			}
			break;
		case SINGLE_SHOT :
			Bullet b3 = BulletPool.sharedBulletPool().obtainPoolItem();
			b3.setPosition(beginX, beginY);

			double radOffset = rad + MathUtils.random( (float)(-Math.PI / 90), (float)(Math.PI / 90)) * (1 - myWeapons[currentWeapon].accurate / 100);
			
			DelayModifier mod = new DelayModifier(myWeapons[currentWeapon].distance / 250f)
			{
				@Override
				protected void onModifierFinished(IEntity pItem) {
					super.onModifierFinished(pItem);
					((Bullet)pItem).isExpired = true;
				}
			};
			
			/*
			MoveModifier mod2 = new MoveModifier(myWeapons[currentWeapon].distance / 250f, b3.getX(), b3.getX()+ (float)(myWeapons[currentWeapon].distance * Math.cos(rad)) , 
					b3.getY(), b3.getY() + (float)(myWeapons[currentWeapon].distance * Math.sin(rad))
					) 
			{
				@Override
				protected void onModifierFinished(IEntity pItem) {
					super.onModifierFinished(pItem);
					((Bullet)pItem).isExpired = true;
				}
			};*/
			
			b3.isPierceShot = myWeapons[currentWeapon].IsPiece;
			b3.setDamage(myWeapons[currentWeapon].damage);
			b3.setVisible(true);
			b3.detachSelf();
			b3.setMoment((int)(myWeapons[currentWeapon].distance * Math.cos(radOffset)), (int)(myWeapons[currentWeapon].distance * Math.sin(radOffset)));
			
			
			scene.attachChild(b3);
			Bullet.bulletSet.add(b3);
			b3.registerEntityModifier(mod);
			if(this.getParent() instanceof Enemy || this.getParent() instanceof Chopper)
				b3.setIsEnemy(true);
			else
				b3.setIsEnemy(false);
			break;
		default:
			break;
		}
		
		

		myWeapons[currentWeapon].gunFire.play();
		animate(myWeapons[currentWeapon].frameDurration, myWeapons[currentWeapon].frames[0], myWeapons[currentWeapon].frames[1], false);
		--(myWeapons[currentWeapon].ammo);
		
	}
	
	public int getDelay()
	{
		return myWeapons[currentWeapon].delay;
	}

	public void SetAim() {
			this.setRotation(0);
	}

}
