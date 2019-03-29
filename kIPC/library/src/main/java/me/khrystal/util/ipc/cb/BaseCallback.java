package me.khrystal.util.ipc.cb;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import me.khrystal.util.ipc.IPCCallback;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/3/29
 * update time:
 * email: 723526676@qq.com
 */
public abstract class BaseCallback extends IPCCallback.Stub {

    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public void onSuccess(final Bundle result) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                onSucceed(result);
            }
        });
    }

    @Override
    public void onFail(final String reason) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                onFailed(reason);
            }
        });
    }

    public abstract void onSucceed(Bundle result);

    public abstract void onFailed(String reason);
}
