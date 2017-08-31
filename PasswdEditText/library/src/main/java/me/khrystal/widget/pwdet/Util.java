package me.khrystal.widget.pwdet;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/8/31
 * update time:
 * email: 723526676@qq.com
 */

public class Util {

    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int dp2px(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) ((dp * displayMetrics.density) + 0.5);
    }
}
