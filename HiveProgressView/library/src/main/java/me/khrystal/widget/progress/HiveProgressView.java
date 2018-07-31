package me.khrystal.widget.progress;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * usage: HiveProgressBar
 * author: kHRYSTAL
 * create time: 18/7/31
 * update time:
 * email: 723526676@qq.com
 */
public class HiveProgressView extends View {

    private final int DEFAULT_DURATION = 6000;
    // 进度条最大值
    private int maxValue = 100;
    // 进度条当前值
    private float currentValue;
    // 名称
    private String name;
    // level/progress
    private String level;
    // 画笔
    private Paint mPaint;
    private Path mPath;
    private int hiveRadius;
    // 字体大小
    private int mTextSize = dp2px(16);
    private int mLevelSize = dp2px(25);
    private int textPadding = dp2px(5);
    // 进度条宽度
    private int mProgressWidth = dp2px(6);
    private int mWidth, mHeight;

    private OnProgressListener mOnProgressListener;

    // 颜色
    @ColorInt
    private int backgroundColor;
    @ColorInt
    private int startColor;
    @ColorInt
    private int endColor;
    @ColorInt
    private int mCurrentColor;

    private ValueAnimator mProgressAnim;
    private int repeatCount;
    private int mAnimDuration = DEFAULT_DURATION;

    public HiveProgressView(Context context) {
        this(context, null);
    }

