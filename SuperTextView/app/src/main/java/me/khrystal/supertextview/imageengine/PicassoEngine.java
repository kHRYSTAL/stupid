package me.khrystal.supertextview.imageengine;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import me.khrystal.widget.supertextview.ImageEngine;
import me.khrystal.widget.supertextview.image_engine.Engine;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/6/14
 * update time:
 * email: 723526676@qq.com
 */

public class PicassoEngine implements Engine {

    private Context context;

    public PicassoEngine(Context context) {
        this.context = context;
    }

    @Override
    public void load(String url, final ImageEngine.Callback callback) {
        Picasso.with(context).load(url).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                callback.onCompleted(new BitmapDrawable(Resources.getSystem(), bitmap));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }
}
