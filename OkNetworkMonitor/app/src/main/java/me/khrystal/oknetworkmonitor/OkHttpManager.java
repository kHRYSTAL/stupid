package me.khrystal.oknetworkmonitor;


import me.khrystal.util.reporter.OkNetworkMonitorInterceptor;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/6/12
 * update time:
 * email: 723526676@qq.com
 */

public class OkHttpManager {
    private static final String TAG = OkHttpManager.class.getSimpleName();
    private volatile static OkHttpManager sIntance;

    public static OkHttpManager getInstance() {
        synchronized (OkHttpManager.class) {
            if (sIntance == null) {
                synchronized (OkHttpManager.class) {
                    sIntance = new OkHttpManager();
                }
            }
        }
        return sIntance;
    }

    public void get(String url, Callback callback) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new OkNetworkMonitorInterceptor())
                .build();
        Request request = new Request.Builder()
                .url(url)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }
}
