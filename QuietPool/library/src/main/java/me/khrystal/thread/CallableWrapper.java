package me.khrystal.thread;

import java.util.concurrent.Callable;

import me.khrystal.thread.callback.GlobalCallback;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/3/6
 * update time:
 * email: 723526676@qq.com
 */
public final class CallableWrapper<T> implements Callable<T> {

    private Callable<T> mProxy;
    private GlobalCallback mGlobalCallback;
    private String mName;

    public CallableWrapper(String name, Callable<T> proxy, GlobalCallback globalCallback) {
        this.mName = name;
        this.mProxy = proxy;
        this.mGlobalCallback = globalCallback;
    }

    @Override
    public T call() throws Exception {
        final Thread thread = Thread.currentThread();
        thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                if (mGlobalCallback != null) {
                    mGlobalCallback.onError(mName, throwable);
                }
            }
        });
        if (mGlobalCallback != null) {
            mGlobalCallback.onStart(mName);
        }
        T t = mProxy == null ? null : mProxy.call();
        if (mGlobalCallback != null) {
            mGlobalCallback.onComplete(mName);
        }
        return t;
    }
}
