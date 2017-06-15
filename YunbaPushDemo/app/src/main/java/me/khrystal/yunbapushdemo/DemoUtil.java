package me.khrystal.yunbapushdemo;

import java.util.List;
import java.util.Random;

import io.yunba.android.manager.YunBaManager;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.telephony.TelephonyManager;

public class DemoUtil {
	public static boolean isEmpty(String s) {
		if (null == s)
			return true;
		if (s.length() == 0)
			return true;
		if (s.trim().length() == 0)
			return true;
		return false;
	}

	public static boolean showNotifation(Context context, String topic,
			String msg) {
		try {
			 Uri alarmSound = RingtoneManager
			 .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			
			 long[] pattern = { 500, 500, 500 };
			 NotificationCompat.Builder mBuilder = new
			 NotificationCompat.Builder(
			 context).setSmallIcon(R.mipmap.ic_launcher)
			 .setContentTitle(topic).setContentText(msg)
			 .setSound(alarmSound).setVibrate(pattern).setAutoCancel(true);
			 // Creates an explicit intent for an Activity in your app
			 Intent resultIntent = new Intent(context, MainActivity.class);
			
			 if (!DemoUtil.isEmpty(topic))
			 resultIntent.putExtra(YunBaManager.MQTT_TOPIC, topic);
			 if (!DemoUtil.isEmpty(msg))
			 resultIntent.putExtra(YunBaManager.MQTT_MSG, msg);

			 TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
			 stackBuilder.addParentStack(MainActivity.class);
			 stackBuilder.addNextIntent(resultIntent);
			 PendingIntent resultPendingIntent =
			 stackBuilder.getPendingIntent(
			 0, PendingIntent.FLAG_UPDATE_CURRENT);
			
			 mBuilder.setContentIntent(resultPendingIntent);
			 NotificationManager mNotificationManager = (NotificationManager)
			 context.getSystemService(Context.NOTIFICATION_SERVICE);
			 // mId allows you to update the notification later on.
			 Random r = new Random();
			 mNotificationManager.notify(r.nextInt(), mBuilder.build());
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static void showToast(final String toast, final Context context) {
	}

	public static boolean isAppOnForeground(Context context) {
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		if (appProcesses == null)
			return false;
		for (RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(context.getPackageName())
					&& appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}
		return false;
	}

	public static <T> String join(T[] array, String cement) {
		StringBuilder builder = new StringBuilder();

		if (array == null || array.length == 0) {
			return null;
		}
		for (T t : array) {
			builder.append(t).append(cement);
		}

		builder.delete(builder.length() - cement.length(), builder.length());

		return builder.toString();
	}

	public static boolean isNetworkEnabled(Context context) {
		ConnectivityManager conn = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = conn.getActiveNetworkInfo();
		return (info != null && info.isConnected());
	}

	public static String getImei(Context context, String imei) {
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		imei = telephonyManager.getDeviceId();
		return imei;
	}

	public static String getAppKey(Context context) {
		Bundle metaData = null;
		String appKey = null;
		try {
			ApplicationInfo ai = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);
			if (null != ai) {
				metaData = ai.metaData;
			}
			if (null != metaData) {
				appKey = metaData.getString("YUNBA_APPKEY");
				if ((null == appKey) || appKey.length() != 24) {
					appKey = "Error";
				}
			}
		} catch (NameNotFoundException e) {

		}
		return appKey;
	}
}