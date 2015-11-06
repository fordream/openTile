package com.tistory.pflower.epd.scene;


import com.tistory.pflower.epd.BaseActivity;
import com.tistory.pflower.epd.GameLoopUpdateHandler;
import com.tistory.pflower.epd.ResourceManager;
import com.tistory.pflower.epd.andEngineExtension.StairXVelocityModifier;

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
    Random rand;
    final Background bg = new Background(1f, 1f, 1f);
    Sprite bgTexture = null;

    Text levelText;
    Text puss;
    Text hitCntText;


    public static int level = 1;
    public static int hitCnt = 0;
    private float r = 1f, g = 1f, b = 1f;

    private static Sprite[] levelBackground = {
            new Sprite(0, 0, ResourceManager.getSharedInstance().getResourceByName("woodenTx"), BaseActivity.getSharedInstance().getVertexBufferObjectManager()),
            new Sprite(0, 0, ResourceManager.getSharedInstance().getResourceByName("backText"), BaseActivity.getSharedInstance().getVertexBufferObjectManager()),
            new Sprite(0, 0, ResourceManager.getSharedInstance().getResourceByName("diaTx"), BaseActivity.getSharedInstance().getVertexBufferObjectManager()),
            new Sprite(0, 0, ResourceManager.getSharedInstance().getResourceByName("magicTx"), BaseActivity.getSharedInstance().getVertexBufferObjectManager()),
    };

    public GameScene() {

        for(Sprite _l : levelBackground) {
            _l.setScaleCenter(_l.getWidth() / 2, _l.getHeight() / 2);
        }

        BaseActivity activity = BaseActivity.getSharedInstance();
        mCamera = BaseActivity.getSharedInstance().mCamera;
        BaseActivity.getSharedInstance().setCurrentScene(this);

        bgTexture = levelBackground[0];

        rand = new Random(System.currentTimeMillis());

        levelText = new Text(0, 0, activity.mFont, "Level : XX",
                activity.getVertexBufferObjectManager());


        puss = new Text(0, 0, activity.mFont,
                "XXXXXXXXXXXXXX",
                activity.getVertexBufferObjectManager());
        hitCntText = new Text(0, 0, activity.mFont, "XXXXXX", activity.getVertexBufferObjectManager());

        levelText.setPosition(activity.mCamera.getWidth() / 2 - levelText.getWidth() / 2, activity.mCamera.getHeight() / 2 - puss.getHeight() - levelText.getHeight() / 2 );
        puss.setPosition(activity.mCamera.getWidth() / 2 - puss.getWidth() / 2, activity.mCamera.getHeight() / 2 - puss.getHeight() / 2);
        hitCntText.setPosition(activity.mCamera.getWidth() / 2 - hitCntText.getWidth() / 2, activity.mCamera.getHeight() / 2 + hitCntText.getHeight());

        final GameScene _s = this;

        registerUpdateHandler(new TimerHandler(0.35f, true, new ITimerCallback() {
            @Override
            public void onTimePassed(TimerHandler pTimerHandler) {
                DelayModifier del = new DelayModifier(0.35f)
                {
                    float timesum = 0.f;
                    boolean isUpper = true;
                    @Override
                    protected void onManagedUpdate(float pSecondsElapsed, IEntity pEntity) {
                        super.onManagedUpdate(pSecondsElapsed, pEntity);
                        timesum += pSecondsElapsed;
                        if(timesum > 0.175f) {
                            isUpper = !isUpper;
                            timesum -= 0.175f;
                        }
                        float rate =(timesum / 0.175f) * 0.15f;
                        if(isUpper) {
                            bgTexture.setScale(1 + rate / 2);
                            bg.setColor(r + rate, g + rate, b + rate);
                        }
                        else {
                            bgTexture.setScale(1 + (0.15f - rate) / 2);
                            bg.setColor(r - rate, g - rate, b - rate);
                        }
                    }
                };
                _s.registerEntityModifier(del);
            }
        }));

        //attachChild(new TreeLayer());
        attachChild(bgTexture);
        attachChild(levelText);
        attachChild(puss);
        attachChild(hitCntText);
        setBackground(bg);

        setOnSceneTouchListener(this);
        resetValues();
    }


    // method to reset values and restart the game
    public void resetValues() {

        clearChildScene();
        registerUpdateHandler(new GameLoopUpdateHandler());
        level = 1;
        hitCnt = 0;

        ResourceManager.getSharedInstance().getMediaByName("bossfight").start();
        hitCntText.setText(String.format("%6d", hitCnt));
        levelText.setText("Level : " + String.format("%2d", level));
        puss.setText(String.format("%.10f%%", (1 / (float) getPossibllity()) * 100));
    }


    @Override
    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
        synchronized (this) {
            if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP)
            {
                ++hitCnt;
                hitCntText.setText(String.format("%6d", hitCnt));

                boolean isAnswer = rand.nextFloat()*(getPossibllity()) < 1;

                createExplosion(pSceneTouchEvent.getX(), pSceneTouchEvent.getY(),
                        this, BaseActivity.getSharedInstance(), isAnswer);
                if(isAnswer){
                    levelUp();
                } else {

                }
            }
            else
            {


            }
        }
        return true;
    }

    private int ipow(int base, int exp)
    {
        int result = 1;
        while (exp != 0)
        {
            if ( (exp & 1) != 0)
                result *= base;
            exp >>= 1;
            base *= base;
        }

        return result;
    }

    private float getPossibllity()
    {
        return (float)Math.pow(1.2, (double)level);
    }
    private void levelUp() {
        hitCnt = 0;
        ++level;
        r = rand.nextFloat();
        g = rand.nextFloat();
        b = rand.nextFloat();
        bg.setColor(r, g, b);
        //setBackground(new Background(r, g, b));
        levelText.setText("Level : " + String.format("%2d", level));
        levelText.setColor(1f - r, 1f - g, 1f - b);
        puss.setText(String.format("%.10f%%", (1 / (float) getPossibllity()) * 100));
        puss.setColor(1f - r, 1f - g, 1f - b);
        hitCntText.setColor(1f - r, 1f - g, 1f - b);

        if ((level - 1 / 10) != level / 10) {
            bgTexture.detachSelf();
            bgTexture = levelBackground[(level / 10 < levelBackground.length - 1 ? level / 10 : levelBackground.length - 1)];

            attachChild(bgTexture);
            bgTexture.setZIndex(-1);
            sortChildren();

        }
        //title1.registerEntityModifier(new MoveXModifier(1, title1.getX(),
        //        activity.mCamera.getWidth() / 2 - title1.getWidth()));
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
        particleSystem
                .addParticleModifier(new StairXVelocityModifier<Rectangle>(0, 0.35f * mTimePart, 0, BaseActivity.CAMERA_WIDTH / 2, 1 / (float)level));

        //final BatchedPseudoSpriteParticleSystem particleSystem = new BatchedPseudoSpriteParticleSystem(
        //        new RectangleParticleEmitter(CAMERA_WIDTH / 2, CAMERA_HEIGHT, CAMERA_WIDTH, 1),
        //        2, 5, 100, mSnowParticleRegion,
        //        this.getVertexBufferObjectManager());
/*
        particleSystem.registerUpdateHandler(new TimerHandler(0.35f, true, new ITimerCallback() {
            @Override
            public void onTimePassed(TimerHandler pTimerHandler) {
                Log.d("Game", "Check");
                DelayModifier del = new DelayModifier(0.35f)
                {
                    float timesum = 0.f;
                    @Override
                    protected void onManagedUpdate(float pSecondsElapsed, IEntity pEntity) {
                        Log.d("Game", "caboom");
                        super.onManagedUpdate(pSecondsElapsed, pEntity);
                        timesum += pSecondsElapsed;
                        if(timesum > 0.175f) {
                            timesum -= 0.175f;
                            particleSystem.set
                        }

                    }
                };
                particleSystem.registerEntityModifier(del);
            }
        }));
*/
        //particleSystem
        //        .addParticleModifier(new RotationParticleModifier<Rectangle>(0,
        //                mTimePart, 0, 360));

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
