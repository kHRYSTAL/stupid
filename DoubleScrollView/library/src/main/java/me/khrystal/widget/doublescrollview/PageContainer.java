package me.khrystal.widget.doublescrollview;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/3/11
 * update time:
 * email: 723526676@qq.com
 */
public class PageContainer extends CoordinatorLayout implements Page.OnScrollListener {

    private Page child;
    private PageBehavior behavior;

    public PageContainer(@NonNull Context context) {
        this(context, null);
    }

    public PageContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PageContainer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnPageChanged(PageBehavior.OnPageChanged onPageChanged) {
        if (behavior != null) {
            behavior.setOnPageChanged(onPageChanged);
        }
    }

    @Override
    public void onScroll(float scrollY, float distance) {
        if (scrollY == -10000) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    scrollTo(0, 0);
                }
            }, -10);
        } else {
            int y = (int) (getScrollY() - scrollY);
            if (y < distance)
                return;
            scrollTo(0, y);
            if (behavior != null) {
                behavior.setScrollY((int) (distance - y));
            }
        }
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        if (child instanceof Page) {
            this.child = ((Page) child);
            this.child.setScrollListener(this);
        }

        LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
        if (layoutParams.getBehavior() != null && layoutParams.getBehavior() instanceof PageBehavior) {
            behavior = (PageBehavior) layoutParams.getBehavior();
        }
    }

    public void backToTop() {
        if (behavior != null) {
            behavior.backToTop();
        }
    }

    public void scrollToBottom() {
        if (behavior != null) {
            behavior.scrollToBottom();
        }
    }
}
