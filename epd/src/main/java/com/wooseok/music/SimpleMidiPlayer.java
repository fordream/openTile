package com.wooseok.music;

import android.content.Context;

import com.leff.midi.MidiFile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Wooseok on 2015-11-07.
 */
public class SimpleMidiPlayer {
    private static final int THREADCOUNT = 10;
    ArrayList<SimpleTrackPlayer> tracks;

    public SimpleMidiPlayer(Context ctx,MidiFile midiFile, float volume) {
        tracks = new ArrayList<SimpleTrackPlayer>();

        for(int i = 1; i < midiFile.getTrackCount() && i < THREADCOUNT; i++) {
            SimpleTrackPlayer tempSTP = new SimpleTrackPlayer(ctx, midiFile.getTracks().get(i), i, volume);
            tempSTP.playSound(12, volume, 1);
            tracks.add(tempSTP);
        }
    }

    public void tickAndPlay() {
        for(Iterator<SimpleTrackPlayer> it = tracks.iterator() ; !it.hasNext();) {
            SimpleTrackPlayer stp = it.next();
            if(stp.isFinished()) {
                stp.cleaer();
                tracks.remove(it);
            }else {
                stp.run();
            }
        }
    }
}
