package com.tistory.pflower.epd;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;

import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.vbo.HighPerformanceSpriteVertexBufferObject;
import org.andengine.entity.sprite.vbo.HighPerformanceTiledSpriteVertexBufferObject;
import org.andengine.entity.sprite.vbo.ISpriteVertexBufferObject;
import org.andengine.opengl.font.BitmapFont;
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
import android.media.MediaPlayer;
import android.net.Uri;


import com.tistory.pflower.epd.BaseActivity;

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
    private HashMap<String, MediaPlayer> mediaMap = new HashMap<String, MediaPlayer>();
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



        /////////////////////////////////////////////////////
        //
        //                  Sprite
        //
        /////////////////////////////////////////////////////

        gameTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 4096,2048, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("sprite/");

        String[] files2 = null;
        try {
            AssetManager assetMgr = activity.getAssets();
            files2 = assetMgr.list("sprite");
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String iter : files2) {
            if(iter.lastIndexOf('.') == -1)
                continue;

            TextureRegion _text = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, iter);
            resourceMap.put(iter.substring(0, iter.lastIndexOf('.')), _text);

        }

        try{
            gameTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(1, 1, 1));
            gameTextureAtlas.load();
        }catch(Exception e){ e.printStackTrace();	}

        /////////////////////////////////////////////////////
        //
        //                  Sounds
        //
        /////////////////////////////////////////////////////

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

        Field[] fields=R.raw.class.getFields();
        for(int count=0; count < fields.length; count++){
            String iter = fields[count].getName();

            try {
                MediaPlayer tempmda = MediaPlayer.create(activity.getApplicationContext(), fields[count].getInt(fields[count]) );
                tempmda.setLooping(true);
                mediaMap.put(iter, tempmda);
            } catch (Exception e) {
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

    public MediaPlayer getMediaByName(String key) {
        return mediaMap.get(key);
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
