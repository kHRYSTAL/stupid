package me.khrystal.view.transferee.style.index;

import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import me.khrystal.view.transferee.style.IIndexIndicator;
import me.khrystal.view.transferee.view.indicator.NumberIndicator;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/3/13
 * update time:
 * email: 723526676@qq.com
 */

public class NumberIndexIndicator implements IIndexIndicator {

    private NumberIndicator numberIndicator;

    @Override
    public void attach(FrameLayout parent) {
        FrameLayout.LayoutParams indexLp= new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        indexLp.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        indexLp.topMargin = 30;
        numberIndicator = new NumberIndicator(parent.getContext());
        numberIndicator.setLayoutParams(indexLp);
        parent.addView(numberIndicator);
    }

    @Override
    public void onShow(ViewPager viewPager) {
        numberIndicator.setVisibility(View.VISIBLE);
        numberIndicator.setViewPager(viewPager);
    }

    @Override
    public void onHide() {
        if (numberIndicator == null)
            return;
        numberIndicator.setVisibility(View.GONE);
    }

    @Override
    public void onRemove() {
        if (numberIndicator == null)
            return;
        ViewGroup vg = (ViewGroup) numberIndicator.getParent();
        if (vg != null) {
            vg.removeView(numberIndicator);
        }
    }
}
