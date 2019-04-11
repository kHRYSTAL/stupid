package me.khrystal.tools.kaudioplayer;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.khrystal.tools.kaudioplayer.exceptions.AudioAssetsInvalidException;
import me.khrystal.tools.kaudioplayer.exceptions.AudioFilePathInvalidException;
import me.khrystal.tools.kaudioplayer.exceptions.AudioRawInvalidException;
import me.khrystal.tools.kaudioplayer.exceptions.AudioUrlInvalidException;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/4/11
 * update time:
 * email: 723526676@qq.com
 */
public class KPlayerService extends Service implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener {

    private static final String TAG = KPlayerService.class.getSimpleName();
    private final IBinder mBinder = new KPlayerServiceBinder();
    private MediaPlayer mediaPlayer;
    private boolean isPlaying;
    private int duration;
    private int currentTime;
    private Audio currentAudio;
    private AudioStatus status = new AudioStatus();
    private List<KPlayerView.KPlayerViewServiceListener> kPlayerViewServiceListeners;
    private List<KPlayerView.OnInvalidPathListener> invalidPathListeners;
    private List<KPlayerView.AudioPlayerViewStatusListener> audioPlayerViewStatusListeners;
    private KPlayerView.KPlayerViewServiceListener notificationListener;
    private AssetFileDescriptor assetFileDescriptor = null; // for asset and raw file;
    private Audio tempAudio;

    public class KPlayerServiceBinder extends Binder {
        public KPlayerService getService() {
            return KPlayerService.this;
        }
    }

    public void registerNotificationListener(KPlayerView.KPlayerViewServiceListener notificationListener) {
        this.notificationListener = notificationListener;
    }

    public void registerServicePlayerListener(KPlayerView.KPlayerViewServiceListener kPlayerViewServiceListener) {
        if (kPlayerViewServiceListeners == null) {
            kPlayerViewServiceListeners = new ArrayList<>();
        }
        if (!kPlayerViewServiceListeners.contains(kPlayerViewServiceListener)) {
            kPlayerViewServiceListeners.add(kPlayerViewServiceListener);
        }
    }

    public void registerInvalidPathListener(KPlayerView.OnInvalidPathListener invalidPathListener) {
        if (invalidPathListeners == null) {
            invalidPathListeners = new ArrayList<>();
        }
        if (!invalidPathListeners.contains(invalidPathListener)) {
            invalidPathListeners.add(invalidPathListener);
        }
    }

