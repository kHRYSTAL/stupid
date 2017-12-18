package me.khrystal.widget.skeleton;

import android.support.annotation.IntRange;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import io.supercharge.shimmerlayout.ShimmerLayout;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/12/18
 * update time:
 * email: 723526676@qq.com
 */

public class SkeletonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int mItemCount;
    private int mLayoutReference;
    private int mColor;
    private boolean mShimmer;
    private int mShimmerDuration;
    private int mShimmerAngle;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (mShimmer) {
            return new ShimmerViewHolder(inflater, parent, mLayoutReference);
        }
        return new RecyclerView.ViewHolder(inflater.inflate(mLayoutReference, parent, false)){};
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (mShimmer) {
            ShimmerLayout layout = (ShimmerLayout) holder.itemView;
            layout.setShimmerAnimationDuration(mShimmerDuration);
            layout.setShimmerAngle(mShimmerAngle);
            layout.setShimmerColor(mColor);
            layout.startShimmerAnimation();
        }
    }

    @Override
    public int getItemCount() {
        return mItemCount;
    }

    public void setLayoutReference(int layoutReference) {
        this.mLayoutReference = layoutReference;
    }

    public void setItemCount(int itemCount) {
        this.mItemCount = itemCount;
    }

    public void setShimmerColor(int color) {
        this.mColor = color;
    }

    public void shimmer(boolean isShimmer) {
        this.mShimmer = isShimmer;
    }

    public void setShimmerDuration(int shimmerDuration) {
        this.mShimmerDuration = shimmerDuration;
    }

    public void setShimmerAngle(@IntRange(from = 0, to = 30) int shimmerAngle) {
        this.mShimmerAngle = shimmerAngle;
    }
}
