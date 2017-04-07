package me.khrystal.widget.badger;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/4/5
 * update time:
 * email: 723526676@qq.com
 */

public abstract class BadgeShape {
    private final Rect rect = new Rect();
    private final float scale;
    private final float aspectRatio;
    private final int gravity;

    protected BadgeShape(@FloatRange(from = 0, to = 1) float scale, float aspectRatio, int gravity) {
        this.scale = scale;
        this.aspectRatio = aspectRatio;
        this.gravity = gravity;
    }

    public Rect draw(@NonNull Canvas canvas, @NonNull Rect bounds, @NonNull Paint paint, int layoutDirection) {
        float width = bounds.width() * scale;
        float height = bounds.height() * scale;
        if (width < height * aspectRatio) {
            height = width / aspectRatio;
        } else {
            width = height * aspectRatio;
        }
        return null;
    }
}
