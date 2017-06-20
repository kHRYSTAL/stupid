package me.khrystal.shortcututils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/6/19
 * update time:
 * email: 723526676@qq.com
 */

public class ShortUtil {

    public static final String TAG = ShortUtil.class.getSimpleName();

    // Action 移除Shortcut
    public static final String ACTION_REMOVE_SHORTCUT = "com.android.launcher.action.UNINSTALL_SHORTCUT";

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
     * 移除快捷方式
     */
    public void deleteShortCut(Context context, String shortcutName) {
        Intent shortcut = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
        //快捷方式的名称
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortcutName);
        Intent intent = new Intent();
        intent.setClass(context, context.getClass());
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
        context.sendBroadcast(shortcut);
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
            Cursor cursor = resolver.query(Uri.parse(url), new String[]{
                            "title", "iconResource"}, "title=?",
                    new String[]{name}, null);
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


    /**
     * 更新桌面快捷方式图标，不一定所有图标都有效(有可能需要系统权限)
     *
     * @param context context
     * @param title   快捷方式名
     * @param intent  快捷方式Intent
     * @param bitmap  快捷方式Icon
     */
    public void updateShortcutIcon(Context context, String title, Intent intent, Bitmap bitmap) {
        if (bitmap == null) {
            Log.i("HJ", "update shortcut icon,bitmap empty");
            return;
        }
        try {
            ContentResolver cr = context.getContentResolver();
            Uri uri = getUriFromLauncher(context);
            Cursor c = cr.query(uri, new String[]{"_id", "title", "intent"},
                    "title=?  and intent=? ",
                    new String[]{title, intent.toUri(0)}, null);
            int index = -1;
            if (c != null && c.getCount() > 0) {
                c.moveToFirst();
                index = c.getInt(0);//获得图标索引
                ContentValues cv = new ContentValues();
                cv.put("icon", flattenBitmap(bitmap));
                Uri uri2 = Uri.parse(uri.toString() + "/favorites/" + index + "?notify=true");
                int i = context.getContentResolver().update(uri2, cv, null, null);
                context.getContentResolver().notifyChange(uri, null);//此处不能用uri2，是个坑
                Log.i("HJ", "update ok: affected " + i + " rows,index is" + index);
            } else {
                Log.i("HJ", "update result failed");
            }
            if (c != null && !c.isClosed()) {
                c.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.i("HJ", "update shortcut icon,get errors:" + ex.getMessage());
        }
    }

    private static byte[] flattenBitmap(Bitmap bitmap) {
        // Try go guesstimate how much space the icon will take when serialized
        // to avoid unnecessary allocations/copies during the write.
        int size = bitmap.getWidth() * bitmap.getHeight() * 4;
        ByteArrayOutputStream out = new ByteArrayOutputStream(size);
        try {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            return out.toByteArray();
        } catch (IOException e) {
            Log.w("HJ", "Could not write icon");
            return null;
        }
    }

    private static Uri getUriFromLauncher(Context context) {
        StringBuilder uriStr = new StringBuilder();
        String authority = LauncherUtil.getAuthorityFromPermissionDefault(context);
        if (authority == null || authority.trim().equals("")) {
            authority = LauncherUtil.getAuthorityFromPermission(context, LauncherUtil.getCurrentLauncherPackageName(context) + ".permission.READ_SETTINGS");
        }
        uriStr.append("content://");
        if (TextUtils.isEmpty(authority)) {
            int sdkInt = android.os.Build.VERSION.SDK_INT;
            if (sdkInt < 8) { // Android 2.1.x(API 7)以及以下的
                uriStr.append("com.android.launcher.settings");
            } else if (sdkInt < 19) {// Android 4.4以下
                uriStr.append("com.android.launcher2.settings");
            } else {// 4.4以及以上
                uriStr.append("com.android.launcher3.settings");
            }
        } else {
            uriStr.append(authority);
        }
        uriStr.append("/favorites?notify=true");
        return Uri.parse(uriStr.toString());
    }


}
