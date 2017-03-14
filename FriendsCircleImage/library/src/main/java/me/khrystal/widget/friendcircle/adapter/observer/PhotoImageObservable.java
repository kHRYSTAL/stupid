package me.khrystal.widget.friendcircle.adapter.observer;

import java.util.ArrayList;

/**
 * usage: 图片被观察者基类
 * author: kHRYSTAL
 * create time: 17/3/14
 * update time:
 * email: 723526676@qq.com
 */

public abstract class PhotoImageObservable<T> {

    protected final ArrayList<T> mObservers = new ArrayList<T>();

    /**
     * 注册观察者
     */
    public void registerObserver(T observer) {
        if (null == observer) {
            throw new IllegalArgumentException("注册 观察者不能为空!");
        }
        synchronized (mObservers) {
            if (mObservers.contains(observer)) {
                throw new IllegalStateException("观察者已经存在!");
            }
            mObservers.add(observer);
        }
    }

    public void unregisterObserver(T observer) {
        if (null == observer) {
            throw new IllegalArgumentException("解除 观察者不能为空!");
        }
        synchronized (mObservers) {
            int index = mObservers.indexOf(observer);
            if (index == -1) {
                throw new IllegalStateException("观察者不存在!");
            }
            mObservers.remove(index);
        }
    }

    public void unregisterAll() {
        synchronized (mObservers) {
            mObservers.clear();
        }
    }
}
