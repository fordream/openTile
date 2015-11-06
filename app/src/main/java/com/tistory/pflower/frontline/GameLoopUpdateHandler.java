package com.tistory.pflower.frontline;

import org.andengine.engine.handler.IUpdateHandler;

import com.tistory.pflower.frontline.scene.GameScene;

public class GameLoopUpdateHandler implements IUpdateHandler{

	@Override
	public void onUpdate(float pSecondsElapsed) {
		((GameScene)BaseActivity.getSharedInstance().mCurrentScene).moveCharacter(pSecondsElapsed);
		((GameScene)BaseActivity.getSharedInstance().mCurrentScene).update(pSecondsElapsed);
		
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

}
