package me.khrystal.widget.friendcircle.adapter.observer;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/3/14
 * update time:
 * email: 723526676@qq.com
 */

public class PhotoAdapterObservable extends PhotoImageObservable<PhotoBaseDataObserver> {

    /**
     * 通知所有观察者数据发生改变
     */
    public void notifyChanged() {
        synchronized (mObservers) {
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).onChanged();
            }
        }
    }

    /**
     * 通知所有观察者数据失效
     */
    public void notifyInvalidated() {
        synchronized (mObservers) {
            for (int i = mObservers.size() - 1; i >= 0 ; i--) {
                mObservers.get(i).onInvalidated();
            }
        }
    }


}
