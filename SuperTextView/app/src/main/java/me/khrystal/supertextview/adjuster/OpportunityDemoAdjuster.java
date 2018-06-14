package me.khrystal.supertextview.adjuster;

import android.graphics.Canvas;
import android.graphics.Paint;

import me.khrystal.supertextview.R;
import me.khrystal.widget.supertextview.SuperTextView;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/6/14
 * update time:
 * email: 723526676@qq.com
 */

public class OpportunityDemoAdjuster extends SuperTextView.Adjuster {

    private float density;
    private Paint paint;

    public OpportunityDemoAdjuster() {
        initPaint();
    }

    private void initPaint() {
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    @Override
    protected void adjust(SuperTextView v, Canvas canvas) {
        int width = v.getWidth();
        int height = v.getHeight();
        if (density == 0) {
            density = v.getResources().getDisplayMetrics().density;
        }
        paint.setColor(v.getResources().getColor(R.color.colorPrimary));
        canvas.drawCircle(width / 2, height / 2, 30 * density, paint);
    }
}
