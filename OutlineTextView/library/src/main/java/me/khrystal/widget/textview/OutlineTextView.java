package me.khrystal.widget.textview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Pair;
import android.util.SparseArray;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.WeakHashMap;

/**
 * usage: OutlineText support show outline with text and can show shadow effect
 * author: kHRYSTAL
 * create time: 17/6/30
 * update time:
 * email: 723526676@qq.com
 */

public class OutlineTextView extends TextView {

    private static final int DEFAULT_COLOR = 0xff000000;

    private ArrayList<Shadow> outerShadows;
    private ArrayList<Shadow> innerShadows;

    private WeakHashMap<String, Pair<Canvas, Bitmap>> canvasStore;

    private Canvas tempCanvas;
    private Bitmap tempBitmap;

    private Drawable foregroundDrawable;

    private float strokeWidth;
    private Integer strokeColor;
    private Paint.Join strokeJoin;
    private float strokeMiter;

    private int[] lockedCompoundPadding;
    private boolean frozen = false;

    public OutlineTextView(Context context) {
        super(context);
        init(context, null);
    }

    public OutlineTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public OutlineTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public OutlineTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        outerShadows = new ArrayList<>();
        innerShadows = new ArrayList<>();
        if (canvasStore == null) {
            canvasStore = new WeakHashMap<>();
        }
        if (null != attrs) {

            //region set Typeface
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.OutlineTextView);
            String typefaceName = ta.getString(R.styleable.OutlineTextView_ot_typeface);
            if (null != typefaceName) {
                try {
                    Typeface tf = Typeface.createFromAsset(context.getAssets(), String.format("fonts/%s.ttf", typefaceName));
                    setTypeface(tf);
                } catch (Exception e) {
                    throw new IllegalStateException("type error");
                }
            }
            //endregion

