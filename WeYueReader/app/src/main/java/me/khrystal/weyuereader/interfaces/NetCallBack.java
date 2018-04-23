package me.khrystal.weyuereader.interfaces;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/4/23
 * update time:
 * email: 723526676@qq.com
 */

public interface NetCallBack<T> {
    void onSuccess(T t);

    void onFail(String reason);
}
