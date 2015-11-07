package com.wooseok.music;

import android.content.Context;

import com.leff.midi.MidiFile;

import java.util.ArrayList;
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
            tempSTP.playSound(12, 1.0f, 1);
            tracks.add(tempSTP);
        }
    }

    public void gogo() {
        for(SimpleTrackPlayer stp : tracks) {
            stp.run();
        }
    }
}
