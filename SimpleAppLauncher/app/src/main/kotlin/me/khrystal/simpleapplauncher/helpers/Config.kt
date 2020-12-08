package me.khrystal.simpleapplauncher.helpers

import android.content.Context
import com.simplemobiletools.commons.helpers.BaseConfig
import me.khrystal.simpleapplauncher.R

/**
 *
 * usage:
 * author: kHRYSTAL
 * create time: 2020/12/8
 * update time:
 * email: 723526676@qq.com
 */
class Config(context: Context) : BaseConfig(context) {
    companion object {
        fun newInstance(context: Context) = Config(context)
    }

    var wasRemoveInfoShown: Boolean
        get() = prefs.getBoolean(WAS_REMOVE_INFO_SHOWN, false)
        set(wasRemoveInfoShown) = prefs.edit().putBoolean(WAS_REMOVE_INFO_SHOWN, wasRemoveInfoShown).apply()
    var closeApp: Boolean
        get() = prefs.getBoolean(CLOSE_APP, true)
        set(value) = prefs.edit().putBoolean(CLOSE_APP, value).apply()

    var columnCnt: Int
        get() = prefs.getInt(COLUMN_CNT, context.resources.getInteger(R.integer.column_count))
        set(value) = prefs.edit().putInt(COLUMN_CNT, value).apply()

}