package me.khrystal.tools.kaudioplayer;

import android.support.v4.app.DialogFragment;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/4/10
 * update time:
 * email: 723526676@qq.com
 */
public class AudioStatus {
    enum State {
        PLAY, PAUSE, STOP, UNINIT
    }

    private Audio audio;
    private long duration;
    private long currentPos;
    private State state;

    public AudioStatus() {
        this(null, 0, 0, State.UNINIT);
    }

    public AudioStatus(Audio audio, long duration, long currentPos, State state) {
        this.audio = audio;
        this.duration = duration;
        this.currentPos = currentPos;
        this.state = state;
    }

    public Audio getAudio() {
        return audio;
    }

    public void setAudio(Audio audio) {
        this.audio = audio;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getCurrentPos() {
        return currentPos;
    }

    public void setCurrentPos(long currentPos) {
        this.currentPos = currentPos;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
