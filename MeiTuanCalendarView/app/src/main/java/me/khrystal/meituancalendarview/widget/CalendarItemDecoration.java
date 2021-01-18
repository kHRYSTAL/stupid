package me.khrystal.meituancalendarview.widget;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 2021/1/6
 * update time:
 * email: 723526676@qq.com
 */

public class CalendarItemDecoration extends RecyclerView.ItemDecoration {
    private Paint mPaint;
    private Paint colorPaint;
    private Paint linePaint;

    public CalendarItemDecoration() {
        mPaint = new Paint();
        colorPaint = new Paint();
        linePaint = new Paint();

        mPaint.setColor(Color.parseColor("#ffffff"));
        mPaint.setStyle(Paint.Style.FILL);
        colorPaint.setColor(Color.parseColor("#ff6600"));
        colorPaint.setAntiAlias(true);
        linePaint.setColor(Color.parseColor("#dddddd"));
        linePaint.setAntiAlias(true);
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        if (parent.getChildCount() <= 0) {
            return;
        }

        int height = 50;
        final float scale = parent.getContext().getResources().getDisplayMetrics().density;
        height = (int) (height * scale +0.5f);

    }
}
