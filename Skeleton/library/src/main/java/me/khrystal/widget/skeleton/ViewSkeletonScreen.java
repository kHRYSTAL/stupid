package me.khrystal.widget.skeleton;

import android.support.annotation.ColorRes;
import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import io.supercharge.shimmerlayout.ShimmerLayout;
import me.khrystal.widget.R;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/12/18
 * update time:
 * email: 723526676@qq.com
 */

public class ViewSkeletonScreen implements SkeletonScreen {

    private static final String TAG = ViewSkeletonScreen.class.getSimpleName();
    private final ViewReplacer mViewReplacer;
    private final View mActualView;
    private final int mSkeletonResId;
    private final int mShimmerColor;
    private final boolean mShimmer;
    private final int mShimmerDuration;
    private final int mShimmerAngle;

    private ViewSkeletonScreen(Builder builder) {
        mActualView = builder.mView;
        mSkeletonResId = builder.mSkeletonLayoutResId;
        mShimmer = builder.mShimmer;
        mShimmerDuration = builder.mShimmerDuration;
        mShimmerAngle = builder.mShimmerAngle;
        mShimmerColor = builder.mShimmerColor;
        mViewReplacer = new ViewReplacer(builder.mView);
    }


    private ShimmerLayout generateShimmerContainerLayout(ViewGroup parentView) {
        ShimmerLayout shimmerLayout = (ShimmerLayout) LayoutInflater.from(mActualView.getContext()).inflate(R.layout.layout_shimmer, parentView, false);
        shimmerLayout.setShimmerColor(mShimmerColor);
        shimmerLayout.setShimmerAngle(mShimmerAngle);
        shimmerLayout.setShimmerAnimationDuration(mShimmerDuration);
        View innerView = LayoutInflater.from(mActualView.getContext()).inflate(mSkeletonResId, shimmerLayout, false);
        shimmerLayout.addView(innerView);
        shimmerLayout.startShimmerAnimation();
        return shimmerLayout;
    }

    private View generateSkeletonLoadingView() {
        ViewParent viewParent = mActualView.getParent();
        if (viewParent == null) {
            Log.e(TAG, "the source view have not attach to any view");
            return null;
        }
        ViewGroup parentView = (ViewGroup) viewParent;
        if (mShimmer) {
            return generateShimmerContainerLayout(parentView);
        }
        return LayoutInflater.from(mActualView.getContext()).inflate(mSkeletonResId, parentView, false);
    }

    @Override
    public void show() {
        View skeletonLoadingView = generateSkeletonLoadingView();
        if (skeletonLoadingView != null) {
            mViewReplacer.replace(skeletonLoadingView);
        }
    }

    @Override
    public void hide() {
        mViewReplacer.restore();
    }

    public static class Builder {
        private final View mView;
        private int mSkeletonLayoutResId;
        private boolean mShimmer = true;
        private int mShimmerColor;
        private int mShimmerDuration = 1000;
        private int mShimmerAngle = 20;

        public Builder(View view) {
            this.mView = view;
            this.mShimmerColor = ContextCompat.getColor(mView.getContext(), R.color.shimmer_color);
        }

        public Builder load(@LayoutRes int skeletonLayoutResId) {
            this.mSkeletonLayoutResId = skeletonLayoutResId;
            return this;
        }

        public Builder color(@ColorRes int shimmerColor) {
            this.mShimmerColor = ContextCompat.getColor(mView.getContext(), shimmerColor);
            return this;
        }

        public ViewSkeletonScreen.Builder shimmer(boolean shimmer) {
            this.mShimmer = shimmer;
            return this;
        }

        public ViewSkeletonScreen.Builder duration(int shimmerDuration) {
            this.mShimmerDuration = shimmerDuration;
            return this;
        }

        public ViewSkeletonScreen.Builder angle(@IntRange(from = 0, to = 30) int shimmerAngle) {
            this.mShimmerAngle = shimmerAngle;
            return this;
        }

        public ViewSkeletonScreen show() {
            ViewSkeletonScreen skeletonScreen = new ViewSkeletonScreen(this);
            skeletonScreen.show();
            return skeletonScreen;
        }

    }
}