            //region set Background
            if (ta.hasValue(R.styleable.OutlineTextView_ot_background)) {
                Drawable background = ta.getDrawable(R.styleable.OutlineTextView_ot_background);
                if (background != null) {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN)
                        setBackground(background);
                    else
                        setBackgroundDrawable(background);
                }
            } else {
                setBackgroundColor(ta.getColor(R.styleable.OutlineTextView_ot_background, Color.TRANSPARENT));
            }
            //endregion

            //region set Inner Shadow Color
            if (ta.hasValue(R.styleable.OutlineTextView_ot_innerShadowColor)) {
                addInnerShadow(ta.getDimensionPixelSize(R.styleable.OutlineTextView_ot_innerShadowRadius, 0),
                        ta.getDimensionPixelOffset(R.styleable.OutlineTextView_ot_innerShadowDx, 0),
                        ta.getDimensionPixelOffset(R.styleable.OutlineTextView_ot_innerShadowDy, 0),
                        ta.getColor(R.styleable.OutlineTextView_ot_innerShadowColor, DEFAULT_COLOR));
            }
            //endregion

            //region set Outer Shadow Color
            if (ta.hasValue(R.styleable.OutlineTextView_ot_outerShadowColor)) {
                addOuterShadow(ta.getDimensionPixelSize(R.styleable.OutlineTextView_ot_outerShadowRadius, 0),
                        ta.getDimensionPixelOffset(R.styleable.OutlineTextView_ot_outerShadowDx, 0),
                        ta.getDimensionPixelOffset(R.styleable.OutlineTextView_ot_outerShadowDy, 0),
                        ta.getColor(R.styleable.OutlineTextView_ot_outerShadowColor, DEFAULT_COLOR));
            }
            //endregion

            //region set About Stroke
            if (ta.hasValue(R.styleable.OutlineTextView_ot_strokeColor)) {
                float strokeWidth = ta.getDimensionPixelSize(R.styleable.OutlineTextView_ot_strokeWidth, 1);
                int strokeColor = ta.getColor(R.styleable.OutlineTextView_ot_strokeColor, DEFAULT_COLOR);
                float strokeMiter = ta.getDimensionPixelSize(R.styleable.OutlineTextView_ot_strokeMiter, 10);
                Paint.Join strokeJoin = null;
                switch (ta.getInt(R.styleable.OutlineTextView_ot_strokeJoinStyle, 0)) {
                    case 0:
                        strokeJoin = Paint.Join.MITER;
                        break;
                    case 1:
                        strokeJoin = Paint.Join.BEVEL;
                        break;
                    case 2:
                        strokeJoin = Paint.Join.ROUND;
                        break;
                }
                setStroke(strokeWidth, strokeColor, strokeJoin, strokeMiter);
            }
            //endregion

            //region judge layer type
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB &&
                    (innerShadows.size() > 0 || foregroundDrawable != null)) {
                setLayerType(LAYER_TYPE_SOFTWARE, null);
            }
            //endregion
        }
    }

    public void setStroke(float strokeWidth, int strokeColor, Paint.Join strokeJoin, float strokeMiter) {
        this.strokeWidth = strokeWidth;
        this.strokeColor = strokeColor;
        this.strokeJoin = strokeJoin;
        this.strokeMiter = strokeMiter;
    }

    public void setStroke(float strokeWidth, int strokeColor) {
        setStroke(strokeWidth, strokeColor);
    }

    private void addOuterShadow(float r, float dx, float dy, int color) {
        if (r == 0) {
            r = 0.0001f;
        }
        outerShadows.add(new Shadow(r, dx, dy, color));
    }

    public void addInnerShadow(float r, float dx, float dy, int color) {
        if (r == 0) {
            r = 0.0001f;
        }
        innerShadows.add(new Shadow(r, dx, dy, color));
    }

    public void clearInnerShadows() {
        innerShadows.clear();
    }

    public void clearOuterShadows() {
        outerShadows.clear();
    }

    public void setForegroundDrawable(Drawable d) {
        this.foregroundDrawable = d;
    }

    public Drawable getForegroundDrawable() {
        return this.foregroundDrawable == null ? this.foregroundDrawable : new ColorDrawable(this.getCurrentTextColor());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        /**
         * do not handle setText, use super to handle
         */
        super.onDraw(canvas);
        freeze();
        Drawable restoreBackground = this.getBackground();
        Drawable[] restoreDrawables = this.getCompoundDrawables();
        int restoreColor = this.getCurrentTextColor();

        this.setCompoundDrawables(null, null, null, null);
        for (Shadow shadow : outerShadows) {
            this.setShadowLayer(shadow.r, shadow.dx, shadow.dy, shadow.color);
            super.onDraw(canvas);
        }
        this.setShadowLayer(0, 0, 0, 0);
        this.setTextColor(restoreColor);

        if (foregroundDrawable != null && foregroundDrawable instanceof BitmapDrawable) {
            generateTempCanvas();
            super.onDraw(tempCanvas);
            Paint paint = ((BitmapDrawable) foregroundDrawable).getPaint();
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
            foregroundDrawable.setBounds(canvas.getClipBounds());
            foregroundDrawable.draw(tempCanvas);
            canvas.drawBitmap(tempBitmap, 0, 0, null);
            tempCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        }

        if (strokeColor != null) {
            TextPaint paint = this.getPaint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(strokeJoin);
            paint.setStrokeMiter(strokeMiter);
            this.setTextColor(strokeColor);
            paint.setStrokeWidth(strokeWidth);
            super.onDraw(canvas);
            paint.setStyle(Paint.Style.FILL);
            this.setTextColor(restoreColor);
        }

        if (innerShadows.size() > 0) {
            generateTempCanvas();
            TextPaint paint = this.getPaint();
            for (Shadow shadow : innerShadows) {
                setTextColor(shadow.color);
                super.onDraw(tempCanvas);
                setTextColor(DEFAULT_COLOR);
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
                paint.setMaskFilter(new BlurMaskFilter(shadow.r, BlurMaskFilter.Blur.NORMAL));
                tempCanvas.save();
                tempCanvas.translate(shadow.dx, shadow.dy);
                super.onDraw(tempCanvas);
                tempCanvas.restore();
                canvas.drawBitmap(tempBitmap, 0, 0, null);
                tempCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

                paint.setXfermode(null);
                paint.setMaskFilter(null);
                this.setTextColor(restoreColor);
                this.setShadowLayer(0, 0, 0, 0);
            }
        }

        if (restoreDrawables != null) {
            setCompoundDrawablesWithIntrinsicBounds(restoreDrawables[0], restoreDrawables[1],
                    restoreDrawables[2], restoreDrawables[3]);
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(restoreBackground);
        } else {
            setBackgroundDrawable(restoreBackground);
        }
        setTextColor(restoreColor);
        unfreeze();
    }

    private void generateTempCanvas() {
        String key = String.format("%dx%d", getWidth(), getHeight());
        Pair<Canvas, Bitmap> stored = canvasStore.get(key);
        if (stored != null) {
            tempCanvas = stored.first;
            tempBitmap = stored.second;
        } else {
            tempCanvas = new Canvas();
            tempBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            tempCanvas.setBitmap(tempBitmap);
            canvasStore.put(key, new Pair<Canvas, Bitmap>(tempCanvas, tempBitmap));
        }
    }

    private void unfreeze() {
        frozen = false;
    }

    private void freeze() {
        lockedCompoundPadding = new int[] {
                getCompoundPaddingLeft(),
                getCompoundPaddingRight(),
                getCompoundPaddingTop(),
                getCompoundPaddingBottom()
        };
        frozen = true;
    }

    @Override
    public void requestLayout() {
        if (!frozen)
            super.requestLayout();
    }

    @Override
    public void postInvalidate() {
        if (!frozen)
            super.postInvalidate();
    }

    @Override
    public void postInvalidate(int left, int top, int right, int bottom) {
        if (!frozen)
            super.postInvalidate(left, top, right, bottom);
    }

    @Override
    public void invalidate() {
        if (!frozen)
            super.invalidate();
    }

    @Override
    public void invalidate(Rect dirty) {
        if (!frozen)
            super.invalidate(dirty);
    }

    @Override
    public void invalidate(int l, int t, int r, int b) {
        if (!frozen)
            super.invalidate(l, t, r, b);
    }

    @Override
    public int getCompoundPaddingLeft() {
        return !frozen ? super.getCompoundPaddingLeft() : lockedCompoundPadding[0];
    }

    @Override
    public int getPaddingRight() {
        return !frozen ? super.getPaddingRight() : lockedCompoundPadding[1];
    }

    @Override
    public int getPaddingTop() {
        return !frozen ? super.getPaddingRight() : lockedCompoundPadding[2];
    }

    @Override
    public int getPaddingBottom() {
        return !frozen ? super.getPaddingRight() : lockedCompoundPadding[3];
    }

    public static class Shadow {
        float r;
        float dx;
        float dy;
        int color;

        public Shadow(float r, float dx, float dy, int color) {
            this.r = r;
            this.dx = dx;
            this.dy = dy;
            this.color = color;
        }
    }
}
