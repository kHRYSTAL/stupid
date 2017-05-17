package me.khrystal.widget.arclayout;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;

import me.khrystal.widget.R;

/**
 * usage: ArcLayout Config
 * author: kHRYSTAL
 * create time: 17/5/17
 * update time:
 * email: 723526676@qq.com
 */

public class ArcLayoutConfig {

    public final static int CROP_INSIDE = 0;
    public final static int CROP_OUTSIDE = 1;

    public final static int POSITION_BOTTOM = 0;
    public final static int POSITION_TOP = 1;
    public static final int POSITION_LEFT = 2;
    public static final int POSITION_RIGHT = 3;

    private boolean cropInside = true;
    private float arcHeight;
    private float elevation;

    private int position;

    private static float dpToPx(Context context, int dp) {
        Resources r = context.getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

    ArcLayoutConfig(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ArcLayout, 0, 0);
        arcHeight = ta.getDimension(R.styleable.ArcLayout_arc_height, dpToPx(context, 10));
        final int cropDirection = ta.getInt(R.styleable.ArcLayout_arc_cropDirection, CROP_INSIDE);
        cropInside = (cropDirection == CROP_INSIDE);
        position = ta.getInt(R.styleable.ArcLayout_arc_position, POSITION_BOTTOM);
        ta.recycle();
    }

    public float getElevation() {
        return elevation;
    }

    public void setElevation(float elevation) {
        this.elevation = elevation;
    }

    public boolean isCropInside() {
        return cropInside;
    }

    public float getArcHeight() {
        return arcHeight;
    }

    public int getPosition() {
        return position;
    }

}
