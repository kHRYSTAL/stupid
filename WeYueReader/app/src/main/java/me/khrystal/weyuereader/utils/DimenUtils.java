package me.khrystal.weyuereader.utils;

import me.khrystal.weyuereader.WYApplication;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/4/24
 * update time:
 * email: 723526676@qq.com
 */

public class DimenUtils {
    public static int dp2px(float dpValue) {
        return (int) (dpValue * (WYApplication.getAppResources().getDisplayMetrics().density) + 0.5f);
    }

    public static int px2dp(float pxValue) {
        return (int) (pxValue / (WYApplication.getAppResources().getDisplayMetrics().density) + 0.5f);
    }
}
