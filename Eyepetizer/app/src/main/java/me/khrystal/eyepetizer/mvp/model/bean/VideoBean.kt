package me.khrystal.eyepetizer.mvp.model.bean

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

/**
 *
 * usage:
 * author: kHRYSTAL
 * create time: 17/12/19
 * update time:
 * email: 723526676@qq.com
 */
data class VideoBean(var feed: String?, var title: String?, var description: String?,
                     var duration: Long?, var playUrl: String?, var category: String?,
                     var blurred: String?, var collect: Int?, var share: Int?, var reply: Int?, var time: Long) : Parcelable, Serializable {

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<VideoBean> = object : Parcelable.Creator<VideoBean> {
            override fun createFromParcel(source: Parcel): VideoBean = VideoBean(source)
            override fun newArray(size: Int): Array<VideoBean?> = arrayOfNulls(size)
        }
    }

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Long::class.java.classLoader) as? Long,
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readLong())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(feed)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeValue(duration)
        parcel.writeString(playUrl)
        parcel.writeString(category)
        parcel.writeString(blurred)
        parcel.writeValue(collect)
        parcel.writeValue(share)
        parcel.writeValue(reply)
        parcel.writeLong(time)
    }

    override fun describeContents(): Int {
        return 0
    }


}