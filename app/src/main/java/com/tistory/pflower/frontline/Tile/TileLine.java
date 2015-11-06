package com.tistory.pflower.frontline.Tile;

import java.util.Iterator;
import java.util.LinkedList;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.batch.SpriteBatch;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.region.TextureRegion;

import com.tistory.pflower.frontline.BaseActivity;
import com.tistory.pflower.frontline.ResourceManager;

import com.tistory.pflower.frontline.pools.TilePool;
import android.util.Log;

import javax.microedition.khronos.opengles.GL10;

public class TileLine extends Rectangle {


    public static final int MAX_HEIGHT = 8;

	//private LinkedList<Tile> tiles;
	public int location;
	public int height;
	public Sprite ground;
    public Tile top;
    public SpriteBatch bottom;


    //public Iterator<Tile> getIterator() {
	//	return tiles.iterator();
	//}

	public void purge() {

        //this.top = null;
		this.location = 0;
		this.height = 0;
		//for (Tile t : tiles) {
		//	t.detachSelf();
		//	TilePool.sharedTilePool().recyclePoolItem(t);
		//}
		//tiles.clear();
	}

	public TileLine(int location, int height) {
		this();
		this.location = location;
		this.height = location;
	}

	public TileLine() {
		super(0, 0, 32, BaseActivity.CAMERA_HEIGHT, BaseActivity.getSharedInstance().getVertexBufferObjectManager());
		//tiles = new LinkedList<Tile>();
        top = new Tile();


        top.setPosition(0, BaseActivity.CAMERA_HEIGHT - (height) * 14);
        top.location = location;
        top.setVisible(true);
        top.setCurrentTileIndex(0);
        attachChild(top);

        bottom = new SpriteBatch(ResourceManager.getSharedInstance().getTiledResourceByName("tile").getTexture(), MAX_HEIGHT, BaseActivity.getSharedInstance().getVertexBufferObjectManager());

		ground = new Sprite(0, BaseActivity.CAMERA_HEIGHT, ResourceManager.getSharedInstance().getResourceByName("ground"), BaseActivity.getSharedInstance()
				.getVertexBufferObjectManager());
		ground.setVisible(true);
        ground.setCullingEnabled(true);
		attachChild(ground);
	}

	public void restart(int location, int height) {
		Log.v("jimvaders", "TileLine restarted");
		//tiles.clear();
		clearEntityModifiers();
		clearUpdateHandlers();
		
		this.location = location;
		this.height = height;

        top.setPosition(0, BaseActivity.CAMERA_HEIGHT - (height) * 14);
        top.location = location;
        top.setVisible(true);
        top.setCurrentTileIndex(0);

        final int _width = (int) ResourceManager.getSharedInstance().getTiledResourceByName("tile").getWidth();
        final int _height = (int) ResourceManager.getSharedInstance().getTiledResourceByName("tile").getHeight();

        bottom.detachSelf();
        bottom.setBlendFunction(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
        bottom.reset();



		for (int i = 1; i < height; i ++) {

            final float x = 0;
            final float y = BaseActivity.CAMERA_HEIGHT - i * 14;

            bottom.draw(ResourceManager.getSharedInstance().getTiledResourceByName("tile").getTextureRegion(0), x, y, _width, _height, 1f, 1f, 1f, 1f);


			//Tile t = TilePool.sharedTilePool().obtainPoolItem();

            //if(i == height)
            //    top = t;

			//t.location = location;
			//t.height = i;
			//t.setPosition(0, BaseActivity.CAMERA_HEIGHT - i * t.getHeight());
			//t.setVisible(true);
		}
        bottom.submit();
        attachChild(bottom);
		
		setPosition(location * 32, 0);
		setColor(0.0f, 0.0f, 0.0f, 0.0f);
	}

	@Override
	public void onDetached() {
		purge();
		clearUpdateHandlers();
		super.onDetached();
	}

	public void init() {
		setVisible(true);
	}

	public void clean() {
		// TODO Auto-generated method stub
		
	}


}