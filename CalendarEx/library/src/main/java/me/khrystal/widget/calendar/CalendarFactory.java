package me.khrystal.widget.calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/2/16
 * update time:
 * email: 723526676@qq.com
 */

public class CalendarFactory {

    private static HashMap<String, List<CalendarBean>> cache = new HashMap<>();

    /**
     * 获取一个月中日期的集合1
     */
    public static List<CalendarBean> getMonthOfDayList(int y, int m) {
        String key = y + "" + m;
        if (cache.containsKey(key)) {
            List<CalendarBean> list = cache.get(key);
            if (list == null)
                cache.remove(key);
            else
                return list;
        }

        List<CalendarBean> list = new ArrayList<>();
        cache.put(key, list);
        // 计算出一月的第一天是星期几
        int fweek = CalendarUtil.getDayOfWeek(y, m, 1);
        int total = CalendarUtil.getDayNumOfMonth(y, m);

        // 根据星期计算出前面还有几个日期显示, (上个月)
        for (int i = fweek - 1; i > 0 ; i--) {
            CalendarBean bean = getCalendarBean(y, m, 1 - i);
            bean.monthFlag = -1;
            list.add(bean);
        }

        // 获取当月的天数
        for (int i = 0; i < total; i++) {
            CalendarBean bean = getCalendarBean(y, m, i + 1);
            list.add(bean);
        }

        // 为了塞满42个格子 显示多出当月的天数 (下个月)
        for (int i = 0; i < 42 - (fweek - 1) - total; i++) {
            CalendarBean bean = getCalendarBean(y, m, total + i + 1);
            bean.monthFlag = 1;
            list.add(bean);
        }
        return list;
    }

    public static CalendarBean getCalendarBean(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DATE);

        CalendarBean bean = new CalendarBean(year, month, day);
        bean.week = CalendarUtil.getDayOfWeek(year, month, day);
        String[] chinaDate = ChinaDate.getChinaDate(year, month, day);
        bean.chinaMonth = chinaDate[0];
        bean.chinaDay = chinaDate[1];

        return bean;
    }



}
