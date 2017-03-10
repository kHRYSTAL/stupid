package me.khrystal.hxdemo.callback;

import com.hyphenate.EMCallBack;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/3/10
 * update time:
 * email: 723526676@qq.com
 */

public class EMCallbackAdapter implements EMCallBack {

    /**
     * 回调成功
     */
    @Override
    public void onSuccess() {

    }

    /**
     * 回调失败
     * 返回错误码
     */
    @Override
    public void onError(int code, String error) {

    }

    /**
     * 正在执行中的进度
     */
    @Override
    public void onProgress(int progress, String status) {

    }
}
