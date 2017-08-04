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

@IntDef({Rule.LIKE, Rule.EQUAL})
@Retention(RetentionPolicy.SOURCE)
public @interface Rule {
    int LIKE = 0;
    int EQUAL = 1;
}
