package me.khrystal.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/6/1
 * update time:
 * email: 723526676@qq.com
 */

public class EmptySpace extends View {
    public EmptySpace(Context context) {
        super(context);
        init();
    }

    public EmptySpace(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EmptySpace(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public EmptySpace(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, 0);
        layoutParams.weight = 1;
        setLayoutParams(layoutParams);
    }
}
