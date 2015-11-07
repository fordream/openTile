package com.wooseok.music;

import android.content.Context;

import com.leff.midi.MidiTrack;
import com.leff.midi.event.MidiEvent;
import com.leff.midi.event.NoteOn;

import java.util.Iterator;

/**
 * Created by Wooseok on 2015-11-07.
 */
public class SimpleTrackPlayer {
    private Iterator<MidiEvent> it;
    private boolean playable = false;
    private long tick = 0;
    private int offset = 6;
    private SimpleNotePlayer simpleNotePlayer;
    private MidiEvent event;

    public SimpleTrackPlayer(Context ctx, MidiTrack midiTrack, int trackNo){
        it = midiTrack.getEvents().iterator();

        if(it.hasNext())  {
            event= it.next();
            simpleNotePlayer = new SimpleNotePlayer(ctx, 1, trackNo);
            playable = true;
        }
    }

    public void playSound(int note, float volume, int length) {
        simpleNotePlayer.playSound(note, volume, length);
    }

    public void tickAndPlay() {
        if(playable) {
            tick+=offset;
            //If midi tick == My Tick, play a note!
            while(true) {
                if(event.getTick() - event.getDelta() <= tick) {
                    if(event instanceof NoteOn) {
                        NoteOn noteOn = (NoteOn)event;
                        simpleNotePlayer.playSound(noteOn.getNoteValue(), 1.0f, 1);
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
}
