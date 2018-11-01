package me.khrystal.suspensionwindow.window;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import me.khrystal.suspensionwindow.WebViewActivity;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/10/26
 * update time:
 * email: 723526676@qq.com
 */
public class WindowShowService extends Service implements WindowUtil.OnPermissionListener {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        WindowUtil.getInstance().showPermissionWindow(this, this);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        WindowUtil.getInstance().dismissWindow();
    }

    @Override
    public void result(boolean isSuccess) {
        Intent intent = new Intent(WebViewActivity.BROAD_CAST_NAME);
        intent.putExtra("permission", isSuccess);
        sendBroadcast(intent);
    }
}
