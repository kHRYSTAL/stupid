package me.khrystal.hxdemo.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.khrystal.hxdemo.R;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/3/13
 * update time:
 * email: 723526676@qq.com
 */

public class NotificationUtils {

    /**
     *  tagList 用来标记是否应该展示notification
     *  比如已经在聊天页面了 实际上就不应该再弹出notification
     */
    private static List<String> notificationTagList = new ArrayList<>();

    /**
     * 添加tag到tagList 在MessageHandler 弹出notification 前回判断是否相等
     * 如果相等 则表示在当前聊天页面 不弹 反之 则弹出
     * @param tag
     */
    public static void addTag(String tag) {
        if (!notificationTagList.contains(tag))
            notificationTagList.add(tag);
    }

    /**
     * 退出页面或熄屏 移除tag
     * @param tag
     */
    public static void removeTag(String tag) {
        notificationTagList.remove(tag);
    }

    /**
     * 判断是否应该弹出notification
     * 判断标准是该tag是否包含在tag list中
     * @param tag
     * @return
     */
    public static boolean isShowNotification(String tag) {
        return  !notificationTagList.contains(tag);
    }

    public static void showNotification(Context context, String title, String content, String sound, Intent intent) {
        intent.setFlags(0);
        int notificationId = (new Random()).nextInt();
        PendingIntent contentIntent = PendingIntent.getBroadcast(context, notificationId, intent, 0);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setContentIntent(contentIntent)
                .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                .setContentText(content);
        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = mBuilder.build();

        if (null != sound && sound.trim().length() > 0) {
            notification.sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + sound);
        }
        manager.notify(notificationId, notification);


    }
}