    public HiveProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HiveProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.HiveProgressView, defStyleAttr, 0);
        try {
            mTextSize = ta.getDimensionPixelSize(R.styleable.HiveProgressView_barTextSize, dp2px(16));
            mLevelSize = ta.getDimensionPixelSize(R.styleable.HiveProgressView_barLevelSize, dp2px(25));
            textPadding = ta.getDimensionPixelSize(R.styleable.HiveProgressView_textPadding, dp2px(5));
            mProgressWidth = ta.getDimensionPixelSize(R.styleable.HiveProgressView_barWidth, dp2px(6));
            backgroundColor = ta.getColor(R.styleable.HiveProgressView_bgColor, Color.GRAY);
            startColor = ta.getColor(R.styleable.HiveProgressView_startColor, 0xff8380ff);
            endColor = ta.getColor(R.styleable.HiveProgressView_endColor, 0xff8380ff);
            hiveRadius = ta.getDimensionPixelSize(R.styleable.HiveProgressView_hiveRadius, dp2px(60));
            mAnimDuration = ta.getInt(R.styleable.HiveProgressView_animDuration, DEFAULT_DURATION);
            repeatCount = ta.getInt(R.styleable.HiveProgressView_repeatCount, 0);
            maxValue = ta.getInt(R.styleable.HiveProgressView_maxValue, 100);
            currentValue = ta.getInt(R.styleable.HiveProgressView_currentValue, 0);
        } finally {
            ta.recycle();
        }
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        float level = currentValue / maxValue * 6; // 级别
        int centerX = getWidth() / 2;
        int radius = hiveRadius; // 六边形边长

        // 背景
        mPaint.setColor(backgroundColor);
        mPaint.setStyle(Paint.Style.STROKE); // 设置空心
        mPaint.setStrokeWidth(mProgressWidth); // 设置环形的宽度
        mPaint.setPathEffect(new CornerPathEffect(mProgressWidth)); // 圆角形状
        mPath = new Path();
        mPath.moveTo(centerX, (float) (mProgressWidth / Math.sqrt(3))); // A
        mPath.lineTo((float) (centerX + (radius * Math.sqrt(3) / 2)), radius / 2); // B
        mPath.lineTo((float) (centerX + (radius * Math.sqrt(3) / 2)), radius / 2 + radius); // C
        mPath.lineTo(centerX, 2 * radius); // D
        mPath.lineTo((float) (centerX - (radius * Math.sqrt(3) / 2)), radius / 2 + radius); // E
        mPath.lineTo((float) (centerX - (radius * Math.sqrt(3) / 2)), radius / 2); // F
        mPath.close();
        canvas.drawPath(mPath, mPaint);

        if (level != 0) {
            // 画进度
            mPaint = new Paint();
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(mProgressWidth);
            mPaint.setPathEffect(new CornerPathEffect(mProgressWidth)); // 圆角形状
            mPaint.setStrokeCap(Paint.Cap.ROUND); // 线帽
            mPaint.setAntiAlias(true);
            mPath = new Path();
            // 渐变色
            LinearGradient shader = new LinearGradient(mWidth, 0, 0,
                    0, new int[]{startColor, mCurrentColor}, null, Shader.TileMode.MIRROR);
            mPaint.setShader(shader);
            if (level <= 1 && level > 0) {
                float remainder = level;
                mPath.moveTo(centerX, (float) (mProgressWidth / Math.sqrt(3))); // A
                mPath.lineTo((float) (centerX + remainder * (radius * Math.sqrt(3) / 2)), radius / 2 * level); // B-
            } else if (level > 1 && level <= 2) {
                float remainder = level - 1;
                mPath.moveTo(centerX, (float) (mProgressWidth / Math.sqrt(3))); // A
                mPath.lineTo((float) (centerX + (radius * Math.sqrt(3) / 2)), radius / 2); // B
                mPath.lineTo((float) (centerX + (radius * Math.sqrt(3) / 2)), (radius / 2) + radius * remainder); // C-
            } else if (level > 2 && level <= 3) {
                float remainder = level - 2;
                mPath.moveTo(centerX, (float) (mProgressWidth / Math.sqrt(3))); // A
                mPath.lineTo((float) (centerX + (radius * Math.sqrt(3) / 2)), radius / 2); // B
                mPath.lineTo((float) (centerX + (radius * Math.sqrt(3) / 2)), radius / 2 + radius); // C
                mPath.lineTo((float) (centerX + (radius * Math.sqrt(3) / 2) - (radius * Math.sqrt(3) / 2) * remainder), radius / 2 + radius + remainder * radius / 2); // D-
            } else if (level > 3 && level <= 4) {
                float remainder = level - 3;
                mPath.moveTo(centerX, (float) (mProgressWidth / Math.sqrt(3))); // A
                mPath.lineTo((float) (centerX + (radius * Math.sqrt(3) / 2)), radius / 2); // B
                mPath.lineTo((float) (centerX + (radius * Math.sqrt(3) / 2)), radius / 2 + radius); // C
                mPath.lineTo(centerX, 2 * radius); // D
                mPath.lineTo((float) (centerX - (radius * Math.sqrt(3) / 2 * remainder)), 2 * radius - radius / 2 * remainder); // E-
            } else if (level > 4 && level <= 5) {
                float remainder = level - 4;
                mPath.moveTo(centerX, (float) (mProgressWidth / Math.sqrt(3))); // A
                mPath.lineTo((float) (centerX + (radius * Math.sqrt(3) / 2)), radius / 2); // B
                mPath.lineTo((float) (centerX + (radius * Math.sqrt(3) / 2)), radius / 2 + radius); // C
                mPath.lineTo(centerX, 2 * radius); // D
                mPath.lineTo((float) (centerX - (radius * Math.sqrt(3) / 2)), radius / 2 + radius); // E
                mPath.lineTo((float) (centerX - (radius * Math.sqrt(3) / 2)), radius / 2 + radius - radius * remainder); // F-
            } else if (level > 5 && level <= 6) {
                float remainder = level - 5;
                mPath.moveTo(centerX, (float) (mProgressWidth / Math.sqrt(3))); // A
                mPath.lineTo((float) (centerX + (radius * Math.sqrt(3) / 2)), radius / 2); // B
                mPath.lineTo((float) (centerX + (radius * Math.sqrt(3) / 2)), radius / 2 + radius); // C
                mPath.lineTo(centerX, 2 * radius); // D
                mPath.lineTo((float) (centerX - (radius * Math.sqrt(3) / 2)), radius / 2 + radius); // E
                mPath.lineTo((float) (centerX - (radius * Math.sqrt(3) / 2)), radius / 2); // F
                mPath.lineTo((float) (centerX - (radius * Math.sqrt(3) / 2) + (radius * Math.sqrt(3) / 2) * remainder), (radius / 2 - (radius / 2 * remainder)) + (float) (mProgressWidth / Math.sqrt(3)) * remainder); // A-
            } else {
                mPath.moveTo(centerX, (float) (mProgressWidth / Math.sqrt(3))); // A
                mPath.lineTo((float) (centerX + (radius * Math.sqrt(3) / 2)), radius / 2); // B
                mPath.lineTo((float) (centerX + (radius * Math.sqrt(3) / 2)), radius / 2 + radius); // C
                mPath.lineTo(centerX, 2 * radius); // D
                mPath.lineTo((float) (centerX - (radius * Math.sqrt(3) / 2)), radius / 2 + radius); // E
                mPath.lineTo((float) (centerX - (radius * Math.sqrt(3) / 2)), radius / 2); // F
                mPath.close();
            }
            canvas.drawPath(mPath, mPaint);
        }
        // 设置文字画笔
        // 要是没有名称则不添加名字
        if (!TextUtils.isEmpty(name)) {
            TextPaint textPaint = new TextPaint();
            textPaint.setColor(Color.BLACK);
            textPaint.setAntiAlias(true);
            textPaint.setTextSize(mTextSize);
            // 写标题
            float textWidth = textPaint.measureText(name, 0, name.length());
            canvas.drawText(name, getWidth() / 2 - textWidth / 2, radius / 2 + mTextSize, textPaint);
            // 写level
            textPaint.setTextSize(mLevelSize);
            String levelStr = String.valueOf((int) (currentValue / maxValue * 10));
            textWidth = textPaint.measureText(levelStr, 0, levelStr.length());
            canvas.drawText(levelStr, getWidth() / 2 - textWidth / 2, radius / 2 + mTextSize + textPadding + mLevelSize, textPaint);
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = (int) (2 * Math.sqrt(3) * hiveRadius) + 2 * mProgressWidth;
        mHeight = (int) (2 * hiveRadius + 2 * (2 * Math.sqrt(3) / 3 * mProgressWidth));
        setMeasuredDimension(mWidth, mHeight);
    }

    private static int getGradientColor(int startColor, int endColor, float percent) {
        int sr = (startColor & 0xff0000) >> 0x10;
        int sg = (startColor & 0xff00) >> 0x8;
        int sb = (startColor & 0xff);

        int er = (endColor & 0xff0000) >> 0x10;
        int eg = (endColor & 0xff00) >> 0x8;
        int eb = (endColor & 0xff);

        int cr = (int) (sr * (1 - percent) + er * percent);
        int cg = (int) (sg * (1 - percent) + eg * percent);
        int cb = (int) (sb * (1 - percent) + eb * percent);
        return Color.argb(0xff, cr, cg, cb);
    }

    //region public method
    public void startAnim() {
        if (mProgressAnim != null && mProgressAnim.isRunning())
            return;
        mProgressAnim = ValueAnimator.ofFloat(0f, maxValue);
        mProgressAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        mProgressAnim.setDuration(mAnimDuration);
        mProgressAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                currentValue = (float) valueAnimator.getAnimatedValue();
                mCurrentColor = getGradientColor(startColor, endColor, currentValue / maxValue);
                if (mOnProgressListener != null) {
                    mOnProgressListener.onProgress(currentValue / maxValue);
                }
                postInvalidate();
            }
        });
        mProgressAnim.setRepeatCount(repeatCount);
        mProgressAnim.start();
    }

    public void stopAnim() {
        if (mProgressAnim != null && mProgressAnim.isRunning()) {
            mProgressAnim.end();
        }
    }

    public void setAnimRepeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
    }

    public void setGradinetColor(@ColorInt int startColor, @ColorInt int endColor) {
        this.startColor = startColor;
        this.endColor = endColor;
    }

    public void setOnProgressListener(OnProgressListener listener) {
        mOnProgressListener = listener;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setTextPadding(int textPadding) {
        this.textPadding = textPadding;
    }

    public void setCurrentValue(float currentValue) {
        this.currentValue = currentValue;
        this.invalidate();
    }

    //endregion

    private int sp2px(float sp) {
        float density = getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * density + 0.5f);
    }

    private int dp2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public interface OnProgressListener {
        /**
         * 0.0f ~ 1.0f
         * @param currentValue
         */
        void onProgress(float currentValue);
    }

}
