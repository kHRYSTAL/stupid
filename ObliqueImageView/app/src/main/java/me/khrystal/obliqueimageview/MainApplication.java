package me.khrystal.obliqueimageview;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/5/5
 * update time:
 * email: 723526676@qq.com
 */

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
            .setDefaultFontPath("fonts/Roboto-Light.ttf")
            .setFontAttrId(R.attr.fontPath).build());
    }
}
