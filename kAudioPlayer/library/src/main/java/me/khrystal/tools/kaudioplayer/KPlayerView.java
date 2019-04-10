package me.khrystal.tools.kaudioplayer;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * usage: TODO
 * author: kHRYSTAL
 * create time: 19/4/10
 * update time:
 * email: 723526676@qq.com
 */
public class KPlayerView extends LinearLayout {

    private static final String TAG = KPlayerView.class.getSimpleName();

    public KPlayerView(Context context) {
        super(context);
    }

    public KPlayerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public KPlayerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public KPlayerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
