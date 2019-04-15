package me.khrystal.kaudioplayersample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import me.khrystal.tools.kaudioplayer.Audio;
import me.khrystal.tools.kaudioplayer.AudioStatus;
import me.khrystal.tools.kaudioplayer.KPlayerView;

public class MainActivity extends AppCompatActivity implements KPlayerView.AudioPlayerViewStatusListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private KPlayerView kPlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        kPlayerView = findViewById(R.id.kPlayer);
        kPlayerView.playAudio(Audio.createFromURL("江南春", "http://static.zhisland.com/mp3/jiangnanchun4.mp3"));
        kPlayerView.registerStatusListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        kPlayerView.createNotification();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        kPlayerView.kill();
    }

    @Override
    public void onPausedStatus(AudioStatus status) {

    }

    @Override
    public void onContinueAudioStatus(AudioStatus status) {

    }

    @Override
    public void onPlayingStatus(AudioStatus status) {

    }

    @Override
    public void onTimeChangedStatus(AudioStatus status) {
        Log.e(TAG, String.valueOf(status.getCurrentPos()));
    }

    @Override
    public void onCompletedAudioStatus(AudioStatus status) {
        Log.e(TAG, String.valueOf(status.getCurrentPos()));
    }

    @Override
    public void onPreparedAudioStatus(AudioStatus status) {

    }
}
