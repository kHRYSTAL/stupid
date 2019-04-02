package me.khrystal.widget.pathview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/4/2
 * update time:
 * email: 723526676@qq.com
 */
public class PathView extends View {

    private static final String TAG = PathView.class.getSimpleName();

    @IntDef({MODE_AIR_PLANE, MODE_TRAIN})
    @Retention(RetentionPolicy.SOURCE)
    private @interface Mode {
    }

    public static final int MODE_AIR_PLANE = 0; // 飞机
    public static final int MODE_TRAIN = 1; // 火车

    public PathView(Context context) {
        super(context);
    }

    public PathView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private static class Keyframes {

        static final float PRECISION = 1f; // 数值越少 numPoints 越大

    }
}
