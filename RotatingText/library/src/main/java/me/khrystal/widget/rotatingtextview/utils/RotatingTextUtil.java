package me.khrystal.widget.rotatingtextview.utils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/5/27
 * update time:
 * email: 723526676@qq.com
 */

public class RotatingTextUtil {

    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    public static int geterateViewId() {
        for (;;) {
            final int result = sNextGeneratedId.get();
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF)
                newValue = 1;
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }
}
