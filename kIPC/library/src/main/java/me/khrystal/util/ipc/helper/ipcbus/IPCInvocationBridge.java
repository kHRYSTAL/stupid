package me.khrystal.util.ipc.helper.ipcbus;

import android.os.IBinder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/4/1
 * update time:
 * email: 723526676@qq.com
 */
public class IPCInvocationBridge implements InvocationHandler {

    private ServerInterface serverInterface;
    private IBinder binder;

    public IPCInvocationBridge(ServerInterface serverInterface, IBinder binder) {
        this.serverInterface = serverInterface;
        this.binder = binder;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        IPCMethod ipcMethod = serverInterface.getIPCMethod(method);
        if (ipcMethod == null)
            throw new IllegalStateException("Can not found the ipc method: " + method.getDeclaringClass().getName()
                    + "@" + method.getName());
        return ipcMethod.callRemote(binder, objects);
    }
}
