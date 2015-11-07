package com.tistory.pflower.epd.gameLogic;

import com.wooseok.music.SimpleNotePlayer;

public class EffectEvent{
    public int insturNum;
    int tone;
    float pExplodeSec;
    public SimpleNotePlayer simpleNotePlayer;
    public EffectEvent()
    {
        this.insturNum = 0;
        this.tone = 0;
        this.pExplodeSec = 0;
    }

    public void init(int insturNum, int tone, float pExplodeSec, SimpleNotePlayer simpleNotePlayer)
    {
        this.insturNum = insturNum;
        this.tone = tone;
        this.pExplodeSec = pExplodeSec;
        this.simpleNotePlayer = simpleNotePlayer;
    }

    public void play()
    {
        simpleNotePlayer.playSound(tone, 0.5f, 1);
    }
};
