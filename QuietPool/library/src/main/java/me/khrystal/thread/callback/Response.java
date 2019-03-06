package me.khrystal.thread.callback;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/3/5
 * update time:
 * email: 723526676@qq.com
 */
public interface Response<T> {
    void onSuccess(T t);

    void onError(Throwable e);
}
