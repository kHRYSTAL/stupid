package me.khrystal.fanfoukotlin.view;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/5/26
 * update time:
 * email: 723526676@qq.com
 */

public class FabHideOnScroll extends FloatingActionButton.Behavior {

    public FabHideOnScroll() {
        super();
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        // child -> Floating Action
        if (dyConsumed > 0) {
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            int fabBottomMargin = layoutParams.bottomMargin;
            child.animate().translationY(child.getHeight() + fabBottomMargin)
                    .setInterpolator(new LinearInterpolator()).start();
        } else if (dyConsumed < 0) {
            child.animate().translationY(0).setInterpolator(new LinearInterpolator()).start();
        }
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }
}
