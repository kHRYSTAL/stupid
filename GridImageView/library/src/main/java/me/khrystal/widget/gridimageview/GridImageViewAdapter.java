package me.khrystal.widget.gridimageview;

import android.content.Context;
import android.widget.ImageView;

import java.util.List;

/**
 * usage: 九宫格控件适配器
 * author: kHRYSTAL
 * create time: 17/3/15
 * update time:
 * email: 723526676@qq.com
 */

public abstract class GridImageViewAdapter<T> {

    /**
     * 显示图片时回调
     */
    protected abstract void onDisplayImage(Context context, ImageView imageView, T t);

    /**
     * 点击回调
     */
    protected void onItemImageClick(Context context, ImageView imageView, int index, List<T> list) {

    }

    /**
     * getView
     */
    protected GridChildImageView generateImageView(Context context) {
        GridChildImageView imageView = new GridChildImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageView;
    }
}
