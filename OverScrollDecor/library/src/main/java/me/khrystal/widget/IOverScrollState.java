package me.khrystal.widget;

/**
 * usage: 当前状态
 * author: kHRYSTAL
 * create time: 17/5/3
 * update time:
 * email: 723526676@qq.com
 */

public class IOverScrollState {
    int STATE_IDLE = 0;
    int STATE_DRAG_STATE_SIDE = 1;
    int STATE_DRAG_END_SIDE = 2;
    // 反弹
    int STATE_BOUNCE_BACK = 3;
}
