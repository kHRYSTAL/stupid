package me.khrystal.server;

import android.app.Application;
import android.content.Context;

import me.khrystal.util.ipc.VCore;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/4/1
 * update time:
 * email: 723526676@qq.com
 */
public class App extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        VCore.init(base);
    }
}
