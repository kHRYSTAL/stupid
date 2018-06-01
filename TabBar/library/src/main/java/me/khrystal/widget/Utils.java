package me.khrystal.widget;

import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/6/1
 * update time:
 * email: 723526676@qq.com
 */

public class Utils {
    static float dp2px(float dp) {
        Resources resources = Resources.getSystem();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}

