package me.khrystal.widget.menu.adapter;

import android.view.View;
import android.widget.FrameLayout;

/**
 * usage: 筛选器(横向) 适配器
 * author: kHRYSTAL
 * create time: 17/4/24
 * update time:
 * email: 723526676@qq.com
 */

public interface MenuAdapter {

    /**
     * 设置筛选条目个数
     */
    int getMenuCount();

    /**
     * 设置每个筛选器默认Title
     */
    String getMenuTitle(int position);

    /**
     * 设置每个筛选条目距离底部距离
     */
    int getBottomMargin(int position);

    /**
     * 设置每个筛选条目的View
     * @param position
     * @param parentContainer
     * @return
     */
    View getView(int position, FrameLayout parentContainer);
}
