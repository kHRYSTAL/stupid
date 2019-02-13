package me.khrystal.widget.circularlayout;

import android.content.Context;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/2/13
 * update time:
 * email: 723526676@qq.com
 */
public abstract class CircularLayoutAdapter<T> {
    protected Context context;
    protected CircularLayout parent;

    public void setContext(Context context) {
        this.context = context;
    }

    public void setParent(CircularLayout parent) {
        this.parent = parent;
    }

    public abstract void add(T item);

    public abstract CircularLayoutItem get(int index);

    public abstract Integer getRoundedIndex(Integer index);
}
