package me.khrystal.shortcututils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/6/19
 * update time:
 * email: 723526676@qq.com
 */

public class ShortUtil {

    public static final String TAG = ShortUtil.class.getSimpleName();

    private Context mContext;
    private static ShortUtil mInstance = null;

    private ShortUtil(Context context) {
        mContext = context;
    }

    public static synchronized ShortUtil getInstance(Context context) {
        if (mInstance == null) {
            synchronized (ShortUtil.class) {
                if (mInstance == null)
                    mInstance = new ShortUtil(context);
            }
        }
        return mInstance;
    }

    public void installShortCut(String name, int icon, Intent intent) {
        Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");

        // 快捷方式的名称
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
        shortcut.putExtra("duplicate", false); // 不允许重复创建

        // 快捷方式的图标
        Intent.ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(mContext, icon);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);

        intent.setAction("android.intent.action.MAIN");// 桌面图标和应用绑定，卸载应用后系统会同时自动删除图标
        intent.addCategory("android.intent.category.LAUNCHER");// 桌面图标和应用绑定，卸载应用后系统会同时自动删除图标
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);

        mContext.sendBroadcast(shortcut);
    }

    /**
     * 删除桌面快捷方式
     * @param context
     * @param shortcutName
     * 快捷方式名
     * @param actionIntent
     * 快捷方式操作，也就是上面创建的Intent
     * @param isDuplicate
     * 为true时循环删除快捷方式（即存在很多相同的快捷方式）
     */
    public void deleteShortcut(Context context , String shortcutName ,
                               Intent actionIntent , boolean isDuplicate) {
        Intent shortcutIntent = new Intent ("com.android.launcher.action.UNINSTALL_SHORTCUT");
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME ,shortcutName);
        shortcutIntent.putExtra("duplicate" , isDuplicate);
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT , actionIntent);
        context.sendBroadcast(shortcutIntent);
    }

    public boolean hasShortcut(String name) {
        String url;

        String packageName = getLauncherPackageName(mContext);
        if (packageName == null || packageName.equals("")
                || packageName.equals("com.android.launcher")) {
            int sdkInt = android.os.Build.VERSION.SDK_INT;
            if (sdkInt < 8) { // Android 2.1.x(API 7)以及以下的
                packageName = "com.android.launcher.settings";
            } else if (sdkInt < 19) {// Android 4.4以下
                packageName = "com.android.launcher2.settings";
            } else {// 4.4以及以上
                packageName = "com.android.launcher3.settings";
            }
        }

        url = "content://" + packageName + ".settings/favorites?notify=true";
        try {
            ContentResolver resolver = mContext.getContentResolver();
            Cursor cursor = resolver.query(Uri.parse(url), new String[] {
                            "title", "iconResource" }, "title=?",
                    new String[] { name }, null);
            if (cursor != null && cursor.getCount() > 0) {
                return true;
            }

        } catch (Exception e) {
            if (e != null) {
                e.printStackTrace();
            }

        }
        return false;
    }

    public String getLauncherPackageName(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        ResolveInfo res = context.getPackageManager()
                .resolveActivity(intent, 0);
        if (res.activityInfo == null) {
            return "";
        }
        if (res.activityInfo.packageName.equals("android")) {
            return "";
        } else {
            return res.activityInfo.packageName;
        }
    }

}
