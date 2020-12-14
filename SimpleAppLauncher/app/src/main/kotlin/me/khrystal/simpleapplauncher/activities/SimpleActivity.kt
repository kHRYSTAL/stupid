package me.khrystal.simpleapplauncher.activities

import com.simplemobiletools.commons.activities.BaseSimpleActivity
import me.khrystal.simpleapplauncher.R
import java.util.ArrayList

/**
 *
 * usage:
 * author: kHRYSTAL
 * create time: 2020/12/14
 * update time:
 * email: 723526676@qq.com
 */
open class SimpleActivity :BaseSimpleActivity() {
    override fun getAppIconIDs() = arrayListOf(
            R.mipmap.ic_launcher_red,
            R.mipmap.ic_launcher_pink,
            R.mipmap.ic_launcher_purple,
            R.mipmap.ic_launcher_deep_purple,
            R.mipmap.ic_launcher_indigo,
            R.mipmap.ic_launcher_blue,
            R.mipmap.ic_launcher_light_blue,
            R.mipmap.ic_launcher_cyan,
            R.mipmap.ic_launcher_teal,
            R.mipmap.ic_launcher_green,
            R.mipmap.ic_launcher_light_green,
            R.mipmap.ic_launcher_lime,
            R.mipmap.ic_launcher_yellow,
            R.mipmap.ic_launcher_amber,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher_grey_black,
            R.mipmap.ic_launcher_deep_orange,
            R.mipmap.ic_launcher_brown,
            R.mipmap.ic_launcher_blue_grey
    )

    override fun getAppLauncherName() = getString(R.string.app_launcher_name)
}