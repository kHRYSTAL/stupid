package me.khrystal.widget.flipviewpager.view;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.VelocityTracker;
import android.view.View;
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

    public FlipViewPager(@NonNull Context context) {
        super(context);
    }

    public FlipViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FlipViewPager(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FlipViewPager(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
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
