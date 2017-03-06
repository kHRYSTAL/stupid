package me.khrystal.uninstallserver;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/3/6
 * update time:
 * email: 723526676@qq.com
 */

public class UnInstallService extends Service {

    SDCardListener[] listeners;

    @SuppressLint("SdCardPath")
    @Override
    public void onCreate() {
        SDCardListener[] listeners = {
                new SDCardListener("/data/data/me.khrystal.uninstallserver", this)
        };
        this.listeners = listeners;

        for (SDCardListener listener : listeners) {
            listener.startWatching();
        }

    }

    @Override
    public void onDestroy() {
        for (SDCardListener listener : listeners) {
            listener.stopWatching();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
