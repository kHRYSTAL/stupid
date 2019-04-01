package me.khrystal.util.ipc.client.core;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;

import me.khrystal.util.ipc.client.ipc.ServiceManagerNative;
import me.khrystal.util.ipc.helper.ipcbus.IPCBus;
import me.khrystal.util.ipc.helper.ipcbus.IServerCache;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/4/1
 * update time:
 * email: 723526676@qq.com
 */
public final class VirtualCore {

    private static final String TAG = VirtualCore.class.getSimpleName();

    private static VirtualCore gCore = new VirtualCore();

    private boolean isStartUp;

    private Context context;

    private VirtualCore() {
    }

    public static VirtualCore get() {
        return gCore;
    }

    public Context getContext() {
        return context;
    }

    public void startUp(Context context) {
        if (!isStartUp) {
            if (Looper.myLooper() != Looper.getMainLooper()) {
                throw new IllegalStateException("VirtualCore.startUp() must called in main thread!");
            }
            ServiceManagerNative.SERVICE_CP_AUTH = context.getPackageName() + "." + ServiceManagerNative.SERVICE_DEF_AUTH;
            this.context = context;
            IPCBus.initialize(new IServerCache() {
                @Override
                public void join(String serverName, IBinder binder) {

                }

                @Override
                public void joinLocal(String serverName, Object object) {

                }

                @Override
                public void removeService(String serverName) {

                }

                @Override
                public void removeLocalService(String serverName) {

                }

                @Override
                public IBinder query(String serverName) {
                    return null;
                }

                @Override
                public Object queryLocal(String serverName) {
                    return null;
                }

                @Override
                public void post(String key, Bundle bundle) {

                }
            });
        }
    }

    public boolean isStartUp() {
        return isStartUp;
    }
}
