package me.khrystal.widget.tools;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.util.Stack;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/10/9
 * update time:
 * email: 723526676@qq.com
 */

public class WxSwipeBackActivityManager extends ActivityLifecycleCallbacksAdapter {

    private static final WxSwipeBackActivityManager instance = new WxSwipeBackActivityManager();
    private Stack<Activity> mActivityStack = new Stack<>();

    private WxSwipeBackActivityManager(){}

    public static WxSwipeBackActivityManager getInstance() {
        return instance;
    }

    public void init(Application application) {
        application.registerActivityLifecycleCallbacks(this);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        mActivityStack.add(activity);
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        mActivityStack.remove(activity);
    }

    /**
     * 获取倒数第二个activity 用于微信侧滑的下层效果
     * @return
     */
    public Activity getPenultimateActivity() {
        return mActivityStack.size() >= 2 ? mActivityStack.get(mActivityStack.size() - 2) : null;
    }
}
