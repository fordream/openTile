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

public class Medibox extends Sprite{


	static Medibox instance = null;
	
	public boolean isAppear;
	private Sprite Parachute;

	public int targetY;
	
	public static Medibox getSharedInstance()
	{
		if(instance == null)
			instance = new Medibox();
		return instance;
	}
	
	private Medibox() {
		super(0, 0, ResourceManager.getSharedInstance().getResourceByName("Medibox"), BaseActivity.getSharedInstance().getVertexBufferObjectManager());
		
		
		Parachute = new Sprite(-27, -52, ResourceManager.getSharedInstance().getResourceByName("Para"), BaseActivity.getSharedInstance().getVertexBufferObjectManager());
		attachChild(Parachute);
		
		setCullingEnabled(true);
	}

	
	public void moveCharacter(Soldier character, float pSecondsElapsed) {
		
	}
	
	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		// TODO Auto-generated method stub
		if(!isAppear)
			return;
		super.onManagedUpdate(pSecondsElapsed);
		
		//Log.d("Medibox", "Incoming x :" + getX() + "y :" + getY());
		
		if(targetY > getY() + getHeight())
		{
			if(!Parachute.isVisible())
				Parachute.setVisible(true);
			setY(getY() + 1);
		}
		else
		{
			if(Parachute.isVisible())
				Parachute.setVisible(false);
			setY(targetY - getHeight());
		}
		
		if(Soldier.getSharedInstance().collidesWith(this) && !Soldier.getSharedInstance().isDead)
		{
			Soldier.getSharedInstance().setHealth(Soldier.getSharedInstance().MAX_HEALTH); 
			ResourceManager.getSharedInstance().getSoundByName("Medibox").play();
			setVisible(false);
			isAppear = false;
		}
	}


}
