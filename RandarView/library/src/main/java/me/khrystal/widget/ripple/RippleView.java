package me.khrystal.widget.ripple;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/5/8
 * update time:
 * email: 723526676@qq.com
 */

public class RippleView extends View implements Runnable {

    private int mWidth, mHeight;//整个图形的长度和宽度
    private Paint mPaintBg;//画圆线需要用到的paint
    private int mMaxRadius;
    private boolean startRipple = false;//只有设置了数据后才会开始扫描
    private int mInterval = 80;
    private int count = 0;
    private Paint mRipplePaint;
    private Paint mCirclePaint;

    //每个圆圈所占的比例
    private static float[] circleProportion = {1 / 13f, 2 / 13f, 3 / 13f, 4 / 13f, 5 / 13f, 6 / 13f};

    public RippleView(Context context) {
        this(context, null);
    }

    public RippleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RippleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        mPaintBg = new Paint();
        mPaintBg.setAntiAlias(true);
        mPaintBg.setStyle(Paint.Style.FILL);
        mPaintBg.setColor(Color.GRAY);

        mRipplePaint = new Paint();
        mRipplePaint.setAntiAlias(true);
        mRipplePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mRipplePaint.setColor(Color.WHITE);
        mRipplePaint.setStrokeWidth(2.0f);

        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setColor(Color.WHITE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureSize(widthMeasureSpec), measureSize(widthMeasureSpec));
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mWidth = mHeight = Math.min(mWidth, mHeight);
        mMaxRadius = mWidth / 2;

    }

    private int measureSize(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = 300;
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        final float px =  getWidth() / 2;
        final float py = getHeight() / 2;
        final float rw = getWidth() / 6;
        //保存画布当前的状态
        int save = canvas.save();
        for (int step = count; step <= mMaxRadius; step += mInterval) {
            //step越大越靠外就越透明
            mRipplePaint.setAlpha(255 * (mMaxRadius - step) / mMaxRadius);
            canvas.drawCircle(px, py, (float) (rw + step), mRipplePaint);
        }
        //恢复Canvas的状态
        canvas.restoreToCount(save);
        drawCenterIcon(canvas);
        postDelayed(this, 80);
    }

    /**
     * 绘制圆形背景
     *
     * @param canvas
     */
    private void drawCenterIcon(Canvas canvas) {
        canvas.drawCircle(mWidth / 2, mHeight / 2, mWidth * circleProportion[1], mPaintBg);
    }

    @Override
    public void run() {
        if (startRipple) {
            //把run对象的引用从队列里拿出来，这样，他就不会执行了，但 run 没有销毁
            removeCallbacks(this);
            count += 2;
            count %= mInterval;
            invalidate();//重绘
        }
    }

    public void startRipple() {
        startRipple = true;
        post(this);
    }

    public void stopRipple() {
        startRipple = false;
    }
}
