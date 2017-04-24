package me.khrystal.util.fingerprint.base;

import android.app.Activity;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/4/24
 * update time:
 * email: 723526676@qq.com
 */

public abstract class BaseFingerprint {

    protected Activity mActivity;
    private FingerprintIdentifyListener mIdentifyListener;
    private FingerprintIdentifyExceptionListener mExceptionListener;

    private int mNotMatchTimes = 0; // 已经验证失败次数
    private int mMaxAvailableTimes = 3; // 最大可验证次数
    private boolean mIsHardwareEnable = false; // 硬件是否可用
    private boolean mIsRegisteredFingerprint = false; // 已经录入了指纹
    private boolean mIsCanceledIdentify = false; //已经关闭了指纹

    public BaseFingerprint(Activity activity, FingerprintIdentifyExceptionListener expectionListener) {
        mActivity = activity;
        mExceptionListener = expectionListener;
    }

    public void startIdentify(int maxAvailableTimes, FingerprintIdentifyListener listener) {
        mMaxAvailableTimes = maxAvailableTimes;
        mIsCanceledIdentify = false;
        mIdentifyListener = listener;
        mNotMatchTimes = 0;
        doIdentify();
    }

    /**
     * 比如在验证指纹时来电话了 就暂时关闭指纹识别, 打完电话后再打开APP时可以继续上次wei
     */
    public void resumeIdentify() {
        if (mIdentifyListener != null && mNotMatchTimes < mMaxAvailableTimes) {
            mIsCanceledIdentify = false;
            doIdentify();
        }
    }

    protected abstract void doIdentify();


    /**
     * 验证指纹时回调的接口
     */
    public interface FingerprintIdentifyListener {
        void onSuccessed();
        void onNotMatch(int availableTimes);
        void onFailed();
    }

    /**
     * 使用指纹库产生的错误回调接口
     */
    public interface FingerprintIdentifyExceptionListener {
        void onCatchException(Throwable exception);
    }


}
