// IEventReceiver.aidl
package me.khrystal.util.ipc;

// Declare any non-default types here with import statements

interface IEventReceiver {
    void onEventReceiver(String key, in Bundle event);
}
