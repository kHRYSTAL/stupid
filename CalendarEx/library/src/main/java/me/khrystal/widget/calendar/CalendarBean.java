package me.khrystal.widget.calendar;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/2/16
 * update time:
 * email: 723526676@qq.com
 */

public class CalendarBean {

    public int year;
    public int month;
    public int day;
    public int week;

    // -1, 0, 1
    public int monthFlag;

    public String chinaMonth;
    public String chinaDay;

    public CalendarBean(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public String getDisplayWeek() {
        String s = "";
        switch (week) {
            case 1:
                s = "星期日";
                break;
            case 2:
                s = "星期一";
                break;
            case 3:
                s = "星期二";
                break;
            case 4:
                s = "星期三";
                break;
            case 5:
                s = "星期四";
                break;
            case 6:
                s = "星期五";
                break;
            case 7:
                s = "星期六";
                break;
        }
        return s;
    }

    @Override
    public String toString() {
        String s = year + "/" + month + "/" + day;
        return s;
    }
}
