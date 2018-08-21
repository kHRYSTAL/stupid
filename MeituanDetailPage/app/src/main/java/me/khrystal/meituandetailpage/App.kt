package me.khrystal.meituandetailpage

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex

/**
 *
 * usage:
 * author: kHRYSTAL
 * create time: 18/8/13
 * update time:
 * email: 723526676@qq.com
 */
class App : Application() {
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }

    companion object {
        private var INSTANCE: App? = null

        @Synchronized
        fun get(): App = INSTANCE!!
    }
}