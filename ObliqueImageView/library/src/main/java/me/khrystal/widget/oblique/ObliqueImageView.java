package me.khrystal.widget.oblique;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.FloatRange;
import android.util.AttributeSet;
import android.widget.ImageView;

import me.khrystal.widget.R;

/**
 * usage: 支持倾斜角度的ImageView
 * author: kHRYSTAL
 * create time: 17/5/4
 * update time:
 * email: 723526676@qq.com
 */

public class ObliqueImageView extends ImageView {

    private final Paint colorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint bitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Bitmap bitmap = null;
    private BitmapShader bitmapShader;
    private Matrix shaderMatrix = new Matrix();
    private int baseColor = Color.TRANSPARENT;
    private float startAngle, endAngle, width, height;
    private Config config;

    public ObliqueImageView(Context context) {
        super(context);
        init(context, null);
    }

    public ObliqueImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ObliqueImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ObliqueImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        config = new Config();
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ObliqueImageView, 0, 0);
        startAngle = ta.getFloat(R.styleable.ObliqueImageView_starting_slant_angle, 90f);
        endAngle = ta.getFloat(R.styleable.ObliqueImageView_ending_slant_angle, 90f);
        baseColor = ta.getColor(R.styleable.ObliqueImageView_basecolor, Color.TRANSPARENT);
        ta.recycle();
        colorPaint.setStyle(Paint.Style.FILL);
        colorPaint.setColor(baseColor);
        colorPaint.setAlpha(255);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        setupBitmap(this, width, height);
        invalidate();
    }

    public void setStartAngle(@FloatRange(from = 0, to = 180) float startAngle) {
        this.startAngle = startAngle;
        invalidate();
    }

    public void setEndAngle(@FloatRange(from = 0, to = 180) float endAngle) {
        this.endAngle = endAngle;
        invalidate();
    }

    public void setBaseColor(int baseColor) {
        this.baseColor = baseColor;
        colorPaint.setColor(baseColor);
        colorPaint.setAlpha(255);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Path path = config.getPath(height, width, startAngle, endAngle);
        if (bitmap != null) {
            canvas.drawPath(path, bitmapPaint);
        }
        if (baseColor != Color.TRANSPARENT) {
            canvas.drawPath(path, colorPaint);
        }
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        setupBitmap(this, width, height);
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        setupBitmap(this, width, height);
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        setupBitmap(this, width, height);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        setupBitmap(this, width, height);
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        if (scaleType == ScaleType.CENTER_CROP || scaleType == ScaleType.FIT_XY) {
            super.setScaleType(scaleType);
        } else {
            throw new IllegalArgumentException(String.format("ScaleType %s not supported.", scaleType));
        }
    }

    @Override
    public void setAdjustViewBounds(boolean adjustViewBounds) {
        if (adjustViewBounds) {
            throw new IllegalArgumentException("adjustViewBounds not supported.");
        }
    }

    private void setupBitmap(ImageView imageView, float width, float height) {
        Drawable drawable = imageView.getDrawable();
        if (drawable == null) {
            return;
        }
        try {
            bitmap = (drawable instanceof BitmapDrawable) ?
                    ((BitmapDrawable) drawable).getBitmap() :
                    Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (bitmap == null) {
            imageView.invalidate();
            return;
        }
        bitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        bitmapPaint.setShader(bitmapShader);
        if (imageView.getScaleType() != ScaleType.CENTER_CROP && imageView.getScaleType() != ScaleType.FIT_XY) {
            imageView.setScaleType(ScaleType.CENTER_CROP);
        }
        setUpScaleType(imageView, width, height);
        imageView.invalidate();
    }

    private void setUpScaleType(ImageView iv, float width, float height) {
        float scaleX = 1, scaleY = 1, dx = 0, dy = 0;
        if (bitmap == null || shaderMatrix == null) {
            return;
        }
        shaderMatrix.set(null);
        if (iv.getScaleType() == ScaleType.CENTER_CROP) {
            if (width != bitmap.getWidth()) {
                scaleX = width / bitmap.getWidth();
            }
            if (scaleX * bitmap.getHeight() < height) {
                scaleX = height / bitmap.getHeight();
            }
            dy = (height - bitmap.getHeight() * scaleX) * 0.5f;
            dx = (width - bitmap.getWidth() * scaleX) * 0.5f;
            shaderMatrix.setScale(scaleX, scaleX);
        } else {
            scaleX = width / bitmap.getWidth();
            scaleY = height / bitmap.getHeight();
            dy = (height - bitmap.getHeight() * scaleY) * 0.5f;
            dx = (width - bitmap.getWidth() * scaleX) * 0.5f;
            shaderMatrix.setScale(scaleX, scaleY);
        }
        shaderMatrix.postTranslate(dx + 0.5f, dy + 0.5f);
        bitmapShader.setLocalMatrix(shaderMatrix);
    }


}
