package me.khrystal.widget.swipebacklayout;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.FloatRange;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Set;

import me.khrystal.widget.tools.SwipeBackUtil;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/10/9
 * update time:
 * email: 723526676@qq.com
 */

public class SwipeBackLayout extends ViewGroup {

    private static final String TAG = SwipeBackLayout.class.getSimpleName();

    public static final int FROM_LEFT = 1 << 0;
    public static final int FROM_RIGHT = 1 << 1;
    public static final int FROM_TOP = 1 << 2;
    public static final int FROM_BOTTOM = 1 << 3;

    @IntDef({FROM_LEFT, FROM_TOP, FROM_RIGHT, FROM_BOTTOM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface  DirectionMode {}

    private int mDirectionMode = FROM_LEFT;
    private final ViewDragHelper mDragHelper;
    private View mDragContentView;
    private View innerScrollView;

    private int width, height;
    private int mTouchSlop;
    private float swipeBackFactor = 0.5f;
    private float swipeBackFraction;
    private int maskAlpha;
    private boolean isSwipeFromEdge = false;
    private float downX, downY;

    private int leftOffset = 0;
    private int topOffset = 0;
    private float autoFinishedVelocityLimit = 2000f;

    private OnSwipeBackListener mSwipeBackListener;

    private int touchedEdge = ViewDragHelper.INVALID_POINTER;

    public SwipeBackLayout(Context context) {
        this(context, null);
    }

    public SwipeBackLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeBackLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        mDragHelper = ViewDragHelper.create(this, 1f, new DragHelperCallback());
        mDragHelper.setEdgeTrackingEnabled(mDirectionMode);
        mTouchSlop = mDragHelper.getTouchSlop();
        setSwipeBackListener(defaultSwipeBackListener);
        init(context, attrs);
    }

    /**
     * 初始化xml布局配置
     */
    @SuppressWarnings("ResourceType")
    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SwipeBackLayout);
        setDirectionMode(ta.getInt(R.styleable.SwipeBackLayout_directionMode, mDirectionMode));
        setSwipeBackFactor(ta.getFloat(R.styleable.SwipeBackLayout_swipeBackFactor, swipeBackFactor));
        setMaskAlpha(ta.getInteger(R.styleable.SwipeBackLayout_maskAlpha, maskAlpha));
        isSwipeFromEdge = ta.getBoolean(R.styleable.SwipeBackLayout_isSwipeFromEdge, isSwipeFromEdge);
        ta.recycle();
    }

    /**
     * 将swipebakclayout替换为decorView的子View, 原来的所有内容添加到swipebacklayout中
     * @param activity
     */
    public void attachToActivity(Activity activity) {
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        ViewGroup decorChild = (ViewGroup) decorView.getChildAt(0);
        decorChild.setBackgroundColor(Color.TRANSPARENT);
        decorView.removeView(decorChild);
        addView(decorChild);
        decorView.addView(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();
        if (childCount > 1) {
            throw new IllegalStateException("SwipeBackLayout must contains only one direct child");
        }
        int defaultMeasuredWidth = 0;
        int defaultMeasureHeight = 0;
        int measureWidth;
        int measureHeight;
        if (childCount > 0) {
            measureChildren(widthMeasureSpec, heightMeasureSpec);
            mDragContentView = getChildAt(0);
            defaultMeasuredWidth = mDragContentView.getMeasuredWidth();
            defaultMeasureHeight = mDragContentView.getMeasuredHeight();
        }

        measureWidth = View.resolveSize(defaultMeasuredWidth, widthMeasureSpec) + getPaddingLeft() + getPaddingRight();
        measureHeight = View.resolveSize(defaultMeasureHeight, heightMeasureSpec) + getPaddingTop() + getPaddingBottom();

        setMeasuredDimension(measureWidth, measureHeight);
    }

    @Override
    protected void onLayout(boolean changed, int i, int i1, int i2, int i3) {
        if (getChildCount() == 0)
            return;
        // 手势滑动时会执行onLayout 因此需要加上偏移量 从而在滑动时对dragContentView进行重新布局
        int left = getPaddingLeft() + leftOffset;
        int top  = getPaddingTop() + topOffset;
        int right = left + mDragContentView.getMeasuredWidth();
        int bottom = top + mDragContentView.getMeasuredHeight();

        if (changed) {
            width = getWidth();
            height = getHeight();
        }
        // TODO: 17/10/9 嵌套滑动列表等 只获取外部滑动布局 可能会导致问题
        innerScrollView = SwipeBackUtil.findAllScrollViews(this);
    }

    /**
     * 在滑动过程中 透明背景颜色渐变
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawARGB(maskAlpha - (int) (maskAlpha * swipeBackFraction), 0, 0, 0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (MotionEventCompat.getActionMasked(ev)) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getRawX();
                downY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (innerScrollView != null && SwipeBackUtil.contains(innerScrollView, downX, downY)) {
                    float distanceX = Math.abs(ev.getRawX() - downX);
                    float distanceY = Math.abs(ev.getRawY() - downY);
                    if (mDirectionMode == FROM_LEFT || mDirectionMode == FROM_RIGHT) {
                        // 如果斜向滑动 y轴偏移长度大于x轴偏移长度 不执行swipebacklayout侧滑 不拦截手势
                        if (distanceY > mTouchSlop && distanceY > distanceX) {
                            return super.onInterceptTouchEvent(ev);
                        }
                    } else if (mDirectionMode == FROM_TOP || mDirectionMode == FROM_BOTTOM) {
                        // 如果斜向滑动 x轴偏移长度大于y轴偏移长度 不执行swipebacklayout侧滑 不拦截手势
                        if (distanceX > mTouchSlop && distanceX > distanceY) {
                            return super.onInterceptTouchEvent(ev);
                        }
                    }
                }
                break;
        }
        // 通过draghelper判断是否进行手势拦截
        boolean handled = mDragHelper.shouldInterceptTouchEvent(ev);
        return handled ? handled : super.onInterceptTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // dragHelper处理事件
        // 假如 onInterceptTouchEvent true进行拦截 则会直接执行onTouchEvent
        // 假如 onInterceptTouchEvent false没拦截 则会向子view继续分发触摸事件 如果子view的onTouchEvent 返回true 进行消费
        // 则不会再执行swipeBackLayout 的onTouchEvent 如果都没进行触摸事件的消费 则swipeBackLayout 返回true 进行消费
        mDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public void smoothScrollToX(int finalLeft) {
        if (mDragHelper.settleCapturedViewAt(finalLeft, getPaddingTop())) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public void smoothScrollToY(int finalTop) {
        if (mDragHelper.settleCapturedViewAt(getPaddingLeft(), finalTop)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private class DragHelperCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == mDragContentView;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            leftOffset = getPaddingLeft();
            if (isSwipeEnabled()) {
                if (mDirectionMode == FROM_LEFT && !SwipeBackUtil.canViewScrollRight(innerScrollView, downX, downY, false)) {
                    leftOffset = Math.min(Math.max(left, getPaddingLeft()), width);
                } else if (mDirectionMode == FROM_RIGHT && !SwipeBackUtil.canViewScrollLeft(innerScrollView, downX, downY, false)) {
                    leftOffset = Math.min(Math.max(left, -width), getPaddingRight());
                }
            }
            return leftOffset;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            topOffset = getPaddingTop();
            if (isSwipeEnabled()) {
                if (mDirectionMode == FROM_TOP && !SwipeBackUtil.canViewScrollUp(innerScrollView, downX, downY, false)) {
                    topOffset = Math.min(Math.max(top, getPaddingTop()), height);
                } else if (mDirectionMode == FROM_BOTTOM && !SwipeBackUtil.canViewScrollDown(innerScrollView, downX, downY, false)) {
                    topOffset = Math.min(Math.max(top, -height), getPaddingBottom());
                }
            }
            return topOffset;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            // view位置改变时
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            left = Math.abs(left);
            top = Math.abs(top);
            switch (mDirectionMode) {
                case FROM_LEFT:
                case FROM_RIGHT:
                    swipeBackFraction = 1.0f * left / width;
                    break;
                case FROM_TOP:
                case FROM_BOTTOM:
                    swipeBackFraction = 1.0f * top / height;
                    break;
            }
            if (mSwipeBackListener != null) {
                mSwipeBackListener.onViewPositionChanged(mDragContentView, swipeBackFraction, swipeBackFactor);
            }
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            leftOffset = topOffset = 0;
            if (!isSwipeEnabled()) {
                touchedEdge = ViewDragHelper.INVALID_POINTER;
                return;
            }
            touchedEdge = ViewDragHelper.INVALID_POINTER;
            boolean isBackToEnd = backJudgeBySpeed(xvel, yvel) || swipeBackFraction >= swipeBackFactor;
            if (isBackToEnd) {
                switch (mDirectionMode) {
                    case FROM_LEFT:
                        smoothScrollToX(width);
                        break;
                    case FROM_TOP:
                        smoothScrollToY(height);
                        break;
                    case FROM_RIGHT:
                        smoothScrollToX(-width);
                        break;
                    case FROM_BOTTOM:
                        smoothScrollToY(-height);
                        break;
                }
            } else {
                switch (mDirectionMode) {
                    case FROM_LEFT:
                    case FROM_RIGHT:
                        smoothScrollToX(getPaddingLeft());
                        break;
                    case FROM_BOTTOM:
                    case FROM_TOP:
                        smoothScrollToY(getPaddingTop());
                        break;
                }
            }
        }

        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
            if (state == ViewDragHelper.STATE_IDLE) {
                if (mSwipeBackListener != null) {
                    if (swipeBackFraction == 0) {
                        mSwipeBackListener.onViewSwipeFinished(mDragContentView, false);
                    } else if (swipeBackFraction == 1) {
                        mSwipeBackListener.onViewSwipeFinished(mDragContentView, true);
                    }
                }
            }
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return width;
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return height;
        }

        @Override
        public void onEdgeTouched(int edgeFlags, int pointerId) {
            super.onEdgeTouched(edgeFlags, pointerId);
            touchedEdge = edgeFlags;
        }
    }

    public void finish() {
        ((Activity) getContext()).finish();
    }

    private boolean isSwipeEnabled() {
        if (isSwipeFromEdge) {
            switch (mDirectionMode) {
                case FROM_LEFT:
                    return touchedEdge == ViewDragHelper.EDGE_LEFT;
                case FROM_TOP:
                    return touchedEdge == ViewDragHelper.EDGE_TOP;
                case FROM_RIGHT:
                    return touchedEdge == ViewDragHelper.EDGE_RIGHT;
                case FROM_BOTTOM:
                    return touchedEdge == ViewDragHelper.EDGE_BOTTOM;
            }
        }
        return true;
    }

    private boolean backJudgeBySpeed(float xvel, float yvel) {
        switch (mDirectionMode) {
            case FROM_LEFT:
                return xvel > autoFinishedVelocityLimit;
            case FROM_TOP:
                return yvel > autoFinishedVelocityLimit;
            case FROM_RIGHT:
                return xvel < -autoFinishedVelocityLimit;
            case FROM_BOTTOM:
                return yvel < -autoFinishedVelocityLimit;
        }
        return false;
    }

    public void setSwipeBackFactor(@FloatRange(from = 0.0f, to = 1.0f) float swipeBackFactor) {
        if (swipeBackFactor > 1) {
            swipeBackFactor = 1;
        } else if (swipeBackFactor < 0) {
            swipeBackFactor = 0;
        }
        this.swipeBackFactor = swipeBackFactor;
    }

    public float getSwipeBackFactor() {
        return swipeBackFactor;
    }

    public void setMaskAlpha(@IntRange(from = 0, to = 255) int maskAlpha) {
        if (maskAlpha > 255) {
            maskAlpha = 255;
        } else if (maskAlpha < 0) {
            maskAlpha = 0;
        }
        this.maskAlpha = maskAlpha;
    }

    public int getMaskAlpha() {
        return maskAlpha;
    }

    public void setDirectionMode(@DirectionMode int direction) {
        this.mDirectionMode = direction;
        mDragHelper.setEdgeTrackingEnabled(direction);
    }

    public int getDirectionMode() {
        return mDirectionMode;
    }

    public float getAutoFinishedVelocityLimit() {
        return autoFinishedVelocityLimit;
    }

    public void setAutoFinishedVelocityLimit(float autoFinishedVelocityLimit) {
        this.autoFinishedVelocityLimit = autoFinishedVelocityLimit;
    }

    public boolean isSwipeFromEdge() {
        return isSwipeFromEdge;
    }

    public void setSwipeFromEdge(boolean swipeFromEdge) {
        isSwipeFromEdge = swipeFromEdge;
    }

    private OnSwipeBackListener defaultSwipeBackListener = new OnSwipeBackListener() {
        @Override
        public void onViewPositionChanged(View view, float swipeBackFraction, float swipeBackFactor) {
            invalidate();
        }

        @Override
        public void onViewSwipeFinished(View view, boolean isEnd) {
            if (isEnd) {
                finish();
            }
        }
    };

    public void setSwipeBackListener(OnSwipeBackListener swipeBackListener) {
        mSwipeBackListener = swipeBackListener;
    }

    public interface OnSwipeBackListener {
        void onViewPositionChanged(View view, float swipeBackFraction, float swipeBackFactor);

        void onViewSwipeFinished(View view, boolean isEnd);
    }
}
