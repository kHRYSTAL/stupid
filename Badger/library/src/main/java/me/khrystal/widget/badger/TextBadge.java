package me.khrystal.widget.badger;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.View;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/4/11
 * update time:
 * email: 723526676@qq.com
 */

public class TextBadge extends BadgeDrawable {

    private static final boolean WHOLO = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    private static final boolean WMATE = Build.VERSION.SDK_INT < Build.VERSION_CODES.M;
    private static final float MAGIC_TEXT_SCALE_FACTOR = 0.6f;

    private final BadgeShape shape;

    private final Paint badgePaint = new Paint();
    private final Paint textPaint = new Paint();
    private boolean paintPreparationNeeded = true;

    private String text = "";

    public TextBadge(@NonNull Context context, @NonNull BadgeShape shape) {
        this(shape, badgeShapeColor(context), badgeTextColor(context));
    }

    protected TextBadge(@NonNull BadgeShape shape, @ColorInt int badgeColor, @ColorInt int textColor) {
        this.shape = shape;
        badgePaint.setColor(badgeColor);
        textPaint.setColor(textColor);
    }

    protected void onPrepareBadgePaint(@NonNull Paint paint) {
        paint.setAntiAlias(true);
    }

    protected void onPrepareTextPaint(@NonNull Paint paint) {
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    @SuppressLint("NewApi")
    public void draw(@NonNull Canvas canvas) {
        if (text.length() == 0) {
            return;
        }
        if (paintPreparationNeeded) {
            paintPreparationNeeded = false;
            onPrepareBadgePaint(badgePaint);
            onPrepareTextPaint(textPaint);
        }

        Rect rect = shape.draw(canvas, getBounds(), badgePaint, getLayoutDirection());
        textPaint.setTextSize(rect.height() * MAGIC_TEXT_SCALE_FACTOR);
        float x = rect.exactCenterX();
        float y = rect.exactCenterY() - (textPaint.ascent() + textPaint.descent()) * 0.5f;
        canvas.drawText(text, x, y, textPaint);
    }

    @SuppressWarnings("ResourceType")
    @Override
    public int getLayoutDirection() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return super.getLayoutDirection();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return View.LAYOUT_DIRECTION_LTR;
        }
        return 0;
    }

    @Override
    public boolean onLayoutDirectionChanged(int layoutDirection) {
        invalidateSelf();
        return true;
    }

    @Override
    @SuppressLint("NewApi")
    public void setAlpha(int alpha) {
        if (getAlpha() != alpha) {
            badgePaint.setAlpha(alpha);
            textPaint.setAlpha(alpha);
            super.setAlpha(alpha);
        }
    }

    @Override
    @SuppressLint("NewApi")
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        if (getColorFilter() != colorFilter) {
            badgePaint.setColorFilter(colorFilter);
            textPaint.setColorFilter(colorFilter);
            super.setColorFilter(colorFilter);
        }
    }

    public final void setText(@Nullable CharSequence text) {
        String trimmedText = text == null ? "" : text.toString().trim();
        if (!this.text.equals(trimmedText)) {
            this.text = trimmedText;
            invalidateSelf();
        }
    }

    public final String getText() {
        return text;
    }

    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    static int badgeShapeColor(Context context) {
        Resources.Theme theme = context.getTheme();
        TypedValue typedValue = new TypedValue();
        if (theme.resolveAttribute(R.attr.badgeShapeColor, typedValue, true)) {
            return typedValue.data;
        }
        if (theme.resolveAttribute(R.attr.colorAccent, typedValue, true)) {
            return typedValue.data;
        }
        if (WHOLO && theme.resolveAttribute(android.R.attr.colorAccent, typedValue, true)) {
            return typedValue.data;
        }
        if (WMATE) {
            return context.getResources().getColor(R.color.badgeShapeColor);
        }
        return context.getColor(R.color.badgeShapeColor);
    }

    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    static int badgeTextColor(Context context) {
        Resources.Theme theme = context.getTheme();
        TypedValue typedValue = new TypedValue();
        if (theme.resolveAttribute(R.attr.badgeTextColor, typedValue, true)) {
            return typedValue.data;
        }
        if (theme.resolveAttribute(R.attr.titleTextColor, typedValue, true)) {
            return typedValue.data;
        }
        if (WMATE) {
            return context.getResources().getColor(R.color.badgeTextColor);
        }
        if (theme.resolveAttribute(android.R.attr.titleTextColor, typedValue, true)) {
            return typedValue.data;
        }
        return context.getColor(R.color.badgeTextColor);
    }

    public static abstract class Factory<T extends TextBadge> implements BadgeDrawable.Factory<T> {
        @NonNull
        protected final BadgeShape shape;

        @ColorInt
        protected final int badgeColor;

        @ColorInt
        protected final int textColor;

        public Factory(@NonNull Context context, @NonNull BadgeShape shape) {
            this(shape, badgeShapeColor(context), badgeTextColor(context));
        }

        public Factory(@NonNull BadgeShape shape, @ColorInt int badgeColor, @ColorInt int textColor) {
            this.shape = shape;
            this.badgeColor = badgeColor;
            this.textColor = textColor;
        }
    }


}
