package me.khrystal.dianpinghomemenu.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import me.khrystal.dianpinghomemenu.util.DensityUtil;

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
                // item 高度是60dp 两行是120dp
                // 假设第二页有4行item 那应该是240dp
                // 此布局滑动距离为 viewpager最大展示高度 - 最小展示两行高度 / 屏幕宽度 * viewpager滑动距离
                scrollTo(0, -(DensityUtil.dp2px(getContext(), 120)) * positionOffsetPixels / DensityUtil.getWidth(getContext()) );
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
