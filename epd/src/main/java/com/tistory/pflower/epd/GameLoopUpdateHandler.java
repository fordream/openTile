package com.tistory.pflower.epd;

import com.tistory.pflower.epd.scene.GameScene;

import org.andengine.engine.handler.IUpdateHandler;

/**
 * Created by My on 2015-04-02.
 */
public class GameLoopUpdateHandler implements IUpdateHandler {
    @Override
    public void onUpdate(float pSecondsElapsed) {
        ((GameScene)BaseActivity.getSharedInstance().mCurrentScene).update(pSecondsElapsed);

    }

    @Override
    public void reset() {
        // TODO Auto-generated method stub

    }
}
