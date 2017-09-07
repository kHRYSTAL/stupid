package me.khrystal.widget.viewpager.indicator;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.AnimatorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;

/**
 * usage: viewpager 指示器
 * author: kHRYSTAL
 * create time: 17/9/7
 * update time:
 * email: 723526676@qq.com
 */

public class CircleIndicator extends LinearLayout {

    private final static int DEFAULT_INDICATOR_WIDTH = 5;
    private ViewPager mViewPager;
    private int mIndicatorMargin = -1;
    private int mIndicatorWidth = -1;
    private int mIndicatorHeight = -1;

    // 滑出动画
    private int mAnimatorResId = R.animator.scale_with_alpha;
    // 滑入动画 默认为反向的滑出动画
    private int mAnimatorReverseResId = 0;
    // 选中 未选中颜色
    private int mIndicatorBackgroundResId = R.drawable.white_radius;
    private int mIndicatorUnselectedBackgroundResId = R.drawable.white_radius;
    // 后续的滑动indicator动画 默认时长200ms
    private Animator mAnimatorOut;
    private Animator mAnimatorIn;
    // 初始化时的indicator动画 默认时长为0
    private Animator mImmediateAnimatorOut;
    private Animator mImmediateAnimatorIn;

    private int mLastPoistion = -1;

    public CircleIndicator(Context context) {
        super(context);
        init(context, null);
    }

