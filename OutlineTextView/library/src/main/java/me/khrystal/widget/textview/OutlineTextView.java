package me.khrystal.widget.textview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Pair;
import android.util.SparseArray;
import android.widget.TextView;

import java.util.WeakHashMap;

/**
 * usage: OutlineText support show outline with text and can show shadow effect
 * author: kHRYSTAL
 * create time: 17/6/30
 * update time:
 * email: 723526676@qq.com
 */

public class OutlineTextView extends TextView {

    private SparseArray<Shadow> outerShadows;
    private SparseArray<Shadow> innerShadows;

    private WeakHashMap<String, Pair<Canvas, Bitmap>> canvasStore;

    private Canvas tempCanvas;
    private Bitmap tempBitmap;

    private Drawable foregroundDrawable;

    private float storkeWidth;
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
        outerShadows = new SparseArray<>();
        innerShadows = new SparseArray<>();
        if (canvasStore == null) {
            canvasStore = new WeakHashMap<>();
        }
        if (null != attrs) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.OutlineTextView);
            String typefaceName = ta.getString(R.styleable.OutlineTextView_typeface);
            if (null != typefaceName) {
                try {
                    Typeface tf = Typeface.createFromAsset(context.getAssets(), String.format("fonts/%s.ttf", typefaceName));
                    setTypeface(tf);
                } catch (Exception e) {
                    throw new IllegalStateException("type error");
                }
            }
        }
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
