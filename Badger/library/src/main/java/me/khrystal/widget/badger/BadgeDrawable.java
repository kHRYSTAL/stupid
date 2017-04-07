package me.khrystal.widget.badger;

import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/4/5
 * update time:
 * email: 723526676@qq.com
 */

public abstract class BadgeDrawable extends Drawable {

    private int alpha = 255;
    private ColorFilter colorFilter;

    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {
        if (alpha > 255) {
            alpha = 255;
        } else if (alpha < 0) {
            alpha = 0;
        }
        if (this.alpha != alpha) {
            this.alpha = alpha;
            invalidateSelf();
        }
    }

    @Override
    public int getAlpha() {
        return alpha;
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        if (this.colorFilter != colorFilter) {
            this.colorFilter = colorFilter;
            invalidateSelf();
        }
    }

    @Override
    public int getOpacity() {
        if (alpha == 255) {
            return PixelFormat.OPAQUE;
        }
        if (alpha == 0) {
            return PixelFormat.TRANSPARENT;
        }
        return PixelFormat.TRANSLUCENT;
    }

    public interface Factory<T extends BadgeDrawable> {
        T createBadge();
    }
}
