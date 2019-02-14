package me.khrystal.widget.circularlayout;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.Timer;
import java.util.TimerTask;

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
                LayoutParams lp = layoutParams(child);
                totalWeight += lp.weight;
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

    public boolean isRotatingHappening() {
        return isRotatingHappening;
    }

    public void setChildsIsPinned(boolean pinned) {
        childsIsPinned = pinned;
        final int childs = getChildCount();
        for (int i = 0; i < childs; i++) {
            final CircularLayoutItem child = (CircularLayoutItem) getChildAt(i);
            child.invalidate();
        }
    }

    public int getRadius() {
        final int width = getWidth();
        final int height = getHeight();
        final float minDimen = width > height ? height : width;
        float radius = (minDimen) / 2f;
        return (int) radius;
    }

    public void getCenter(PointF p) {
        p.set(getWidth() / 2f, getHeight() / 2f);
    }

    public void setAngleOffset(float offset) {
        mAngleOffset = offset;
        requestLayout();
        invalidate();
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        init();
    }

    public float getAngleOffset() {
        return mAngleOffset;
    }

    public int getCurrentStep() {
        return currentStep;
    }

    public void setAdapter(CircularLayoutAdapter adapter) {
        adapter.setContext(getContext());
        this.adapter = adapter;
        this.adapter.setParent(this);
    }

    public void setOffsetY(int y) {
        centerHeightParameter = (int) dp2px(getContext(), y);
    }

    public void setRadius(int r) {
        radiusParameter = (int) dp2px(getContext(), r);
    }

    public void setChildrenCount(int count) {
        childCount = count;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int count = getChildCount();
        int maxHeight = 0;
        int maxWidth = 0;

        // find rightmost and bottommost child
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
                maxWidth = Math.max(maxWidth, child.getMeasuredWidth());
                maxHeight = Math.max(maxHeight, child.getMeasuredHeight());
            }
        }
        // check against our minimum height and width
        maxHeight = Math.max(maxHeight / 2, getSuggestedMinimumHeight());
        maxWidth = Math.max(maxWidth / 2, getSuggestedMinimumWidth());

        int width = resolveSize(maxWidth, widthMeasureSpec);
        int height = resolveSize(maxHeight, heightMeasureSpec);

        setMeasuredDimension(width, height);
    }

    private LayoutParams layoutParams(View child) {
        return (LayoutParams) child.getLayoutParams();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final int width = getWidth();
        final int height = getHeight();

        final float minDimen = width > height ? height : width;
        float radius = (minDimen) / 2f;

        radius = (float) (width / 2) + radiusParameter;

        int x = width / 2 + centerWidthParameter;
        int y = height / 3 + (int) radius + centerHeightParameter;

        Paint paint = new Paint();
        paint.setStrokeWidth(dp / 10f);
        paint.setColor(Color.GRAY);
        paint.setAlpha(60);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(x, y - radius, dp / 2 + 2, paint);
        super.onDraw(canvas);
    }

    private static float dp2px(Context context, float dp) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int childs = getChildCount();
        float totalWeight = 0f;
        for (int i = 0; i < childs; i++) {
            final View child = getChildAt(i);
            LayoutParams lp = layoutParams(child);
            totalWeight += lp.weight;
        }

        shiftAngle = mAngleRange / totalWeight;
        final int width = getWidth();
        final int height = getHeight();

        final float minDimen = width > height ? height : width;
        float radius = (minDimen) / 2f;

        radius = (float) (width / 2) + radiusParameter;

        float startAngle = mAngleOffset + 270;

        for (int i = 0; i < childs; i++) {
            final View child = getChildAt(i);
            final LayoutParams lp = layoutParams(child);

            final float angle = mAngleRange / totalWeight * lp.weight;
            final float centerAngle = startAngle;
            final int x;
            final int y;
            if (child instanceof CircularLayoutItem) {
                CircularLayoutItem item = (CircularLayoutItem) child;
                item.setRotationAngle((int) (centerAngle) + 90);
            }

            if (childs > 1) {
                x = (int) (radius * Math.cos(Math.toRadians(centerAngle))) + width / 2 + centerWidthParameter;
                y = (int) (radius * Math.sin(Math.toRadians(centerAngle))) + height / 3 + centerHeightParameter;
            } else {
                x = width / 2;
                y = height / 2;
            }

            final int halfChildWidth = child.getMeasuredWidth() / 2;
            final int halfChildHeight = child.getMeasuredHeight() / 2;

            final int left = lp.width != LayoutParams.MATCH_PARENT ? x - halfChildWidth : 0;
            final int top = lp.height != LayoutParams.MATCH_PARENT ? y - halfChildHeight : 0;
            final int right = lp.width != LayoutParams.MATCH_PARENT ? x + halfChildWidth : width;
            final int bottom = lp.height != LayoutParams.MATCH_PARENT ? y + halfChildHeight : height;

            child.layout(left, top, right, bottom);
        }
        invalidate();
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        LayoutParams lp = new LayoutParams(p.width, p.height);

        if (p instanceof LinearLayout.LayoutParams) {
            lp.weight = ((LinearLayout.LayoutParams) p).weight;
        }

        return lp;
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                this.oldX = event.getX();
                isRotatingHappening = true;
                oldStep = currentStep;
                break;
            case MotionEvent.ACTION_MOVE:
                float newX = event.getX();
                rotate((newX - this.oldX) / 12);
                this.oldX = newX;
                break;
            case MotionEvent.ACTION_UP:
                balanceRotate();
                if (oldStep != currentStep) {
                    adapter.get(oldStep).onUnFocus();
                }
                break;
        }
        return true;
    }

    public void smoothRotate(float angle) {
        if (!isRotatingHappening()) {
            try {
                if (balancingTimer != null) {
                    balancingTimer.cancel();
                    balancingTimer.purge();
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
            balancingTimer = new Timer();
            RotationTimerTask timerTask = new RotationTimerTask(angle);
            balancingTimer.schedule(timerTask, 0, 15);
        }
    }

    public void rotate(float distance) {
        this.setAngleOffset(getAngleOffset() + distance);
        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            if (child instanceof CircularLayoutItem) {
                CircularLayoutItem item = (CircularLayoutItem) child;
                item.setRotation((int) getAngleOffset());
            }
        }
        float ratio = getAngleOffset() / shiftAngle;
        int roundedRatio = Math.round(ratio);
        int angle1 = (int) (roundedRatio * shiftAngle);

        int prevStep = currentStep;
        currentStep = (int) (angle1 / shiftAngle);
        if (prevStep != currentStep) {
            isRotateRight = currentStep <= prevStep;
            if (isRotateRight) {
                pinnedChildsRotationAngle = 20;
            } else {
                pinnedChildsRotationAngle = -20;
            }
            updateChilds();
        }
    }

    public void rotateToAngle(float angle) {
        this.setAngleOffset(angle);
        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            if (child instanceof CircularLayoutItem) {
                CircularLayoutItem item = (CircularLayoutItem) child;
                item.setRotation((int) getAngleOffset());
            }
        }
        float ratio = getAngleOffset() / shiftAngle;
        int roundedRatio = Math.round(ratio);
        int angle1 = (int) (roundedRatio * shiftAngle);

        int prevStep = currentStep;
        currentStep = (int) (angle1 / shiftAngle);

        if (prevStep != currentStep) {
            isRotateRight = currentStep <= prevStep;
            if (isRotateRight) {
                pinnedChildsRotationAngle = 20;
            } else {
                pinnedChildsRotationAngle = -20;
            }
            updateChilds();
        }
    }

    public void updateChildAtIndex(int index) {
        int replacementIndex = ((-1 * (currentStep + index)) % getChildCount() + getChildCount()) + getChildCount();
        View view = getChildAt(replacementIndex);
        removeView(view);
        CircularLayoutItem item = adapter.get(currentStep + index);
        item.setLayoutParams(lp);
        item.setParent(this);
        addView(item, replacementIndex);
    }

    private void updateChilds() {
        if (!isRotateRight) {
            updateChildAtIndex(Math.round(getChildCount() / 4));
        } else {
            updateChildAtIndex(-1 * Math.round(getChildCount() / 4));
        }
    }

    public void balanceRotate() {
        if (childsIsPinned) {
            final int childs = getChildCount();
            for (int i = 0; i < childs; i++) {
                final CircularLayoutItem item = (CircularLayoutItem) getChildAt(i);
                item.balance();
            }
        }
        isRotatingHappening = false;
        float ratio = getAngleOffset() / shiftAngle;
        int roundedRatio = Math.round(ratio);
        int angle = (int) (roundedRatio * shiftAngle);
        smoothRotate(angle);
        int prevStep = currentStep;
        currentStep = (int) (angle / shiftAngle);
        if (prevStep != currentStep) {
            updateChilds();
        }
    }

    public void rotateStep(int i) {
        if (!isRotatingHappening) {
            adapter.get(currentStep).onUnFocus();
            float ratio = getAngleOffset() / shiftAngle;
            int roundedRatio = Math.round(ratio);

            int angle = (int) (roundedRatio * shiftAngle + i * shiftAngle);
            smoothRotate(angle);
            int prevStep = currentStep;
            currentStep = (int) (angle / shiftAngle);
            if (prevStep != currentStep) {
                updateChilds();
            }
        }
    }

    public void showCenteredImage() {
        isCenteredImageVisible = true;
        invalidate();
    }

    public void hideCenteredImage() {
        isCenteredImageVisible = false;
        invalidate();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        // TODO: 19/2/14  
    }

    public static class LayoutParams extends ViewGroup.LayoutParams {

        public float weight = 1f;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }
    }

    class RotationTimerTask extends TimerTask {

        int desiredAngle = 0;
        float fCurrentAngle = 0;
        int currentAngle = 0;

        public RotationTimerTask(float angle) {

            isRotatingHappening = true;
            desiredAngle = (int) angle;
            fCurrentAngle = CircularLayout.this.getAngleOffset();
            currentAngle = (int) fCurrentAngle;
            float difference = fCurrentAngle - angle;
            if (difference > 0 && difference < 1) {
                currentAngle = desiredAngle + 1;
            }
            if (difference > -1 && difference < 0) {
                currentAngle = desiredAngle - 1;
            }
        }

        @Override
        public void run() {
            CircularLayout.this.parentActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (currentAngle > desiredAngle) {
                        CircularLayout.this.rotateToAngle(currentAngle -= 1);
                    } else if (currentAngle < desiredAngle) {
                        CircularLayout.this.rotateToAngle(currentAngle += 1);
                    } else {
                        CircularLayout.this.balancingTimer.cancel();
                        balancingTimer.purge();
                        adapter.get(currentStep).onFocus();
                        isRotatingHappening = false;
                    }
                }
            });
        }
    }
}
