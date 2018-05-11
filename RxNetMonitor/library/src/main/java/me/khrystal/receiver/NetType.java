package me.khrystal.receiver;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/5/11
 * update time:
 * email: 723526676@qq.com
 */

public class NetType {

    @NetTypeChecker
    public static final int WIFI = 1;
    @NetTypeChecker
    public static final int CMNET = 2;
    @NetTypeChecker
    public static final int CMWAP = 3;
    @NetTypeChecker
    public static final int NONE = 4;


    @IntDef({WIFI, CMNET, CMWAP, NONE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface NetTypeChecker {
    }
}
