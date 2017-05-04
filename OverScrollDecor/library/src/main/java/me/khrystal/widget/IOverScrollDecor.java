package me.khrystal.widget;

import android.view.View;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/5/3
 * update time:
 * email: 723526676@qq.com
 */

public interface IOverScrollDecor {

    View getView();

    void setOverScrollStateListener(IOverScrollStateListener listener);
    void setOverScrollUpdateListener(IOverScrollStateListener listener);

    int getCurrentState();

    void detach();
}
