package me.khrystal.customtransition.part3;

import android.view.View;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/1/11
 * update time:
 * email: 723526676@qq.com
 */

public class ViewState {

    private final int top;
    private final int visibility;

    public static ViewState ofView(View view) {
        int top = view.getTop();
        int visibility = view.getVisibility();
        return new ViewState(top, visibility);
    }

    private ViewState(int top, int visibility) {
        this.top = top;
        this.visibility = visibility;
    }

    public boolean hasMoveVertically(View view) {
        return view.getTop() != top;
    }

    /**
     * 上一个layout内的view是隐藏的
     * 且即将要显示的view需要显示
     * @param view
     * @return
     */
    public boolean hasAppeard(View view) {
        int newVisibility = view.getVisibility();
        return visibility != newVisibility && newVisibility == View.VISIBLE;
    }

    public boolean hasDisappeared(View view) {
        int newVisibility = view.getVisibility();
        return visibility != newVisibility && newVisibility != View.VISIBLE;
    }

    public int getY() {
        return top;
    }
}
