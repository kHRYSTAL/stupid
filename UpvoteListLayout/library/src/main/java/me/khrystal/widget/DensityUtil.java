package me.khrystal.widget;

import android.content.Context;
import android.util.TypedValue;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/7/4
 * update time:
 * email: 723526676@qq.com
 */

public class DensityUtil {
    public static Float dp2px(float dipValue, Context context) {
        float value = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, context.getResources().getDisplayMetrics());
        return value;
    }
}
