package me.khrystal.widget.scrollimageview;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/5/27
 * update time:
 * email: 723526676@qq.com
 */

public interface ScrollingImageViewBitmapLoader {
    Bitmap loadBitmap(Context context, int resourceId);
}
