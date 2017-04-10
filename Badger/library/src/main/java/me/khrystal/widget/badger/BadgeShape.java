package me.khrystal.widget.badger;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.view.Gravity;

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
        applyGravity((int) width, (int) height, bounds, layoutDirection);
        onDraw(canvas, rect, paint);
        return rect;
    }

    private void applyGravity(int width, int height, Rect bounds, int layoutDirection) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Gravity.apply(gravity, width, height, bounds, rect);
        } else {
            Gravity.apply(gravity, width, height, bounds, rect, layoutDirection);
        }
    }

    protected abstract void onDraw(@NonNull Canvas canvas, @NonNull Rect region, @NonNull Paint paint);

    public static BadgeShape circle(@FloatRange(from = 0, to = 1) float scale, int gravity) {
        return oval(scale, 1, gravity);
    }

    public static BadgeShape oval(@FloatRange(from = 0, to = 1) float scale, float aspectRatio, int gravity) {
        return new BadgeShape(scale, aspectRatio, gravity) {
            private final RectF region = new RectF();

            @Override
            protected void onDraw(@NonNull Canvas canvas, @NonNull Rect region, @NonNull Paint paint) {
                this.region.set(region);
                canvas.drawOval(this.region, paint);
            }
        };
    }

    public static BadgeShape rect(@FloatRange(from = 0, to = 1) float scale, float aspectRatio, int gravity) {
        return new BadgeShape(scale, aspectRatio, gravity) {
            @Override
            protected void onDraw(@NonNull Canvas canvas, @NonNull Rect region, @NonNull Paint paint) {
                canvas.drawRect(region, paint);
            }
        };
    }

    public static BadgeShape rect(@FloatRange(from = 0, to = 1) float scale, float aspectRatio, int gravity,
                                  @FloatRange(from = 0, to = 1) final float radiusFactor) {
        if (radiusFactor == 0) {
            return rect(scale, aspectRatio, gravity);
        }
        return new BadgeShape(scale, aspectRatio, gravity) {
            private final RectF region = new RectF();

            @Override
            protected void onDraw(@NonNull Canvas canvas, @NonNull Rect region, @NonNull Paint paint) {
                this.region.set(region);
                float r = 0.5f * Math.min(region.width(), region.height()) * radiusFactor;
                canvas.drawRoundRect(this.region, r, r, paint);
            }
        };
    }

    public static BadgeShape square(@FloatRange(from = 0, to = 1) float scale, int gravity) {
        return rect(scale, 1, gravity);
    }

    public static BadgeShape square(@FloatRange(from = 0, to = 1) float scale, int gravity,
                                    @FloatRange(from = 0, to = 1) float radiusFactor) {
        return rect(scale, 1, gravity, radiusFactor);
    }
}
