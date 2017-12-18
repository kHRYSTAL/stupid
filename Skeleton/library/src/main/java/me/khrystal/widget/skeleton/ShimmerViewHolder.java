package me.khrystal.widget.skeleton;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import me.khrystal.widget.R;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/12/18
 * update time:
 * email: 723526676@qq.com
 */

public class ShimmerViewHolder extends RecyclerView.ViewHolder {

    public ShimmerViewHolder(LayoutInflater inflater, ViewGroup parent, int innerViewResId) {
        super(inflater.inflate(R.layout.layout_shimmer, parent, false));
        ViewGroup layout = (ViewGroup) itemView;
        inflater.inflate(innerViewResId, layout, true);
    }
}
