package me.khrystal.widget.toast;

import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * usage: support disable notification permission show Toast
 *          when notification permission is open, show System Toast
 * author: kHRYSTAL
 * create time: 17/6/12
 * update time:
 * email: 723526676@qq.com
 */

public class Toast {
    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

    private static int checkNotification = 0;
    private Object mToast;
    public static final int LENGTH_SHORT = 0;
    public static final int LENGTH_LONG = 1;

    private Toast(Context context, String message, int duration) {
        try {
            checkNotification = isNotificationEnabled(context) ? 0 : 1;
            if (checkNotification == 1 && context instanceof Activity) {
                mToast = KToast.makeText(context, message, duration);
            } else {
                mToast = android.widget.Toast.makeText(context, message, duration);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Toast(Context context, int resId, int duration) {
        if (checkNotification == -1) {
            checkNotification = isNotificationEnabled(context) ? 0 : 1;
        }

        if (checkNotification == 1 && context instanceof  Activity) {
            mToast = KToast.makeText(context, resId, duration);
        } else {
            mToast = android.widget.Toast.makeText(context, resId, duration);
        }
    }

    public static Toast makeText(Context context, String message, int duration) {
        return new Toast(context, message, duration);
    }

    public static Toast makeText(Context context, int resId, int duration) {
        return new Toast(context, resId, duration);
    }

    public void show() {
        if (mToast instanceof KToast) {
            ((KToast) mToast).show();
        } else if (mToast instanceof android.widget.Toast) {
            ((android.widget.Toast) mToast).show();
        }
    }

    public void cancel() {
        if (mToast instanceof KToast) {
            ((KToast) mToast).cancel();
        } else if (mToast instanceof android.widget.Toast) {
            ((android.widget.Toast) mToast).cancel();
        }
    }

    public void setText(int resId) {
        if (mToast instanceof KToast) {
            ((KToast) mToast).setText(resId);
        } else if (mToast instanceof android.widget.Toast) {
            ((android.widget.Toast) mToast).setText(resId);
        }
    }

    public void setText(CharSequence s){
        if(mToast instanceof KToast){
            ((KToast) mToast).setText(s);
        }else if(mToast instanceof android.widget.Toast){
            ((android.widget.Toast) mToast).setText(s);
        }
    }

    private static boolean isNotificationEnabled(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return true;
        }
        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();

        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;
        Class appOpsClass = null;

        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE,
                    Integer.TYPE, String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);
            int value = (int) opPostNotificationValue.get(Integer.class);
            return ((int) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
