package me.khrystal.hxdemo.util.keyboardevent.keyboard;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/5/25
 * update time:
 * email: 723526676@qq.com
 */
@SuppressWarnings("NewApi")
public abstract class AutoActivityLifecycleCallback implements Application.ActivityLifecycleCallbacks {

    private final Activity mTargetActivity;

    public AutoActivityLifecycleCallback(Activity targetActivity) {
        mTargetActivity = targetActivity;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        if (activity == mTargetActivity) {
            mTargetActivity.getApplication().unregisterActivityLifecycleCallbacks(this);
            onTargetActivityDestroyed();
        }
    }

    protected abstract void onTargetActivityDestroyed();
}
