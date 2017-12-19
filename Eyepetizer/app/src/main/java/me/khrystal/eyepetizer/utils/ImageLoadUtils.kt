package me.khrystal.eyepetizer.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy

/**
 *
 * usage:
 * author: kHRYSTAL
 * create time: 17/12/18
 * update time:
 * email: 723526676@qq.com
 */
class ImageLoadUtils {
    // 伴生对象  companion object, kotlin中不存在静态变量
    // 因此使用伴生对象 在类的初始化时声明静态变量或函数
    // 外部不需要实例化 这个类即可直接调用
    // object 表明是单例的 实际上companion 编译后是一个内部类
    companion object {
        fun display(context: Context, imageView: ImageView?, url: String) {
            if (imageView == null) {
                throw IllegalArgumentException("argument error")
            }
            Glide.with(context).load(url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
//                    .placeholder(R.drawable.ic_empty_picture)
                    .crossFade().into(imageView)
        }

        fun displayHigh(context: Context, imageView: ImageView?, url: String) {
            if (imageView == null) {
                throw IllegalArgumentException("argument error")
            }
            Glide.with(context).load(url)
                    .asBitmap()
                    .format(DecodeFormat.PREFER_ARGB_8888)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
//                    .placeholder(R.drawable.ic_image_loading)
//                    .error(R.drawable.ic_empty_picture)
                    .into(imageView)
        }
    }
}