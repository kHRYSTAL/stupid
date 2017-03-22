package me.khrystal.widget.bubble;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import me.khrystal.widget.dialog.R;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/3/14
 * update time:
 * email: 723526676@qq.com
 */

public class BubbleLinearLayout extends LinearLayout {

    /**
     * 尖角方向
     */
    public enum BubbleLegOrientation {
        TOP, LEFT, RIGHT, BOTTOM, NONE
    }

    public static int Padding = 30;
    public static int LegHalfBase = 30;
    public static float StrokeWidth = 2.0f;
    public static float CornerRadius = 8.0f;
    public static int ShadowColor = Color.argb(100, 0, 0, 0);
    public static float MinLegDistance = Padding + LegHalfBase;

    private Paint mFillPaint = null;
    private final Path mPath = new Path();
    private final Path mBubbleLegPrototype = new Path();
    private final Paint mPaint = new Paint(Paint.DITHER_FLAG);

    private float mBubbleLegOffset = 0.75f;
    private BubbleLegOrientation mBubbleOrientation = BubbleLegOrientation.LEFT;


    public BubbleLinearLayout(Context context) {
        this(context, null);
    }

    public BubbleLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BubbleLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BubbleLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
//        setGravity(Gravity.CENTER);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.bubble);
            try {
                Padding = ta.getDimensionPixelSize(R.styleable.bubble_padding, Padding);
                ShadowColor = ta.getInt(R.styleable.bubble_shadowColor, ShadowColor);
                LegHalfBase = ta.getDimensionPixelSize(R.styleable.bubble_halfBaseOfLeg, LegHalfBase);
                MinLegDistance = Padding + LegHalfBase;
                CornerRadius = ta.getFloat(R.styleable.bubble_cornerRadius, CornerRadius);
            } finally {
                if (ta != null) {
                    ta.recycle();
                }
            }
        }

        mPaint.setColor(ShadowColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeCap(Paint.Cap.BUTT);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(StrokeWidth);
        mPaint.setStrokeJoin(Paint.Join.MITER);
        mPaint.setPathEffect(new CornerPathEffect(CornerRadius));

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(LAYER_TYPE_SOFTWARE, mPaint);
//        }

        mFillPaint = new Paint(mPaint);
        mFillPaint.setColor(Color.WHITE);
        mFillPaint.setShader(new LinearGradient(100f, 0f, 100f, 200f, Color.WHITE,
                Color.WHITE, Shader.TileMode.CLAMP));

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(LAYER_TYPE_SOFTWARE, mFillPaint);
//        }

        mPaint.setShadowLayer(2f, 2f, 5f, ShadowColor);
        renderBubbleLegPrototype();
        setPadding(Padding, Padding, Padding, Padding);
    }

    /**
     * 尖角path
     */
    private void renderBubbleLegPrototype() {
        mBubbleLegPrototype.moveTo(0, 0);
        mBubbleLegPrototype.lineTo(Padding * 1.2f, -Padding / 1.2f);
        mBubbleLegPrototype.lineTo(Padding * 1.2f, Padding / 1.2f);
        mBubbleLegPrototype.close();
    }

    public void setBubbleParams(final BubbleLegOrientation bubbleLegOrientation, final float bubbleOffset) {
        mBubbleLegOffset = bubbleOffset;
        mBubbleOrientation = bubbleLegOrientation;
    }

    public void setBubbleOrientation(final BubbleLegOrientation bubbleOrientation) {
        mBubbleOrientation = bubbleOrientation;
    }

    /**
     * 根据显示方向 获取尖角位置的矩阵
     * @return
     */
    private Matrix renderBubbleLegMatrix(final float width, final float height) {
        final float offset = Math.max(mBubbleLegOffset, MinLegDistance);
        float dstX = 0;
        float dstY = Math.min(offset, height - MinLegDistance);
        final Matrix matrix = new Matrix();

        switch (mBubbleOrientation) {
            case TOP:
                dstX = Math.min(offset, width - MinLegDistance);
                dstY = 0;
                matrix.postRotate(90);
                break;
            case RIGHT:
                dstX = width;
                dstY = Math.min(offset, height - MinLegDistance);
                matrix.postRotate(180);
                break;
            case BOTTOM:
                dstX = Math.min(offset, width - MinLegDistance);
                dstY = height;
                matrix.postRotate(270);
                break;
        }
        matrix.postTranslate(dstX, dstY);
        return matrix;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final float width = canvas.getWidth();
        final float height = canvas.getHeight();

        mPath.rewind();
        mPath.addRoundRect(new RectF(Padding, Padding, width - Padding, height - Padding),
                CornerRadius, CornerRadius, Path.Direction.CW);
        mPath.addPath(mBubbleLegPrototype, renderBubbleLegMatrix(width, height));
        canvas.drawPath(mPath, mPaint);
        canvas.scale((width - StrokeWidth) / width, (height - StrokeWidth) / height,
                width / 2f, height / 2f);
        canvas.drawPath(mPath, mFillPaint);
    }
}
