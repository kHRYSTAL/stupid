package me.khrystal.simpleapplauncher.activities

import android.content.Intent
import com.simplemobiletools.commons.activities.BaseSplashActivity

/**
 *
 * usage:
 * author: kHRYSTAL
 * create time: 2020/12/14
 * update time:
 * email: 723526676@qq.com
 */
class SplashActivity : BaseSplashActivity() {
    override fun initActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

}