package me.khrystal.suspensionwindow.window;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import me.khrystal.suspensionwindow.WebViewActivity;
import me.khrystal.suspensionwindow.util.SPUtil;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/10/26
 * update time:
 * email: 723526676@qq.com
 */
public class ApplicationLifecycle implements Application.ActivityLifecycleCallbacks {

    private static final String TAG = ApplicationLifecycle.class.getSimpleName();
    private int started = 0;

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityStarted(Activity activity) {
        started++;
        if (started == 1) {
            Log.e(TAG, "应用在前台了");
            if (SPUtil.getIntDefault(WebViewActivity.ARTICLE_ID, -1) > 0) {
                activity.startService(new Intent(activity, WindowShowService.class));
            }
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        started--;
        if (started == 0) {
            Log.e(TAG, "应用在后台了");
            activity.stopService(new Intent(activity, WindowShowService.class));
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
