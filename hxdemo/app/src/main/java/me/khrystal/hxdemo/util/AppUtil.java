package me.khrystal.hxdemo.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;

import java.util.Iterator;
import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/3/9
 * update time:
 * email: 723526676@qq.com
 */

public class AppUtil {

    public static String getAppName(Context context, int processID) {
        String processName = null;
        ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = context.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == processID) {
                    // 根据进程信息获取当前进程名字
                    processName = info.processName;
                    // 返回当前进程名字
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }
}
