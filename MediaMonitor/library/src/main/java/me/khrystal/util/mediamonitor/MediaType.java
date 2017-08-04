package me.khrystal.util.mediamonitor;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/8/4
 * update time:
 * email: 723526676@qq.com
 */
@IntDef({MediaType.ALL, MediaType.MEDIA_TYPE_IMAGE, MediaType.MEDIA_TYPE_AUDIO, MediaType.MEDIA_TYPE_VIDEO})
@Retention(RetentionPolicy.SOURCE)
public @interface MediaType {
    int ALL = 0;
    int MEDIA_TYPE_IMAGE = 1;
    int MEDIA_TYPE_AUDIO = 2;
    int MEDIA_TYPE_VIDEO = 3;
}
