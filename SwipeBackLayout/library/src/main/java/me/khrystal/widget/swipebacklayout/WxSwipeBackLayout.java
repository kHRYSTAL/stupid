package me.khrystal.widget.swipebacklayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import me.khrystal.widget.tools.SwipeBackUtil;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/10/10
 * update time:
 * email: 723526676@qq.com
 */

public class WxSwipeBackLayout extends SwipeBackLayout {

    private static final String TAG = WxSwipeBackLayout.class.getSimpleName();

    public WxSwipeBackLayout(Context context) {
        super(context);
    }

    public WxSwipeBackLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WxSwipeBackLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setSwipeBackListener(defaultSwipeBackListener);
    }

    @Override
    public void setDirectionMode(@DirectionMode int direction) {
        super.setDirectionMode(direction);
        if (direction != SwipeBackLayout.FROM_LEFT) {
            throw new IllegalArgumentException("The direction of WxSwipeBackLayout must be FROM_LEFT");
        }
    }

    private OnSwipeBackListener defaultSwipeBackListener = new OnSwipeBackListener() {
        @Override
        public void onViewPositionChanged(View view, float swipeBackFraction, float swipeBackFactor) {
            invalidate();
            SwipeBackUtil.onPanelSlide(swipeBackFraction);
        }

        @Override
        public void onViewSwipeFinished(View view, boolean isEnd) {
            if (isEnd) {
                finish();
            }
            SwipeBackUtil.onPanelReset();
        }
    };
}
