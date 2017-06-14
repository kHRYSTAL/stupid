package me.khrystal.wallpaper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/6/13
 * update time:
 * email: 723526676@qq.com
 */

public class Application extends android.app.Application {

    public static SharedPreferences APP_PREFERENCE;
    public static Context CONTEXT;

    @Override
    public void onCreate() {
        super.onCreate();
        CONTEXT = this;
        APP_PREFERENCE = this.getApplicationContext()
                .getSharedPreferences("wallpaper", Context.MODE_PRIVATE);
    }
}
