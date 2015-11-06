package com.tistory.pflower.epd;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.ZoomCamera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.color.Color;


import com.tistory.pflower.epd.scene.GameScene;
import com.tistory.pflower.epd.scene.SplashScene;
import com.tistory.pflower.epd.ResourceManager;

import android.graphics.Typeface;
import android.util.Log;


public class BaseActivity extends SimpleBaseGameActivity {

    public static final int CAMERA_WIDTH = 1280 / 2;
    public static final int CAMERA_HEIGHT = 768 / 2;

    public Font mFont;
    public ZoomCamera mCamera;

    // A reference to the current scene
    public Scene mCurrentScene;
    public static BaseActivity instance;

    public static BaseActivity getSharedInstance() {
        return instance;
    }

    @Override
    public EngineOptions onCreateEngineOptions() {
        instance = this;
        mCamera = new ZoomCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
        mCamera.setZoomFactor(1f);

        EngineOptions engineOpt = new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR,
                new FillResolutionPolicy(), mCamera);
        //EngineOptions engineOpt = new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR,
        //				new CroppedResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera);
        engineOpt.getRenderOptions().setDithering(true);

        engineOpt.getAudioOptions().setNeedsMusic(true);
        engineOpt.getAudioOptions().setNeedsSound(true);

        return engineOpt;
    }

    @Override
    protected void onCreateResources() {

        mFont = FontFactory.create(this.getFontManager(),
                this.getTextureManager(), 256, 256,
                Typeface.create(Typeface.DEFAULT, Typeface.NORMAL), 32, 0xffffffff);

        mFont.load();

        ResourceManager.getSharedInstance();
    }

    @Override
    protected Scene onCreateScene() {
        mEngine.registerUpdateHandler(new FPSLogger());
        mCurrentScene = new SplashScene();
        return mCurrentScene;
    }

    // to change the current main scene
    public void setCurrentScene(Scene scene) {
        mCurrentScene = null;
        mCurrentScene = scene;
        getEngine().setScene(mCurrentScene);

    }

    @Override
    public Engine onCreateEngine(EngineOptions pEngineOptions) {
        //Engine engine = new LimitedFPSEngine(pEngineOptions, FPS_LIMITS);
        //return engine;
        return super.onCreateEngine(pEngineOptions);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.v("Jimvaders",
                "BaseActivity BackPressed " + mCurrentScene.toString());
        if (mCurrentScene instanceof GameScene)
            ((GameScene) mCurrentScene).detach();

        mCurrentScene = null;
        finish();
        System.exit(0);
    }

}