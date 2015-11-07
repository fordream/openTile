package com.wooseok.music;

import android.content.Context;
import android.util.Log;

import com.leff.midi.MidiFile;
import com.leff.midi.MidiTrack;
import com.leff.midi.event.MidiEvent;
import com.leff.midi.event.meta.Tempo;

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
        MidiTrack mt = midiFile.getTracks().get(0);
        Iterator<MidiEvent> it = mt.getEvents().iterator();
        float bpm = 120;

        while(it.hasNext()) {
            MidiEvent me = it.next();
            if(me instanceof Tempo) {
                Tempo t = (Tempo)me;
                bpm = t.getBpm();
            }
        }
        tracks = new ArrayList<SimpleTrackPlayer>();
        int offset = 1600 / (int)bpm;

        for(int i = 1; i < midiFile.getTrackCount() && i < THREADCOUNT; i++) {
            SimpleTrackPlayer tempSTP = new SimpleTrackPlayer(ctx, midiFile.getTracks().get(i), i, volume, offset);
            tempSTP.playSound(12, 0, 1);
            tracks.add(tempSTP);
            tempSTP.setInstruNum(i);
        }
    }

    public void tickAndPlay(float pSecondsElapsed) {
        for(int i = 0; i < tracks.size(); i++) {
            SimpleTrackPlayer stp = tracks.get(i);
            if(stp.isFinished()) {
                stp.cleaer();
                tracks.remove(i--);
            }else {
                stp.run();
            }
        }
    }
}
