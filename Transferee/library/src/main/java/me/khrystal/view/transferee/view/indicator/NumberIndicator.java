package me.khrystal.view.transferee.view.indicator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

/**
 * usage: 数字索引指示器
 * author: kHRYSTAL
 * create time: 18/3/13
 * update time:
 * email: 723526676@qq.com
 */

@SuppressLint("AppCompatCustomView")
public class NumberIndicator extends TextView {

    private static final String STR_NUM_FORMAT = "%s/%s";

    private ViewPager mViewPager;

    private final ViewPager.OnPageChangeListener mInternalPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            if (mViewPager.getAdapter() == null || mViewPager.getAdapter().getCount() <= 0) {
                return;
            }

            setText(String.format(Locale.getDefault(), STR_NUM_FORMAT,
                    position + 1,
                    mViewPager.getAdapter().getCount()));
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    public NumberIndicator(Context context) {
        this(context, null);
    }

    public NumberIndicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumberIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initNumberIndicator();
    }

    private void initNumberIndicator() {
        setTextColor(Color.WHITE);
        setTextSize(18);
    }

    public void setViewPager(ViewPager viewPager) {
        if (viewPager != null && viewPager.getAdapter() != null) {
            mViewPager = viewPager;
            mViewPager.removeOnPageChangeListener(mInternalPageChangeListener);
            mViewPager.addOnPageChangeListener(mInternalPageChangeListener);
            mInternalPageChangeListener.onPageSelected(mViewPager.getCurrentItem());
        }
    }

}
