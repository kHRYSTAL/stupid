package me.khrystal.widget.kenburnsview;

import android.graphics.RectF;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/5/18
 * update time:
 * email: 723526676@qq.com
 */

public class MathUtils {

    protected static float truncate(float f, int decimalPlaces) {
        float decimalShift = (float) Math.pow(10, decimalPlaces);
        return Math.round(f * decimalShift) / decimalShift;
    }

    protected static boolean haveSameAspectRation(RectF r1, RectF r2) {
        float srcRectRatio = MathUtils.truncate(MathUtils.getRectRatio(r1), 3);
        float dstRectRatio = MathUtils.truncate(MathUtils.getRectRatio(r2), 3);

        return (Math.abs(srcRectRatio - dstRectRatio) <= 0.01f);
    }

    protected static float getRectRatio(RectF rect) {
        return rect.width() / rect.height();
    }
}
