package me.khrystal.widget.calendar;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.Date;
import java.util.List;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/2/16
 * update time:
 * email: 723526676@qq.com
 */

public class CalendarView extends ViewGroup {

    private static final String TAG = "CalendarView";

    private int selectPosition = -1;
    private CalendarAdapter adapter;
    private List<CalendarBean> data;
    private OnItemClickListener onItemClickListener;

    private int row = 6;
    private int column = 7;
    private int itemWidth;
    private int itemHeight;

    private boolean isToday;

    public interface OnItemClickListener {
        void onItemClick(View view, int position, CalendarBean bean);
    }

    public CalendarView(Context context, int row) {
        super(context);
        this.row = row;
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public int getItemHeight() {
        return itemHeight;
    }

    public void setAdapter(CalendarAdapter adapter) {
        this.adapter = adapter;
    }

    public void setData(List<CalendarBean> data, boolean isToday) {
        this.data = data;
        this.isToday = isToday;
        setItem();
        requestLayout();
    }

    private void setItem() {
        selectPosition = -1;
        if (adapter == null) {
            throw new RuntimeException("adapter is null!");
        }
        for (int i = 0; i < data.size(); i++) {
            CalendarBean bean = data.get(i);
            View view = getChildAt(i);
            View childView = adapter.getView(view, this, bean);
            if (view == null || view != childView) {
                addViewInLayout(childView, i, childView.getLayoutParams(), true);
            }

            if (isToday && selectPosition == -1) {
                int[] date = CalendarUtil.getYMD(new Date());
                if (bean.year == date[0] && bean.month == date[1] && bean.day == date[2]) {
                    selectPosition = i;
                }
            } else {
                if (selectPosition == -1 && bean.day == 1) {
                    selectPosition = i;
                }
            }

            childView.setSelected(selectPosition == i);

            setItemClickListener(childView, i, bean);
        }
    }

    private void setItemClickListener(final View childView, final int position, final CalendarBean bean) {
        childView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectPosition != -1) {
                    getChildAt(selectPosition).setSelected(false);
                    getChildAt(position).setSelected(true);
                }
                selectPosition = position;
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(childView, position, bean);
                }
            }
        });
    }

    public Object[] getSelect() {
        return new Object[] {getChildAt(selectPosition), selectPosition, data.get(selectPosition)};
    }

    public int[] getSelectPosition() {
        Rect rect = new Rect();
        try {
            getChildAt(selectPosition).getHitRect(rect);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new int[] {rect.left, rect.top, rect.right, rect.bottom};
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int parentWidth = MeasureSpec.getSize(MeasureSpec.makeMeasureSpec(widthMeasureSpec, MeasureSpec.EXACTLY));

        itemWidth = parentWidth / column;
        itemHeight = itemWidth;

        View view = getChildAt(0);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}
