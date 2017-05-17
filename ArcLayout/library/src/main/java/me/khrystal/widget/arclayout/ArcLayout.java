package me.khrystal.widget.arclayout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/5/17
 * update time:
 * email: 723526676@qq.com
 */

public class ArcLayout extends FrameLayout {

    private ArcLayoutConfig config;
    private int height = 0;
    private int width = 0;
    private Path clipPath;

    public ArcLayout(Context context) {
        super(context);
        init(context, null);
    }

    public ArcLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        config = new ArcLayoutConfig(context, attrs);
        config.setElevation(ViewCompat.getElevation(this));

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    private Path createClipPath() {
        final Path path = new Path();
        float arcHeight = config.getArcHeight();
        switch (config.getPosition()) {
            case ArcLayoutConfig.POSITION_BOTTOM:
                if (config.isCropInside()) {
                    path.moveTo(0, 0);
                    path.lineTo(0, height);
                    path.quadTo(width / 2, height - 2 * arcHeight, width, height);
                    path.lineTo(width, 0);
                    path.close();
                } else {
                    path.moveTo(0, 0);
                    path.lineTo(0, height - arcHeight);
                    path.quadTo(width / 2, height + arcHeight, width, height - arcHeight);
                    path.lineTo(width, 0);
                    path.close();
                }
                break;
            case ArcLayoutConfig.POSITION_TOP:
                if (config.isCropInside()) {
                    path.moveTo(0, height);
                    path.lineTo(0, 0);
                    path.quadTo(width / 2, 2 * arcHeight, width, 0);
                    path.lineTo(width, height);
                    path.close();
                } else {
                    path.moveTo(0, arcHeight);
                    path.quadTo(width / 2, -arcHeight, width, arcHeight);
                    path.lineTo(width, height);
                    path.lineTo(0, height);
                    path.close();
                }
                break;
            case ArcLayoutConfig.POSITION_LEFT:
                if (config.isCropInside()) {
                    path.moveTo(width, 0);
                    path.lineTo(0, 0);
                    path.quadTo(arcHeight * 2, height / 2, 0, height);
                    path.lineTo(width, height);
                    path.close();
                } else {
                    path.moveTo(width, 0);
                    path.lineTo(arcHeight, 0);
                    path.quadTo(-arcHeight, height / 2, arcHeight, height);
                    path.lineTo(width, height);
                    path.close();
                }
                break;
            case ArcLayoutConfig.POSITION_RIGHT:
                if (config.isCropInside()) {
                    path.moveTo(0, 0);
                    path.lineTo(width, 0);
                    path.quadTo(width - arcHeight * 2, height / 2, width, height);
                    path.lineTo(0, height);
                    path.close();
                } else {
                    path.moveTo(0, 0);
                    path.lineTo(width - arcHeight, 0);
                    path.quadTo(width + arcHeight, height / 2, width - arcHeight, height);
                    path.lineTo(0, height);
                    path.close();
                }
                break;
        }
        return path;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            calculateLayout();
        }
    }

    private void calculateLayout() {
        if (config == null)
            return;
        height = getMeasuredHeight();
        width = getMeasuredWidth();
        if (width > 0 && height > 0) {
            clipPath = createClipPath();
            ViewCompat.setElevation(this, config.getElevation());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !config.isCropInside()) {
                ViewCompat.setElevation(this, config.getElevation());
                setOutlineProvider(new ViewOutlineProvider() {

                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void getOutline(View view, Outline outline) {
                        outline.setConvexPath(clipPath);
                    }
                });
            }
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.save();
        canvas.clipPath(clipPath);
        super.dispatchDraw(canvas);
        canvas.restore();
    }
}