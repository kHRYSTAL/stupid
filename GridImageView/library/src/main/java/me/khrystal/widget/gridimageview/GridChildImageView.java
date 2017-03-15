package me.khrystal.widget.gridimageview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * usage: 九宫格图片的子view 增加了按下图片增加蒙板阴影效果
 * author: kHRYSTAL
 * create time: 17/3/15
 * update time:
 * email: 723526676@qq.com
 */
public class GridChildImageView extends ImageView {

    private static final String TAG = GridChildImageView.class.getSimpleName();

    public GridChildImageView(Context context) {
        super(context);
    }

    public GridChildImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            // 图片按下时 显示蒙层
            case MotionEvent.ACTION_DOWN:
                 Drawable drawable = getDrawable();
                 if (null != drawable) {
                    Log.e(TAG, drawable.toString());
                    drawable.mutate().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                 }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                Drawable drawableUp = getDrawable();
                if (null != drawableUp) {
                    drawableUp.mutate().clearColorFilter();
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    // 控制内存
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        setImageDrawable(null);
    }
}
