package me.khrystal.partofcircleview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 2020/11/23
 * update time:
 * email: 723526676@qq.com
 */
public class PartOfCircleView extends View {

    private int size;

    public PartOfCircleView(Context context) {
        super(context);
    }

    public PartOfCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PartOfCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        size = Math.min(widthSize, heightSize);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        //设置贝塞尔
        Path mPath = new Path();

        Point startPoint = new Point((int) (5 / 192.0f * size), (int) (size * 55 / 96.0f));
        Point endPoint = new Point((int) (size - 5 / 192.0f), (int) (size * 55 / 96.0f));//设置终点（可以自己定）
        Point assistPoint = new Point(size / 2, size);//设置中间辅助切线点
        // 起点
        mPath.moveTo(startPoint.x, startPoint.y);
        // 开始画贝塞尔曲线
        mPath.quadTo(assistPoint.x, assistPoint.y, endPoint.x, endPoint.y);
        // 画路径
        canvas.drawPath(mPath, paint);

    }

    public int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
