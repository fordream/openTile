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

public class RankScene extends CameraScene implements IOnSceneTouchListener {

	BaseActivity activity;

	public RankScene(Camera mCamera) {
		super(mCamera);
		activity = BaseActivity.getSharedInstance();
		setBackground(new Background(new Color(0f, 102 / 255f, 102 / 255f)));

		Text text = new Text(0, 0, activity.mFont,
				"Ranking"
				,BaseActivity	.getSharedInstance().getVertexBufferObjectManager());
		
		text.setColor(Color.WHITE);
		
		attachChild(text);
		
		final int x = (int) (mCamera.getWidth() / 2 - text.getWidth() / 2);
		
		text.setX(x);
		
		setOnSceneTouchListener(this);
		
	}

	@Override
	public boolean onSceneTouchEvent(Scene arg0, TouchEvent arg1) {
		activity.setCurrentScene(new MainMenuScene());
		return false;
	}

}
