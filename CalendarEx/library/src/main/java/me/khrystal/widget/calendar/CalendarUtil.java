package me.khrystal.widget.calendar;

import java.util.Calendar;
import java.util.Date;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/2/16
 * update time:
 * email: 723526676@qq.com
 */

public class CalendarUtil {

    /**
     * 获取日期是星期几
     */
    public static int getDayOfWeek(int y, int m, int d) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(y, m - 1, d);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 获取当前月份的最大天数
     */
    public static int getDayNumOfMonth(int y, int m) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(y, m - 1, 1);
        int dateNumber = calendar.getActualMaximum(Calendar.DATE);
        return dateNumber;
    }

    /**
     * 获取当前月份是几月
     */
    @Deprecated
    public static int getMonthOfMonth(int y, int m) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(y, m - 1, 1);
        int month = calendar.get(Calendar.MONTH);
        return month + 1;
    }

    /**
     * 通过date获取年月日组成的数组
     */
    public static int[] getYMD(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return new int[]{calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE)};
    }


}
