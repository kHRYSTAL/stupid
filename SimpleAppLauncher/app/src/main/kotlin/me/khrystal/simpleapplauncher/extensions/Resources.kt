package me.khrystal.simpleapplauncher.extensions

import android.content.res.Resources
import android.graphics.drawable.Drawable
import me.khrystal.simpleapplauncher.R

/**
 *
 * usage:
 * author: kHRYSTAL
 * create time: 2020/12/8
 * update time:
 * email: 723526676@qq.com
 */
fun Resources.getLauncherDrawable(packageName: String): Drawable {
    return getDrawable(when (packageName) {
        "me.khrystal.calculator" -> R.mipmap.ic_calculator
        "me.khrystal.calendar.pro" -> R.mipmap.ic_calendar
        "me.khrystal.camera" -> R.mipmap.ic_camera
        "me.khrystal.clock" -> R.mipmap.ic_clock
        "me.khrystal.contacts.pro" -> R.mipmap.ic_contacts
        "me.khrystal.dialer" -> R.mipmap.ic_dialer
        "me.khrystal.draw.pro" -> R.mipmap.ic_draw
        "me.khrystal.filemanager.pro" -> R.mipmap.ic_file_manager
        "me.khrystal.flashlight" -> R.mipmap.ic_flashlight
        "me.khrystal.gallery.pro" -> R.mipmap.ic_gallery
        "me.khrystal.musicplayer" -> R.mipmap.ic_music_player
        "me.khrystal.notes.pro" -> R.mipmap.ic_notes
        "me.khrystal.smsmessenger" -> R.mipmap.ic_sms_messenger
        "me.khrystal.thankyou" -> R.mipmap.ic_thank_you
        "me.khrystal.voicerecorder" -> R.mipmap.ic_voice_recorder
        else -> throw RuntimeException("Invalid launcher package name $packageName")
    })
}