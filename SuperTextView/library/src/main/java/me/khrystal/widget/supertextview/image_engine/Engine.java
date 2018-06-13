package me.khrystal.widget.supertextview.image_engine;

import android.app.Application;
import android.graphics.drawable.Drawable;

import me.khrystal.widget.supertextview.ImageEngine;

/**
 * usage: 通过{@link ImageEngine#install(Engine)}安装到SuperTextView，开发者可以在
 * {@link Application#onCreate()}中进行配置（需要注意任何时候新安装的引擎总会替换掉原本的引擎）。
 * author: kHRYSTAL
 * create time: 18/6/12
 * update time:
 * email: 723526676@qq.com
 */

public interface Engine {
    /**
     * 开发者需要实现这个方法，在其中处理下载图片的逻辑。
     * 最后，需要通过{@link ImageEngine.Callback#onCompleted(Drawable)}返回一个Drawable给SuperTextView
     *
     * @param url 网络图片地址
     * @param callback 回调对象，需要通过{@link ImageEngine.Callback#onCompleted(Drawable)}返回一个Drawable给SuperTextView
     */
    void load(String url, ImageEngine.Callback callback);
}
