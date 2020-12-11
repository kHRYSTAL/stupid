package me.khrystal.simpleapplauncher.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.LauncherApps
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import com.simplemobiletools.commons.helpers.SORT_BY_CUSTOM
import me.khrystal.simpleapplauncher.helpers.Config
import me.khrystal.simpleapplauncher.helpers.DBHelper
import me.khrystal.simpleapplauncher.models.AppLauncher

/**
 *
 * usage:
 * author: kHRYSTAL
 * create time: 2020/12/9
 * update time:
 * email: 723526676@qq.com
 */
val Context.config: Config get() = Config.newInstance(applicationContext)
val Context.dbHelper: DBHelper get() = DBHelper.newInstance(applicationContext)

/**
 * 获取没在launcher中展示的app
 */
@SuppressLint("WrongConstant")
fun Context.getNotDisplayedLaunchers(displayedLaunchers: ArrayList<AppLauncher>): ArrayList<AppLauncher> {
    val allApps = ArrayList<AppLauncher>()
    val intent = Intent(Intent.ACTION_MAIN, null)
    intent.addCategory(Intent.CATEGORY_LAUNCHER)
    val list = packageManager.queryIntentActivities(intent, PackageManager.PERMISSION_GRANTED)
    for (info in list) {
        val componentInfo = info.activityInfo.applicationInfo
        val label = componentInfo.loadLabel(packageManager).toString()
        val packageName = componentInfo.packageName

        var drawable: Drawable? = null
        try {
            val launcher = getSystemService(Context.LAUNCHER_APPS_SERVICE) as LauncherApps
            val activityList = launcher.getActivityList(packageName, android.os.Process.myUserHandle())[0]
            drawable = activityList.getBadgedIcon(0)
        } catch (e: Exception) {
        }

        if (drawable == null) {
            drawable = if (packageName.isAPredefinedApp()) {
                resources.getLauncherDrawable(packageName)
            } else {
                try {
                    packageManager.getApplicationIcon(packageName)
                } catch (ignored: Exception) {
                    // 找不到icon 跳出本次循环
                    continue
                }
            }
        }
        allApps.add(AppLauncher(0, label, packageName, 0, drawable))
    }

    if (config.sorting and SORT_BY_CUSTOM != 0) { // 默认按首字母排序
        allApps.sortBy { it.title.toLowerCase() }
    } else { // 自定义排序
        AppLauncher.sorting = config.sorting
        allApps.sort()
    }
    val unique = allApps.distinctBy { it.packageName }
    return unique.filter { !displayedLaunchers.contains(it) && it.packageName != "me.khrystal.simpleapplauncher" } as ArrayList<AppLauncher>
}