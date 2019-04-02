package me.khrystal.util.ipc.service;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

import me.khrystal.util.ipc.IServiceFetcher;
import me.khrystal.util.ipc.client.core.VirtualCore;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/4/1
 * update time:
 * email: 723526676@qq.com
 */
public class BinderProvider extends ContentProvider {

    private final ServiceFetcher mServiceFetcher = new ServiceFetcher();

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DaemonService.startup(context);
        if (VirtualCore.get().isStartUp()) {
            return true;
        }
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

    private class ServiceFetcher extends IServiceFetcher.Stub {

        @Override
        public IBinder getService(String name) throws RemoteException {
            if (name != null) {
                return ServiceCache.getService(name);
            }
            return null;
        }

        @Override
        public void addService(String name, IBinder service) throws RemoteException {
            if (name != null && service != null) {
                ServiceCache.addService(name, service);
            }
        }

        @Override
        public void addEventListener(String name, IBinder service) throws RemoteException {
            if (name != null && service != null) {
                ServiceCache.addEventListener(name, service);
            }
        }

        @Override
        public void removeService(String name) throws RemoteException {
            if (name != null) {
                ServiceCache.removeService(name);
            }
        }

        @Override
        public void removeEventListener(String name) throws RemoteException {
            if (name != null) {
                ServiceCache.removeEventListener(name);
            }
        }

        @Override
        public void post(String key, Bundle result) throws RemoteException {
            if (result != null) {
                ServiceCache.sendEvent(key, result);
            }
        }
    }
}
