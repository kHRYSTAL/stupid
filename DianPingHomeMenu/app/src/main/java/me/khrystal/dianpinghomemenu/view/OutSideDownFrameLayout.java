package me.khrystal.dianpinghomemenu.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Scroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 2020/12/16
 * update time:
 * email: 723526676@qq.com
 */

public class OutSideDownFrameLayout extends FrameLayout {

    private InsideHeaderLayout mInsideLayout;

    public OutSideDownFrameLayout(@NonNull Context context) {
        this(context, null);
    }

    public OutSideDownFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setInsideLayout(InsideHeaderLayout insideLayout) {
        mInsideLayout = insideLayout;
        mInsideLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (positionOffsetPixels == 0)
                    return;
                // TODO: 2020/12/16 需要解决问题: 向下滑动的数值应该以 viewpager内容最多的一页的高度去计算 应该与positionOffsetPixels是成比例的
                scrollTo(0, -(int) (positionOffsetPixels * 0.2f));
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

}
