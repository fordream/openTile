package com.tistory.pflower.frontline;

import java.io.IOException;
import java.util.HashMap;

import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.vbo.HighPerformanceSpriteVertexBufferObject;
import org.andengine.entity.sprite.vbo.HighPerformanceTiledSpriteVertexBufferObject;
import org.andengine.entity.sprite.vbo.ISpriteVertexBufferObject;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.region.BaseTextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.HighPerformanceVertexBufferObject;
import org.andengine.opengl.vertex.RectangleVertexBuffer;

import android.content.res.AssetManager;

import com.tistory.pflower.frontline.object.Weapon;

import javax.microedition.khronos.opengles.GL11;

public class ResourceManager {

    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================
	static ResourceManager instance = null;
	
	private HashMap<String, TiledTextureRegion> tiledResourceMap = new HashMap<String, TiledTextureRegion>();
	private HashMap<String, TextureRegion> resourceMap = new HashMap<String, TextureRegion>();
	private HashMap<String, BitmapFont> bitmapFontMap = new HashMap<String, BitmapFont>();
	private HashMap<String, Sound> soundMap = new HashMap<String, Sound>();
    private HashMap<BaseTextureRegion, ISpriteVertexBufferObject> sharedTextureMap = new HashMap<>();
	
	private BuildableBitmapTextureAtlas gameTextureAtlas;
	//private BuildableBitmapTextureAtlas mBitmapTextureAtlas;

    // ===========================================================
    // Methods
    // ===========================================================

	public static ResourceManager getSharedInstance()
	{
		if(instance == null)
		{
			instance = new ResourceManager();
		}
		
		return instance;
	}
	