    public CircleIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CircleIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CircleIndicator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        // 处理 xml 配置
        handleTypedArray(context, attrs);
        // 检查 indicator 配置
        checkIndicatorConfig(context);
    }

    private void handleTypedArray(Context context, AttributeSet attrs) {
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

        mAnimatorResId =
                typedArray.getResourceId(R.styleable.CircleIndicator_ci_animator, R.animator.scale_with_alpha);
        mAnimatorReverseResId =
                typedArray.getResourceId(R.styleable.CircleIndicator_ci_animator_reverse, 0);
        mIndicatorBackgroundResId =
                typedArray.getResourceId(R.styleable.CircleIndicator_ci_drawable, R.drawable.white_radius);
        mIndicatorUnselectedBackgroundResId =
                typedArray.getResourceId(R.styleable.CircleIndicator_ci_drawable_unselected,
                        mIndicatorBackgroundResId);
        int orientation = typedArray.getInt(R.styleable.CircleIndicator_ci_orientation, -1);
        setOrientation(orientation == VERTICAL ? VERTICAL : HORIZONTAL);

        int gravity = typedArray.getInt(R.styleable.CircleIndicator_ci_gravity, -1);
        setGravity(gravity >= 0 ? gravity : Gravity.CENTER);

        typedArray.recycle();
    }

    private void checkIndicatorConfig(Context context) {
        mIndicatorWidth = (mIndicatorWidth < 0) ? dip2px(DEFAULT_INDICATOR_WIDTH) : mIndicatorWidth;
        mIndicatorHeight =
                (mIndicatorHeight < 0) ? dip2px(DEFAULT_INDICATOR_WIDTH) : mIndicatorHeight;
        mIndicatorMargin =
                (mIndicatorMargin < 0) ? dip2px(DEFAULT_INDICATOR_WIDTH) : mIndicatorMargin;

        mAnimatorResId = (mAnimatorResId == 0) ? R.animator.scale_with_alpha : mAnimatorResId;
        mAnimatorOut = createAnimatorOut(context);
        mImmediateAnimatorOut = createAnimatorOut(context);
        mImmediateAnimatorOut.setDuration(0);

        mAnimatorIn = createAnimatorIn(context);
        mImmediateAnimatorIn = createAnimatorIn(context);
        mImmediateAnimatorIn.setDuration(0);

        mIndicatorBackgroundResId = (mIndicatorBackgroundResId == 0) ? R.drawable.white_radius
                : mIndicatorBackgroundResId;
        mIndicatorUnselectedBackgroundResId =
                (mIndicatorUnselectedBackgroundResId == 0) ? mIndicatorBackgroundResId
                        : mIndicatorUnselectedBackgroundResId;
    }

    private Animator createAnimatorOut(Context context) {
        return AnimatorInflater.loadAnimator(context, mAnimatorResId);
    }

    private Animator createAnimatorIn(Context context) {
        Animator animatorIn;
        if (mAnimatorReverseResId == 0) {
            // 如果reverse(滑入)动画为空 则反向执行滑出动画
            animatorIn = AnimatorInflater.loadAnimator(context, mAnimatorResId);
            animatorIn.setInterpolator(new ReverseInterpolator());
        } else {
            animatorIn = AnimatorInflater.loadAnimator(context, mAnimatorReverseResId);
        }
        return animatorIn;
    }



    //region public method

    public void setViewPager(ViewPager viewPager) {
        mViewPager = viewPager;
        if (mViewPager != null && mViewPager.getAdapter() != null) {
            mLastPoistion = -1;
            createIndicators();
            // 如果viewpager原来存在mInternalPageChangeListener, 移除
            mViewPager.removeOnPageChangeListener(mInternalPageChangeListener);
            mViewPager.addOnPageChangeListener(mInternalPageChangeListener);
            mInternalPageChangeListener.onPageSelected(mViewPager.getCurrentItem());
        }
    }

    public void configureIndicator(int indicatorWidth, int indicatorHeight, int indicatorMargin) {
        configureIndicator(indicatorWidth, indicatorHeight, indicatorMargin,
                R.animator.scale_with_alpha, 0, R.drawable.white_radius, R.drawable.white_radius);
    }

    public void configureIndicator(int indicatorWidth, int indicatorHeight, int indicatorMargin,
                                   @AnimatorRes int animatorId, @AnimatorRes int animatorReverseId,
                                   @DrawableRes int indicatorBackgroundResId,
                                   @DrawableRes int indicatorUnselectedBackgroundResId) {
        mIndicatorWidth = indicatorWidth;
        mIndicatorHeight = indicatorHeight;
        mIndicatorMargin = indicatorMargin;

        mAnimatorResId = animatorId;
        mAnimatorReverseResId = animatorReverseId;
        mIndicatorBackgroundResId = indicatorBackgroundResId;
        mIndicatorUnselectedBackgroundResId = indicatorUnselectedBackgroundResId;
        checkIndicatorConfig(getContext());
    }
    //endregion

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
            if (mLastPoistion >= 0 && (currentIndicator = getChildAt(mLastPoistion)) != null) {
                currentIndicator.setBackgroundResource(mIndicatorUnselectedBackgroundResId);
                mAnimatorIn.setTarget(currentIndicator);
                mAnimatorIn.start();
            }

            View selectedIndicator = getChildAt(position);
            if (selectedIndicator != null) {
                selectedIndicator.setBackgroundResource(mIndicatorBackgroundResId);
                mAnimatorOut.setTarget(selectedIndicator);
                mAnimatorOut.start();
            }

            mLastPoistion = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

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
                addIndicator(orientation, mIndicatorBackgroundResId, mImmediateAnimatorOut);
            } else {
                addIndicator(orientation, mIndicatorUnselectedBackgroundResId, mImmediateAnimatorIn);
            }
        }
    }

    private void addIndicator(int orientation, @DrawableRes int backgroundDrawableId, Animator animator) {
        if (animator.isRunning()) {
            animator.end();
            animator.cancel();
        }

        View indicator = new View(getContext());
        indicator.setBackgroundResource(backgroundDrawableId);
        addView(indicator, mIndicatorWidth, mIndicatorHeight);
        LayoutParams params = (LayoutParams) indicator.getLayoutParams();

        if (orientation == HORIZONTAL) {
            params.leftMargin = mIndicatorMargin;
            params.rightMargin = mIndicatorMargin;
        } else {
            params.topMargin = mIndicatorMargin;
            params.bottomMargin = mIndicatorMargin;
        }

        indicator.setLayoutParams(params);
        animator.setTarget(indicator);
        animator.start();

    }

    private class ReverseInterpolator implements Interpolator {

        @Override
        public float getInterpolation(float v) {
            return Math.abs(1.0f - v);
        }
    }

    private int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
