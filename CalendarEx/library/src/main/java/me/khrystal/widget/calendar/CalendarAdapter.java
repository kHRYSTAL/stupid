package me.khrystal.widget.calendar;

import android.view.View;
import android.view.ViewGroup;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/2/16
 * update time:
 * email: 723526676@qq.com
 */

public interface CalendarAdapter {
    View getView(View convertView, ViewGroup parentView, CalendarBean bean);
}
