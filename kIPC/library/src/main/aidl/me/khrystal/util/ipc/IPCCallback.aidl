// IPCCallback.aidl
package me.khrystal.util.ipc;

// Declare any non-default types here with import statements

interface IPCCallback {
    void onSuccess(in Bundle result);
    void onFail(String reason);
}
