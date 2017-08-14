package me.khrystal.customnestedscrollview;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/8/14
 * update time:
 * email: 723526676@qq.com
 */

public class CustomNestedScrollView extends NestedScrollView {

    private OnScrollStateChangeListener mScrollListener;
    // 停止滑动
    public static final int SCROLL_STATE_IDLE = 1;
    // 手势滑动
    public static final int SCROLL_STATE_DRAGGING = 2;
    // 手势离开屏幕滑动
    public static final int SCROLL_STATE_FLING = 3;

    private int mState = SCROLL_STATE_IDLE;

    private int lastY = 0;
    private int touchEventId = -9983761;

    WeakHandler handler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            View scroller = (View) msg.obj;

            if (msg.what == touchEventId) {
                if (lastY == scroller.getScrollY()) {
                    dispatchScrollState(SCROLL_STATE_IDLE);
                } else {
                    if (mState != SCROLL_STATE_FLING) {
                        dispatchScrollState(SCROLL_STATE_FLING);
                    }

                    Message message = new Message();
                    message.what = touchEventId;
                    message.obj = scroller;
                    handler.sendMessageDelayed(message, 1);
                    lastY = scroller.getScrollY();
                }
            }
            return true;
        }
    });

    public CustomNestedScrollView(Context context) {
        super(context);
    }

    public CustomNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        dispatchScrollState(SCROLL_STATE_DRAGGING);
        return super.onStartNestedScroll(child, target, nestedScrollAxes);
    }

    @Override
    public boolean startNestedScroll(int axes) {
        boolean superScroll = super.startNestedScroll(axes);
        dispatchScrollState(SCROLL_STATE_DRAGGING);
        return superScroll;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int eventAction = ev.getAction();
        switch (eventAction) {
            case MotionEvent.ACTION_UP:
                Message message = new Message();
                message.what = touchEventId;
                message.obj = this;
                handler.sendMessageDelayed(message, 5);
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void dispatchScrollState(int state) {
        if (mScrollListener != null && mState != state) {
            mScrollListener.onNestedScrollViewStateChanged(state);
            mState = state;
        }
    }

    public void setOnScrollStateChangeListener(OnScrollStateChangeListener listener) {
        mScrollListener = listener;
    }

    public interface OnScrollStateChangeListener {
        public void onNestedScrollViewStateChanged(int state);
    }
}
