package com.tistory.pflower.epd.andEngineExtension;

import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.SingleValueSpanEntityModifier;

import java.util.Random;

public class SpeedXSwingModifier extends SingleValueSpanEntityModifier {
	
	private float mInitialX;
    private int mLevelPart;
    private float nextPercent;
    private PhysicsHandler mPhysicsHandler;
    private boolean isTowardCenter;
    private boolean isFacingLeft;


    private static final Random RANDOM = new Random(System.currentTimeMillis());

	public SpeedXSwingModifier(float pDuration, float pFromValue, float pToValue, int pLevelPart, boolean pIsLeft, PhysicsHandler physicsHandler) {
		super(pDuration, pFromValue, pToValue);
        mLevelPart = pLevelPart;
        mPhysicsHandler = physicsHandler;
        nextPercent = pLevelPart;
        isTowardCenter = false;
        isFacingLeft = pIsLeft;
    }

	@Override
	protected void onSetInitialValue(IEntity pItem, float pValue) {
		mInitialX = pItem.getX();
	}

	@Override
	protected void onSetValue(IEntity pItem, float pPercentageDone, float pValue) {
        if(nextPercent > pPercentageDone ) {
            nextPercent += mLevelPart;
            isTowardCenter = !isTowardCenter;
        }
        if(isTowardCenter) mPhysicsHandler.setVelocityX(RANDOM.nextInt() % 2 == 0 ? -50 : 50);
		//pItem.setX(mInitialX);
	}

	@Override
	public SpeedXSwingModifier deepCopy() throws DeepCopyNotSupportedException {
		throw new DeepCopyNotSupportedException();
	}

}
