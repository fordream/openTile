package com.tistory.pflower.epd.gameLogic;

class EffectEvent{
    int insturNum;
    int tone;
    float pExplodeSec;
    public EffectEvent()
    {
        this.insturNum = 0;
        this.tone = 0;
        this.pExplodeSec = 0;
    }

    public void init(int insturNum, int tone, float pExplodeSec)
    {
        this.insturNum = insturNum;
        this.tone = tone;
        this.pExplodeSec = pExplodeSec;
    }
};
