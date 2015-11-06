package com.tistory.pflower.frontline;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.util.math.MathUtils;

import com.tistory.pflower.frontline.Tile.Tile;
import com.tistory.pflower.frontline.Tile.TileLayer;
import com.tistory.pflower.frontline.Tile.TileLine;
import com.tistory.pflower.frontline.object.Enemy;
import com.tistory.pflower.frontline.object.Soldier;
import com.tistory.pflower.frontline.object.Weapon;
import com.tistory.pflower.frontline.scene.GameScene;

import com.tistory.pflower.frontline.pools.BulletPool;
import com.tistory.pflower.frontline.pools.ProjectilePool;
import com.tistory.pflower.frontline.pools.ProjectilePool.ProjectType;
import android.animation.ArgbEvaluator;
import android.util.Log;

public class Projectile extends Rectangle{

    public static HashSet<Projectile> list = new HashSet<Projectile>();
    public static HashSet<Projectile> trash = new HashSet<Projectile>();
    public boolean isExpired;
	public boolean isEnemy;
	public Sprite sprite = null;
	public static final float GRAVITY = 200f;
	public static final float AIR_FRICTION = 0.5f;
	public static final float COLLISION_FACTOR = 0.5f;
	public static final int FLAGSHOT_AMOUNT = 30;
	public float VelX;
	public float VelY;
    private boolean isGround = false;

    public Projectile(TextureRegion mTextureRegion) {
		super(0, 0, 0, 0, BaseActivity.getSharedInstance()
				.getVertexBufferObjectManager());
		
		sprite = new Sprite(0, 0, mTextureRegion, BaseActivity.getSharedInstance()
				.getVertexBufferObjectManager());
		setSize(sprite.getWidth(), sprite.getHeight());
		
		isExpired = false;
        isGround = false;
		setColor(0.09904f, 0.5f, 0.1786f);

		attachChild(sprite);
	}


	public Projectile() {
		super(0, 0, 0, 0, BaseActivity.getSharedInstance()
				.getVertexBufferObjectManager());
		setColor(0.09904f, 0.5f, 0.1786f);
        isGround = false;
		sprite = null;
	}

	public void init(ProjectType frag)
	{
        Projectile.list.add(this);
        isGround = false;
		switch (frag) {
		case FRAG:
			sprite = new Sprite(0, 0, ProjectilePool.mTextureRegion, BaseActivity.getSharedInstance()
					.getVertexBufferObjectManager());
			setSize(sprite.getWidth(), sprite.getHeight());
			attachChild(sprite);
			break;
		case GUN_ROUND:
			detachChildren();
			setSize(2, 1);
			setColor(1f, 1f, 0f);
			break;
		case SHOT_ROUND:
			detachChildren();
			setSize(3, 2);
			setColor(1f, 0f, 0f);
			break;
		default:
			break;
		}
		
	}


	public void clean()
	{
		detachChildren();
		VelX = 0.0f;
		VelY = 0.0f;
		isExpired = false;
        isGround = false;
	}
	
	public void setVelocity(float Vx, float Vy)
	{
		VelX = Vx;
		VelY = Vy;
	}
	

	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);
	}


    public void die()
    {

            GameScene scene = (GameScene) BaseActivity.getSharedInstance().mCurrentScene;
            // BOOM
            // ���� �߻�
            for(int i = 0; i < FLAGSHOT_AMOUNT; i ++)
            {
                Bullet b = BulletPool.sharedBulletPool().obtainPoolItem();
                b.setPosition(getX(), getY());

                // ��ź
                float radOffset = MathUtils.random( 0, (float) (2 * Math.PI));
                float rangeOffset = 	MathUtils.random(1f, 2f);

                Weapon[] myWeapons = Soldier.getSharedInstance().myWeapons;
                int currentWeapon = 4;//Soldier.getSharedInstance().arms.currentWeapon;

                DelayModifier mod = new DelayModifier(myWeapons[currentWeapon].distance * rangeOffset / 500f)
                {
                    @Override
                    protected void onModifierFinished(IEntity pItem) {
                        super.onModifierFinished(pItem);
                        ((Bullet)pItem).isExpired = true;
                    }
                };

                b.setDamage(myWeapons[currentWeapon].damage);
                b.isPierceShot = myWeapons[currentWeapon].IsPiece;
                b.setVisible(true);
                b.setIsEnemy(false);
                b.setMoment((int)(myWeapons[currentWeapon].distance * rangeOffset * Math.cos(radOffset)), (int)(myWeapons[currentWeapon].distance * rangeOffset * Math.sin(radOffset)));


                scene.attachChild(b);
                Bullet.bulletSet.add(b);
                b.registerEntityModifier(mod);
            }

            ResourceManager.getSharedInstance().getSoundByName("Grenade").play();

            //Projectile.list.remove(this);
            Projectile.trash.add(this);
            //ProjectilePool.sharedProjPool().recyclePoolItem(this);

    }
    public void update(float pSecondsElapsed) {

        if(isExpired)
        {
            return;
        }
        TileLine _l = TileLayer.getSharedInstance().lines.get( (int)((getX() + getWidth() / 2) / 32));



        boolean found = false;
        if(!isGround && _l != null)
        {
            //Iterator<Tile> line = _l.getIterator();

            /*while(line.hasNext())
            {
                Tile t = line.next();

                if(t.collidesWith(this))
                {
                    setX(getX() - VelX*pSecondsElapsed);
                    synchronized (this) {
                        setY(BaseActivity.getSharedInstance().CAMERA_HEIGHT - _l.height * 14);
                    }
                    Log.d("frag", "1" );
                    isGround = true;
                    found = true;
                }
            }*/

            if(!found && _l.ground.collidesWith(this))
            {
                setX(getX() - VelX * pSecondsElapsed);
                setY(getY() - VelY*pSecondsElapsed);
                Log.d("frag", "2" );
                isGround = true;
                //setX(oldX);
                //setX(oldY);
                //setX(_l.getX() - _l.ground.getX());
                //setY(_l.getY() - _l.ground.getY() - getHeight());
                //setY(0);
            }
        }

        if(!isGround) {
            VelY += GRAVITY * pSecondsElapsed;

            setX(getX() + VelX*pSecondsElapsed);
            setY(getY() + VelY*pSecondsElapsed);
        } else {
            Log.d("frag", "x : " + getX() + " y : " + getY() + " vx : " + VelX +  " xy : " + VelY + " is " + isGround );
            VelY = VelX = 0;
        }
    }
}