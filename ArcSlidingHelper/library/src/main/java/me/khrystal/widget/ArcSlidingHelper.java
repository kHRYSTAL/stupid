package me.khrystal.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewParent;
import android.widget.Scroller;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/3/22
 * update time:
 * email: 723526676@qq.com
 */
public class ArcSlidingHelper {

    private static final String TAG = ArcSlidingHelper.class.getSimpleName();

    private int mPivotX, mPivotY;
    private float mStartX, mStartY;
    private float mLastScrollOffset;
    private float mScrollAvailabilityRatio;
    private boolean isSelfSliding;
    private boolean isInertialSlidingEnable;
    private boolean isClockwiseScrolling;

    private boolean isShouldBeGetY;
    private boolean isRecycled;
    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;

    private OnSlidingListener mSlidingListener;
    private OnSlideFinishListener mSlideFinishListener;
    private InertialSlidingHandler mHandler;

    public static ArcSlidingHelper create(@NonNull View targetView, @NonNull OnSlidingListener listener) {
        int width = targetView.getWidth();
        int height = targetView.getHeight();
        if (width <= 0) {
            Log.e(TAG, "targetView width <= 0! please invoke the updatePivotX(int) method to update the PivotX!", new RuntimeException());
        }
        if (height <= 0) {
            Log.e(TAG, "targetView height <= 0! please invoke the updatePivotY(int) method to update the PivotY!", new RuntimeException());
        }
        width /= 2;
        height /= 2;
        int x = (int) getAbsoluteX(targetView);
        int y = (int) getAbsoluteY(targetView);
        return new ArcSlidingHelper(targetView.getContext(), x + width, y + height, listener);
    }

    private ArcSlidingHelper(Context context, int pivotX, int pivotY, OnSlidingListener listener) {
        mPivotX = pivotX;
        mPivotY = pivotY;
        mSlidingListener = listener;
        mScroller = new Scroller(context);
        mVelocityTracker = VelocityTracker.obtain();
        mScrollAvailabilityRatio = .3F;
        mHandler = new InertialSlidingHandler(this);
    }

    /**
     * 设置自身滑动
     *
     * @param isSelfSliding
     */
    public void setSelfSliding(boolean isSelfSliding) {
        checkIsRecycled();
        this.isSelfSliding = isSelfSliding;
    }

    /**
     * 设置惯性滑动
     *
     * @param enable
     */
    public void enableInertialSliding(boolean enable) {
        checkIsRecycled();
        isInertialSlidingEnable = enable;
    }

    public void handleMovement(MotionEvent event) {
        checkIsRecycled();
        float x, y;
        if (isSelfSliding) {
            x = event.getRawX();
            y = event.getRawY();
        } else {
            x = event.getX();
            y = event.getY();
        }
        mVelocityTracker.addMovement(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                handleActionMove(x, y);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
                if (isInertialSlidingEnable) {
                    // 单位为 1000毫秒内移动的像素
                    mVelocityTracker.computeCurrentVelocity(1000);
                    mScroller.fling(0, 0, (int) mVelocityTracker.getXVelocity(), (int) mVelocityTracker.getYVelocity(),
                            Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
                    startFling();
                }
                break;
            default:
                break;
        }
        mStartX = x;
        mStartY = y;
    }

    public void abortAnimation() {
        checkIsRecycled();
        if (!mScroller.isFinished()) {
            mScroller.abortAnimation();
        }
    }

    /**
     * 更新当前手指触摸的坐标，在ViewGroup的onInterceptTouchEvent中使用
     */
    public void updateMovement(MotionEvent event) {
        checkIsRecycled();
        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
            if (isSelfSliding) {
                mStartX = event.getRawX();
                mStartY = event.getRawY();
            } else {
                mStartX = event.getX();
                mStartY = event.getY();
            }
        }
    }

    //region 更新圆心坐标
    public void updatePivotX(int pivotX) {
        checkIsRecycled();
        mPivotX = pivotX;
    }

    public void updatePivotY(int pivotY) {
        checkIsRecycled();
        mPivotY = pivotY;
    }
    //endregion

    /**
     * 设置惯性滑动利用率
     * @param ratio
     */
    public void setScrollAvailabilityRatio(@FloatRange(from = 0, to = 1) float ratio) {
        checkIsRecycled();
        mScrollAvailabilityRatio = ratio;
    }

    public void release() {
        checkIsRecycled();
        mScroller = null;
        mVelocityTracker.recycle();
        mVelocityTracker = null;
        mSlidingListener = null;
        mHandler = null;
        isRecycled = true;
    }

    /**
     * 计算滑动的角度
     */
    private void handleActionMove(float x, float y) {
        // TODO: 19/3/27
    }

    /**
     * 处理惯性滑动
     */
    private void computeInertialSliding() {
        checkIsRecycled();
        // TODO: 19/3/27
    }

    private void startFling() {
        mHandler.sendEmptyMessage(0);
    }

    private void checkIsRecycled() {
        if (isRecycled) {
            throw new IllegalStateException("ArcSlidingHelper is recycled");
        }
    }

    /**
     * 获取view在屏幕中的绝对x坐标
     */
    private static float getAbsoluteX(View view) {
        float x = view.getX();
        ViewParent parent = view.getParent();
        if (parent != null && parent instanceof View) {
            x += getAbsoluteX((View) parent);
        }
        return x;
    }

    /**
     * 获取view在屏幕中的绝对y坐标
     */
    private static float getAbsoluteY(View view) {
        float y = view.getY();
        ViewParent parent = view.getParent();
        if (parent != null && parent instanceof View) {
            y += getAbsoluteY((View) parent);
        }
        return y;
    }



    public void setOnSlideFinishListener(OnSlideFinishListener listener) {
        mSlideFinishListener = listener;
    }

    public interface OnSlidingListener {

        /**
         * @param angle 本次滑动角度
         */
        void onSliding(float angle);
    }

    public interface OnSlideFinishListener {
        /**
         * 滚动完毕
         */
        void onSlideFinished();
    }

    /**
     * 主线程回调惯性滑动
     */
    private static class InertialSlidingHandler extends Handler {
        ArcSlidingHelper mHelper;

        public InertialSlidingHandler(ArcSlidingHelper helper) {
            this.mHelper = helper;
        }

        @Override
        public void handleMessage(Message msg) {
            mHelper.computeInertialSliding();
        }
    }
}
