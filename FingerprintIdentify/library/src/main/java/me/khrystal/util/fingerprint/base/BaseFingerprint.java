package me.khrystal.util.fingerprint.base;

import android.app.Activity;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/4/24
 * update time:
 * email: 723526676@qq.com
 */

public abstract class   BaseFingerprint {

    protected Activity mActivity;
    private FingerprintIdentifyListener mIdentifyListener;
    private FingerprintIdentifyExceptionListener mExceptionListener;

    private int mNumberOfFailures = 0; // 已经验证失败次数
    private int mMaxAvailableTimes = 3; // 最大可验证次数
    private boolean mIsHardwareEnable = false; // 硬件是否可用
    private boolean mIsRegisteredFingerprint = false; // 已经录入了指纹
    private boolean mIsCanceledIdentify = false; //已经关闭了指纹
    private boolean mIsCalledStartIdentify = false;

    public BaseFingerprint(Activity activity, FingerprintIdentifyExceptionListener expectionListener) {
        mActivity = activity;
        mExceptionListener = expectionListener;
    }

    public void startIdentify(int maxAvailableTimes, FingerprintIdentifyListener listener) {
        mMaxAvailableTimes = maxAvailableTimes;
        mIsCanceledIdentify = false;
        mIsCalledStartIdentify = true;
        mIdentifyListener = listener;
        mNumberOfFailures = 0;

        doIdentify();
    }

    /**
     * 比如在验证指纹时来电话了 就暂时关闭指纹识别, 打完电话后再打开APP时可以继续上次wei
     */
    public void resumeIdentify() {
        if (mIdentifyListener != null && mNumberOfFailures < mMaxAvailableTimes) {
            mIsCanceledIdentify = false;
            doIdentify();
        }
    }

    public void cancelIdentify() {
        mIsCanceledIdentify = true;
        doCancelIdentify();
    }

    /**
     * 当不匹配时 是否需要重新进行验证
     *
     * @return
     */
    protected boolean needToCallDoIdentifyAgainAfterNotMatch() {
        return true;
    }

    /**
     * 捕获所有异常
     *
     * @param exception
     */
    protected void onCatchException(Throwable exception) {
        if (mExceptionListener != null && exception != null) {
            mExceptionListener.onCatchException(exception);
        }
    }

    /**
     * 验证
     */
    protected abstract void doIdentify();


    /**
     * 取消验证
     */
    protected abstract void doCancelIdentify();

    /**
     * 验证成功回调
     */
    protected void onSucceed() {
        if (mIsCanceledIdentify) {
            return;
        }
        mNumberOfFailures = mMaxAvailableTimes;
        if (mIdentifyListener != null) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mIdentifyListener.onSuccessed();
                }
            });
        }
        cancelIdentify();
    }

    protected void onNotMatch() {
        if (mIsCanceledIdentify) {
            return;
        }
        if (++mNumberOfFailures < mMaxAvailableTimes) {
            if (mIdentifyListener != null) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mIdentifyListener.onNotMatch(mMaxAvailableTimes - mNumberOfFailures);
                    }
                });
            }
            if (needToCallDoIdentifyAgainAfterNotMatch()) {
                doIdentify();
            }
        }

        onFailed();
    }

    protected void onFailed() {
        if (mIsCanceledIdentify)
            return;
        mNumberOfFailures = mMaxAvailableTimes;
        if (mIdentifyListener != null) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mIdentifyListener.onFailed();
                }
            });
        }
        cancelIdentify();
    }

    public boolean isEnable() {
        return mIsHardwareEnable && mIsRegisteredFingerprint;
    }

    public boolean isHardwareEnable() {
        return mIsHardwareEnable;
    }

    protected void setHardwareEnable(boolean hardwareEnable) {
        mIsHardwareEnable = hardwareEnable;
    }

    public boolean isRegisteredFingerprint() {
        return mIsRegisteredFingerprint;
    }

    protected void setRegisteredFingerprint(boolean registeredFingerprint) {
        mIsRegisteredFingerprint = registeredFingerprint;
    }


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
