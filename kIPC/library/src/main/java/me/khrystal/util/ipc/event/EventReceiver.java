package me.khrystal.util.ipc.event;

import android.os.Bundle;
import android.os.RemoteException;

import me.khrystal.util.ipc.IEventReceiver;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/4/1
 * update time:
 * email: 723526676@qq.com
 */
public class EventReceiver extends IEventReceiver.Stub {

    private static final EventReceiver EVENT_RECEIVER = new EventReceiver();

    private EventReceiver() {
    }

    public static final EventReceiver getInstance() {
        return EVENT_RECEIVER;
    }

    @Override
    public void onEventReceive(String key, Bundle event) throws RemoteException {
        EventCenter.onEventReceive(key, event);
    }
}
