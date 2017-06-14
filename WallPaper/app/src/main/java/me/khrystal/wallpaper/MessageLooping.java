package me.khrystal.wallpaper;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.hardware.Camera;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;

/**
 * usage: 检测包名轮询
 * author: kHRYSTAL
 * create time: 17/6/14
 * update time:
 * email: 723526676@qq.com
 */

public class MessageLooping {

    private static final String TAG = MessageLooping.class.getSimpleName();

    private static MessageLooping INSTANCE;

    private static final int TIME_INTERVAL = 10;

    private Subscription subscribe;
    private Context mContext;
    private Camera mCamera;

    private static final String SELF_PKG_NAME = "me.khrystal.wallpaper";
    private static String SystemCameraPkgName;


    private MessageLooping(Context context, Camera camera) {
        mContext = context;
        mCamera = camera;

        // 获取系统相机包名
        Intent infoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ResolveInfo res = context.getPackageManager().resolveActivity(infoIntent, 0);
        if (res != null) {
            SystemCameraPkgName = res.activityInfo.packageName;
        }
    }

    public static MessageLooping getInstance(Context context, Camera camera) {
        if (INSTANCE == null) {
            synchronized (MessageLooping.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MessageLooping(context, camera);
                }
            }
        }
        return INSTANCE;
    }

    public void startLooping() {
        checkPackageNameTask();
        subscribe = Observable.interval(TIME_INTERVAL, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        checkPackageNameTask();
                    }
                });
    }

    public void stopLooping() {
        if (subscribe != null && !subscribe.isUnsubscribed()) {
            subscribe.unsubscribe();
        }
    }

    /**
     * 校验包名是否为系统相机
     */
    private void checkPackageNameTask() {
        String topPkgName = getTopPkgName();
        if (!TextUtils.isEmpty(SystemCameraPkgName) && !TextUtils.isEmpty(topPkgName)) {
//            Log.e(TAG, "pkg:" + SystemCameraPkgName + "," + topPkgName + "," + SELF_PKG_NAME);
            if (TextUtils.equals(SystemCameraPkgName, topPkgName) || TextUtils.equals(topPkgName, SELF_PKG_NAME)) {
                if (mCamera != null) {
                    try {
                        mCamera.stopPreview();
                        mCamera.setPreviewCallback(null);
                        mCamera.release();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mCamera = null;
                }
            }
        }
    }

    /**
     * 获取最上方应用包名
     */
    private String getTopPkgName() {
        String topPkgName = null;
        if (mContext != null) {
            ActivityManager mActivityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
            topPkgName = rti.get(0).topActivity.getPackageName();
        }
        return topPkgName;
    }

}
