package me.khrystal.videotimeline;

import android.app.Application;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 2020/12/1
 * update time:
 * email: 723526676@qq.com
 */

public class App extends Application {
    public static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