    public void registerStatusListener(KPlayerView.AudioPlayerViewStatusListener statusListener) {
        if (audioPlayerViewStatusListeners == null) {
            audioPlayerViewStatusListeners = new ArrayList<>();
        }
        if (!audioPlayerViewStatusListeners.contains(statusListener)) {
            audioPlayerViewStatusListeners.add(statusListener);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public KPlayerService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    public void pause(Audio audio) {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            duration = mediaPlayer.getDuration();
            currentTime = mediaPlayer.getCurrentPosition();
            isPlaying = false;
        }

        for (KPlayerView.KPlayerViewServiceListener kPlayerViewServiceListener : kPlayerViewServiceListeners) {
            kPlayerViewServiceListener.onPaused();
        }
        if (notificationListener != null) {
            notificationListener.onPaused();
        }
        if (audioPlayerViewStatusListeners != null) {
            for (KPlayerView.AudioPlayerViewStatusListener audioPlayerViewStatusListener : audioPlayerViewStatusListeners) {
                status.setAudio(audio);
                status.setDuration(duration);
                status.setCurrentPos(currentTime);
                status.setState(AudioStatus.State.PAUSE);
                audioPlayerViewStatusListener.onPausedStatus(status);
            }
        }
    }

    public void destroy() {
        stop();
        stopSelf();
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        isPlaying = false;
    }

    public void play(Audio audio) {
        tempAudio = this.currentAudio;
        this.currentAudio = audio;
        if (isAudioFileValid(audio.getPath(), audio.getSource())) {
            try {
                if (mediaPlayer == null) {
                    mediaPlayer = new MediaPlayer();

                    if (audio.getSource() == Source.URL) {
                        mediaPlayer.setDataSource(audio.getPath());
                    } else if (audio.getSource() == Source.RAW) {
                        assetFileDescriptor = getApplicationContext().getResources().openRawResourceFd(Integer.parseInt(audio.getPath()));
                        if (assetFileDescriptor == null) return; // TODO need throw error
                        mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(),
                                assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
                        assetFileDescriptor.close();
                        assetFileDescriptor = null;
                    } else if (audio.getSource() == Source.ASSETS) {
                        assetFileDescriptor = getApplicationContext().getAssets().openFd(audio.getPath());
                        mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(),
                                assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
                        assetFileDescriptor.close();
                        assetFileDescriptor = null;
                    } else if (audio.getSource() == Source.FILE_PATH) {
                        mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(audio.getPath()));
                    }

                    mediaPlayer.prepareAsync();
                    mediaPlayer.setOnPreparedListener(this);
                    mediaPlayer.setOnBufferingUpdateListener(this);
                    mediaPlayer.setOnCompletionListener(this);
                    mediaPlayer.setOnErrorListener(this);
                } else {
                    if (isPlaying) {
                        stop();
                        play(audio);
                    } else {
                        if (tempAudio != audio) {
                            stop();
                            play(audio);
                        } else {
                            mediaPlayer.start();
                            isPlaying = true;

                            if (kPlayerViewServiceListeners != null) {
                                for (KPlayerView.KPlayerViewServiceListener kPlayerViewServiceListener : kPlayerViewServiceListeners) {
                                    kPlayerViewServiceListener.onContinueAudio();
                                }
                            }
                            if (audioPlayerViewStatusListeners != null) {
                                for (KPlayerView.AudioPlayerViewStatusListener audioPlayerViewStatusListener : audioPlayerViewStatusListeners) {
                                    status.setAudio(audio);
                                    status.setState(AudioStatus.State.PLAY);
                                    status.setDuration(mediaPlayer.getDuration());
                                    status.setCurrentPos(mediaPlayer.getCurrentPosition());
                                    audioPlayerViewStatusListener.onContinueAudioStatus(status);
                                }
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            updateTimeAudio();
            for (KPlayerView.KPlayerViewServiceListener serviceListener : kPlayerViewServiceListeners) {
                serviceListener.onPlaying();
            }
            if (audioPlayerViewStatusListeners != null) {
                for (KPlayerView.AudioPlayerViewStatusListener audioPlayerViewStatusListener : audioPlayerViewStatusListeners) {
                    status.setAudio(audio);
                    status.setState(AudioStatus.State.PLAY);
                    status.setDuration(0);
                    status.setCurrentPos(0);
                    audioPlayerViewStatusListener.onPlayingStatus(status);
                }
            }

            if (notificationListener != null)
                notificationListener.onPlaying();
        } else {
            throwError(audio.getPath(), audio.getSource());
        }
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
        isPlaying = true;
        this.duration = mediaPlayer.getDuration();
        this.currentTime = mediaPlayer.getCurrentPosition();
        updateTimeAudio();
        if (kPlayerViewServiceListeners != null) {
            for (KPlayerView.KPlayerViewServiceListener serviceListener : kPlayerViewServiceListeners) {
                serviceListener.updateTitle(currentAudio.getTitle());
                serviceListener.onPreparedAudio(currentAudio.getTitle(), mediaPlayer.getDuration());
            }
        }

        if (notificationListener != null) {
            notificationListener.updateTitle(currentAudio.getTitle());
            notificationListener.onPreparedAudio(currentAudio.getTitle(), mediaPlayer.getDuration());
        }

        if (audioPlayerViewStatusListeners != null) {
            for (KPlayerView.AudioPlayerViewStatusListener audioPlayerViewStatusListener : audioPlayerViewStatusListeners) {
                status.setAudio(currentAudio);
                status.setState(AudioStatus.State.PLAY);
                status.setDuration(duration);
                status.setCurrentPos(currentTime);
                audioPlayerViewStatusListener.onPreparedAudioStatus(status);
            }
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {

    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if (kPlayerViewServiceListeners != null) {
            for (KPlayerView.KPlayerViewServiceListener serviceListener : kPlayerViewServiceListeners) {
                serviceListener.onCompletedAudio();
            }
        }
        if (notificationListener != null) {
            notificationListener.onCompletedAudio();
        }
        if (audioPlayerViewStatusListeners != null) {
            for (KPlayerView.AudioPlayerViewStatusListener audioPlayerViewStatusListener : audioPlayerViewStatusListeners) {
                audioPlayerViewStatusListener.onCompletedAudioStatus(status);
            }
        }
    }

    public void seekTo(int time) {
        Log.d("time = ", Integer.toString(time));
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(time);
        }
    }

    private void updateTimeAudio() {
        new Thread() {
            @Override
            public void run() {
                while (isPlaying) {
                    try {
                        if (kPlayerViewServiceListeners != null) {
                            for (KPlayerView.KPlayerViewServiceListener serviceListener : kPlayerViewServiceListeners) {
                                serviceListener.onTimeChanged(mediaPlayer.getCurrentPosition());
                            }
                        }
                        if (notificationListener != null) {
                            notificationListener.onTimeChanged(mediaPlayer.getCurrentPosition());
                        }
                        if (audioPlayerViewStatusListeners != null) {
                            for (KPlayerView.AudioPlayerViewStatusListener audioPlayerViewStatusListener : audioPlayerViewStatusListeners) {
                                status.setState(AudioStatus.State.PLAY);
                                status.setDuration(mediaPlayer.getDuration());
                                status.setCurrentPos(mediaPlayer.getCurrentPosition());
                                audioPlayerViewStatusListener.onTimeChangedStatus(status);
                            }
                        }
                        Thread.sleep(200);
                    } catch (IllegalStateException | InterruptedException | NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    private void throwError(String path, Source source) {
        if (source == Source.URL) {
            throw new AudioUrlInvalidException(path);
        } else if (source == Source.RAW) {
            try {
                throw new AudioRawInvalidException(path);
            } catch (AudioRawInvalidException e) {
                e.printStackTrace();
            }
        } else if (source == Source.ASSETS) {
            try {
                throw new AudioAssetsInvalidException(path);
            } catch (AudioAssetsInvalidException e) {
                e.printStackTrace();
            }
        } else if (source == Source.FILE_PATH) {
            try {
                throw new AudioFilePathInvalidException(path);
            } catch (AudioFilePathInvalidException e) {
                e.printStackTrace();
            }
        }
        if (invalidPathListeners != null) {
            for (KPlayerView.OnInvalidPathListener onInvalidPathListener : invalidPathListeners) {
                onInvalidPathListener.onPathError(currentAudio);
            }
        }
    }

    private boolean isAudioFileValid(String path, Source source) {
        if (source == Source.URL) {
            return path.startsWith("http");
        } else if (source == Source.RAW) {
            assetFileDescriptor = null;
            assetFileDescriptor = getApplicationContext().getResources().openRawResourceFd(Integer.parseInt(path));
            return assetFileDescriptor != null;
        } else if (source == Source.ASSETS) {
            try {
                assetFileDescriptor = null;
                assetFileDescriptor = getApplicationContext().getAssets().openFd(path);
                return assetFileDescriptor != null;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else if (source == Source.FILE_PATH) {
            File file = new File(path);
            return file.exists();
        } else {
            // should never arrive here
            return false;
        }
    }

    public Audio getCurrentAudio() {
        return currentAudio;
    }
}
