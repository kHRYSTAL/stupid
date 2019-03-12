package me.khrystal.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/3/12
 * update time:
 * email: 723526676@qq.com
 */
public class GlideImageLoader extends ImageLoader {

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        Glide.with(context.getApplicationContext())
                .load(path)
                .crossFade()
                .into(imageView);
    }
}
