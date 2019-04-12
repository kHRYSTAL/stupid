package me.khrystal.tools.kaudioplayer;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;
import java.util.List;

import me.khrystal.tools.kaudioplayer.exceptions.AudioListNullPointerException;

/**
 * usage: AudioPlayer widget
 * author: kHRYSTAL
 * create time: 19/4/10
 * update time:
 * email: 723526676@qq.com
 */
public class KPlayerView extends LinearLayout implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private static final String TAG = KPlayerView.class.getSimpleName();

    private static final int PULSE_ANIM_DURATION = 200;
    private static final int TITLE_ANIM_DURATION = 600;

    private TextView tvAudioTitle;
    private ImageButton btnPrev;
    private ImageButton btnNext;
    private ImageButton btnPlay;
    private ProgressBar progressBar;
    private KAudioPlayer kAudioPlayer;
    private TextView tvTotalDuration;
    private TextView tvCurrentDuration;
    private SeekBar seekBar;
    private boolean isInit;

    private OnInvalidPathListener onInvalidPathListener = new OnInvalidPathListener() {
        @Override
        public void onPathError(Audio audio) {
            dismissProgressBar();
        }
    };

    // TODO
    KPlayerViewServiceListener kPlayerViewServiceListener = new KPlayerViewServiceListener() {
        @Override
        public void onPreparedAudio(String audioName, int duration) {
            dismissProgressBar();
            resetPlayerInfo();
            long aux = duration / 1000;
            int minute = (int) (aux / 60);
            int second = (int) (aux % 60);

            final String sDuration = (minute < 10 ? "0" + minute : minute + "") // minute
                    + ":" + (second < 10 ? "0" + second : second + ""); // second
            seekBar.setMax(duration);
            tvTotalDuration.post(new Runnable() {
                @Override
                public void run() {
                    tvTotalDuration.setText(sDuration);
                }
            });
        }

        @Override
        public void onCompletedAudio() {
            resetPlayerInfo();
            try {
                kAudioPlayer.nextAudio();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onPaused() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                btnPlay.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_play_black, null));
            } else {
                btnPlay.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_play_black, null));
            }
            btnPlay.setTag(R.drawable.ic_play_black);
        }

        @Override
        public void onContinueAudio() {
            dismissProgressBar();
        }

        @Override
        public void onPlaying() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                btnPlay.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_pause_black, null));
            } else {
                btnPlay.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_pause_black, null));
            }
            btnPlay.setTag(R.drawable.ic_pause_black);
        }

        @Override
        public void onTimeChanged(long currentTime) {
            long aux = currentTime / 1000;
            int minutes = (int) (aux / 60);
            int seconds = (int) (aux % 60);
            final String sMinutes = minutes < 10 ? "0" + minutes : minutes + "";
            final String sSeconds = seconds < 10 ? "0" + seconds : seconds + "";

            seekBar.setProgress((int) currentTime);
            tvCurrentDuration.post(new Runnable() {
                @Override
                public void run() {
                    tvCurrentDuration.setText(String.valueOf(sMinutes + ":" + sSeconds));
                }
            });
        }

        @Override
        public void updateTitle(final String title) {
            YoYo.with(Techniques.FadeInLeft)
                    .duration(TITLE_ANIM_DURATION)
                    .playOn(tvAudioTitle);
            tvAudioTitle.post(new Runnable() {
                @Override
                public void run() {
                    tvAudioTitle.setText(title);
                }
            });
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

    /**
     * initialize the playlist and controls
     *
     * @param playlist
     */
    public void initPlaylist(List<Audio> playlist) {
        if (!isAlreadySorted(playlist)) {
            sortPlaylist(playlist);
        }
        kAudioPlayer = new KAudioPlayer(getContext(), playlist, kPlayerViewServiceListener);
        kAudioPlayer.registerInvalidPathListener(onInvalidPathListener);
        isInit = true;
    }

    /**
     * initialize an anonymous playlist with a default KPlayer title for all audios
     *
     * @param playlist
     */
    public void initAnonPlaylist(List<Audio> playlist) {
        sortPlaylist(playlist);
        generateTitleAudio(playlist, getContext().getString(R.string.track_number));
        kAudioPlayer = new KAudioPlayer(getContext(), playlist, kPlayerViewServiceListener);
        kAudioPlayer.registerInvalidPathListener(onInvalidPathListener);
        isInit = true;
    }

    public void initWithTitlePlaylist(List<Audio> playlist, String title) {
        sortPlaylist(playlist);
        generateTitleAudio(playlist, title);
        kAudioPlayer = new KAudioPlayer(getContext(), playlist, kPlayerViewServiceListener);
        kAudioPlayer.registerInvalidPathListener(onInvalidPathListener);
        isInit = true;
    }

    /**
     * Add an audio for the playlist We can track the Audio by
     * its id. So here we returning its id after adding to list
     *
     * @param audio
     * @return
     */
    public long addAudio(Audio audio) {
        createAudioPlayer();
        List<Audio> playlist = kAudioPlayer.getPlaylist();
        int lastPosition = playlist.size();

        audio.setId(lastPosition + 1);
        audio.setPosition(lastPosition + 1);

        if (!playlist.contains(audio)) {
            playlist.add(lastPosition, audio);
        }
        return audio.getId();
    }

    public void removeAudio(Audio audio) {
        if (kAudioPlayer != null) {
            List<Audio> playlist = kAudioPlayer.getPlaylist();
            if (playlist != null && playlist.contains(audio)) {
                if (playlist.size() > 1) {
                    // play next audio when currently played audio is removed.
                    if (kAudioPlayer.isPlaying()) {
                        if (kAudioPlayer.getCurrentAudio().equals(audio)) {
                            playlist.remove(audio);
                            pause();
                            resetPlayerInfo();
                        } else {
                            playlist.remove(audio);
                        }
                    }
                } else {
                    playlist.remove(audio);
                }
            } else {
                // TODO: 19/4/12 Maybe need #stopPlay() for stopping the player;
                playlist.remove(audio);
                pause();
                resetPlayerInfo();
            }
        }
    }

    public void playAudio(Audio audio) {
        showProgressBar();
        createAudioPlayer();
        if (!kAudioPlayer.getPlaylist().contains(audio)) {
            kAudioPlayer.getPlaylist().add(audio);
        }

        try {
            kAudioPlayer.playAudio(audio);
        } catch (AudioListNullPointerException e) {
            dismissProgressBar();
            e.printStackTrace();
        } catch (NullPointerException e) {
            // Service is not bounded yet
            kAudioPlayer.lazyPlayAudio(audio);
        }
    }

    public void next() {
        if (kAudioPlayer.getCurrentAudio() == null) {
            return;
        }
        resetPlayerInfo();
        showProgressBar();
        try {
            kAudioPlayer.nextAudio();
        } catch (Exception e) {
            dismissProgressBar();
            e.printStackTrace();
        }
    }

    public void continueAudio() {
        showProgressBar();
        try {
            kAudioPlayer.continueAudio();
        } catch (AudioListNullPointerException e) {
            dismissProgressBar();
            e.printStackTrace();
        }
    }

    public void pause() {
        kAudioPlayer.pauseAudio();
    }

    public void previous() {
        resetPlayerInfo();
        showProgressBar();
        try {
            kAudioPlayer.previousAudio();
        } catch (Exception e) { // AudioListNullPointerException
            dismissProgressBar();
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        if (isInit) {
            if (view.getId() == R.id.btnPlay) {
                YoYo.with(Techniques.Pulse)
                        .duration(PULSE_ANIM_DURATION)
                        .playOn(btnPlay);
                if (btnPlay.getTag().equals(R.drawable.ic_pause_black)) {
                    pause();
                } else {
                    continueAudio();
                }
            }
        }

        if (view.getId() == R.id.btnNext) {
            YoYo.with(Techniques.Pulse)
                    .duration(PULSE_ANIM_DURATION)
                    .playOn(btnNext);
            next();
        }
        if (view.getId() == R.id.btnPrev) {
            YoYo.with(Techniques.Pulse)
                    .duration(PULSE_ANIM_DURATION)
                    .playOn(btnPrev);
            previous();
        }
    }

    /**
     * Create a notification player with same playlist with a custom icon
     *
     * @param iconResource icon path
     */
    public void createNotification(int iconResource) {
        if (kAudioPlayer != null) kAudioPlayer.createNewNotification(iconResource);
    }

    public void createNotification() {
        if (kAudioPlayer != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // for light theme
                kAudioPlayer.createNewNotification(R.drawable.ic_notification_default_black);
            } else {
                // for dark theme
                kAudioPlayer.createNewNotification(R.drawable.ic_notification_default_white);
            }
        }
    }

    public List<Audio> getMyPlaylist() {
        return kAudioPlayer.getPlaylist();
    }

    public boolean isPlaying() {
        return kAudioPlayer.isPlaying();
    }

    public boolean isPaused() {
        return kAudioPlayer.isPaused();
    }

    private void createAudioPlayer() {
        if (kAudioPlayer == null) {
            List<Audio> playlist = new ArrayList<>();
            kAudioPlayer = new KAudioPlayer(getContext(), playlist, kPlayerViewServiceListener);
        }
        kAudioPlayer.registerInvalidPathListener(onInvalidPathListener);
        isInit = true;
    }

    private void sortPlaylist(List<Audio> playlist) {
        for (int i = 0; i < playlist.size(); i++) {
            Audio audio = playlist.get(i);
            audio.setId(i);
            audio.setPosition(i);
        }
    }

    /**
     * Check if playlist already sorted or not
     * We need to check because there is a possibility that the user reload previous playlist
     * from persistence storage like sharedPreference or SQLite
     *
     * @param playlist playlist list of Audio
     * @return true if sorted
     */
    private boolean isAlreadySorted(List<Audio> playlist) {
        // If there is position in the first audio, then playlist is already sorted.
        if (playlist != null) {
            if (playlist.get(0).getPosition() != -1) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    private void generateTitleAudio(List<Audio> playlist, String title) {
        for (int i = 0; i < playlist.size(); i++) {
            if (title.equals(getContext().getString(R.string.track_number))) {
                playlist.get(i).setTitle(getContext().getString(R.string.track_number) + " " + String.valueOf(i + 1));
            } else {
                playlist.get(i).setTitle(title);
            }
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
        if (fromUser && kAudioPlayer != null)
            kAudioPlayer.seekTo(i);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        showProgressBar();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        dismissProgressBar();
    }

    private void showProgressBar() {
        progressBar.setVisibility(VISIBLE);
        btnPlay.setVisibility(GONE);
        btnNext.setClickable(false);
        btnPrev.setClickable(false);
    }

    private void dismissProgressBar() {
        progressBar.setVisibility(GONE);
        btnPlay.setVisibility(VISIBLE);
        btnNext.setClickable(true);
        btnPrev.setClickable(true);
    }

    private void resetPlayerInfo() {
        seekBar.setProgress(0);
        tvAudioTitle.setText("");
        tvCurrentDuration.setText(getContext().getString(R.string.play_initial_time));
        tvTotalDuration.setText(getContext().getString(R.string.play_initial_time));
    }

    public void registerInvalidPathListener(OnInvalidPathListener registerInvalidPathListener) {
        if (kAudioPlayer != null) {
            kAudioPlayer.registerInvalidPathListener(registerInvalidPathListener);
        }
    }

    public void kill() {
        if (kAudioPlayer != null)
            kAudioPlayer.kill();
    }

    public void registerServiceListener(KPlayerViewServiceListener serviceListener) {
        if (kAudioPlayer != null)
            kAudioPlayer.registerServiceListener(serviceListener);
    }

    public void registerStatusListener(AudioPlayerViewStatusListener statusListener) {
        if (kAudioPlayer != null)
            kAudioPlayer.registerStatusListener(statusListener);
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
