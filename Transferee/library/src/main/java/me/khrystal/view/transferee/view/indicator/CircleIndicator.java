package me.khrystal.view.transferee.view.indicator;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;

import me.khrystal.view.transferee.R;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/3/13
 * update time:
 * email: 723526676@qq.com
 */

public class CircleIndicator extends LinearLayout {

    private final static int DEFAULT_INDICATOR_WIDTH = 5;

    private ViewPager mViewPager;
    private GradientDrawable mIndicatorBackground;

    private Animator mAnimatorOut;
    private Animator mAnimatorIn;
    private Animator mImmediateAnimatorOut;
    private Animator mImmediateAnimatorIn;

    private int mIndicatorMargin = -1;
    private int mIndicatorWidth = -1;
    private int mIndicatorHeight = -1;

    private int mLashPosition = -1;

    private final ViewPager.OnPageChangeListener mInternalPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            if (mViewPager.getAdapter() == null || mViewPager.getAdapter().getCount() <= 0) {
                return;
            }

            if (mAnimatorIn.isRunning()) {
                mAnimatorIn.end();
                mAnimatorIn.cancel();
            }

            if (mAnimatorOut.isRunning()) {
                mAnimatorOut.end();
                mAnimatorOut.cancel();
            }

            View currentIndicator;
            if (mLashPosition >= 0 && (currentIndicator = getChildAt(mLashPosition)) != null) {
                currentIndicator.setBackgroundDrawable(mIndicatorBackground);
                mAnimatorIn.setTarget(currentIndicator);
                mAnimatorIn.start();
            }

            View selectedIndicator = getChildAt(position);
            if (selectedIndicator != null) {
                selectedIndicator.setBackgroundDrawable(mIndicatorBackground);
                mAnimatorOut.setTarget(selectedIndicator);
                mAnimatorOut.start();
            }
            mLashPosition = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    private DataSetObserver mInternalDataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            if (mViewPager == null) {
                return;
            }
            int newCount = mViewPager.getAdapter().getCount();
            int currentCount = getChildCount();
            if (newCount == currentCount) { // no change
                return;
            } else if (mLashPosition < newCount) {
                mLashPosition = mViewPager.getCurrentItem();
            } else {
                mLashPosition = -1;
            }
            createIndicators();
        }
    };

    public CircleIndicator(Context context) {
        this(context, null);
    }

    public CircleIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public CircleIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mIndicatorBackground = new GradientDrawable();
        mIndicatorBackground.setShape(GradientDrawable.OVAL);
        mIndicatorBackground.setColor(Color.WHITE);
        handleTypeArray(context, attrs);
        checkIndicatorConfig(context);
    }

    private void handleTypeArray(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleIndicator);
        mIndicatorWidth =
                typedArray.getDimensionPixelSize(R.styleable.CircleIndicator_ci_width, -1);
        mIndicatorHeight =
                typedArray.getDimensionPixelSize(R.styleable.CircleIndicator_ci_height, -1);
        mIndicatorMargin =
                typedArray.getDimensionPixelSize(R.styleable.CircleIndicator_ci_margin, -1);

        int orientation = typedArray.getInt(R.styleable.CircleIndicator_ci_orientation, -1);
        setOrientation(orientation == VERTICAL ? VERTICAL : HORIZONTAL);

        int gravity = typedArray.getInt(R.styleable.CircleIndicator_ci_gravity, -1);
        setGravity(gravity >= 0 ? gravity : Gravity.CENTER);

        typedArray.recycle();
    }

    /**
     * Create and configure Indicator in Java code.
     */
    public void configureIndicator(int indicatorWidth, int indicatorHeight, int indicatorMargin) {
        mIndicatorWidth = indicatorWidth;
        mIndicatorHeight = indicatorHeight;
        mIndicatorMargin = indicatorMargin;

        checkIndicatorConfig(getContext());
    }

    private void checkIndicatorConfig(Context context) {
        mIndicatorWidth = (mIndicatorWidth < 0) ? dip2px(DEFAULT_INDICATOR_WIDTH) : mIndicatorWidth;
        mIndicatorHeight =
                (mIndicatorHeight < 0) ? dip2px(DEFAULT_INDICATOR_WIDTH) : mIndicatorHeight;
        mIndicatorMargin =
                (mIndicatorMargin < 0) ? dip2px(DEFAULT_INDICATOR_WIDTH) : mIndicatorMargin;

        mAnimatorOut = createAnimatorOut();
        mImmediateAnimatorOut = createAnimatorOut();
        mImmediateAnimatorOut.setDuration(0);

        mAnimatorIn = createAnimatorIn();
        mImmediateAnimatorIn = createAnimatorIn();
        mImmediateAnimatorIn.setDuration(0);
    }

    private Animator createAnimatorOut() {
        ObjectAnimator alphaAnima = ObjectAnimator.ofFloat(null, "alpha", .5f, 1.f);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(null, "scaleX", 1.0f, 1.8f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(null, "scaleY", 1.0f, 1.8f);
        AnimatorSet animatorOut = new AnimatorSet();
        animatorOut.play(alphaAnima).with(scaleX).with(scaleY);
        return animatorOut;
    }

    private Animator createAnimatorIn() {
        Animator animatorIn = createAnimatorOut();
        animatorIn.setInterpolator(new ReverseInterpolator());
        return animatorIn;
    }

    public void setViewPager(ViewPager viewPager) {
        mViewPager = viewPager;
        if (mViewPager != null && mViewPager.getAdapter() != null) {
            mLashPosition = -1;
            createIndicators();
            mViewPager.removeOnPageChangeListener(mInternalPageChangeListener);
            mViewPager.addOnPageChangeListener(mInternalPageChangeListener);
            mInternalPageChangeListener.onPageSelected(mViewPager.getCurrentItem());
        }
    }


    private void createIndicators() {
        removeAllViews();
        int count = mViewPager.getAdapter().getCount();
        if (count <= 0) {
            return;
        }
        int currentItem = mViewPager.getCurrentItem();
        int orientation = getOrientation();
        for (int i = 0; i < count; i++) {
            if (currentItem == i) {
                addIndicator(orientation, mImmediateAnimatorOut);
            } else {
                addIndicator(orientation, mImmediateAnimatorIn);
            }
        }
    }

    private void addIndicator(int orientation, Animator animator) {
        if (animator.isRunning()) {
            animator.end();
            animator.cancel();
        }
        View indicator = new View(getContext());
        indicator.setBackgroundDrawable(mIndicatorBackground);
        addView(indicator, mIndicatorWidth, mIndicatorHeight);
        LayoutParams lp = (LayoutParams) indicator.getLayoutParams();
        if (orientation == HORIZONTAL) {
            lp.leftMargin = mIndicatorMargin;
            lp.rightMargin = mIndicatorMargin;
        } else {
            lp.topMargin = mIndicatorMargin;
            lp.bottomMargin = mIndicatorMargin;
        }
        indicator.setLayoutParams(lp);
        animator.setTarget(indicator);
        animator.start();
    }

    public int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private class ReverseInterpolator implements Interpolator {
        @Override
        public float getInterpolation(float value) {
            return Math.abs(1.0f - value);
        }
    }
}
