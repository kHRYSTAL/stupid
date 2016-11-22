package me.khrystal.widget.swipe;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 16/11/14
 * update time:
 * email: 723526676@qq.com
 */

public interface Openable {

    /**
     * The menu is open?
     *
     * @return true, otherwise false.
     */
    boolean isMenuOpen();

    /**
     * The menu is open?
     *
     * @return true, otherwise false.
     */
    boolean isMenuOpenNotEqual();

    /**
     * The menu is open on the right?
     *
     * @return true, otherwise false.
     */
    boolean isRightMenuOpen();

    /**
     * The menu is open on the right?
     *
     * @return true, otherwise false.
     */
    boolean isRightMenuOpenNotEqual();

    /**
     * Open the current menu.
     */
    void smoothOpenMenu();

    /**
     * Open the menu on right.
     */
    void smoothOpenRightMenu();

    /**
     * Open the menu on right for the duration.
     *
     * @param duration duration time.
     */
    void smoothOpenRightMenu(int duration);
}
