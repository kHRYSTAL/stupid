package me.khrystal.widget.supertextview;

import android.app.Application;
import android.graphics.drawable.Drawable;

import me.khrystal.widget.supertextview.image_engine.DefaultEngine;
import me.khrystal.widget.supertextview.image_engine.Engine;

/**
 * usage: SuperTextView的图片加载引擎
 * author: kHRYSTAL
 * create time: 18/6/12
 * update time:
 * email: 723526676@qq.com
 */

public class ImageEngine {
    private Engine engine;

    private ImageEngine() {

    }

    private static final class Holder {
        private static final ImageEngine instance = new ImageEngine();
    }

    /**
     * 必须先安装一个引擎 后安装的Engine总会替换前面的
     * 默认情况下 SuperTextView有一个十分简易的Engine, 建议开发者根据项目
     * 所使用的图片框架实现一个{@link Engine}
     * 建议开发者在{@link Application#onCreate()}中进行配置
     * @param engine 图片加载引擎
     */
    public static void install(Engine engine) {
        synchronized (Holder.instance) {
            Holder.instance.engine = engine;
        }
    }

    static void load(String url, Callback callback) {
        if (Holder.instance.engine == null) {
            throw new IllegalStateException("you must first install one engine");
        }
        Holder.instance.engine.load(url, callback);
    }

    /**
     * @hide
     */
    static void checkEngine() {
        if (Holder.instance.engine == null) {
            Holder.instance.engine = new DefaultEngine();
        }
    }

    public static interface Callback {
        void onCompleted(Drawable drawable);
    }

}
