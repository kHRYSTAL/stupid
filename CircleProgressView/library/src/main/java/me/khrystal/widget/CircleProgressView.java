package me.khrystal.widget;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;


public class CircleProgressView extends View {

    private static final int DEFAULT_HEIGHT = 250;
    private static final int DEFAULT_WIDTH = 250;
    private static final int DEFAULT_PADDING = 20;
    private static final float DEFAULT_START_ANGLE = 0f;
    private final static float DEFAULT_END_ANGLE = 360f;
    private final static int DEFAULT_CENTER_TEXTSIZE = 60;
    private final static int DEFAULT_STORKE_WIDTH = 10;
    private final static int DEFAULT_ANIM_DURATION = 3000;


    private int mWidth, mHeight;

    private Paint mProgressPaint, mSecondProgressPaint, mRingPaint;

    private RectF mRingRect, mProgressRect;

    private float mCurrentAngle;
    private float mSecondCurrentAngle;

    private float mTotalAngle;

    private float[] pos;

    private float[] tan;


    private Paint mCenterTextPaint, mUnitTextPaint;

    private float mStartAngle, mEndAngle;

    private String mCenterText;
    private int mCenterTextSize;
    private int mStorkeWidth;
    private int mAnimDuration;
    private int mAnimRepeatCount;
    private int mStrokeAlpha;
    private boolean isStarted;

    private @ColorInt
    int mColor, mSecondColor, mStrokeColor;

    private ValueAnimator mAngleAnim;

    private OnProgressListener mOnProgressListener;


    public CircleProgressView(Context context) {
        this(context, null);
    }

