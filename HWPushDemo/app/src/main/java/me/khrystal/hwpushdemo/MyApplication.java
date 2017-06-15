package me.khrystal.hwpushdemo;

import android.app.Application;

public class MyApplication extends Application {

    private static PustDemoActivity mPustTestActivity = null;

    private static MyApplication mInstance;

    public static MyApplication instance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        exit();
    }

    public void setMainActivity(PustDemoActivity activity) {
        mPustTestActivity = activity;
    }

    public PustDemoActivity getMainActivity() {
        return mPustTestActivity;
    }

    public void exit() {
        if (mPustTestActivity != null) {
            mPustTestActivity.finish();
        }
        System.exit(0);
    }

}