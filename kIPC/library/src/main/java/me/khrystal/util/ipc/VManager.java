package me.khrystal.util.ipc;

import java.util.concurrent.ConcurrentHashMap;

import me.khrystal.util.ipc.helper.ipcbus.IPCBus;
import me.khrystal.util.ipc.helper.ipcbus.IPCSingleton;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/4/1
 * update time:
 * email: 723526676@qq.com
 */
public class VManager {

    private static final VManager sVM = new VManager();

    private ConcurrentHashMap<Class, IPCSingleton> mIPCSingletonArrayMap = new ConcurrentHashMap<>();

    public static VManager get() {
        return sVM;
    }

    public <T> T getService(Class<T> ipcClass) {
        T t = IPCBus.get(ipcClass);
        if (t != null) {
            return t;
        }
        IPCSingleton<T> tipcSingleton = mIPCSingletonArrayMap.get(ipcClass);
        if (tipcSingleton == null) {
            tipcSingleton = new IPCSingleton<>(ipcClass);
            mIPCSingletonArrayMap.put(ipcClass, tipcSingleton);
        }
        return tipcSingleton.get();
    }
}
