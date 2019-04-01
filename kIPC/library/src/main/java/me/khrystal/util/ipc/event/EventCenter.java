package me.khrystal.util.ipc.event;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/4/1
 * update time:
 * email: 723526676@qq.com
 */
public class EventCenter {

    private static final Handler sHandler = new Handler(Looper.getMainLooper());

    private static ConcurrentHashMap<String, List<WeakReference<EventCallback>>> subscribers = new ConcurrentHashMap<>();

    private EventCenter() {
    }

    public static synchronized void subscirbe(String key, EventCallback callback) {
        List<WeakReference<EventCallback>> eventCallbacks = subscribers.get(key);
        if (eventCallbacks == null) {
            eventCallbacks = new ArrayList<>(5);
        }
        eventCallbacks.add(new WeakReference<EventCallback>(callback));
        subscribers.put(key, eventCallbacks);
    }

    /**
     * remove key event callback listeners;
     */
    public static synchronized void unSubscribe(String key) {
        subscribers.remove(key);
    }

    /**
     * Remove determined event listeners;
     *
     * @param callback
     */
    public static synchronized void unSubscribe(EventCallback callback) {
        for (Map.Entry<String, List<WeakReference<EventCallback>>> entry : subscribers.entrySet()) {
            List<WeakReference<EventCallback>> listeners = entry.getValue();
            for (WeakReference<EventCallback> weakRef : listeners) {
                if (callback == weakRef.get()) {
                    listeners.remove(weakRef);
                    break;
                }
            }
        }
    }

    public static synchronized void onEventReceive(String key, final Bundle event) {
        if (event == null) {
            return;
        }

        if (key != null) {
            List<WeakReference<EventCallback>> messageCallbacks = subscribers.get(key);
            if (messageCallbacks != null) {
                for (int i = messageCallbacks.size() - 1; i >= 0; --i) {
                    final WeakReference<EventCallback> eventCallback = messageCallbacks.get(i);
                    final EventCallback ec = eventCallback.get();
                    if (ec != null) {
                        sHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                ec.onEventCallBack(event);
                            }
                        });
                    } else {
                        messageCallbacks.remove(i);
                    }
                }
            }
        }
    }
}
