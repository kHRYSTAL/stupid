package me.khrystal.eyepetizer.utils

import android.content.Context

/**
 *
 * usage:
 * author: kHRYSTAL
 * create time: 17/12/18
 * update time:
 * email: 723526676@qq.com
 */
object UiUtils {
    fun dip2px(context: Context, dipValue: Float): Float {
        val scale: Float = context.resources.displayMetrics.density
        return dipValue * scale + 0.5f
    }

    fun px2dip(context: Context, pxValue: Float): Int {
        val scale: Float = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }
}