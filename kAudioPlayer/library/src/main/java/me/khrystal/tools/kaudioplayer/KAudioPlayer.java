package me.khrystal.tools.kaudioplayer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import java.io.Serializable;
import java.util.List;

import me.khrystal.tools.kaudioplayer.exceptions.AudioListNullPointerException;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/4/12
 * update time:
 * email: 723526676@qq.com
 */
public class KAudioPlayer {

    private KPlayerService kPlayerService;
    private KPlayerView.KPlayerViewServiceListener listener;
    private KPlayerView.OnInvalidPathListener invalidPathListener;
    private KPlayerView.AudioPlayerViewStatusListener statusListener;
    private KNotificationPlayerService kNotificationPlayerService;
    private List<Audio> playlist;
    private Audio currentAudio;
    private int currentPosList;
    private Context context;
    private static KAudioPlayer instance = null;
    private boolean mBound = false;
    private boolean playing;
    private boolean paused;
    private int position = 1;
    private ConnectedListener connectedListener;

    public KAudioPlayer(Context context, List<Audio> playlist, KPlayerView.KPlayerViewServiceListener listener) {
        this.context = context;
        this.playlist = playlist;
        this.listener = listener;
        instance = KAudioPlayer.this;
        this.kNotificationPlayerService = new KNotificationPlayerService(context);
        initService();
    }

    public void setInstance(KAudioPlayer instance) {
        this.instance = instance;
    }

    public void registerNotificationListener(KPlayerView.KPlayerViewServiceListener serviceListener) {
        this.listener = serviceListener;
        if (kNotificationPlayerService != null) {
            kPlayerService.registerNotificationListener(serviceListener);
        }
    }

    public void registerInvalidPathListener(KPlayerView.OnInvalidPathListener invalidPathListener) {
        this.invalidPathListener = invalidPathListener;
        if (kNotificationPlayerService != null) {
            kPlayerService.registerInvalidPathListener(invalidPathListener);
        }
    }

    public void registerServiceListener(KPlayerView.KPlayerViewServiceListener serviceListener) {
        this.listener = serviceListener;
        if (kPlayerService != null) {
            kPlayerService.registerServicePlayerListener(serviceListener);
        }
    }

    public void registerStatusListener(KPlayerView.AudioPlayerViewStatusListener statusListener) {
        this.statusListener = statusListener;
        if (kPlayerService != null) {
            kPlayerService.registerStatusListener(statusListener);
        }
    }

    public static KAudioPlayer getInstance() {
        return instance;
    }

    public void lazyPlayAudio(final Audio audio) {
        connectedListener = new ConnectedListener() {
            @Override
            public void connected() {
                playAudio(audio);
            }
        };
    }

    public void playAudio(Audio audio) {
        if (playlist == null || playlist.size() == 0) {
            throw new AudioListNullPointerException();
        }
        currentAudio = audio;
        kPlayerService.play(currentAudio);
        updatePositionAudioList();
        playing = true;
        paused = false;
    }

    private void initService() {
        if (!mBound) {
            startKPlayerService();
        } else {
            mBound = true;
        }
    }

    public void nextAudio() throws AudioListNullPointerException {
        if (playlist == null || playlist.size() == 0) {
            throw new AudioListNullPointerException();
        } else {
            if (currentAudio != null) {
                try {
                    Audio audio = playlist.get(currentPosList + position);
                    this.currentAudio = audio;
                    kPlayerService.stop();
                    kPlayerService.play(audio);
                } catch (IndexOutOfBoundsException e) {
                    playAudio(playlist.get(0));
                    e.printStackTrace();
                }
            }
            updatePositionAudioList();
            playing = true;
            paused = false;
        }
    }

    public void previousAudio() throws AudioListNullPointerException {
        if (playlist == null || playlist.size() == 0) {
            throw new AudioListNullPointerException();
        } else {
            if (currentAudio != null) {
                try {
                    Audio audio = playlist.get(currentPosList - position);
                    this.currentAudio = audio;
                    kPlayerService.stop();
                    kPlayerService.play(audio);
                } catch (IndexOutOfBoundsException e) {
                    playAudio(playlist.get(0));
                    e.printStackTrace();
                }
            }
            updatePositionAudioList();
            playing = true;
            paused = false;
        }
    }

    public void pauseAudio() {
        kPlayerService.pause(currentAudio);
        paused = true;
        playing = false;
    }

    public void continueAudio() throws AudioListNullPointerException {
        if (playlist == null || playlist.size() == 0) {
            throw new AudioListNullPointerException();
        } else {
            if (currentAudio == null) {
                currentAudio = playlist.get(0);
            }
            playAudio(currentAudio);
            playing = true;
            paused = false;
        }
    }

    public void createNewNotification(int iconResource) {
        if (currentAudio != null) {
            kNotificationPlayerService.createNotificationPlayer(currentAudio.getTitle(), iconResource);
        }
    }

    public void updateNotification() {
        kNotificationPlayerService.updateNotification();
    }

    public void seekTo(int time) {
        if (kPlayerService != null) {
            kPlayerService.seekTo(time);
        }
    }

    private void updatePositionAudioList() {
        for (int i = 0; i < playlist.size(); i++) {
            if (playlist.get(i).getId() == currentAudio.getId()) {
                this.currentPosList = i;
            }
        }
    }

    private synchronized void startKPlayerService() {
        if (!mBound) {
            Intent intent = new Intent(context.getApplicationContext(), KPlayerService.class);
            intent.putExtra(KNotificationPlayerService.PLAYLIST, (Serializable) playlist);
            intent.putExtra(KNotificationPlayerService.CURRENT_AUDIO, currentAudio);
            context.bindService(intent, mConnection, context.getApplicationContext().BIND_AUTO_CREATE);
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            KPlayerService.KPlayerServiceBinder binder = (KPlayerService.KPlayerServiceBinder) iBinder;
            kPlayerService = binder.getService();

            if (listener != null) {
                kPlayerService.registerServicePlayerListener(listener);
            }

            if (invalidPathListener != null) {
                kPlayerService.registerInvalidPathListener(invalidPathListener);
            }

            if (statusListener != null) {
                kPlayerService.registerStatusListener(statusListener);
            }

            if (connectedListener != null) {
                connectedListener.connected();
            }

            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBound = false;
            playing = false;
            paused = true;
        }
    };

    public boolean isPaused() {
        return paused;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void kill() {
        if (kPlayerService != null) {
            kPlayerService.stop();
            kPlayerService.destroy();
        }

        if (mBound) {
            try {
                context.unbindService(mConnection);
            } catch (IllegalArgumentException e) {
                // TODO: 19/4/12
            }
        }
        if (kNotificationPlayerService != null) {
            kNotificationPlayerService.destroyNotificationIfExists();
        }
        if (getInstance() != null) {
            KAudioPlayer.getInstance().setInstance(null);
        }
    }

    public List<Audio> getPlaylist() {
        return playlist;
    }

    public Audio getCurrentAudio() {
        return kPlayerService.getCurrentAudio();
    }

    interface ConnectedListener {
        void connected();
    }
}
