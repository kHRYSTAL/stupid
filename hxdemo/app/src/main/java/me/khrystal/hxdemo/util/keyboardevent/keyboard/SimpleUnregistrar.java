package me.khrystal.hxdemo.util.keyboardevent.keyboard;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;

import java.lang.ref.WeakReference;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/5/25
 * update time:
 * email: 723526676@qq.com
 */

public class SimpleUnregistrar implements Unregistrar {

    private WeakReference<Activity> mActivityWeakReference;

    private WeakReference<ViewTreeObserver.OnGlobalLayoutListener> mOnGlobalLayoutListenerWeakReference;

    public SimpleUnregistrar(Activity activity, ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener) {
        mActivityWeakReference = new WeakReference<Activity>(activity);
        mOnGlobalLayoutListenerWeakReference = new WeakReference<ViewTreeObserver.OnGlobalLayoutListener>(globalLayoutListener);
    }

    @Override
    public void unregister() {
        Activity activity = mActivityWeakReference.get();
        ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener = mOnGlobalLayoutListenerWeakReference.get();

        if (null != activity && null != globalLayoutListener) {
            View activityRoot = KeyboardVisibilityEvent.getActivityRoot(activity);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                activityRoot.getViewTreeObserver()
                        .removeOnGlobalLayoutListener(globalLayoutListener);
            } else {
                activityRoot.getViewTreeObserver()
                        .removeGlobalOnLayoutListener(globalLayoutListener);
            }
        }

        mActivityWeakReference.clear();
        mOnGlobalLayoutListenerWeakReference.clear();
    }
}
