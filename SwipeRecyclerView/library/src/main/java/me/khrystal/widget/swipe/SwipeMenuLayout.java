package me.khrystal.widget.swipe;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.OverScroller;
import android.widget.TextView;


import me.khrystal.library.R;

public class SwipeMenuLayout extends FrameLayout implements SwipeSwitch {

    public static final int DEFAULT_SCROLLER_DURATION = 200;
    private static SwipeMenuLayout mSwipeCache;

    private int mRightViewId = 0;
    private int mContentViewId = 0;

    private float mOpenPercent = 0.5f;
    private int mScrollerDuration = DEFAULT_SCROLLER_DURATION;
    private int mScaledTouchSlop;
    private int mLastX;
    private int mLastY;
    private int mDownX;
    private int mDownY;

    private View mContentView;
    private SwipeRightHorizontal mSwipeRightHorizontal;
    private SwipeHorizontal mSwipeCurrentHorizontal;

    private boolean shouldResetSwipe;
    private boolean mDragging;
    private boolean swipeEnable = true;
    private OverScroller mScroller;
    private VelocityTracker mVelocityTracker;
    private int mScaledMinimumFlingVelocity;
    private int mScaledMaximumFlingVelocity;
    private OnSwipeListener mListener;

    public SwipeMenuLayout(Context context) {
        this(context, null);
    }

