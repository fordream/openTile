package com.tistory.pflower.epd.scene;

import com.tistory.pflower.epd.BaseActivity;
import com.tistory.pflower.epd.ResourceManager;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.scene.CameraScene;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.color.Color;


public class ResultScene extends CameraScene implements IOnSceneTouchListener {

    BaseActivity activity;
    GameScene scene;

    public ResultScene(Camera mCamera, int score) {
        super(mCamera);
        activity = BaseActivity.getSharedInstance();
        //setBackgroundEnabled(false);
        ResourceManager.getSharedInstance().getMediaByName("gameover").start();
        setBackground(new Background(new Color(0f, 0f, 0f)));

        float accureay = 0;
        if (Float.isNaN(accureay))
            accureay = 0;
        accureay *= 100;

        Text result = new Text(0, 0, activity.mFont,
                "점수 : " + score + "\n\n"
                , BaseActivity	.getSharedInstance().getVertexBufferObjectManager());

        result.setColor(Color.WHITE);

        final int x = (int) (mCamera.getWidth() / 2 - result.getWidth() / 2);
        final int y = (int) (mCamera.getHeight() / 2 - result.getHeight() / 2);

        Line line = new Line(mCamera.getWidth() / 2 - result.getWidth() / 2 , mCamera.getHeight() / 2 + result.getWidth() / 2
                , mCamera.getWidth() / 2 + result.getWidth() / 2, mCamera.getHeight() / 2 + result.getWidth() / 2
                , BaseActivity	.getSharedInstance().getVertexBufferObjectManager());
        line.setColor(Color.WHITE);

        result.setPosition(x, y);
        attachChild(result);
        attachChild(line);

        setOnSceneTouchListener(this);

        this.scene = scene;
    }

    @Override
    public boolean onSceneTouchEvent(Scene arg0, TouchEvent arg1) {
        ResourceManager.getSharedInstance().getSoundByName("gameover").stop();
        activity.setCurrentScene(new GameScene());
        scene.resetValues();
        return false;
    }

}
