package me.khrystal.widget.swipe;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 16/11/14
 * update time:
 * email: 723526676@qq.com
 */

public interface Closeable {

    /**
     * Smooth closed the menu on the right.
     */
    void smoothCloseRightMenu();

    /**
     * Smooth closed the menu.
     */
    void smoothCloseMenu();

    /**
     * Smooth closed the menu for the duration.
     *
     * @param duration duration time.
     */
    void smoothCloseMenu(int duration);

}
