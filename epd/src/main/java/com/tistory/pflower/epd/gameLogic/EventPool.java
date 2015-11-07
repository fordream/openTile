
package com.tistory.pflower.epd.gameLogic;

import java.util.LinkedList;

import org.andengine.util.adt.pool.GenericPool;


import android.util.Log;

public class EventPool extends GenericPool<EffectEvent> {


    static LinkedList<EffectEvent> list;

    public static EventPool instance;

    public static EventPool getSharedInstance() {

        if (instance == null)
            instance = new EventPool();
        return instance;

    }

    private EventPool() {
        super();

        list = new LinkedList<EffectEvent>();
    }

    @Override
    protected EffectEvent onAllocatePoolItem() {
        EffectEvent e = new EffectEvent();
        list.add(e);
        return e;
    }

    protected void onHandleRecycleItem(final EffectEvent e) {
        Log.v("Effect", "EffectPool onHandleRecycleItem()");
    }

    public static void clear() {
        for (EffectEvent e : list) {
            if(!e.equals(null))
                EventPool.getSharedInstance().recyclePoolItem(e);
        }
        list.clear();
    }
}