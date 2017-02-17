package me.khrystal.widget.calendar;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import java.util.HashMap;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/2/16
 * update time:
 * email: 723526676@qq.com
 */

public class CalendarDateView extends ViewPager implements CalendarTopView {

    HashMap<Integer, CalendarView> views = new HashMap<>();
    private CalendarTopViewChangeListener mCalendarLayoutChangeListener;

    public CalendarDateView(Context context) {
        super(context);
    }

    public CalendarDateView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int[] getCurrentSelectPosition() {
        return new int[0];
    }

    @Override
    public int getItemHeight() {
        return 0;
    }

    @Override
    public void setCalendarTopViewChangeListener(CalendarTopViewChangeListener listener) {

    }
}
