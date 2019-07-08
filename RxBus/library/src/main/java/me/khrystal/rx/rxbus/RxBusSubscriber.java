package me.khrystal.rx.rxbus;

import android.util.Log;

import rx.Subscriber;

/**
 * usage: RxBus使用的Subscriber, 用于try-catch 防止crash
 * author: kHRYSTAL
 * create time: 19/7/8
 * update time:
 * email: 723526676@qq.com
 */
public abstract class RxBusSubscriber<T> extends Subscriber<T> {

    private static final String TAG = RxBusSubscriber.class.getSimpleName();

    @Override
    public void onNext(T t) {
        try {
            onEvent(t);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        Log.e(TAG, e.getMessage());
    }

    protected abstract void onEvent(T t);
}
