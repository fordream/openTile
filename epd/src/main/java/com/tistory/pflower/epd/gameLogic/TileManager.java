package com.tistory.pflower.epd.gameLogic;

import android.support.annotation.NonNull;
import android.util.Log;

import com.tistory.pflower.epd.layer.TileLayer;
import com.wooseok.music.SimpleNotePlayer;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 * Created by 2012105064 on 2015-11-07.
 */
public class TileManager {

    public static TileManager instance = null;

    public static float mSumElapsed;

    public Random rnd = new Random(System.currentTimeMillis());

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

        EffectEvent e = eventQueue.poll();

        while(e != null && e.pExplodeSec < mSumElapsed) {
            //Log.d("popEvent", e.pExplodeSec + " " + mSumElapsed);

            int i = (rnd.nextInt(16) + 1) % 16;
            int j = i - 1;
            while(i != j) {
                if(!TileLayer.getInstance().At(i).effect.isVisible()) break;
                i = (i + 1) % 16;
            }
            TileLayer.getInstance().At(i).setExplosion(e, 3.0f);
            e = eventQueue.poll();
        }
    }

    public void pushEvent(int insturID, int tone, float pExplodeSec, SimpleNotePlayer simpleNotePlayer) {

        //Log.d("pushEvent", insturID + " " + tone + " " + pExplodeSec);

        EffectEvent e = EventPool.getSharedInstance().obtainPoolItem();
        e.init(insturID, tone, pExplodeSec, simpleNotePlayer);
        eventQueue.add( e );
    }
}