    public CircleProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CircleProgressView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }


    private void init(AttributeSet attrs) {

        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.CircleProgressView);
        mStrokeColor = ta.getColor(R.styleable.CircleProgressView_strokeColor, Color.GRAY);
        mStorkeWidth = ta.getDimensionPixelSize(R.styleable.CircleProgressView_strokeWidth, dp2px(DEFAULT_STORKE_WIDTH));
        mAnimDuration = ta.getInt(R.styleable.CircleProgressView_duration, DEFAULT_ANIM_DURATION);
        mColor = ta.getColor(R.styleable.CircleProgressView_progressColor, Color.WHITE);
        mSecondColor = ta.getColor(R.styleable.CircleProgressView_secondaryProgressColor, Color.WHITE);
        mAnimRepeatCount = ta.getInt(R.styleable.CircleProgressView_animRepeatCount, 0);
        mCenterTextSize = ta.getDimensionPixelSize(R.styleable.CircleProgressView_centerTextSize, sp2px(DEFAULT_CENTER_TEXTSIZE));
        mStrokeAlpha = ta.getInt(R.styleable.CircleProgressView_strokeAlpha, 60);
        mStartAngle = ta.getFloat(R.styleable.CircleProgressView_startAngle, DEFAULT_START_ANGLE);
        mEndAngle = ta.getFloat(R.styleable.CircleProgressView_endAngle, DEFAULT_END_ANGLE);
        mTotalAngle = ta.getFloat(R.styleable.CircleProgressView_totalAngle, DEFAULT_END_ANGLE);
        ta.recycle();

        mRingPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRingPaint.setStrokeWidth(mStorkeWidth);
        mRingPaint.setColor(mStrokeColor);
        mRingPaint.setStyle(Paint.Style.STROKE);
        mRingPaint.setAlpha(mStrokeAlpha);

        mProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mProgressPaint.setStrokeWidth(mStorkeWidth);
        mProgressPaint.setStyle(Paint.Style.STROKE);
        mProgressPaint.setStrokeCap(Paint.Cap.ROUND);

        mSecondProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSecondProgressPaint.setStrokeWidth(mStorkeWidth);
        mSecondProgressPaint.setStyle(Paint.Style.STROKE);
        mSecondProgressPaint.setStrokeCap(Paint.Cap.ROUND);

        mCenterTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCenterTextPaint.setTextAlign(Paint.Align.CENTER);
        Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
        mCenterTextPaint.setTypeface(font);

        mUnitTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mUnitTextPaint.setTextAlign(Paint.Align.CENTER);

        pos = new float[2];
        tan = new float[2];
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidthSize(widthMeasureSpec), measureHeightSize(heightMeasureSpec));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int paddingLeft = getPaddingLeft() == 0 ? DEFAULT_PADDING : getPaddingLeft();
        int paddingRight = getPaddingRight() == 0 ? DEFAULT_PADDING : getPaddingRight();
        int paddingTop = getPaddingTop() == 0 ? DEFAULT_PADDING : getPaddingTop();
        int paddingBottom = getPaddingBottom() == 0 ? DEFAULT_PADDING : getPaddingBottom();
        int width = getWidth();
        int height = getHeight();

        mWidth = w;
        mHeight = h;

        mRingRect = new RectF(
                paddingLeft, paddingTop,
                width - paddingRight, height - paddingBottom);
        mProgressRect = new RectF(
                paddingLeft, paddingTop,
                width - paddingRight, height - paddingBottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawRing(canvas);
        drawProgress(canvas);
        drawNumberText(canvas);
    }


    private void drawRing(Canvas canvas) {
        canvas.drawArc(mRingRect, 0f, 360f, false, mRingPaint);
    }

    private void drawProgress(Canvas canvas) {
        canvas.save();
        canvas.rotate(-90, getWidth() / 2, getHeight() / 2);
        Path path = new Path();
        Path secondPath = new Path();
        path.addArc(mProgressRect, mStartAngle, mCurrentAngle > 360f ? mCurrentAngle - (360f * (int) (mTotalAngle / mCurrentAngle)) : mCurrentAngle);
        float secondAngle = mSecondCurrentAngle == 0f ? mCurrentAngle + 90f : mSecondCurrentAngle;
        secondPath.addArc(mProgressRect, mStartAngle, secondAngle > 360f ? secondAngle - (360f * (int) (mTotalAngle / secondAngle)) : secondAngle);
        PathMeasure pathMeasure = new PathMeasure(path, false);
        pathMeasure.getPosTan(pathMeasure.getLength() * 1, pos, tan);
        PathMeasure secondPathMeasure = new PathMeasure(secondPath, false);
        secondPathMeasure.getPosTan(pathMeasure.getLength() * 1, pos, tan);
        mProgressPaint.setColor(mColor);
        mSecondProgressPaint.setColor(mSecondColor);
        if (mCurrentAngle == mStartAngle) {
            // do nothing
        } else {
            secondPath.addArc(mProgressRect, mStartAngle, secondAngle);
            canvas.drawPath(secondPath, mSecondProgressPaint);
        }
        path.addArc(mProgressRect, mStartAngle, mCurrentAngle);
        canvas.drawPath(path, mProgressPaint);
        canvas.restore();
    }

    private void drawNumberText(Canvas canvas) {
        mCenterTextPaint.setTextSize(mCenterTextSize);
        if (mAngleAnim != null && mAngleAnim.isRunning()) {
            isStarted = true;
            mCenterTextPaint.setColor(Color.BLACK);
        }
        if (!isStarted)
            mCenterTextPaint.setColor(Color.BLACK);
        if (mCenterText != null)
            canvas.drawText(mCenterText, mWidth / 2, mHeight * 4 / 7, mCenterTextPaint);
    }

    private int measureWidthSize(int measureSpec) {
        int defSize = dp2px(DEFAULT_WIDTH);
        int specSize = MeasureSpec.getSize(measureSpec);
        int specMode = MeasureSpec.getMode(measureSpec);

        int result = 0;
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                result = Math.min(defSize, specSize);
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
        }
        return result;
    }


    private int measureHeightSize(int measureSpec) {
        int defSize = dp2px(DEFAULT_HEIGHT);
        int specSize = MeasureSpec.getSize(measureSpec);
        int specMode = MeasureSpec.getMode(measureSpec);

        int result = 0;
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                result = Math.min(defSize, specSize);
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
        }
        return result;
    }

    public void startAnim() {
        if (mAngleAnim != null && mAngleAnim.isRunning())
            return;
        mAngleAnim = ValueAnimator.ofFloat(mCurrentAngle, mTotalAngle);
        final float scale = mSecondCurrentAngle / mCurrentAngle; // scale may be is zero,
        // at that time secondCurrentAngle will larger than currentAngle at 25.0f
        mAngleAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        mAngleAnim.setDuration(mAnimDuration);
        mAngleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mCurrentAngle = (float) valueAnimator.getAnimatedValue();
                mSecondCurrentAngle = scale * mCurrentAngle;
                if (mOnProgressListener != null) {
                    mOnProgressListener.onProgress(mCurrentAngle / mTotalAngle);
                }
                postInvalidate();
            }
        });
        mAngleAnim.setRepeatCount(mAnimRepeatCount);
        mAngleAnim.start();
    }


    public void stopAnim() {
        if (mAngleAnim != null && mAngleAnim.isRunning()) {
            mAngleAnim.end();
        }
    }

    public void setProgress(float progress) {
        if (mAngleAnim != null && mAngleAnim.isRunning())
            mAngleAnim.end();
        if (progress > 100) {
            mCurrentAngle = 360f;
            postInvalidate();
            return;
        }
        if (progress < 0) {
            mCurrentAngle = 0f;
            postInvalidate();
            return;
        }
        mCurrentAngle = 360f * progress / 100f;
        if (mOnProgressListener != null)
            mOnProgressListener.onProgress(mCurrentAngle / mTotalAngle);
        postInvalidate();
    }

    public void setSecondaryProgress(float secondaryProgress) {
        if (mAngleAnim != null && mAngleAnim.isRunning())
            mAngleAnim.end();
        if (secondaryProgress > 100) {
            mSecondCurrentAngle = 360f;
            postInvalidate();
            return;
        }
        if (secondaryProgress < 0) {
            mSecondCurrentAngle = 0f;
            postInvalidate();
            return;
        }
        mSecondCurrentAngle = 360f * secondaryProgress / 100f;
        postInvalidate();
    }

    public CircleProgressView setCenterText(String centerText) {
        mCenterText = centerText;
        return this;
    }

    public String getCenterText() {
        return mCenterText;
    }

    public CircleProgressView setCenterTextSize(int spTextSize) {
        mCenterTextSize = sp2px(spTextSize);
        return this;
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void pauseAnim() {
        if (mAngleAnim != null) {
            mAngleAnim.pause();
        }
    }

    public void setAnimRepeatCount(int repeatCount) {
        mAnimRepeatCount = repeatCount;
    }


    public CircleProgressView setStorkeWidth(int dpStorkeWidth) {
        mStorkeWidth = dp2px(dpStorkeWidth);
        invalidate();
        return this;
    }

    /**
     * 1 ~ 100
     *
     * @param alpha
     * @return
     */
    public CircleProgressView setStorkeAlpha(int alpha) {
        mStrokeAlpha = alpha;
        invalidate();
        return this;
    }

    public CircleProgressView setStrokeColor(@ColorInt int color) {
        mStrokeColor = color;
        return this;
    }

    public CircleProgressView setStartAngle(float startAngle) {
        mStartAngle = startAngle;
        invalidate();
        return this;
    }

    public CircleProgressView setEndAngle(float endAngle) {
        mEndAngle = endAngle;
        invalidate();
        return this;
    }

    public CircleProgressView setTotalAngle(float totalAngle) {
        mTotalAngle = totalAngle;
        invalidate();
        return this;
    }

    public void setAnimDuration(int duration) {
        if (mAngleAnim != null && mAngleAnim.isRunning())
            return;
        mAnimDuration = duration;
    }

    public void setColor(@ColorInt int color) {
        mColor = color;
    }

    public void setSecondColor(@ColorInt int secondColor) {
        mSecondColor = secondColor;
    }

    public void setOnProgressListener(OnProgressListener listener) {
        mOnProgressListener = listener;
    }

    public int dp2px(int values) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (values * density + 0.5f);
    }

    public int sp2px(float sp) {
        float density = getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * density + 0.5f);

    }

    public interface OnProgressListener {
        /**
         * 0.0f ~ 1.0f
         *
         * @param currentValue
         */
        void onProgress(float currentValue);
    }

}
