package me.khrystal.widget.circularlayout;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.Timer;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/2/13
 * update time:
 * email: 723526676@qq.com
 */
public class CircularLayout<T> extends ViewGroup {
    
    private static final String TAG = CircularLayout.class.getSimpleName();
    private static final float DEFAULT_ANGLE_OFFSET = 90f;
    private static final float DEFAULT_ANGLE_RANGE = 360f;

    private int radiusParameter = 250; // radius offset
    private int centerHeightParameter = -10; // height offset
    private int centerWidthParameter = 0; // width offset
    private CircularLayoutAdapter<T> adapter;
    private int childCount = 10;

    private boolean childsIsPinned = false; // if true, the children is pinned to the circle so they spin around a pivot
    private int pinnedChildsRotationAngle = 0;
    private Activity parentActivity = (Activity) this.getContext();
    private float shiftAngle = 0;
    private int currentStep = 0;
    private int oldStep = 0;
    private float oldX = 0;
    private Timer balancingTimer; // to schedule rotation balancing task
    private int dp = 0;
    private CircularLayout.LayoutParams lp;
    private boolean isRotateRight = false;
    private float mAngleOffset;
    private float mAngleRange;
    private boolean isRotatingHappening = false;
    private boolean isCenteredImageVisible = false;
    private boolean isInitialized = false;


    public CircularLayout(Context context) {
        this(context, null);
    }

    public CircularLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircularLayout, 0, 0);
        try {
            mAngleOffset = ta.getFloat(R.styleable.CircularLayout_angleOffset, DEFAULT_ANGLE_OFFSET);
            mAngleRange = ta.getFloat(R.styleable.CircularLayout_angleRange, DEFAULT_ANGLE_RANGE);
        } finally {
            ta.recycle();
        }
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {

    }

    public void init() {
        pinnedChildsRotationAngle = 0;
        this.removeAllViews();
        this.invalidate();
        try {
            if (!isInitialized) {
                isInitialized = true;
                dp = getHeight() - 30;
                dp = Math.min(dp, 120);
                dp = (int) dp2px(getContext(), dp);
                lp = new CircularLayout.LayoutParams(dp, dp);
            }
            for (int i = 0; i < childCount / 2; i++) {
                int index = -1 * i;
                CircularLayoutItem item = adapter.get(index);
                item.setLayoutParams(lp);
                item.setParent(this);
                this.addView(item);
            }
            for (int i = childCount / 2; i > 0; i--) {
                int index = i;
                CircularLayoutItem item = adapter.get(index);
                item.setLayoutParams(lp);
                item.setParent(this);
                this.addView(item);
            }
            
            final int childs = getChildCount();
            float totalWeight = 0f;
            for (int i = 0; i < childs; i++) {
                final View child = getChildAt(i);
                // TODO: 19/2/13  
//                LinearLayout.LayoutParams lp = layoutParams(child);
//                totalWeight += lp.weight;
            }
            shiftAngle = mAngleRange / totalWeight;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public int getPinnedChildsRotationAngle() {
        return pinnedChildsRotationAngle;
    }

    public boolean isChildsIsPinned() {
        return childsIsPinned;
    }

    public int getCurrentStep() {
        return currentStep;
    }

    private static float dp2px(Context context, float dp) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }
}
