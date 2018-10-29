package me.khrystal.suspensionwindow;

import android.app.Application;

import me.khrystal.suspensionwindow.window.ApplicationLifecycle;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/10/26
 * update time:
 * email: 723526676@qq.com
 */
public class App extends Application {

    public static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        registerActivityLifecycleCallbacks(new ApplicationLifecycle());
    }

    public static App getInstance() {
        return instance;
    }
}
