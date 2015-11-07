package com.tistory.pflower.epd.gameLogic;

import android.support.annotation.NonNull;
import android.util.Log;

import com.tistory.pflower.epd.layer.TileLayer;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by 2012105064 on 2015-11-07.
 */
public class TileManager {

    public static TileManager instance = null;

    public static float mSumElapsed;

    Queue<EffectEvent> eventQueue;

    public TileManager() {
        reset();
        eventQueue = new LinkedList<EffectEvent>();
    }


    public static void reset()
    {
        mSumElapsed = 0.0f;
    }

    public static TileManager getSharedInstance() {
        if(instance == null)
        {
            instance = new TileManager();
        }
        return instance;
    }

    public synchronized void tick(float pElapsedSecond) {

        mSumElapsed += pElapsedSecond;

        EffectEvent e = eventQueue.peek();

        while(e != null && eventQueue.peek().pExplodeSec > pElapsedSecond) {
            EffectEvent ee = eventQueue.poll();
            TileLayer.getInstance().At(ee.insturNum).setExplosion(ee.insturNum, 3.0f);
        }
    }

    public void pushEvent(int insturID, int tone, float pExplodeSec) {

        Log.d("pushEvent", insturID + " " + tone + " " + pExplodeSec);

        EffectEvent e = EventPool.getSharedInstance().obtainPoolItem();
        e.init(insturID, tone, pExplodeSec);
        eventQueue.add( e );
    }
}
