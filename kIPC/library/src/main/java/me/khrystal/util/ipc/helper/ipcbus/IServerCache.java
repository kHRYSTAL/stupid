package me.khrystal.util.ipc.helper.ipcbus;

import android.os.Bundle;
import android.os.IBinder;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/4/1
 * update time:
 * email: 723526676@qq.com
 */
public interface IServerCache {
    void join(String serverName, IBinder binder);

    void joinLocal(String serverName, Object object);

    void removeService(String serverName);

    void removeLocalService(String serverName);

    IBinder query(String serverName);

    Object queryLocal(String serverName);

    void post(String key, Bundle bundle);
}
