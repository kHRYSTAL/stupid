package me.khrystal.widget.sliding;

import android.support.annotation.NonNull;
import android.view.View;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/7/20
 * update time:
 * email: 723526676@qq.com
 */

public interface ISlidingUpPanel<T extends View> {
    @NonNull
    T getPanelView();

    int getPanelExpandHeight();

    @SlidingUpPanelLayout.SlideState
    int getSlideState();

    void setSlideState(@SlidingUpPanelLayout.SlideState int slideState);

    int getPanelTopBySlidingState(@SlidingUpPanelLayout.SlideState int slideState);

    void onSliding(@NonNull ISlidingUpPanel panel, int top, int dy, float slideProgress);

}
