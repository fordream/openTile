package com.tistory.pflower.epd.scene;


import android.graphics.Typeface;

import com.tistory.pflower.epd.gameLogic.TileManager;
import com.tistory.pflower.epd.sprites.EffectCube;
import com.wooseok.music.SimpleMidiPlayer;
import com.tistory.pflower.epd.BaseActivity;
import com.tistory.pflower.epd.GameLoopUpdateHandler;
import com.tistory.pflower.epd.ResourceManager;
import com.tistory.pflower.epd.layer.TileLayer;
import com.tistory.pflower.epd.sprites.Cube;
import com.tistory.pflower.epd.sprites.Hero;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.IEntityFactory;
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
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.color.Color;

import java.util.Random;


public class GameScene extends Scene implements IOnSceneTouchListener {
    private final Font mFont;
    SimpleMidiPlayer simpleMidiPlayer;
    Camera mCamera;
    Random rand = new Random(System.currentTimeMillis());
    final Background bg = new Background(1f, 1f, 1f);
    Sprite bgTexture = null;

    public TileLayer tileLayer;

    public float targetX;
    public float targetY;

    Text scores;

    private static Sprite[] levelBackground = {
            new Sprite(0, 0, ResourceManager.getSharedInstance().getResourceByName("woodenTx"), BaseActivity.getSharedInstance().getVertexBufferObjectManager())
    };

    public GameScene() {
        simpleMidiPlayer = new SimpleMidiPlayer(BaseActivity.getSharedInstance().getApplicationContext(), ResourceManager.getSharedInstance().getMidiFileByName("test3"), 0.4f);
        tileLayer = new TileLayer();
        this.mFont = FontFactory.create(BaseActivity.getSharedInstance().getFontManager(), BaseActivity.getSharedInstance().getTextureManager(), 256, 256, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 48);
        this.mFont.load();

        scores = new Text(0, 0, mFont, "점수 : XXXXX", BaseActivity.getSharedInstance().getVertexBufferObjectManager());
        scores.setPosition(BaseActivity.CAMERA_WIDTH / 2 - scores.getWidth(), BaseActivity.CAMERA_HEIGHT - scores.getHeight());
        scores.setColor(Color.WHITE);


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
        attachChild(scores);

        setOnSceneTouchListener(this);
        resetValues();
    }

    // method to reset values and restart the game
    public void resetValues() {

        clearChildScene();
        registerUpdateHandler(new GameLoopUpdateHandler());

       // ResourceManager.getSharedInstance().getMediaByName("bossfight").start();
    }


    @Override
    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
        synchronized (this) {
            if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN)
            {
                targetX = pSceneTouchEvent.getX();
                targetY = pSceneTouchEvent.getY();

                Hero player = Hero.getSharedInstance();

                if(player != null){

                    // UP
                    if (targetY < BaseActivity.CAMERA_HEIGHT / 2) {
                        // 1사분면
                        if (targetX > BaseActivity.CAMERA_WIDTH / 2) {
                            player.move(Cube.Direction.UP_RIGHT);
                        }
                        // 2사분면
                        else {
                            player.move(Cube.Direction.UP_LEFT);
                        }
                    }
                    // DOWN
                    else {
                        // 3사분면
                        if (targetX > BaseActivity.CAMERA_WIDTH / 2) {
                            player.move(Cube.Direction.DOWN_RIGHT);
                        }
                        // 4사분면
                        else {
                            player.move(Cube.Direction.DOWN_LEFT);
                        }
                    }
                }
                // DOWN
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
            simpleMidiPlayer.tickAndPlay(pSecondsElapsed);
            TileManager.getSharedInstance().tick(pSecondsElapsed);

            if(Hero.getSharedInstance().isDead())
            {
                BaseActivity.getSharedInstance().setCurrentScene(new ResultScene(BaseActivity.getSharedInstance().mCamera, EffectCube.score));
            }
            else
            {
                scores.setText("점수 : " + EffectCube.score);
            }
        }

    }
}