	public ResourceManager()
	{
		
			BaseActivity activity = BaseActivity.getSharedInstance();
			gameTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 4096,2048, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
			BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("game/");
			
			TiledTextureRegion _Arms = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "Arms.png", 10, 5);
			TiledTextureRegion _Legs = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "Legs.png",9, 3);
			TiledTextureRegion _Torso = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "Torso.png", 9, 9);
			TextureRegion _Medibox = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "Medibox.png");
			TextureRegion _Para = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "Para.png");
            TextureRegion _reload = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "reload.png");
			TiledTextureRegion _debris = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "debris.png", 3, 4);

            resourceMap.put("reload", _reload);
			resourceMap.put("Medibox", _Medibox);
			resourceMap.put("Para", _Para);
			tiledResourceMap.put("Arms", _Arms);
			tiledResourceMap.put("Legs", _Legs);
			tiledResourceMap.put("Torso", _Torso);	
			
			BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("game/chopper/");
			
			TextureRegion _body = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "chopperBody.png");
			TextureRegion _rotor = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "rotor.png");
			TiledTextureRegion _cock = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "cockpit.png", 1, 3);
			TiledTextureRegion _tail = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "tail.png", 1, 3);
			
			resourceMap.put("chopperBody", _body);
			resourceMap.put("rotor", _rotor);
			tiledResourceMap.put("cockpit", _cock);
			tiledResourceMap.put("tail", _tail);	
			
			
			//gameTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 2048,2048, TextureOptions.BILINEAR);;
			// �÷��̾� �ൿ
			BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("game/hud/");
			
			TextureRegion _danger = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "danger.png");
			
			TiledTextureRegion _arrow = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "arrow.png", 1, 2);
			TiledTextureRegion _pistol = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "pistol.png", 1, 2);
			TiledTextureRegion _MG = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "MG.png", 1, 2);
			TiledTextureRegion _SG = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "SG.png", 1, 2);
			TiledTextureRegion _SN = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "sna.png", 1, 2);
			TiledTextureRegion _FR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "frag.png", 1, 2);
			TiledTextureRegion _Shield= BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "shield.png", 1, 2);
			TiledTextureRegion _magazine = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "magazine.png", 1, 2);
			
			TiledTextureRegion _buy = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "buy.png", 1, 2);

            TextureRegion _headshot =  BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "headshot.png");
            TextureRegion _heavydam =  BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "heavydam.png");

            TextureRegion _gauge =  BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "gauge.png");
			
			BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("game/hud/char/");
			//mBitmapTextureAtlas = new BuildableBitmapTextureAtlas(BaseActivity.getSharedInstance().getTextureManager(), 2048, 2048, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
	        TiledTextureRegion fontTile = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, BaseActivity.getSharedInstance(), "num.png",12,2);
	        
	        TiledTextureRegion fontKill = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, BaseActivity.getSharedInstance(), "kCnt.png", 10,1);
	        TiledTextureRegion fontMeter = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, BaseActivity.getSharedInstance(), "mCnt.png", 10,1);
	        TiledTextureRegion fontHit = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, BaseActivity.getSharedInstance(), "HitCnt.png", 10,1);
	        TiledTextureRegion Points = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, BaseActivity.getSharedInstance(), "pts.png", 10,1);

	        
	        TextureRegion killch =  BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, BaseActivity.getSharedInstance(), "kill.png");
	        TextureRegion ptsch =  BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, BaseActivity.getSharedInstance(), "ptsch.png");
	        TextureRegion hitch =  BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, BaseActivity.getSharedInstance(), "Hit.png");
	        TextureRegion mch =  BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, BaseActivity.getSharedInstance(), "m.png");

	        
	        resourceMap.put("danger", _danger);
	        
	        tiledResourceMap.put("arrow", _arrow);
	        tiledResourceMap.put("pistol", _pistol);
	        tiledResourceMap.put("MG", _MG);
	        tiledResourceMap.put("SG", _SG);	
	        tiledResourceMap.put("sna", _SN);
	        tiledResourceMap.put("frag", _FR);
	        tiledResourceMap.put("shield", _Shield);	
	        tiledResourceMap.put("magazine", _magazine);
	        tiledResourceMap.put("buy", _buy);
	        
	        tiledResourceMap.put("debris", _debris);
			
	        tiledResourceMap.put("num", fontTile);
	        tiledResourceMap.put("kCnt", fontKill);
	        tiledResourceMap.put("mCnt", fontMeter);	
	        tiledResourceMap.put("HitCnt", fontHit);
	        tiledResourceMap.put("pts", Points);

            resourceMap.put("heavydam", _heavydam);
            resourceMap.put("headshot", _headshot);
            resourceMap.put("gauge", _gauge);
	        resourceMap.put("kill", killch);
	        resourceMap.put("ptsch", ptsch);
	        resourceMap.put("Hit", hitch);
	        resourceMap.put("m", mch);
			
	        
			BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("game/");
			
			TiledTextureRegion _tile = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "tile.png", 1, 4);
			
			
			tiledResourceMap.put("tile", _tile);
            this.registerTextureRegion(_tile);
			
			
			
			BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("game/");
			// �÷��̾� �ൿ
			TextureRegion _ground = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "ground.png");
			
			resourceMap.put("ground", _ground);
			
			BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("game/");
			TextureRegion _bg = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "bg.png");
			TextureRegion _sky = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "sky.png");

			resourceMap.put("bg", _bg);
			resourceMap.put("sky", _sky);
			
			//try{
			//	mBitmapTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
			//	mBitmapTextureAtlas.load();
			//}catch(Exception e){ e.printStackTrace();	}
			
			try{
				gameTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(1, 1, 1));
				gameTextureAtlas.load();
			}catch(Exception e){ e.printStackTrace();	}
			
			
			
			Weapon.loadWeaponsResource();
			
			bitmapFontMap.put("mFont", new BitmapFont(
                    new char[]{'0', '1', '2', '3', '4', '5',
                                    '6', '7', '8', '9', 'X', 'Z',
                                    'A','B','C','D','E','F','G','H',
                                    'I','J', 'K', 'L'},
                                    fontTile, BaseActivity.getSharedInstance().getVertexBufferObjectManager()));

            bitmapFontMap.put("mMFont", new BitmapFont(
                new char[]{'0', '1', '2', '3', '4', '5',
                        '6', '7', '8', '9'},
                fontMeter, BaseActivity.getSharedInstance().getVertexBufferObjectManager()));

            bitmapFontMap.put("mKFont", new BitmapFont(
                new char[]{'0', '1', '2', '3', '4', '5',
                        '6', '7', '8', '9'},
                fontKill, BaseActivity.getSharedInstance().getVertexBufferObjectManager()));

			bitmapFontMap.put("mKFont", new BitmapFont(
		            new char[]{'0', '1', '2', '3', '4', '5',
                            '6', '7', '8', '9'},
                            fontKill, BaseActivity.getSharedInstance().getVertexBufferObjectManager()));
			
			bitmapFontMap.put("mHFont", new BitmapFont(
		    		new char[]{'0', '1', '2', '3', '4', '5',
		                    '6', '7', '8', '9'},
		                            fontHit, BaseActivity.getSharedInstance().getVertexBufferObjectManager()));
			
			bitmapFontMap.put("mPFont", new BitmapFont(
		    		new char[]{'0', '1', '2', '3', '4', '5',
		                    '6', '7', '8', '9'},
		                            Points, BaseActivity.getSharedInstance().getVertexBufferObjectManager()));

			SoundFactory.setAssetBasePath("sound/");
			
			
			String[] files = null;
			try {
			    AssetManager assetMgr = activity.getAssets();
			    files = assetMgr.list("sound");
			} catch (IOException e) {
			    e.printStackTrace();
			}
			
			for (String iter : files) {
				 if(iter.lastIndexOf('.') == -1)
				        continue;
				 
				try {
					Sound tempSnd = SoundFactory.createSoundFromAsset(activity.getSoundManager(), activity, iter);
					tempSnd.setLooping(false);
                    tempSnd.setVolume(2.0f);
					soundMap.put(iter.substring(0, iter.lastIndexOf('.')), tempSnd);
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
			
			
	}
	public void clear()
	{
		gameTextureAtlas.unload();
		//mBitmapTextureAtlas.unload();
	}

	public TiledTextureRegion getTiledResourceByName(String key) {
		return tiledResourceMap.get(key);
	}

	public TextureRegion getResourceByName(String key)	{
		return resourceMap.get(key);
	}
	
	public BitmapFont getFontByName(String key) {
		return bitmapFontMap.get(key);
	}
	
	public Sound getSoundByName(String key) {
		return soundMap.get(key);
	}

    private void registerTextureRegion(BaseTextureRegion region)
    {
        final ISpriteVertexBufferObject _sharedVertexBuffer = new HighPerformanceSpriteVertexBufferObject(BaseActivity.getSharedInstance().getVertexBufferObjectManager(),
                Sprite.SPRITE_SIZE, DrawType.STATIC, true, Sprite.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT);

        sharedTextureMap.put(region, _sharedVertexBuffer);
    }
    public ISpriteVertexBufferObject getSharedVertexByTextureRegion(BaseTextureRegion region) {
        return sharedTextureMap.get(region);
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
