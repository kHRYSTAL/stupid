package me.khrystal.hxdemo.util;

import android.content.Context;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/6/21
 * update time:
 * email: 723526676@qq.com
 */
public class DensityUtil {

    public static int dip2px(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
