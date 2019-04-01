package me.khrystal.util.ipc.service;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/4/1
 * update time:
 * email: 723526676@qq.com
 */
public class DaemonService extends Service {

    private static final int NOTIFY_ID = 1001;

    public static void startup(Context context) {
        context.startService(new Intent(context, DaemonService.class));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // make this service daemon process
        startup(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // when create call inner service
        startService(new Intent(this, InnerService.class));
        // make daemon to a foreground service
        startForeground(NOTIFY_ID, new Notification());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    public static final class InnerService extends Service {

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            // make inner service to a foreground service
            startForeground(NOTIFY_ID, new Notification());
            stopForeground(true);
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }
}
