package me.khrystal.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/5/2
 * update time:
 * email: 723526676@qq.com
 */

public class RippleLayout extends FrameLayout {

    // 图片横向 纵向的格数
    private final int MESH_WIDTH = 20;
    private final int MESH_HEIGHT = 20;

    // 图片的顶点数
    private final int VERTS_COUNT = (MESH_WIDTH + 1) * (MESH_HEIGHT + 1);
    // 原坐标数组
    private final float[] staticVerts = new float[VERTS_COUNT * 2];
    // 转换后的坐标数组
    private final float[] targetVerts = new float[VERTS_COUNT * 2];
    // 当前控件的图片
    private Bitmap bitmap;
    // 水波纹宽度的一半
    private float rippleWidth = 100f;
    // 水波纹扩散速度
    private float rippleSpeed = 15f;
    // 水波纹半径
    private float rippleRadius;
    // 水波纹动画是否执行中
    private boolean isRippling;

    public RippleLayout(Context context) {
        super(context);
    }

    public RippleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RippleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RippleLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (isRippling && bitmap != null) {
            canvas.drawBitmapMesh(bitmap, MESH_WIDTH, MESH_HEIGHT, targetVerts, 0, null, 0, null);
        } else {
            super.dispatchDraw(canvas);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                showRipple(ev.getX(), ev.getY());
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    public void showRipple(final float originX, final float originY) {
        if (isRippling) {
            return;
        }
        initData();
        if (bitmap == null) {
            return;
        }
        isRippling = true;
        // 循环次数 通过控件对角线距离计算, 确保水波纹完全消失
        int viewLength = (int) getLength(bitmap.getWidth(), bitmap.getHeight());
        final int count = (int) ((viewLength + rippleWidth) / rippleSpeed);
        Observable.interval(0, 10, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .take(count + 1)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {
                        rippleRadius = aLong * rippleSpeed;
                        warp(originX, originY);
                        if (aLong == count) {
                            isRippling = false;
                        }
                    }
                });
    }

    private void initData() {
        bitmap = getCacheBitmapFromView(this);
        if (bitmap == null) {
            return;
        }
        float bitmapWidth = bitmap.getWidth();
        float bitmapHeight = bitmap.getHeight();
        int index = 0;
        for (int height = 0; height <= MESH_HEIGHT; height++) {
            float y = bitmapHeight * height / MESH_HEIGHT;
            for (int width = 0; width <= MESH_WIDTH; width++) {
                float x = bitmapWidth * width / MESH_WIDTH;
                staticVerts[index * 2] = targetVerts[index * 2] = x;
                staticVerts[index * 2 + 1] = targetVerts[index * 2 + 1] = y;
                index += 1;
            }
        }
    }

    private void warp(float originX, float originY) {
        for (int i = 0; i < VERTS_COUNT * 2; i += 2) {
            float staticX = staticVerts[i];
            float staticY = staticVerts[i + 1];
            float length = getLength(staticX - originX, staticY - originY);
            if (length > rippleRadius - rippleWidth && length < rippleRadius + rippleWidth) {
                PointF point = getRipplePoint(originX, originY, staticX, staticY);
                targetVerts[i] = point.x;
                targetVerts[i + 1] = point.y;
            } else {
                // 复原
                targetVerts[i] = staticVerts[i];
                targetVerts[i + 1] = staticVerts[i + 1];
            }
        }
        invalidate();
    }

    private PointF getRipplePoint(float originX, float originY, float staticX, float staticY) {
        float length = getLength(staticX - originX, staticY - originY);
        // 偏移点与原点间的角度
        float angle = (float) Math.atan(Math.abs((staticY - originY) / (staticX - originX)));
        // 计算偏移距离
        float rate = (length - rippleRadius) / rippleWidth;
        float offset = (float) Math.cos(rate) * 10f;
        float offsetX = offset * (float) Math.cos(angle);
        float offsetY = offset * (float) Math.sin(angle);
        // 计算偏移后的坐标
        float targetX;
        float targetY;
        if (length < rippleRadius + rippleWidth && length > rippleRadius) {
            //波峰外的偏移坐标
            if (staticX > originY) {
                targetX = staticX + offsetX;
            } else {
                targetX = staticX - offsetX;
            }
            if (staticY > originY) {
                targetY = staticY + offsetY;
            } else {
                targetY = staticY - offsetY;
            }
        } else {
            //波峰内的偏移坐标
            if (staticX > originY) {
                targetX = staticX - offsetX;
            } else {
                targetX = staticX + offsetX;
            }
            if (staticY > originY) {
                targetY = staticY - offsetY;
            } else {
                targetY = staticY + offsetY;
            }
        }
        return new PointF(targetX, targetY);
    }

    private float getLength(float width, float height) {
        return (float) Math.sqrt(width * width + height * height);
    }

    private Bitmap getCacheBitmapFromView(View view) {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache(true);
        final Bitmap drawingCache = view.getDrawingCache();
        Bitmap bitmap;
        if (drawingCache != null) {
            bitmap = Bitmap.createBitmap(drawingCache);
            view.setDrawingCacheEnabled(false);
        } else {
            bitmap = null;
        }
        return bitmap;
    }
}
