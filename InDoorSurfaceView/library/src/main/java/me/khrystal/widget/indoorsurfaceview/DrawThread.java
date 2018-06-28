package me.khrystal.widget.indoorsurfaceview;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

import me.khrystal.widget.indoorsurfaceview.joggle.DrawFramesListener;
import me.khrystal.widget.indoorsurfaceview.joggle.FramesListener;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/6/28
 * update time:
 * email: 723526676@qq.com
 */

public class DrawThread extends Thread {
    private boolean _run = true;
    private boolean canPaint = true; // 是否直接暂停
    private int FRAME_INTERVAL = 10; // 默认刷新率为10毫秒1次 100Hz
    private DrawFramesListener listener;
    private FramesListener listener2;

    private final SurfaceHolder surfaceHolder;
    private Canvas c = null;

    public DrawThread(SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
    }

    public void setOnDrawingListener(DrawFramesListener listener) {
        this.listener = listener;
    }

    public void setOnFramesListener(FramesListener listener) {
        this.listener2 = listener;
    }

    @Override
    public void run() {
        while (_run) {
            showBit();
        }
    }

    private void showBit() {
        if (canPaint && surfaceHolder != null) { // fps: !stop
            long startTime = System.currentTimeMillis();
            c = surfaceHolder.lockCanvas(); // 注意lock的时间消耗
            try {
                synchronized (surfaceHolder) {
                    // 调用外部接口
                    this.listener.onDraw(c);
                    // 调用外部接口
                    long endTime = System.currentTimeMillis();
                    // 计算出绘画一次更新的毫秒数
                    int diffTime = (int) (endTime - startTime);
                    if (diffTime < FRAME_INTERVAL) {
                        try {
                            Thread.sleep(FRAME_INTERVAL - diffTime);
                        } catch (InterruptedException e) {
                            Log.e("DrawThread", "DrawThread sleep exception," + e.getMessage(), e.getCause());
                        }
                    }
                    // 发送一次循环运行总时间
                    if (listener2 != null) {
                        listener2.onRefresh(System.currentTimeMillis() - startTime);
                    }
                }
            } finally {
                if (c != null) {
                    surfaceHolder.unlockCanvasAndPost(c);
                }
            }
        } else { // fps: stop
            try {
                Thread.sleep(FRAME_INTERVAL);
            } catch (InterruptedException e) {
                Log.e("DrawThread", "DrawThread sleep exception," + e.getMessage(), e.getCause());
            }
            if (listener2 != null) {
                listener2.onRefresh(1000f);
            }
        }
    }

    public void setThreadRun(boolean run) { // 设置是否暂停
        this._run = run;
    }

    public void setCanPaint(boolean can) { // 设置是否绘制
        this.canPaint = can;
    }
}
