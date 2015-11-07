package com.wooseok.music;

import android.content.Context;
import android.util.Log;

import com.leff.midi.MidiTrack;
import com.leff.midi.event.MidiEvent;
import com.leff.midi.event.NoteOn;
import com.tistory.pflower.epd.gameLogic.TileManager;

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
    private boolean finished = true;
    private int InstruNum;

    public void cleaer() {
        if(simpleNotePlayer != null)
            simpleNotePlayer.clear();
    }

    public boolean isFinished() {
        return finished;
    }

    public void setInstruNum(int i)
    {
        this.InstruNum = i;
    }
    
    public SimpleTrackPlayer(Context ctx, MidiTrack midiTrack, int trackNo, float volume, int offset) {
        this.offset = offset;
        it = midiTrack.getEvents().iterator();
        this.volume = volume;
        Random random = new Random();
        if(it.hasNext())  {
            event= it.next();
            simpleNotePlayer = new SimpleNotePlayer(ctx, random.nextInt(30)+1, trackNo);
            playable = true;
            finished = false;
        }
    }

    public void playSound(int note, float volume, int length) {
        simpleNotePlayer.playSound(note, volume, length);
    }

    public synchronized void pushExplode(int insturID, int tone, float pExplodeSec) {
        TileManager.getSharedInstance().pushEvent(insturID, tone, pExplodeSec);
    }

    public void tickAndPlay() {
        if(playable) {
            tick+=offset;
            //If midi tick == My Tick, play a note!
            while(true) {

                if(event.getTick() - event.getDelta() <= tick + 15) {
                    
                    if(event instanceof NoteOn) {
                        NoteOn noteOn = (NoteOn)event;
                        simpleNotePlayer.playSound(noteOn.getNoteValue(), volume, 1);
                        pushExplode(InstruNum, noteOn.getNoteValue(), tick * 0.2f);
                    }

                }

                if(event.getTick() - event.getDelta() <= tick) {
                    //Log.d("Tick", "tick :" + tick + " + " + System.currentTimeMillis() );
                    if(event instanceof NoteOn) {
                        NoteOn noteOn = (NoteOn)event;
                        simpleNotePlayer.playSound(noteOn.getNoteValue(), volume, 1);
                    }
                    if(it.hasNext()) {
                        event = it.next();
                    } else {
                        playable = false;
                        finished = true;
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
