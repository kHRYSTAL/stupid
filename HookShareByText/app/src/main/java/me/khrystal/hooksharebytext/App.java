package me.khrystal.hooksharebytext;

import android.app.Application;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 2019-10-22
 * update time:
 * email: 723526676@qq.com
 */
public class App extends Application {

    public static AppConfig APP_CONFIG;

    public AppConfig getAppConfig() {
        AppConfig config = new AppConfig();
        config.setRootDir("khappdemo");
        config.setSchema("khrystal");
        return config;
    }
}
