package me.khrystal.scrollingtextview;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

import androidx.annotation.ColorInt;
import androidx.annotation.IntDef;
import androidx.annotation.IntRange;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 2021/5/25
 * update time:
 * email: 723526676@qq.com
 */

public class ScrollingNumbersView extends View implements ValueAnimator.AnimatorUpdateListener {

    @IntDef({ALIGNMENT_CENTER, ALIGNMENT_LEFT, ALIGNMENT_RIGHT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Alignment {
    }

    public static final int ALIGNMENT_CENTER = 0;
    public static final int ALIGNMENT_LEFT = 1;
    public static final int ALIGNMENT_RIGHT = 2;

    public static final float DEFAULT_TEXT_SIZE = 105f;
    public static final float DEFAULT_LINE_SPACING_MULTIPLIER = 1.5f;
    public static final float DEFAULT_SINGLE_NUMBER_WIDTH_MULTIPLIER = 1.2f;
    public static final float DEFAULT_SUB_TEXT_SIZE = 36f;

    public static final int NO_ADJUST_SUBTEXT = Integer.MAX_VALUE;

    private final int mMaxDigits; // The count of all numbers
    private int mShowingCount; // The count of showing numbers
    @Alignment
    private int mAlignment; // Default: ALIGNMENT_CENTER
    private float mLineSpacingMult; // Default: DEFAULT_LINE_SPACING_MULTIPLIER
    private float mSingleNumberWidthMult; // Default: DEFAULT_SINGLE_NUMBER_WIDTH_MULTIPLIER
    private String mSubText; // May be "%" or "分"
    private float mSubTextSize; // Default: DEFAULT_SUB_TEXT_SIZE
    private boolean mPauseAt95; // Whether to pause at 95%
    private int mAdjustSubTextThreshold = NO_ADJUST_SUBTEXT; // adjust sub text x when centered
    private int mCurrentValue;                     //  0 9 9
    private int mAdjacentValue;                    //  1 0 0
    private final int[] mCurrentNumbers;           // |0|9|9|
    private final int[] mAdjacentNumbers;          // |1|0|0|

    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Point mTextWidthHeight = new Point();
    private final Point mSubWidthHeight = new Point();
    private final Point mSubLocation = new Point();
    private final Rect mDrawingArea = new Rect();

    private final ArrayList<ScrollingNumber> mNumbers = new ArrayList<>();
    private final AnimatorSet mAnimatorSet = new AnimatorSet();
    private final ArrayList<Animator> mAnimators = new ArrayList<>();

    public ScrollingNumbersView(Context context) {
        this(context, null);
    }

    public ScrollingNumbersView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollingNumbersView(Context context, AttributeSet attrs,
                                int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ScrollingNumbersView(Context context, AttributeSet attrs,
                                int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.ScrollingNumbersView, defStyleAttr, defStyleRes);

        int maxDigits = a.getInt(R.styleable.ScrollingNumbersView_maxDigits, 3);
        int initialValue = a.getInt(R.styleable.ScrollingNumbersView_initialValue, 0);
        int textColor = a.getColor(R.styleable.ScrollingNumbersView_textColor, Color.RED);
        float textSize = a.getDimension(R.styleable.ScrollingNumbersView_textSize,
                DEFAULT_TEXT_SIZE);
        int styleIndex = a.getInt(R.styleable.ScrollingNumbersView_textStyle, Typeface.NORMAL);
        int alignment = a.getInt(R.styleable.ScrollingNumbersView_alignment, ALIGNMENT_CENTER);
        float lineSpacingMult = a.getFloat(R.styleable.ScrollingNumbersView_lineSpacingMultiplier,
                DEFAULT_LINE_SPACING_MULTIPLIER);
        float singleNumberWidthMult = a.getFloat(
                R.styleable.ScrollingNumbersView_singleNumberWidthMultiplier,
                DEFAULT_SINGLE_NUMBER_WIDTH_MULTIPLIER);
        float subTextSize = a.getDimension(R.styleable.ScrollingNumbersView_subTextSize,
                DEFAULT_SUB_TEXT_SIZE);
        String subText = a.getString(R.styleable.ScrollingNumbersView_subText);
        int adjustSubTextThreshold = a.getInt(
                R.styleable.ScrollingNumbersView_adjustSubTextThreshold, NO_ADJUST_SUBTEXT);

        a.recycle();

        mMaxDigits = maxDigits;
        mCurrentNumbers = new int[mMaxDigits];
        mAdjacentNumbers = new int[mMaxDigits];

        for (int i = 0; i < mMaxDigits; i++) {
            addScrollingNumber(new ScrollingNumber(i), true);
        }

        setPaintColor(textColor);
        setPaintTextSize(textSize);
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/HelveticaNeue.ttc");
        setPaintTypeface(Typeface.create(tf, styleIndex));
        //setPaintTypeface(Typeface.createFromFile("/system/fonts/Smartisan_Latin-Bold.otf"));
        //setPaintTypeface(Typeface.defaultFromStyle(styleIndex));
        setAlignment(alignment);
        setLineSpacingMultiplier(lineSpacingMult);
        setSingleNumberWidthMultiplier(singleNumberWidthMult);
        setSubTextSize(subTextSize);
        setSubText(subText);
        setAdjustSubTextThresholdWhenCentered(adjustSubTextThreshold);
        setInitialValue(initialValue);
    }

    private void addScrollingNumber(ScrollingNumber sn, boolean toLeft) {
        if (toLeft) {
            mNumbers.add(0, sn);
        } else {
            mNumbers.add(sn);
        }
        ValueAnimator animator = sn.getValueAnimator();
        animator.addUpdateListener(this);
        mAnimators.add(animator);
        mAnimatorSet.playTogether(mAnimators);
    }

    /*private void removeScrollingNumber(boolean fromLeft) {
        if (mNumbers.isEmpty()) {
            return;
        }
        ScrollingNumber sn;
        if (fromLeft) {
            sn = mNumbers.remove(0);
        } else {
            sn = mNumbers.remove(mNumbers.size() - 1);
        }
        if (sn == null) {
            return;
        }
        ValueAnimator animator = sn.getValueAnimator();
        animator.removeUpdateListener(this);
        mAnimators.remove(animator);
        mAnimatorSet.playTogether(mAnimators);
        sn.removeNumberChangeListener();
    }*/

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        int allNumberWidth = (int) (mMaxDigits * mTextWidthHeight.x * mSingleNumberWidthMult);
        //int height = getPaddingTop() + mTextWidthHeight.y + getPaddingBottom();
        int height = (int) mPaint.getTextSize();
        int paddingVertical = (height - mTextWidthHeight.y) / 2;
        setPadding(getPaddingLeft(), paddingVertical, getPaddingRight(), paddingVertical);
        int width = getPaddingLeft() + allNumberWidth + mSubWidthHeight.x + getPaddingRight();
        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            // Use width, height
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            height = heightSpecSize;
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            width = widthSpecSize;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        updateNumberLocation();
        mDrawingArea.set(0, 0, getWidth(), getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();

        int height = getHeight();
        int color = mPaint.getColor();
        float factor = 0.5f - mTextWidthHeight.y * 0.5f / height;
        LinearGradient verticalGradient = new LinearGradient(0, 0, 0, height,
                new int[]{Color.TRANSPARENT, color, color, Color.TRANSPARENT},
                new float[]{0, factor, 1f - factor, 1f},
                Shader.TileMode.CLAMP);
        mPaint.setShader(verticalGradient);

        drawNumberOnCanvas(canvas, mPaint);

        mPaint.setShader(null);

        if (!TextUtils.isEmpty(mSubText)) {
            float textSize = mPaint.getTextSize();
            mPaint.setTextSize(mSubTextSize);
            canvas.drawText(mSubText, mSubLocation.x, mSubLocation.y, mPaint);
            mPaint.setTextSize(textSize);
        }

        canvas.restore();
    }

    private void drawNumberOnCanvas(Canvas canvas, Paint paint) {
        for (ScrollingNumber sn : mNumbers) {
            sn.drawOnCanvas(canvas, paint);
        }
    }

    public void setPaintTextSize(float textSize) {
        mPaint.setTextSize(textSize);
        updateTextWidthHeight();
        updateSubWidthHeight();
        //updateNumberLocation();
    }

    private void updateTextWidthHeight() {
        Rect bounds = new Rect();
        mPaint.getTextBounds("0", 0, 1, bounds);
        mTextWidthHeight.set(bounds.width(), bounds.height());
        updateNumberLocation();
    }

    public void setPaintColor(@ColorInt int color) {
        mPaint.setColor(color);
    }

    public void setPaintTypeface(Typeface tf) {
        mPaint.setTypeface(tf);
    }

    public void setAlignment(@Alignment int alignment) {
        mAlignment = alignment;
        updateNumberLocation();
    }

    public void setLineSpacingMultiplier(float lineSpacingMult) {
        mLineSpacingMult = lineSpacingMult;
    }

    public void setSingleNumberWidthMultiplier(float singleNumberWidthMult) {
        mSingleNumberWidthMult = singleNumberWidthMult;
        updateNumberLocation();
    }

    public void setSubText(String text) {
        mSubText = text == null ? "" : text.trim();
        updateSubWidthHeight();
    }

    public void setSubTextSize(float subTextSize) {
        mSubTextSize = subTextSize;
        updateSubWidthHeight();
    }

    private void updateSubWidthHeight() {
        if (mSubTextSize < 0.001f) {
            mSubText = "";
        }
        if (TextUtils.isEmpty(mSubText)) {
            mSubWidthHeight.set(0, 0);
        } else {
            float textSize = mPaint.getTextSize();
            mPaint.setTextSize(mSubTextSize);
            Rect bounds = new Rect();
            mPaint.getTextBounds(mSubText, 0, 1, bounds);
            mSubWidthHeight.set(bounds.width(), bounds.height());
            mPaint.setTextSize(textSize);
        }
        updateNumberLocation();
    }

    public void setPauseAt95Percent(boolean pauseAt95) {
        mPauseAt95 = pauseAt95;
    }

    public void setInitialValue(int initValue) {
        mCurrentValue = initValue;
        mAdjacentValue = initValue;
        valueToArray(mCurrentValue, mCurrentNumbers);
        valueToArray(mAdjacentValue, mAdjacentNumbers);
        setNumberWithValue(initValue);
    }

    private void setNumberWithValue(int value) {
        for (ScrollingNumber sn : mNumbers) {
            sn.setNumberWithValue(value);
        }
        int newCount = updateNumberCanShowZero() + 1;
        updateShowingNumberCount(newCount);
    }

    private int updateNumberCanShowZero() {
        int count = 0;
        boolean canShowZero = false;
        for (int i = 0; i < mAdjacentNumbers.length - 1; i++) {
            mNumbers.get(i).setCanShowZero(canShowZero);
            if (!canShowZero && mAdjacentNumbers[i] != 0) {
                count = mAdjacentNumbers.length - (i + 1);
                canShowZero = true;
            }
        }
        // The ones digit can show zero
        mNumbers.get(mAdjacentNumbers.length - 1).setCanShowZero(true);
        return count;
    }

    private void updateShowingNumberCount(int newCount) {
        if (newCount == mShowingCount) {
            return;
        }
        mShowingCount = newCount;
        updateNumberLocation();
    }

    public void setAdjustSubTextThresholdWhenCentered(int adjustSubTextThreshold) {
        mAdjustSubTextThreshold = adjustSubTextThreshold;
        updateNumberLocation();
    }

    // Call this when mAlignment, mTextWidthHeight, mSubWidthHeight, mSingleNumberWidthMult,
    // mShowingCount, mAdjustSubTextThreshold changed and onLayout
    private void updateNumberLocation() {
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int newX = paddingLeft;
        int singleNumberWidth = (int) (mTextWidthHeight.x * mSingleNumberWidthMult);
        int allNumberWidth = mMaxDigits * singleNumberWidth;
        int showingNumberWidth = mShowingCount * singleNumberWidth;
        int offsetX = allNumberWidth - showingNumberWidth;
        switch (mAlignment) {
            case ALIGNMENT_CENTER:
                int subWidth = (mShowingCount >= mAdjustSubTextThreshold) ? mSubWidthHeight.x : 0;
                newX = (int) ((getWidth() - showingNumberWidth - subWidth) / 2f) - offsetX;
                break;
            case ALIGNMENT_LEFT:
                newX -= offsetX;
                break;
            case ALIGNMENT_RIGHT:
                newX = getWidth() - paddingRight - mSubWidthHeight.x - allNumberWidth;
                break;
        }
        mSubLocation.set(newX + allNumberWidth, paddingTop + mSubWidthHeight.y);
        int newY = paddingTop + mTextWidthHeight.y;
        for (ScrollingNumber sn : mNumbers) {
            sn.setLocation(newX, newY);
            newX += singleNumberWidth;
        }
    }

    public void setNumberAnimatorValues(int... targetValues) {
        for (ScrollingNumber sn : mNumbers) {
            sn.setAnimatorValues(mCurrentValue, targetValues);
        }
    }

    public void setNumberAnimatorValues(Integer... targetValues) {
        for (ScrollingNumber sn : mNumbers) {
            sn.setAnimatorValues(mCurrentValue, targetValues);
        }
    }

    public void setNumberAnimatorDuration(long duration) {
        mAnimatorSet.setDuration(duration);
    }

    public void startNumberAnimator() {
        mAnimatorSet.start();
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        invalidate();
    }

    private static int[] valueToArray(int value, int length) {
        int[] array = new int[length];
        valueToArray(value, array);
        return array;
    }

    private static void valueToArray(int value, int[] array) {
        /*
        Arrays.fill(array, 0);
        char[] chars = String.valueOf(value).toCharArray();
        int charlen = chars.length;
        for (int i = 0; i < charlen; i++) {
            array[i + array.length - charlen] = chars[i] - 48;
        }
        */
        for (int i = array.length - 1; i >= 0; i--) {
            array[i] = value % 10;
            value /= 10;
        }
    }

    private static int arrayToValue(int[] array) {
        /*
        int len = array.length;
        char[] chars = new char[len];
        for (int i = 0; i < len; i++) {
            chars[i] = (char) (array[i] + 48);
        }
        return Integer.parseInt(String.valueOf(chars));
        */
        int value = 0;
        int pow = 1;
        for (int i = array.length - 1; i >= 0; i--) {
            value += array[i] * pow;
            pow *= 10;
        }
        return value;
    }

    public void onNumberChanged(ScrollingNumber sn, int oldNumber, int newNumber) {
        mCurrentNumbers[mMaxDigits - sn.getDigit() - 1] = newNumber;
        int oldValue = mCurrentValue;
        mCurrentValue = arrayToValue(mCurrentNumbers);
    }

    public void onAdjacentNumberStartShowing(ScrollingNumber sn, int adjacentNumber) {
        mAdjacentNumbers[mMaxDigits - sn.getDigit() - 1] = adjacentNumber;
        int newCount = updateNumberCanShowZero() + 1;
        updateShowingNumberCount(newCount);
        mAdjacentValue = arrayToValue(mAdjacentNumbers);
    }

    private class ScrollingNumber implements ValueAnimator.AnimatorUpdateListener,
            Animator.AnimatorListener {
        private final int mDigit;
        private final int mPower; // 10^digit
        private final ValueAnimator mValueAnimator;

        private int mPreNumber, mNumber, mNextNumber;
        private int mLocationX, mLocationY, mScrollingY;
        private int mScrollYAccumulation; // |mScrollYAccumulation| < mLineSpacing;
        private int mLastAnimatedValue;
        private boolean mNotifyAdjacentNumberStartShowing;
        private boolean mCanShowZero;

        public ScrollingNumber(int digit) {
            mDigit = digit;
            mPower = (int) Math.pow(10, digit);
            mValueAnimator = new ValueAnimator();
            mValueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            mValueAnimator.addUpdateListener(this);
            mValueAnimator.addListener(this);
        }

        public int getDigit() {
            return mDigit;
        }

        private void setNumber(@IntRange(from = 0, to = 9) int number) {
            onNumberChanged(this, mNextNumber, number);
            mNotifyAdjacentNumberStartShowing = true;
            mNumber = number;
            mPreNumber = (mNumber + 9) % 10;
            mNextNumber = (mNumber + 1) % 10;
        }

        public void setNumberWithValue(int value) {
            setNumber((value / mPower) % 10);
        }

        private boolean isVisible(int number) {
            int lineSpacing = (int) (mTextWidthHeight.y * mLineSpacingMult);
            if (number == mPreNumber) {
                return mScrollingY - lineSpacing > 0;
            } else if (number == mNumber) {
                return mScrollingY > 0
                        || mScrollingY - mTextWidthHeight.y < mDrawingArea.height();
            } else if (number == mNextNumber) {
                return mScrollingY + lineSpacing - mTextWidthHeight.y < mDrawingArea.height();
            }
            return false;
        }

        public void setLocation(int newX, int newY) {
            mLocationX = newX;
            int offsetY = newY - mLocationY;
            mLocationY = newY;
            mScrollingY += offsetY;
        }

        private void scrollYBy(int offsetY) {
            if (offsetY == 0) {
                return;
            }
            int lineSpacing = (int) (mTextWidthHeight.y * mLineSpacingMult);
            mScrollingY += offsetY;
            mScrollYAccumulation += offsetY;
            int valueDiff = mScrollYAccumulation / lineSpacing;
            if (valueDiff != 0) {
                int newNumber = (mNumber - valueDiff) % 10;
                if (newNumber < 0) {
                    newNumber += 10;
                }
                setNumber(newNumber);
                mScrollingY -= valueDiff * lineSpacing;
                mScrollYAccumulation %= lineSpacing;
            }
            if (mNotifyAdjacentNumberStartShowing) {
                int adjacentNumber = offsetY > 0 ? mPreNumber : mNextNumber;
                if (isVisible(adjacentNumber)) {
                    onAdjacentNumberStartShowing(this, adjacentNumber);
                    mNotifyAdjacentNumberStartShowing = false;
                }
            }
        }

        public void setCanShowZero(boolean canShowZero) {
            mCanShowZero = canShowZero;
        }

        public void drawOnCanvas(Canvas canvas, Paint paint) {
            int lineSpacing = (int) (mTextWidthHeight.y * mLineSpacingMult);
            if ((mPreNumber != 0 || mCanShowZero) && isVisible(mPreNumber)) {
                canvas.drawText("" + mPreNumber, mLocationX, mScrollingY - lineSpacing, paint);
            }
            if ((mNumber != 0 || mCanShowZero) && isVisible(mNumber)) {
                canvas.drawText("" + mNumber, mLocationX, mScrollingY, paint);
            }
            if ((mNextNumber != 0 || mCanShowZero) && isVisible(mNextNumber)) {
                canvas.drawText("" + mNextNumber, mLocationX, mScrollingY + lineSpacing, paint);
            }
        }

        public ValueAnimator getValueAnimator() {
            return mValueAnimator;
        }

        public void setAnimatorValues(int currentValue, int... targetValues) {
            int lineSpacing = (int) (mTextWidthHeight.y * mLineSpacingMult);
            int len = targetValues.length + 1;
            int[] intValues = new int[len];
            //intValues[0] = 0;
            for (int i = 1; i < len; i++) {
                intValues[i] = (currentValue / mPower - targetValues[i - 1] / mPower) * lineSpacing;
            }
            mValueAnimator.setIntValues(intValues);
        }

        public void setAnimatorValues(int currentValue, Integer... targetValues) {
            int lineSpacing = (int) (mTextWidthHeight.y * mLineSpacingMult);
            int len = targetValues.length + 1;
            int[] intValues = new int[len];
            //intValues[0] = 0;
            for (int i = 1; i < len; i++) {
                intValues[i] = (currentValue / mPower - targetValues[i - 1] / mPower) * lineSpacing;
            }
            mValueAnimator.setIntValues(intValues);
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            int curAnimatedValue = (int) animation.getAnimatedValue();
            int diff = curAnimatedValue - mLastAnimatedValue;
            scrollYBy(diff);
            mLastAnimatedValue = curAnimatedValue;
        }

        @Override
        public void onAnimationStart(Animator animation) {
            mScrollingY = mLocationY;
            mScrollYAccumulation = 0;
            mLastAnimatedValue = 0;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
        }

        @Override
        public void onAnimationCancel(Animator animation) {
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }
    }
}
