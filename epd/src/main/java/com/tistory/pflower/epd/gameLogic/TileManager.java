package com.tistory.pflower.epd.gameLogic;

import com.tistory.pflower.epd.layer.TileLayer;

import java.util.Queue;

/**
 * Created by 2012105064 on 2015-11-07.
 */
public class TileManager {

    public static TileManager instance = null;

    public Queue<EffectEvent> eventQueue;
    public static float mSumElapsed;

    public TileManager() {
        reset();
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

    public void tick(float pElapsedSecond) {

        mSumElapsed += pElapsedSecond;

        EffectEvent e = eventQueue.peek();

        while(e != null && eventQueue.peek().pExplodeSec > pElapsedSecond) {
            EffectEvent ee = eventQueue.poll();
            TileLayer.getInstance().At(ee.insturNum).setExplosion(ee.insturNum, 3.0f);
        }
    }

    public void pushEvent(int insturID, int tone, float pExplodeSec) {

        EffectEvent e = EventPool.getSharedInstance().obtainPoolItem();
        e.init(insturID, tone, pExplodeSec);
        eventQueue.add( e );
    }
}
