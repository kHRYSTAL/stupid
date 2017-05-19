package me.khrystal.widget.kenburnsview;

import android.graphics.RectF;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/5/18
 * update time:
 * email: 723526676@qq.com
 */

public interface TransitionGenerator {

    public Transition generateNextTransition(RectF drawableBounds, RectF viewport);
}
