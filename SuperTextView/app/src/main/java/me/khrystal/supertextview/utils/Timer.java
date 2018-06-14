package me.khrystal.supertextview.utils;

import android.os.Build;
import android.util.Log;

import me.khrystal.supertextview.BuildConfig;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/6/14
 * update time:
 * email: 723526676@qq.com
 */

public enum Timer {
    ONE;
    private long startTime;
    public static final int LOG_E = 0x001;

    public void begin() {
        begin(System.currentTimeMillis());
    }

    public void begin(long startTime) {
        this.startTime = startTime;
    }

    public long deltaT(long endTime) {
        if (startTime != 0) {
            long temp = startTime;
            startTime = 0;
            return  endTime - temp;
        } else {
            return -1;
        }
    }

    public String printDeltaT(String tag) {
        return printDeltaT(tag, System.currentTimeMillis());
    }

    public String printDeltaT(String tag, long endTime) {
        StringBuilder sb = new StringBuilder();
        sb.append(tag);
        if (!tag.contains(":")) {
            sb.append(": ");
        }
        long deltaT = deltaT(endTime);
        if (deltaT != -1) {
            sb.append("+ ").append(deltaT).append(" ms");
        } else {
            sb.append("统计参数错误, 请仔细核对!");
        }
        return sb.toString();
    }

    public void logDeltaT(String tag, int type) {
        if (BuildConfig.DEBUG) {
            if (type == LOG_E) {
                Log.e("Timer: ", printDeltaT(tag));
            }
        }
    }
}
