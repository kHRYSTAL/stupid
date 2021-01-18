package me.khrystal.meituancalendarview.widget;

import java.io.Serializable;
import java.util.Date;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 2021/1/18
 * update time:
 * email: 723526676@qq.com
 */

public class MTDate implements Serializable {
    // item type
    public static final int ITEM_TYPE_DAY = 1;
    public static final int ITEM_TYPE_MON = 2;
    // item state
    public static final int ITEM_STATE_BEGIN_DATE = 1;
    public static final int ITEM_STATE_END_DATE = 2;
    public static final int ITEM_STATE_SELECTED = 3;
    public static final int ITEM_STATE_NORMAL = 4;

    private int itemType = ITEM_TYPE_DAY;
    private int itemState = ITEM_STATE_NORMAL;

    private Date date;
    private String dayStr;
    private String monthStr;

    public int getItemType() {
        return itemType;
    }

    public int getItemState() {
        return itemState;
    }

    public Date getDate() {
        return date;
    }

    public String getDayStr() {
        return dayStr;
    }

    public String getMonthStr() {
        return monthStr;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public void setItemState(int itemState) {
        this.itemState = itemState;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDayStr(String dayStr) {
        this.dayStr = dayStr;
    }

    public void setMonthStr(String monthStr) {
        this.monthStr = monthStr;
    }
}
