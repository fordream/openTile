package com.tistory.pflower.frontline.scene;

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

import com.tistory.pflower.frontline.BaseActivity;
import com.tistory.pflower.frontline.R;

public class ResultScene extends CameraScene implements IOnSceneTouchListener {

	BaseActivity activity;
	GameScene scene;

	public ResultScene(Camera mCamera, GameScene scene) {
		super(mCamera);
		activity = BaseActivity.getSharedInstance();
		//setBackgroundEnabled(false);
		setBackground(new Background(new Color(0f, 102 / 255f, 102 / 255f)));
		//GameScene scene = (GameScene) activity.mCurrentScene;
		float accureay = 0;//1 - (float) scene.missCount / scene.bulletCount;
		if (Float.isNaN(accureay))
			accureay = 0;
		accureay *= 100;

		
		int sum = (int) ((scene.maximumX / 16) * (scene.maximumX / 16) + scene.character.killCnt * 100 + scene.character.money);
		
		Text result = new Text(0, 0, activity.mFont,
				activity.getString(R.string.result) + "\n\n"  
		+ activity.getString(R.string.remainPts) + "\t" + String.format("%d", scene.character.money) + " pts\n"
		+ activity.getString(R.string.kills) + "\t" + String.format("%d", scene.character.killCnt) + " person\n"
		+ activity.getString(R.string.meter) + "\t" + String.format("%d", (int)scene.maximumX / 16) + " meter\n\n\n"
		
		+ activity.getString(R.string.sum) + "\t" + String.format("%d", sum) + "\n"		
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
		activity.setCurrentScene(new MainMenuScene());
		scene.resetValues();
		return false;
	}

}
