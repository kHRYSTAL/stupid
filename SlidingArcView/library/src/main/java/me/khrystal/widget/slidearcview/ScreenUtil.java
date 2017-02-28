package me.khrystal.widget.slidearcview;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/2/28
 * update time:
 * email: 723526676@qq.com
 */

public class ScreenUtil {

    private static int screenW;
    private static int screenH;
    private static float screenDensity;

    public void initScreen(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenW = metrics.widthPixels;
        screenH = metrics.heightPixels;
        screenDensity = metrics.density;
    }

    public static int getScreenW() {
        return screenW;
    }

    public static int getScreenH() {
        return screenH;
    }

    public static float getScreenDensity() {
        return screenDensity;
    }

    public static int dp2px(float dpValue) {
        return (int) (dpValue * getScreenDensity() + 0.5f);
    }

    public static int px2dp(float pxValue) {
        return (int) (pxValue / getScreenDensity() + 0.5f);
    }
}
