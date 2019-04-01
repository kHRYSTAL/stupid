package me.khrystal.util.ipc;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;

import me.khrystal.util.ipc.client.core.VirtualCore;
import me.khrystal.util.ipc.client.ipc.ServiceManagerNative;
import me.khrystal.util.ipc.event.EventCallback;
import me.khrystal.util.ipc.event.EventCenter;
import me.khrystal.util.ipc.helper.ipcbus.IPCBus;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/4/1
 * update time:
 * email: 723526676@qq.com
 */
public class VCore {

    private static final VCore V_CORE = new VCore();

    public static void init(Context context) {
        VirtualCore.get().startUp(context);
    }

    public static VCore getCore() {
        return V_CORE;
    }

    public VCore registerService(Class<?> interfaceClass, Object server) {
        if (VirtualCore.get().getContext() == null) {
            return this;
        }
        Object o = IPCBus.getLocalService(interfaceClass);
        IBinder service = ServiceManagerNative.getService(interfaceClass.getName());
        if (service != null && o != null) {
            return this;
        }
        IPCBus.registerLocal(interfaceClass, server);
        IPCBus.register(interfaceClass, server);
        return this;
    }

    public VCore unRegisterServer(Class<?> interfaceClass) {
        IPCBus.unregisterLocal(interfaceClass);
        IPCBus.removeService(interfaceClass.getName());
        return this;
    }

    public <T> T getService(Class<T> ipcClass) {
        T localService = IPCBus.getLocalService(ipcClass);
        if (localService != null) {
            return localService;
        }
        return VManager.get().getService(ipcClass);
    }

    public VCore registerLocalService(Class<?> interfaceClass, Object server) {
        Object o = IPCBus.getLocalService(interfaceClass);
        if (o != null) {
            return this;
        }
        IPCBus.registerLocal(interfaceClass, server);
        return this;
    }

    public VCore unRegisterLocalService(Class<?> interfaceClass) {
        IPCBus.unregisterLocal(interfaceClass);
        return this;
    }

    public <T> T getLocalService(Class<T> ipcClass) {
        T localService = IPCBus.getLocalService(ipcClass);
        return localService;
    }

    /**
     * Subscription listener
     *
     * @param key
     * @param eventCallback
     */
    public void subscribe(String key, EventCallback eventCallback) {
        EventCenter.subscirbe(key, eventCallback);
    }

    /**
     * remove determined event listeners
     *
     * @param eventCallback
     */
    public void unSubscribe(EventCallback eventCallback) {
        EventCenter.unSubscribe(eventCallback);
    }

    /**
     * remove key all event callback listeners
     *
     * @param key
     */
    public void unSubscribe(String key) {
        EventCenter.unSubscribe(key);
    }

    /**
     * send data
     *
     * @param key
     * @param event
     */
    public void post(String key, Bundle event) {
        IPCBus.post(key, event);
    }

}
