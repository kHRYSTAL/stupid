package me.khrystal.rxnetmonitor;

import android.app.Application;

import me.khrystal.receiver.RxNetStateReceiver;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/5/11
 * update time:
 * email: 723526676@qq.com
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RxNetStateReceiver.registerNetworkStateReceiver(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        RxNetStateReceiver.unRegisterNetworkStateReceiver(this);
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
