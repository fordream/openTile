package com.tistory.pflower.epd.andEngineExtension;

import org.andengine.entity.IEntity;
import org.andengine.entity.particle.Particle;
import org.andengine.entity.particle.initializer.IParticleInitializer;

public class RegisterRandomXSwingEntityModifierInitializer<T extends IEntity> implements
		IParticleInitializer<T> {
	
	private float mDuration;
	private float mFromValue;
	private float mToValue;
    private int mLevelPart;
    private boolean mIsFacingLeft;

	public RegisterRandomXSwingEntityModifierInitializer(float pDuration, float pFromValue, float pToValue, int pLevelPart, boolean isFacingLeft) {
		mDuration = pDuration;
		mFromValue = pFromValue;
		mToValue = pToValue;
        mLevelPart = pLevelPart;
        mIsFacingLeft = isFacingLeft;
	}

	@Override
	public void onInitializeParticle(Particle<T> pParticle) {
		pParticle.getEntity().registerEntityModifier(
			new SpeedXSwingModifier(mDuration,
					mFromValue, mToValue, mLevelPart, mIsFacingLeft, pParticle.getPhysicsHandler() ));
	}

}
