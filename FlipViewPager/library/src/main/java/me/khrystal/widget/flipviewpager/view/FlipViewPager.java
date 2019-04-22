package me.khrystal.widget.flipviewpager.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.LinearInterpolator;
import android.widget.EdgeEffect;
import android.widget.FrameLayout;
import android.widget.Scroller;

import java.util.HashMap;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/4/19
 * update time:
 * email: 723526676@qq.com
 */
public class FlipViewPager extends FrameLayout {

    public static final int FLIP_ANIM_DURATION = 300;
    public static final int FLIP_DISTANCE = 180;
    public static final int FLIP_SHADE_ALPHA = 130;
    public static final int INVALID_POINTER = -1;

    @SuppressLint("UseSparseArrays")
    private final HashMap<Integer, PageItem> pages = new HashMap<>();

    private final PageItem mPrev = new PageItem();
    private final PageItem mCurrent = new PageItem();
    private final PageItem mNext = new PageItem();

    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;
    private EdgeEffect mLeftEdgeEffect;
    private EdgeEffect mRightEdgeEffect;

    private Rect mRightRect = new Rect();
    private Rect mLeftRect = new Rect();
    private Camera mCamera = new Camera();
    private Matrix mMatrix = new Matrix();
    private Paint mShadePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mShinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private int mMinimumVelocity;
    private int mMaximumVelocity;

    private int mPageCount = -1;
    private int mCurrentPageIndex = -1;
    private int mRow = 0;
    private int mMaxItems = 0;

    private boolean flipping;
    private boolean overFlipping;

    private float mFlipDistance = -1;
    private int mTouchSlop;

    private float mLastMotionX = -1;
    private float mLastMotionY = -1;

    private int mActivePointerId = INVALID_POINTER;

    private OnChangePageListener onChangePageListener;

    public FlipViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mScroller = new Scroller(getContext(), new LinearInterpolator());
        mTouchSlop = configuration.getScaledPagingTouchSlop();
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        mLeftEdgeEffect = new EdgeEffect(getContext());
        mRightEdgeEffect = new EdgeEffect(getContext());
        mShadePaint.setColor(Color.BLACK);
        mShinePaint.setColor(Color.WHITE);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mLeftRect.set(0, 0, getWidth() / 2, getHeight());
        mRightRect.set(getWidth() / 2, 0, getWidth(), getHeight());
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        // flush data of canvas
        if (!mScroller.isFinished() && mScroller.computeScrollOffset()) {
            setFlipDistance(mScroller.getCurrY());
        }

        if (flipping || !mScroller.isFinished()) {
            // Drawing prev half
            canvas.save();
            canvas.clipRect(mLeftRect);
            PageItem leftPage = getDegressDone() >= 90 ? mPrev : mCurrent;
            drawChild(canvas, leftPage.pageView, 0);
            canvas.restore();
            // Drawing next half
            canvas.save();
            canvas.clipRect(mRightRect);
            PageItem rightPage = getDegressDone() >= 90 ? mCurrent : mNext;
            drawChild(canvas, rightPage.pageView, 0);
            canvas.restore();
            // Drawing rotation
            drawFlippingHalf(canvas);
        } else {
            mScroller.abortAnimation();
            drawChild(canvas, mCurrent.pageView, 0);
            if (onChangePageListener != null) {
                onChangePageListener.onFlipped(mCurrentPageIndex);
            }
        }
        if (drawEdges(canvas)) {
            invalidate();
        }
    }

    public void setOnChangePageListener(OnChangePageListener onChangePageListener) {
        this.onChangePageListener = onChangePageListener;
    }

    private void drawFlippingHalf(Canvas canvas) {
        canvas.save();
        mCamera.save();
        canvas.clipRect(getDegressDone() > 90 ? mLeftRect : mRightRect);
        mCamera.rotateY(getDegressDone() > 90 ? 180 - getDegressDone() : -getDegressDone());
        mCamera.getMatrix(mMatrix);

        mMatrix.preScale(0.25f, 0.25f);
        mMatrix.postScale(4.0f, 4.0f);
        mMatrix.preTranslate(-getWidth() / 2, -getHeight() / 2);
        mMatrix.postTranslate(getWidth() / 2, getHeight() / 2);

        canvas.concat(mMatrix);
        drawChild(canvas, mCurrent.pageView, 0);
        drawFlippingShadeShine(canvas);
        mCamera.restore();
        canvas.restore();
    }

    private void drawFlippingShadeShine(Canvas canvas) {
        if (getDegressDone() < 90) {
            mShinePaint.setAlpha((int) (getDegressDone() / 90) * FLIP_SHADE_ALPHA);
            canvas.drawRect(mRightRect, mShinePaint);
        } else {
            mShinePaint.setAlpha((int) ((Math.abs(getDegressDone() - 180) / 90f) * FLIP_SHADE_ALPHA));
            canvas.drawRect(mLeftRect, mShadePaint);
        }
    }

    private float getDegressDone() {
        float flipDistance = mFlipDistance % FLIP_DISTANCE;
        if (flipDistance < 0) {
            flipDistance += FLIP_DISTANCE;
        }
        return (flipDistance / FLIP_DISTANCE) * 180;
    }

    public boolean drawEdges(Canvas canvas) {
        boolean shouldContinue = false;
        boolean rightNotFinished = !mRightEdgeEffect.isFinished();
        if (rightNotFinished || !mLeftEdgeEffect.isFinished()) {
            canvas.save();
            mRightEdgeEffect.setSize(getHeight(), getWidth());
            canvas.rotate(rightNotFinished ? 270 : 90);
            canvas.translate(rightNotFinished ? -getHeight() : 0, rightNotFinished ? 0 : -getWidth());
            shouldContinue = rightNotFinished ? mRightEdgeEffect.draw(canvas) : mLeftEdgeEffect.draw(canvas);
            canvas.restore();
        }
        return shouldContinue;
    }

    // update data of flip view
    private void setFlipDistance(float flipDistance) {
        if (flipDistance == mFlipDistance) return;
        mFlipDistance = flipDistance;
        int currentPageIndex = Math.round(mFlipDistance / FLIP_DISTANCE);
        if (mCurrentPageIndex != currentPageIndex) {
            mCurrentPageIndex = currentPageIndex;
            recycleActiveViews();
            if (mCurrentPageIndex > 0) {
                mPrev.fill(mCurrentPageIndex - 1);
            }
            if (mCurrentPageIndex >= 0 && mCurrentPageIndex < mPageCount) {
                mCurrent.fill(mCurrentPageIndex);
            }
            if (mCurrentPageIndex < mPageCount - 1) {
                mNext.fill(mCurrentPageIndex + 1);
            }
            invalidate();
        }
    }

    private void recycleActiveViews() {
        mPrev.recycle();
        mCurrent.recycle();
        mNext.recycle();
    }

    // Internal interface to store page position
    public interface OnChangePageListener {
        public void onFlipped(int page);
    }

    class PageItem {
        View pageView;

        void recycle() {
            removeView(pageView);
        }

        void fill(int i) {
            pageView = pages.get(i).pageView;
            addView(pageView);
        }
    }
}
