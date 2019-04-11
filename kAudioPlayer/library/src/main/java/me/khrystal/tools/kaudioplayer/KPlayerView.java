package me.khrystal.tools.kaudioplayer;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * usage: AudioPlayer widget
 * author: kHRYSTAL
 * create time: 19/4/10
 * update time:
 * email: 723526676@qq.com
 */
public class KPlayerView extends LinearLayout implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private static final String TAG = KPlayerView.class.getSimpleName();

    private static final int PULSE_ANIM_DURATION = 101;
    private static final int TITLE_ANIM_DURATION = 202;

    private TextView tvAudioTitle;
    private ImageButton btnPrev;
    private ImageButton btnNext;
    private ImageButton btnPlay;
    private ProgressBar progressBar;
    private TextView tvTotalDuration;
    private TextView tvCurrentDuration;
    private SeekBar seekBar;
    private boolean isInit;

    private OnInvalidPathListener onInvalidPathListener = new OnInvalidPathListener() {
        @Override
        public void onPathError(Audio audio) {
            // TODO: 19/4/11
        }
    };

    // TODO
    KPlayerViewServiceListener kPlayerViewServiceListener = new KPlayerViewServiceListener() {
        @Override
        public void onPreparedAudio(String audioName, int duration) {

        }

        @Override
        public void onCompletedAudio() {

        }

        @Override
        public void onPaused() {

        }

        @Override
        public void onContinueAudio() {

        }

        @Override
        public void onPlaying() {

        }

        @Override
        public void onTimeChanged(long currentTime) {

        }

        @Override
        public void updateTitle(String title) {

        }
    };

    public KPlayerView(Context context) {
        super(context);
        init();
    }

    public KPlayerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public KPlayerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public KPlayerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.layout_kplayer, this);
        this.progressBar = findViewById(R.id.progressBarPlay);
        this.btnNext = findViewById(R.id.btnNext);
        this.btnPrev = findViewById(R.id.btnPrev);
        this.btnPlay = findViewById(R.id.btnPlay);

        this.tvTotalDuration = findViewById(R.id.tvTotalDuration);
        this.tvCurrentDuration = findViewById(R.id.tvCurrentDuration);
        this.tvAudioTitle = findViewById(R.id.tvAudioTitle);
        this.seekBar = findViewById(R.id.seekBar);

        this.btnPlay.setTag(R.drawable.ic_pause_black);

        btnNext.setOnClickListener(this);
        btnPrev.setOnClickListener(this);
        btnPlay.setOnClickListener(this);

        seekBar.setOnSeekBarChangeListener(this);

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public interface OnInvalidPathListener {
        void onPathError(Audio audio);
    }

    public interface AudioPlayerViewStatusListener {
        void onPausedStatus(AudioStatus status);

        void onContinueAudioStatus(AudioStatus status);

        void onPlayingStatus(AudioStatus status);

        void onTimeChangedStatus(AudioStatus status);

        void onCompletedAudioStatus(AudioStatus status);

        void onPreparedAudioStatus(AudioStatus status);
    }

    public interface KPlayerViewServiceListener {
        void onPreparedAudio(String audioName, int duration);

        void onCompletedAudio();

        void onPaused();

        void onContinueAudio();

        void onPlaying();

        void onTimeChanged(long currentTime);

        void updateTitle(String title);
    }
}
