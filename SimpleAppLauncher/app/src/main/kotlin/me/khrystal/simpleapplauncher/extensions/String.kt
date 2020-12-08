package me.khrystal.simpleapplauncher.extensions

import me.khrystal.simpleapplauncher.helpers.predefinedPackageNames

/**
 *
 * usage:
 * author: kHRYSTAL
 * create time: 2020/12/8
 * update time:
 * email: 723526676@qq.com
 */
fun String.isAPredefinedApp() = predefinedPackageNames.contains(this)