package me.khrystal.widget.gridimageview;

import android.content.Context;
import android.widget.ImageView;

import java.util.List;

/**
 * usage: 图片点击监听器
 * author: kHRYSTAL
 * create time: 17/3/15
 * update time:
 * email: 723526676@qq.com
 */

public interface ItemImageClickListener<T> {
    void onItemImageClick(Context context, ImageView imageView, int index, List<T> list);
}
