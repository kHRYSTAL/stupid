package me.khrystal.exercisepanel;

import android.text.Spannable;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 2019-11-01
 * update time:
 * email: 723526676@qq.com
 */
public class ScrollingMovementMethodSupport extends ScrollingMovementMethod {

    private long lastClickTime;
    private static final long CLICK_DELAY = 2000L;
    private boolean isOutSideTouchEvent;
    private ExercisePanel panel;

    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            lastClickTime = System.currentTimeMillis();
        }


        if (event.getAction() == MotionEvent.ACTION_MOVE && System.currentTimeMillis() - lastClickTime > CLICK_DELAY) {
            Log.e("ExercisePanel", "交给外部处理按下");
            isOutSideTouchEvent = true;
            // 按下如果大于2s不进行处理 也就是说 滑动事件只能处理2秒内的手势滑动
            panel.onLongClick(widget);
            panel.getTouchListener().onTouch(widget, event);
            return false;
        }

        if (isOutSideTouchEvent && (event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP)) {
            isOutSideTouchEvent = false;
            Log.e("ExercisePanel", "交给外部处理抬起");
            panel.getTouchListener().onTouch(widget, event);
        }

        Log.e("ExercisePanel", "内部处理滑动事件");
        return super.onTouchEvent(widget, buffer, event);
    }

    public static ScrollingMovementMethodSupport getInstance() {
        if (null == sInstance) {
            sInstance = new ScrollingMovementMethodSupport();
        }
        return sInstance;
    }

    private static ScrollingMovementMethodSupport sInstance;

    public void setExercisePanel(ExercisePanel exercisePanel) {
        this.panel = exercisePanel;
    }
}
