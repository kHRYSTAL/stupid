package me.khrystal.spiderman;

import android.app.Application;

import me.khrystal.util.spiderman.CrashModel;
import me.khrystal.util.spiderman.SpiderMan;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/8/1
 * update time:
 * email: 723526676@qq.com
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SpiderMan.getInstance()
                .init(this)
                // 设置是否捕获异常 不弹出崩溃框
                .setEnable(true)
                //设置是否显示崩溃信息展示页面
                .showCrashMessage(true)
                // 是否回调一场信息 友盟等第三方崩溃信息收集平台会用到
                .setOnCrashListener(new SpiderMan.OnCrashListener() {
                    @Override
                    public void onCrash(Thread t, Throwable ex, CrashModel model) {
                        // CrashModel 崩溃信息 包含设备信息
                    }
                });
    }
}
