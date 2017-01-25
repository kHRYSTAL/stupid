package me.khrystal.toast;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * usage: 监听开机广播启动服务
 * author: kHRYSTAL
 * create time: 17/1/25
 * update time:
 * email: 723526676@qq.com
 */

public class BootCompletedReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ListenClipboardService.startForWeakLock(context, intent);
    }
}
