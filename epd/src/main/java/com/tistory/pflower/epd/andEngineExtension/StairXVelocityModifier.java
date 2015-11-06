package com.tistory.pflower.epd.andEngineExtension;

import android.util.Log;

import org.andengine.entity.IEntity;
import org.andengine.entity.particle.Particle;
import org.andengine.entity.particle.modifier.BaseSingleValueSpanParticleModifier;
import org.andengine.entity.particle.modifier.IParticleModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.util.modifier.ease.EaseLinear;
import org.andengine.util.modifier.ease.IEaseFunction;

/**
 * Created by My on 2015-04-05.
 */

public class StairXVelocityModifier<T extends IEntity> extends BaseSingleValueSpanParticleModifier<T> {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    private float mLevelPart;
    private float mBaseX;

    // ===========================================================
    // Constructors
    // ===========================================================

    public StairXVelocityModifier(float pFromTime, float pToTime, float pFromX, float pToX, float pLevelPart) {
        this(pFromTime, pToTime, pToX, pFromX, pLevelPart, EaseStep.getInstance());
    }

    public StairXVelocityModifier(final float pFromTime, final float pToTime, float pFromX, final float pToX, float pLevelPart, final IEaseFunction pEaseFunction) {
        super(pFromTime, pToTime, pFromX, pToX, pEaseFunction);
        EaseStep.getInstance().setPartPercent(pLevelPart);
        mLevelPart = pLevelPart;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    protected void onSetInitialValue(final Particle<T> pParticle, final float pX) {
        mBaseX = pX;
        //Log.d("paticle", "px : " + pX);
        pParticle.getEntity().setX(pX);
    }

    @Override
    protected void onSetValue(final Particle<T> pParticle, final float pPercentageDone, final float pX) {
        //Log.d("paticle", "px : " + pX);
        pParticle.getEntity().setX(pX);
    }

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
