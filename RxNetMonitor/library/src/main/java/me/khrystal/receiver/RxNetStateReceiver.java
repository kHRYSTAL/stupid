package me.khrystal.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/5/11
 * update time:
 * email: 723526676@qq.com
 */

public class RxNetStateReceiver extends BroadcastReceiver {

    private static final String TAG = RxNetStateReceiver.class.getSimpleName();
    public final static String CUSTOM_ANDROID_NET_CHANGE_ACTION = "me.khrystal.api.netstatus.CONNECTIVITY_CHANGE";
    private final static String ANDROID_NET_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    private static boolean isNetAvailable = false;
    private static int mNetType;
    private static volatile RxNetStateReceiver mBroadcastReceiver;
    private final PublishSubject<Object> mEventBus = PublishSubject.create();

    public static RxNetStateReceiver getReceiver() {
        if (null == mBroadcastReceiver) {
            synchronized (RxNetStateReceiver.class) {
                if (null == mBroadcastReceiver) {
                    mBroadcastReceiver = new RxNetStateReceiver();
                }
            }
        }
        return mBroadcastReceiver;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mBroadcastReceiver = RxNetStateReceiver.this;
        if (intent.getAction().equalsIgnoreCase(ANDROID_NET_CHANGE_ACTION) || intent.getAction().equalsIgnoreCase(CUSTOM_ANDROID_NET_CHANGE_ACTION)) {
            if (!NetUtils.isNetworkAvailable(context)) {
                isNetAvailable = false;
            } else {
                isNetAvailable = true;
                mNetType = NetUtils.getAPNType(context);
            }
            notifyObserver();
        }
    }

    public static void registerNetworkStateReceiver(Context mContext) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(CUSTOM_ANDROID_NET_CHANGE_ACTION);
        filter.addAction(ANDROID_NET_CHANGE_ACTION);
        mContext.getApplicationContext().registerReceiver(getReceiver(), filter);
    }

    public static void checkNetworkState(Context mContext) {
        Intent intent = new Intent();
        intent.setAction(CUSTOM_ANDROID_NET_CHANGE_ACTION);
        mContext.sendBroadcast(intent);
    }

    public static void unRegisterNetworkStateReceiver(Context mContext) {
        if (mBroadcastReceiver != null) {
            try {
                mContext.getApplicationContext().unregisterReceiver(mBroadcastReceiver);
            } catch (Exception e) {

            }
        }

    }

    public static boolean isNetworkAvailable() {
        return isNetAvailable;
    }

    public static @NetType.NetTypeChecker
    int getAPNType() {
        return mNetType;
    }

    private void notifyObserver() {
        mEventBus.onNext(new NetState(
                isNetworkAvailable() ? mNetType : NetType.NONE,
                isNetworkAvailable()
        ));
    }

    /**
     * 添加网络监听
     */
    public Observable<NetState> toObservable() {
        return mEventBus.ofType(NetState.class);
    }
}
