package me.khrystal.widget.badger;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/4/11
 * update time:
 * email: 723526676@qq.com
 */

public class CountBadge extends TextBadge {

    private int count = 0;

    public CountBadge(@NonNull Context context, @NonNull BadgeShape shape) {
        super(context, shape);
    }

    protected CountBadge(@NonNull BadgeShape shape, @ColorInt int badgeColor, @ColorInt int textColor) {
        super(shape, badgeColor, textColor);
    }

    public final void setCount(@IntRange(from = 0) int count) {
        if (count < 0) {
            throw  new IllegalArgumentException("must be 0 <= count");
        }
        if (this.count != count) {
            this.count = count;
            setText(count == 0 ? null : String.valueOf(count));
        }
    }

    public final int getCount() {
        return count;
    }

    public final static  class Factory extends TextBadge.Factory<CountBadge> {

        public Factory(@NonNull Context context, @NonNull BadgeShape shape) {
            super(context, shape);
        }

        public Factory(@NonNull BadgeShape shape, @ColorInt int badgeColor, @ColorInt int textColor) {
            super(shape, badgeColor, textColor);
        }

        @Override
        public CountBadge createBadge() {
            return new CountBadge(shape, badgeColor, textColor);
        }
    }
}
