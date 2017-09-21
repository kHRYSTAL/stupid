package me.khrystal.widget.magnifier;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/9/21
 * update time:
 * email: 723526676@qq.com
 */

public class ImageMagnifier extends View {

    private Bitmap bitmap;
    private ShapeDrawable drawable;

    private static final int RADIUS = 80;
    private static final int FACTOR = 3;

    private Matrix matrix = new Matrix();

    public ImageMagnifier(Context context) {
        this(context, null);
    }

    public ImageMagnifier(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.im_cia);
        BitmapShader shader = new BitmapShader(
                Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() * FACTOR, bitmap.getHeight() * FACTOR, true),
                Shader.TileMode.CLAMP,
                Shader.TileMode.CLAMP
        );

        // circle drawable
        drawable = new ShapeDrawable(new OvalShape());
        drawable.getPaint().setShader(shader);
        drawable.setBounds(0, 0, RADIUS * 2, RADIUS * 2);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int x = (int) event.getX();
        final int y = (int) event.getY();
        // draw shader start point
        matrix.setTranslate(RADIUS - x * FACTOR, RADIUS - y * FACTOR);
        drawable.getPaint().getShader().setLocalMatrix(matrix);
        // bounds
        drawable.setBounds(x - RADIUS, y - RADIUS, x + RADIUS, y + RADIUS);
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmap, 0, 0, null);
        drawable.draw(canvas);
    }
}
