package me.khrystal.toast.clipboard;

import android.content.Context;
import android.os.Build;

import java.util.ArrayList;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/1/25
 * update time:
 * email: 723526676@qq.com
 */

public abstract class ClipboardManagerCompat {

    protected final ArrayList<OnPrimaryClipChangedListener> mPrimaryClipChangedListeners
            = new ArrayList<>();
    public static ClipboardManagerCompat creat(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return new ClipboardManagerImpl11(context);
        } else {
            return new ClipboardManagerImpl9(context);
        }
    }

    public void addPrimaryClipChangedListener(OnPrimaryClipChangedListener listener) {
        synchronized (mPrimaryClipChangedListeners) {
            mPrimaryClipChangedListeners.add(listener);
        }
    }

    protected final void notifyPrimaryClipChanged() {
        synchronized (mPrimaryClipChangedListeners) {
            for (int i = 0; i < mPrimaryClipChangedListeners.size(); i++) {
                mPrimaryClipChangedListeners.get(i).onPrimaryClipChanged();
            }
        }
    }

    public void removePrimaryClipChangedListener(OnPrimaryClipChangedListener listener) {
        synchronized (mPrimaryClipChangedListeners) {
            mPrimaryClipChangedListeners.remove(listener);
        }
    }

    public abstract CharSequence getText();

    public interface OnPrimaryClipChangedListener {
        void onPrimaryClipChanged();
    }
}
