package me.khrystal.supertextview.imageengine;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import me.khrystal.widget.supertextview.ImageEngine;
import me.khrystal.widget.supertextview.image_engine.Engine;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/6/14
 * update time:
 * email: 723526676@qq.com
 */

public class GlideEngine implements Engine {

    private Context context;

    public GlideEngine(Context context) {
        this.context = context;
    }

    @Override
    public void load(String url, final ImageEngine.Callback callback) {
        Glide.with(context).load(url).into(new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                callback.onCompleted(resource);
            }
        });
    }
}
