package me.khrystal.simpleapplauncher

import androidx.multidex.MultiDexApplication
import com.facebook.stetho.Stetho
import com.simplemobiletools.commons.extensions.checkUseEnglish

/**
 *
 * usage:
 * author: kHRYSTAL
 * create time: 2020/12/8
 * update time:
 * email: 723526676@qq.com
 */
class App : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }
        checkUseEnglish()
    }
}