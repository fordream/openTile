package com.wooseok.music;

import android.content.Context;

import com.leff.midi.MidiTrack;
import com.leff.midi.event.MidiEvent;
import com.leff.midi.event.NoteOn;

import org.andengine.util.call.Callable;

import java.util.Iterator;
import java.util.Random;

/**
 * Created by Wooseok on 2015-11-07.
 */
public class SimpleTrackPlayer implements Runnable{
    private Iterator<MidiEvent> it;
    private boolean playable = false;
    private long tick = 0;
    private int offset = 5;
    private SimpleNotePlayer simpleNotePlayer;
    private MidiEvent event;
    private float volume = 0.5f;
    private boolean finished = false;

    public void cleaer() {
        simpleNotePlayer.clear();
    }

    public SimpleTrackPlayer(Context ctx, MidiTrack midiTrack, int trackNo, float volume) {
        it = midiTrack.getEvents().iterator();
        this.volume = volume;
        Random random = new Random();
        if(it.hasNext())  {
            event= it.next();
            simpleNotePlayer = new SimpleNotePlayer(ctx, random.nextInt(40), trackNo);
            playable = true;
        }
    }

    public void playSound(int note, float volume, int length) {
        simpleNotePlayer.playSound(note, volume, length);
    }

    public synchronized void pushExplode() {

    }

    public void tickAndPlay() {
        if(playable) {
            tick+=offset;
            //If midi tick == My Tick, play a note!
            while(true) {

                if(event.getTick() - event.getDelta() <= tick + 5) {

                }

                if(event.getTick() - event.getDelta() <= tick) {
                    if(event instanceof NoteOn) {
                        NoteOn noteOn = (NoteOn)event;
                        simpleNotePlayer.playSound(noteOn.getNoteValue(), volume, 1);
                    }
                    if(it.hasNext()) {
                        event = it.next();
                    } else {
                        playable = false;
                        break;
                    }
                }else {
                    break;
                }
            }
        }
    }

    @Override
    public void run() {
        tickAndPlay();
    }
}
