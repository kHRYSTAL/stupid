package me.khrystal.widget.calendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import java.util.HashMap;
import java.util.LinkedList;

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
    private CalendarView.OnItemClickListener onItemClickListener;

    private LinkedList<CalendarView> cache = new LinkedList<>();
    private int MAX_COUNT = 6;

    private int row = 6;
    private CalendarAdapter mAdapter;

    public void setAdapter(CalendarAdapter adapter) {
        this.mAdapter = adapter;
        initData();
    }

    public void setOnItemClickListener(CalendarView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public CalendarDateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CalendarDateView);
        row = ta.getInteger(R.styleable.CalendarDateView_cbd_calendar_row, 6);
        ta.recycle();
        init();
    }

    private void init() {

    }

    private void initData() {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

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
