package me.khrystal.thread.callback;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/3/5
 * update time:
 * email: 723526676@qq.com
 */
public interface GlobalCallback {

    void onStart(String threadName);

    void onComplete(String threadName);

    void onError(String threadName, Throwable t);
}