    public SwipeMenuLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeMenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SwipeMenuLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(attrs);
    }

    private void initView(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SwipeMenuLayout);
        mContentViewId = typedArray.getResourceId(R.styleable.SwipeMenuLayout_contentViewId, mContentViewId);
        mRightViewId = typedArray.getResourceId(R.styleable.SwipeMenuLayout_rightViewId, mRightViewId);
        typedArray.recycle();

        ViewConfiguration mViewConfig = ViewConfiguration.get(getContext());
        mScaledTouchSlop = mViewConfig.getScaledTouchSlop();
        mScroller = new OverScroller(getContext());
        mScaledMinimumFlingVelocity = mViewConfig.getScaledMinimumFlingVelocity();
        mScaledMaximumFlingVelocity = mViewConfig.getScaledMaximumFlingVelocity();
        setClickable(true);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (mRightViewId != 0 && mSwipeRightHorizontal == null) {
            View view = findViewById(mRightViewId);
            mSwipeRightHorizontal = new SwipeRightHorizontal(view);
        }
        if (mContentViewId != 0 && mContentView == null) {
            mContentView = findViewById(mContentViewId);
        } else {
            TextView errorView = new TextView(getContext());
            errorView.setClickable(true);
            errorView.setGravity(Gravity.CENTER);
            errorView.setTextSize(16);
            errorView.setText("You may not have set the ContentView.");
            mContentView = errorView;
            addView(mContentView);
        }
    }

    /**
     * Set whether open swipe. Default is true.
     *
     * @param swipeEnable true open, otherwise false.
     */
    public void setSwipeEnable(boolean swipeEnable) {
        this.swipeEnable = swipeEnable;
    }

    /**
     * Open the swipe function of the Item?
     *
     * @return open is true, otherwise is false.
     */
    public boolean isSwipeEnable() {
        return swipeEnable;
    }

    /**
     * Set open percentage.
     *
     * @param openPercent such as 0.5F.
     */
    public void setOpenPercent(float openPercent) {
        this.mOpenPercent = openPercent;
    }

    /**
     * Get open percentage.
     *
     * @return such as 0.5F.
     */
    public float getOpenPercent() {
        return mOpenPercent;
    }

    /**
     * The duration of the set.
     *
     * @param scrollerDuration such as 500.
     */
    public void setScrollerDuration(int scrollerDuration) {
        this.mScrollerDuration = scrollerDuration;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                if (mSwipeCache != null) {
                    if (mSwipeCache != this) {
                        mSwipeCache.smoothCloseMenu();
                    }
                }
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean isIntercepted = super.onInterceptTouchEvent(ev);
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mDownX = mLastX = (int) ev.getX();
                mDownY = (int) ev.getY();
                isIntercepted = false;
                break;
            case MotionEvent.ACTION_MOVE:
                int disX = (int) (ev.getX() - mDownX);
                int disY = (int) (ev.getY() - mDownY);
                isIntercepted = Math.abs(disX) > mScaledTouchSlop && Math.abs(disX) > Math.abs(disY);
                break;
            case MotionEvent.ACTION_UP:
                isIntercepted = false;
                if (isMenuOpen() && mSwipeCurrentHorizontal.isClickOnContentView(getWidth(), ev.getX())) {
                    smoothCloseMenu();
                    isIntercepted = true;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                isIntercepted = false;
                if (!mScroller.isFinished())
                    mScroller.abortAnimation();
                break;
        }
        return isIntercepted;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mVelocityTracker == null) mVelocityTracker = VelocityTracker.obtain();
        mVelocityTracker.addMovement(ev);
        int dx;
        int dy;
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastX = (int) ev.getX();
                mLastY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isSwipeEnable()) break;
                int disX = (int) (mLastX - ev.getX());
                int disY = (int) (mLastY - ev.getY());
                if (!mDragging && Math.abs(disX) > mScaledTouchSlop && Math.abs(disX) > Math.abs(disY)) {
                    mDragging = true;
                }
                if (mDragging) {
                    if (mSwipeCurrentHorizontal == null || shouldResetSwipe) {
                        mSwipeCurrentHorizontal = mSwipeRightHorizontal;
                    }
                    scrollBy(disX, 0);
                    mLastX = (int) ev.getX();
                    mLastY = (int) ev.getY();
                    shouldResetSwipe = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                dx = (int) (mDownX - ev.getX());
                dy = (int) (mDownY - ev.getY());
                mDragging = false;
                mVelocityTracker.computeCurrentVelocity(1000, mScaledMaximumFlingVelocity);
                int velocityX = (int) mVelocityTracker.getXVelocity();
                int velocity = Math.abs(velocityX);
                if (velocity > mScaledMinimumFlingVelocity) {
                    if (mSwipeCurrentHorizontal != null) {
                        int duration = getSwipeDuration(ev, velocity);
                        if (mSwipeCurrentHorizontal instanceof SwipeRightHorizontal) {
                            if (velocityX < 0) {
                                smoothOpenMenu(duration);
                            } else {
                                smoothCloseMenu(duration);
                            }
                        } else {
                            if (velocityX > 0) {
                                smoothOpenMenu(duration);
                            } else {
                                smoothCloseMenu(duration);
                            }
                        }
                        ViewCompat.postInvalidateOnAnimation(this);
                    }
                } else {
                    judgeOpenClose(dx, dy);
                }
                mVelocityTracker.clear();
                mVelocityTracker.recycle();
                mVelocityTracker = null;
                if (Math.abs(mDownX - ev.getX()) > mScaledTouchSlop || Math.abs(mDownY - ev.getY()) > mScaledTouchSlop || isMenuOpen()) {
                    ev.setAction(MotionEvent.ACTION_CANCEL);
                    super.onTouchEvent(ev);
                    return true;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                mDragging = false;
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                } else {
                    dx = (int) (mDownX - ev.getX());
                    dy = (int) (mDownY - ev.getY());
                    judgeOpenClose(dx, dy);
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * compute finish duration.
     *
     * @param ev       up event.
     * @param velocity velocity x.
     * @return finish duration.
     */
    private int getSwipeDuration(MotionEvent ev, int velocity) {
        int sx = getScrollX();
        int dx = (int) (ev.getX() - sx);
        final int width = mSwipeCurrentHorizontal.getMenuWidth();
        final int halfWidth = width / 2;
        final float distanceRatio = Math.min(1f, 1.0f * Math.abs(dx) / width);
        final float distance = halfWidth + halfWidth * distanceInfluenceForSnapDuration(distanceRatio);
        int duration;
        if (velocity > 0) {
            duration = 4 * Math.round(1000 * Math.abs(distance / velocity));
        } else {
            final float pageDelta = (float) Math.abs(dx) / width;
            duration = (int) ((pageDelta + 1) * 100);
        }
        duration = Math.min(duration, mScrollerDuration);
        return duration;
    }

    float distanceInfluenceForSnapDuration(float f) {
        f -= 0.5f; // center the values about 0.
        f *= 0.3f * Math.PI / 2.0f;
        return (float) Math.sin(f);
    }

    private void judgeOpenClose(int dx, int dy) {
        if (mSwipeCurrentHorizontal != null) {
            if (Math.abs(getScrollX()) >= (mSwipeCurrentHorizontal.getMenuView().getWidth() * mOpenPercent)) { // auto open
                if (Math.abs(dx) > mScaledTouchSlop || Math.abs(dy) > mScaledTouchSlop) { // swipe up
                    if (isMenuOpenNotEqual())
                        smoothCloseMenu();
                    else
                        smoothOpenMenu();
                } else { // normal up
                    if (isMenuOpen())
                        smoothCloseMenu();
                    else smoothOpenMenu();
                }
            } else { // auto close
                smoothCloseMenu();
            }
        }
    }

    @Override
    public void scrollTo(int x, int y) {
        if (mSwipeCurrentHorizontal == null) {
            super.scrollTo(x, y);
        } else {
            SwipeHorizontal.Checker checker = mSwipeCurrentHorizontal.checkXY(x, y);
            shouldResetSwipe = checker.shouldResetSwipe;
            if (checker.x != getScrollX()) {
                super.scrollTo(checker.x, checker.y);
            }
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset() && mSwipeCurrentHorizontal != null) {
            if (mSwipeCurrentHorizontal instanceof SwipeRightHorizontal) {
                scrollTo(Math.abs(mScroller.getCurrX()), 0);
                invalidate();
            } else {
                scrollTo(-Math.abs(mScroller.getCurrX()), 0);
                invalidate();
            }
        }
    }

    @Override
    public boolean isMenuOpen() {
        return isRightMenuOpen();
    }

    @Override
    public boolean isRightMenuOpen() {
        return mSwipeRightHorizontal != null && mSwipeRightHorizontal.isMenuOpen(getScrollX());
    }

    @Override
    public boolean isMenuOpenNotEqual() {
        return isRightMenuOpenNotEqual();
    }

    @Override
    public boolean isRightMenuOpenNotEqual() {
        return mSwipeRightHorizontal != null && mSwipeRightHorizontal.isMenuOpenNotEqual(getScrollX());
    }

    @Override
    public void smoothOpenRightMenu() {
        smoothOpenRightMenu(mScrollerDuration);
    }

    @Override
    public void smoothOpenRightMenu(int duration) {
        if (mSwipeRightHorizontal != null) {
            mSwipeCurrentHorizontal = mSwipeRightHorizontal;
            smoothOpenMenu(duration);
        }
    }

    @Override
    public void smoothOpenMenu() {
        smoothOpenMenu(mScrollerDuration);
    }

    private void smoothOpenMenu(int duration) {
        mSwipeCache = this;
        if (mListener != null)
            mListener.OnSwipeOpen(this);
        if (mSwipeCurrentHorizontal != null) {
            mSwipeCurrentHorizontal.autoOpenMenu(mScroller, getScrollX(), duration);
            invalidate();
        }
    }

    @Override
    public void smoothCloseRightMenu() {
        if (mSwipeRightHorizontal != null) {
            mSwipeCurrentHorizontal = mSwipeRightHorizontal;
            smoothCloseMenu();
        }
    }

    @Override
    public void smoothCloseMenu(int duration) {
        mSwipeCache = null;
        if (mListener != null)
            mListener.OnSwipeClose(this);
        if (mSwipeCurrentHorizontal != null) {
            mSwipeCurrentHorizontal.autoCloseMenu(mScroller, getScrollX(), duration);
            invalidate();
        }
    }

    @Override
    public void smoothCloseMenu() {
        smoothCloseMenu(mScrollerDuration);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int contentViewHeight = 0;
        if (mContentView != null) {
            int contentViewWidth = mContentView.getMeasuredWidthAndState();
            contentViewHeight = mContentView.getMeasuredHeightAndState();
            LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
            int start = getPaddingLeft();
            int top = getPaddingTop() + lp.topMargin;
            mContentView.layout(start, top, start + contentViewWidth, top + contentViewHeight);
        }

        if (mSwipeRightHorizontal != null) {
            View rightMenu = mSwipeRightHorizontal.getMenuView();
            int menuViewWidth = rightMenu.getMeasuredWidthAndState();
            int menuViewHeight = rightMenu.getMeasuredHeightAndState();
            LayoutParams lp = (LayoutParams) rightMenu.getLayoutParams();
            int top = getPaddingTop() + lp.topMargin;

            int parentViewWidth = getMeasuredWidthAndState();
            rightMenu.layout(parentViewWidth, top, parentViewWidth + menuViewWidth, top + menuViewHeight);
        }
    }


    public void setOnSwipeListener(OnSwipeListener listener) {
        mListener = listener;
    }

    public interface OnSwipeListener {
        void OnSwipeOpen(SwipeMenuLayout layout);
        void OnSwipeClose(SwipeMenuLayout layout);
    }

}
