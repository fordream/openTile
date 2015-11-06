package com.tistory.pflower.epd.andEngineExtension;

import android.util.Log;

import org.andengine.util.modifier.ease.EaseLinear;
import org.andengine.util.modifier.ease.IEaseFunction;

/**
 * Created by My on 2015-04-05.
 */

public class EaseStep implements IEaseFunction {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    private static EaseStep INSTANCE;
    private static float percentPart = 0.01f;

    // ===========================================================
    // Constructors
    // ===========================================================

    private EaseStep() {

    }

    public static EaseStep getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new EaseStep();
        }
        return INSTANCE;
    }

    public void setPartPercent(final float pPercentPart) {
        if(percentPart <= 0)
            percentPart = 0.01f;
        percentPart = pPercentPart;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public float getPercentage(final float pSecondsElapsed, final float pDuration) {
        int currentPartNum = (int)((pSecondsElapsed / pDuration) / percentPart); // 현재 몇번쨰 파트?
        Log.d("EaseStep", "pSecondsElapsed : " + pSecondsElapsed + "pDuration : " + pDuration);
        Log.d("EaseStep", "Num : " + (pSecondsElapsed / pDuration) + "percentPart : " + percentPart);
        //Log.d("EaseStep", "ft(x) :" + (pDuration / percentPart));
        //return (pDuration / percentPart);

        if(currentPartNum % 2 == 0) { // 직선
            Log.d("EaseStep", "ft(x) :" + ((pDuration / percentPart) * currentPartNum));
            return (pDuration / percentPart) * currentPartNum;

        }
        else {
            float former = ( pDuration / (percentPart - 1) ) * (currentPartNum - 1);
            Log.d("EaseStep", "ft(x) :" + (former + (pSecondsElapsed - (pDuration / percentPart) * currentPartNum) / pDuration));
            return former + (pSecondsElapsed - former) / pDuration;
        }

    }

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
