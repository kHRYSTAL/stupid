package me.khrystal.widget.indoorsurfaceview.adapter;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.List;

import me.khrystal.widget.indoorsurfaceview.joggle.BitBuffer;
import me.khrystal.widget.indoorsurfaceview.unit.PathUnit;

/**
 * usage: 负责向view输出bitmap缓存图片, 并清理了构造最终图片时候的bitmap, 防止内存泄漏
 * author: kHRYSTAL
 * create time: 18/6/28
 * update time:
 * email: 723526676@qq.com
 */

public abstract class BitAdapter implements BitBuffer {

    private Bitmap bitmap = null;
    private Canvas bufferCanvas = null;
    private List<PathUnit> pathUnitList = null;
    private AttrListener listener;

    public BitAdapter() {
    }

    public interface AttrListener {
        void onRefresh();
    }

    @Override
    public void setOnAdapterListener(AttrListener listener) {
        this.listener = listener;
    }

    public void drawBitmap(BitAdapter child) {
        Bitmap bg = child.getBgBitmap();
        if (bg != null) {
            // 根据底图申请缓冲区
            bitmap = Bitmap.createBitmap(bg.getWidth(), bg.getHeight(), Bitmap.Config.RGB_565); // 创建内存位图
            // 创建空白绘图画布
            bufferCanvas = new Canvas(bitmap);
            // 底图进来后绘制到缓冲区
            bufferCanvas.drawBitmap(bg, new Rect(0, 0, bg.getWidth(), bg.getHeight()),
                    new Rect(0, 0, bg.getWidth(), bg.getHeight()), null);
            // 改变背景改变缩放
            if (listener != null)
                listener.onRefresh();
            // 刷新监听器缩放
        }
    }

    public void drawBuffer(BitAdapter child) {
        // 填充数据
        pathUnitList = child.getPathUnit();
        // 画图案
        for (PathUnit pathUnit : pathUnitList) {
            bufferCanvas.drawPath(pathUnit.path, getPaint());
        }
        bufferCanvas = null;
    }

    public abstract List<PathUnit> getPathUnit();

    public abstract Bitmap getBgBitmap();

    @Override
    public Bitmap getBitBuffer() {
        return bitmap;
    }

    private Paint paint; // 画笔属性

    private Paint getPaint() {
        if (paint == null) {
            paint = new Paint();
            paint.setColor(Color.BLUE);
            paint.setStyle(Paint.Style.FILL);
            paint.setAlpha(30);
        }
        return paint;
    }
}
