package com.tistory.pflower.epd.scene;


import com.tistory.pflower.epd.BaseActivity;
import com.tistory.pflower.epd.GameLoopUpdateHandler;
import com.tistory.pflower.epd.ResourceManager;
import com.tistory.pflower.epd.andEngineExtension.StairXVelocityModifier;
import com.tistory.pflower.epd.layer.TileLayer;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.IEntityFactory;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.particle.ParticleSystem;
import org.andengine.entity.particle.emitter.PointParticleEmitter;
import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.entity.particle.modifier.AlphaParticleModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import java.util.Random;


public class GameScene extends Scene implements IOnSceneTouchListener {

    Camera mCamera;
    Random rand = new Random(System.currentTimeMillis());

    final Background bg = new Background(1f, 1f, 1f);
    Sprite bgTexture = null;

    public TileLayer tileLayer;

    public float targetX;
    public float targetY;

    private static Sprite[] levelBackground = {
            new Sprite(0, 0, ResourceManager.getSharedInstance().getResourceByName("woodenTx"), BaseActivity.getSharedInstance().getVertexBufferObjectManager())
    };

    public GameScene() {

        tileLayer = new TileLayer();

        for(Sprite _l : levelBackground) {
            _l.setScaleCenter(_l.getWidth() / 2, _l.getHeight() / 2);
        }

        bgTexture = levelBackground[0];

        mCamera = BaseActivity.getSharedInstance().mCamera;
        BaseActivity.getSharedInstance().setCurrentScene(this);

        rand = new Random(System.currentTimeMillis());

        attachChild(bgTexture);
        attachChild(tileLayer);
        setBackground(bg);

        setOnSceneTouchListener(this);
        resetValues();
    }


    // method to reset values and restart the game
    public void resetValues() {

        clearChildScene();
        registerUpdateHandler(new GameLoopUpdateHandler());

        ResourceManager.getSharedInstance().getMediaByName("bossfight").start();
    }


    @Override
    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
        synchronized (this) {
            if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN)
            {
                targetX = pSceneTouchEvent.getX();
                targetY = pSceneTouchEvent.getY();
            }
            else
            {


            }
        }
        return true;
    }


    private void createExplosion(final float posX, final float posY,
                                 final IEntity target, final SimpleBaseGameActivity activity, final boolean isAnswer) {

        int mNumPart = 15;
        int mTimePart = 2;

        PointParticleEmitter particleEmitter = new PointParticleEmitter(posX,
                posY);
        IEntityFactory<Rectangle> recFact = new IEntityFactory<Rectangle>() {

            @Override
            public Rectangle create(float pX, float pY) {
                Rectangle rect = new Rectangle(rand.nextInt(BaseActivity.CAMERA_WIDTH - BaseActivity.CAMERA_WIDTH / 8) + BaseActivity.CAMERA_WIDTH / 8, BaseActivity.CAMERA_HEIGHT, 10, 10,
                        activity.getVertexBufferObjectManager());
                rect.setColor(1f, 1f, 1f);
                if(isAnswer) {
                    rect.setPosition(BaseActivity.CAMERA_WIDTH / 2, BaseActivity.CAMERA_HEIGHT);
                    rect.setColor(1f, 0f, 0f);
                }
                return rect;
            }

        };
        final ParticleSystem<Rectangle> particleSystem = new ParticleSystem<Rectangle>(
                recFact, particleEmitter, 500, 500, mNumPart);

        particleSystem
                .addParticleInitializer(new VelocityParticleInitializer<Rectangle>(
                        0, 0, -BaseActivity.CAMERA_HEIGHT * (1 / 0.35f), -BaseActivity.CAMERA_HEIGHT * (1 / 0.35f)));

        particleSystem
                .addParticleModifier(new AlphaParticleModifier<Rectangle>(0,
                        0.35f * mTimePart, 1, 0));

        target.attachChild(particleSystem);
        target.registerUpdateHandler(new TimerHandler(mTimePart,
                new ITimerCallback() {
                    @Override
                    public void onTimePassed(final TimerHandler pTimerHandler) {
                        particleSystem.detachSelf();
                        target.sortChildren();
                        target.unregisterUpdateHandler(pTimerHandler);
                    }
                }));

    }


    public void detach() {
        synchronized (this) {
        }

    }

    public void update(float pSecondsElapsed) {
        synchronized (this) {
        }
    }
}