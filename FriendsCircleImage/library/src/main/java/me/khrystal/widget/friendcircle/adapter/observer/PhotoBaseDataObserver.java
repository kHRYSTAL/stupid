package me.khrystal.widget.friendcircle.adapter.observer;

import android.database.DataSetObserver;

/**
 * usage: 观察者基类
 * author: kHRYSTAL
 * create time: 17/3/14
 * update time:
 * email: 723526676@qq.com
 */

public class PhotoBaseDataObserver extends DataSetObserver {

    /**
     * 观察数据发生了改变回调
     */
    @Override
    public void onChanged() {
        super.onChanged();
    }

    /**
     * 观察数据失效回调
     */
    @Override
    public void onInvalidated() {
        super.onInvalidated();
    }
}
