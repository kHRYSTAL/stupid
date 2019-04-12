package me.khrystal.tools.kaudioplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import me.khrystal.tools.kaudioplayer.exceptions.AudioListNullPointerException;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/4/12
 * update time:
 * email: 723526676@qq.com
 */
public class KPlayerNotificationReceiver extends BroadcastReceiver {

    public KPlayerNotificationReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        KAudioPlayer kAudioPlayer = KAudioPlayer.getInstance();
        String action = "";

        if (intent.hasExtra(KNotificationPlayerService.ACTION)) {
            action = intent.getStringExtra(KNotificationPlayerService.ACTION);
        }

        switch (action) {
            case KNotificationPlayerService.PLAY:
                try {
                    kAudioPlayer.continueAudio();
                    kAudioPlayer.updateNotification();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case KNotificationPlayerService.PAUSE:
                try {
                    if (kAudioPlayer != null) {
                        kAudioPlayer.pauseAudio();
                        kAudioPlayer.updateNotification();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case KNotificationPlayerService.NEXT:
                try {
                    kAudioPlayer.nextAudio();
                } catch (AudioListNullPointerException e) {
                    try {
                        kAudioPlayer.continueAudio();
                    } catch (AudioListNullPointerException e1) {
                        e1.printStackTrace();
                    }
                }
                break;
            case KNotificationPlayerService.PREVIOUS:
                try {
                    kAudioPlayer.previousAudio();
                } catch (Exception e) {
                    try {
                        kAudioPlayer.continueAudio();
                    } catch (AudioListNullPointerException e1) {
                        e1.printStackTrace();
                    }
                }
                break;
        }
    }
}
