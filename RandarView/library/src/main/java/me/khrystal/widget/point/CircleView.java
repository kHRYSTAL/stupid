package me.khrystal.widget.point;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * usage: 地图上随机产生的小圆点
 * author: kHRYSTAL
 * create time: 17/5/9
 * update time:
 * email: 723526676@qq.com
 */

public class CircleView extends View {

    private Paint mPaint;
    private Bitmap mBitmap;
    private float radius = 10;
    private float disX; // 位置X
    private float disY; // 位置Y
    private float angel; // 旋转角度
    private float proportion; // 根据远近距离的不同计算得到的应占半径比例

    public float getProportion() {
        return proportion;
    }

    public CircleView(Context context) {
        super(context);
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
