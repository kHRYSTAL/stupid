package me.khrystal.toast.clipboard;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/1/25
 * update time:
 * email: 723526676@qq.com
 */

public class ClipboardManagerImpl9 extends ClipboardManagerCompat implements Runnable {

    private static Handler sHandler;

    static {
        sHandler = new Handler(Looper.getMainLooper());
    }

    ClipboardManager mClipboardManager;
    private CharSequence mLastData;
    private boolean mWorking = false;

    public ClipboardManagerImpl9(Context context) {
        mClipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
    }

    @Override
    public void addPrimaryClipChangedListener(OnPrimaryClipChangedListener listener) {
        super.addPrimaryClipChangedListener(listener);
        synchronized (mPrimaryClipChangedListeners) {
            if (mPrimaryClipChangedListeners.size() == 1) {
                startListen();
            }
        }
    }

    /**
     * 开机启动后延迟10秒进行监听
     */
    private void startListen() {
        mWorking = true;
        sHandler.postDelayed(this, 10000);
    }

    private void stopListen() {
        mWorking = false;
        sHandler.removeCallbacks(this);
    }

    @Override
    public void removePrimaryClipChangedListener(OnPrimaryClipChangedListener listener) {
        super.removePrimaryClipChangedListener(listener);
        synchronized (mPrimaryClipChangedListeners) {
            if (mPrimaryClipChangedListeners.size() == 0) {
                stopListen();
            }
        }
    }

    @Override
    public CharSequence getText() {
        return mClipboardManager.getText();
    }

    /**
     * 每隔1秒执行一次检测
     * 如果粘贴板数据为空不执行 最后一次粘贴板数据与上一次数据相同不执行
     * 如果最后一次粘贴板数据与上一次不同 执行方法回调
     * {@link ClipboardManagerCompat.me.khrystal.toast.clipboard.ClipboardManagerCompat.OnPrimaryClipChangedListener}
     */
    @Override
    public void run() {
        if (mWorking) {
            CharSequence data = getText();
            Log.d("uc-toast", "run: " + data);
            check(data);
            sHandler.postDelayed(this, 1000);
        }
    }

    private void check(CharSequence data) {
        if (TextUtils.isEmpty(mLastData) && TextUtils.isEmpty(data)) {
            return;
        }
        if (!TextUtils.isEmpty(mLastData) && mLastData.equals(data)) {
            return;
        }
        mLastData = data;
        notifyPrimaryClipChanged();
    }
}
