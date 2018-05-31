package me.khrystal.widget.scorebar;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import java.lang.reflect.Type;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/5/31
 * update time:
 * email: 723526676@qq.com
 */

public class ScoreBarView extends View {

    // 渐变颜色
    private static final int[][] SECTION_COLORS = {{0xff8380ff, 0xff934cea, 0xffa318d6}
            , {0xffe9ba00, 0xfff49000, 0xffff6600}, {0xff50afff, 0xff5e57ff, 0xff6c00ff}
            , {0xff21c8ab, 0xff11c66a, 0xff00c52a}, {0xff00baff, 0xff009aff, 0xff0079ff}};

    // 动画长度
    private final int DEFAULT_DURATION = 1000;
    // 配色方案
    private int colorStyle = 0;
    // 评分方案 可以是进度 50% 也可以是评级 A, B, C
    private int scoreStyle = 0;
    // 进度条最大值
    private float maxValue = 100;
    // 进度条当前值
    private float currentValue;
    // 进度条最终值
    private float finalValue;
    // 名称
    private String name;
    // 评分
    private String score;
    // 画笔
    private Paint mPaint;
    private int mWidth, mHeight;
    // 字体大小
    private int mTextSize = dp2px(16);
    // 进度条高度
    private int mBarHeight = dp2px(32);
    // 控件高度
    private int mViewHeight = mBarHeight;

    public ScoreBarView(Context context) {
        super(context);
    }

    public ScoreBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        getAttrs(context, attrs, 0);
    }

    public ScoreBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttrs(context, attrs, defStyleAttr);
    }

    public ScoreBarView(Context context, int textSize, int barHeight) {
        super(context);
        mTextSize = textSize;
        mBarHeight = barHeight;
    }

    private void getAttrs(Context context, AttributeSet attrs, int defStyleAttrs) {
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ScoreBar, defStyleAttrs, 0);
        try {
            mTextSize = ta.getDimensionPixelSize(R.styleable.ScoreBar_barTextSize, dp2px(16));
            mBarHeight = ta.getDimensionPixelSize(R.styleable.ScoreBar_barHeight, dp2px(32));
            mViewHeight = ta.getDimensionPixelSize(R.styleable.ScoreBar_viewHeight, mBarHeight);
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
        // 进度百分比
        float section = currentValue / maxValue;
        // 进度条
        RectF progressRect = new RectF(0, (mHeight - mBarHeight) / 2, mWidth * section,
                (mHeight + mBarHeight) / 2);
        // 渐变色
        LinearGradient shader = new LinearGradient(0, (mHeight - mBarHeight) / 2, mWidth,
                (mHeight + mBarHeight) / 2, SECTION_COLORS[colorStyle], null, Shader.TileMode.MIRROR);
        mPaint.setShader(shader);
        canvas.drawRect(progressRect, mPaint);
        // 设置文字画笔
        TextPaint textPaint = new TextPaint();
        textPaint.setColor(Color.WHITE);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(mTextSize);
        // 要是没有名称则不添加名字
        if (!TextUtils.isEmpty(name)) {
            canvas.drawText(name, mBarHeight / 3, (mHeight + mTextSize) / 2 - 4, textPaint);
        }
        if (scoreStyle == 0) {
            score = String.valueOf((int) currentValue);
            float textWidth = Layout.getDesiredWidth(score, textPaint);
            canvas.drawText(score, progressRect.width() - mBarHeight / 8 - textWidth,
                    (mHeight + mTextSize) / 2 - 4, textPaint);
        } else if (scoreStyle == 1) {
            // 画 A, B, C
            String grade;
            switch ((int) finalValue / 10) {
                case 10:
                case 9:
                    grade = "A++";
                    break;
                case 8:
                case 7:
                    grade = "A+";
                    break;
                case 6:
                case 5:
                    grade = "A";
                    break;
                case 4:
                case 3:
                    grade = "B";
                    break;
                default:
                    grade = "C";
                    break;
            }

            float textWidth = Layout.getDesiredWidth(grade, textPaint);
            canvas.drawText(grade, progressRect.width() - mBarHeight / 8 - textWidth,
                    (mHeight + mTextSize) / 2 - 4, textPaint);
        }
    }

    // 设置最大进度值
    public void setMaxValue(float maxValue) {
        this.maxValue = maxValue;
    }

    // 设置当前进度值
    public void setCurrentValue(float currentValue) {
        this.currentValue = currentValue > maxValue ? maxValue : currentValue;
        invalidate();
    }

    // 设置名称
    public void setName(String name) {
        this.name = name;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public float getMaxValue() {
        return maxValue;
    }

    public float getCurrentValue() {
        return currentValue;
    }

    public String getName() {
        return name;
    }

    public String getScore() {
        return score;
    }

    public int getColorStyle() {
        return colorStyle;
    }

    public void setColorStyle(int colorStyle) {
        this.colorStyle = colorStyle;
    }

    public int getScoreStyle() {
        return scoreStyle;
    }

    public void setScoreStyle(int scoreStyle) {
        this.scoreStyle = scoreStyle;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthSpecMode == MeasureSpec.EXACTLY || widthSpecMode == MeasureSpec.AT_MOST) {
            mWidth = widthSpecSize;
        } else {
            mWidth = 0;
        }
        if (heightSpecMode == MeasureSpec.AT_MOST || heightSpecMode == MeasureSpec.UNSPECIFIED) {
            // 可设置与bar的高度不同，bar会居中显示
            mHeight = mViewHeight;
        } else {
            mHeight = heightSpecSize;
        }
        setMeasuredDimension(mWidth, mHeight);
    }

    private ValueAnimator.AnimatorUpdateListener animatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            setCurrentValue((Float) valueAnimator.getAnimatedValue());
            // 此处可以根据进度对 name 进行隐藏
        }
    };

    public ValueAnimator createValueChangeAnimation(int value) {
        return createValueChangeAnimation(value, DEFAULT_DURATION);
    }

    public ValueAnimator createValueChangeAnimation(int value, int duration) {
        if (scoreStyle == 1)
            value = (6 - value) * 20;
        finalValue = value;
        ValueAnimator animator = ValueAnimator.ofFloat(currentValue, value).setDuration(duration);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(animatorUpdateListener);
        return animator;
    }

    private int dp2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
