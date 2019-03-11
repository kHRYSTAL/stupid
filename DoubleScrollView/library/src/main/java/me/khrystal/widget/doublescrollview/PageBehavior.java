package me.khrystal.widget.doublescrollview;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Scroller;

import java.lang.ref.WeakReference;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/3/11
 * update time:
 * email: 723526676@qq.com
 */
public class PageBehavior extends CoordinatorLayout.Behavior {
    private static final int PAGE_ONE = 1;
    private static final int PAGE_TWO = 2;

    private WeakReference<View> dependentView, childView;

    private Scroller scroller;
    private Handler handler;

    private int status = PAGE_ONE; // 记录状态
    private float gap;
    private int totalOne = 0;
    private int scrollY = 0;
    private int mode = 0;

    public OnPageChanged mOnPageChanged;

    public void setOnPageChanged(OnPageChanged onPageChanged) {
        mOnPageChanged = onPageChanged;
    }

    public PageBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        scroller = new Scroller(context);
        handler = new Handler();
        gap = context.getResources().getDimensionPixelSize(R.dimen.gap);
    }

    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
        // 设定依赖id是pageOne的View
        if (dependency.getId() == R.id.pageOne) {
            dependentView = new WeakReference<>(dependency);
            childView = new WeakReference<>(child);
            return true;
        }
        return false;
    }

    @Override
    public boolean onLayoutChild(@NonNull CoordinatorLayout parent, @NonNull View child, int layoutDirection) {
        // child 的布局
        child.layout(0, 0, parent.getWidth(), child.getMeasuredHeight());
        return true;
    }

    @Override
    public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
        // 让child 随着依赖视图的熟悉变动而变动
        child.setTranslationY(dependency.getMeasuredHeight() + dependency.getTranslationY());
        return true;
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View directTargetChild, @NonNull View target, int nestedScrollAxes, int type) {
        // 只检测垂直方向滑动
        scroller.abortAnimation(); // 停止动画
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);

        if (status == PAGE_ONE) {
            if (dyUnconsumed > 0) {
                View dependentView = getDependentView();
                totalOne = (totalOne - dyUnconsumed);
                dependentView.setTranslationY(totalOne * 0.5f);
            }

            if (dependentView.get().getTranslationY() < 0 && dyConsumed < 0) {
                mode = 1;
                ((Page) dependentView.get()).setScrollAble(false, dependentView.get().getTranslationY());
            }
        } else if (status == PAGE_TWO) {
            if (dyUnconsumed < 0) {
                View dependentView = getDependentView();
                totalOne = totalOne + dyUnconsumed;
                int newTranslate = (int) (-dependentView.getMeasuredHeight() - (totalOne * 0.5));
                dependentView.setTranslationY(newTranslate);
            }

            if (dependentView.get().getTranslationY() > -dependentView.get().getHeight() && dyConsumed > 0) {
                mode = 1;
                ((Page) child).setScrollAble(false, dependentView.get().getTranslationY());
            }
        }
    }

    @Override
    public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int type) {
        super.onStopNestedScroll(coordinatorLayout, child, target, type);
        totalOne = 0;
        View dependentView = getDependentView();
        float translationY = dependentView.getTranslationY();
        if (status == PAGE_ONE) {
            if (mode == 0) {
                if (translationY < -gap) {
                    scroller.startScroll(0, (int) translationY, 0, (int) (-dependentView.getMeasuredHeight() - translationY));
                    status = PAGE_TWO;
                    if (mOnPageChanged != null) {
                        mOnPageChanged.toBottom();
                    }
                } else {
                    scroller.startScroll(0, (int) translationY, 0, (int) (-translationY));
                }
            } else {
                mode = 0;
                if (scrollY < -gap) {
                    scroller.startScroll(0, scrollY, 0, -dependentView.getMeasuredHeight() - scrollY);
                    status = PAGE_TWO;
                    if (mOnPageChanged != null) {
                        mOnPageChanged.toBottom();
                    }
                } else {
                    scroller.startScroll(0, scrollY, 0, -scrollY);
                }
            }
        } else if (status == PAGE_TWO) {
            if (mode == 0) {
                if (translationY < -dependentView.getMeasuredHeight() + gap) {
                    scroller.startScroll(0, (int) translationY, 0, (int) (-dependentView.getMeasuredHeight() - translationY));
                } else {
                    scroller.startScroll(0, (int) translationY, 0, (int) (-translationY));
                    status = PAGE_ONE;
                    if (mOnPageChanged != null) {
                        mOnPageChanged.toTop();
                    }
                }
            } else {
                mode = 0;
                if (scrollY < -dependentView.getMeasuredHeight() + gap) {
                    scroller.startScroll(0, scrollY, 0, -dependentView.getMeasuredHeight() - scrollY);
                } else {
                    scroller.startScroll(0, scrollY, 0, -scrollY);
                    status = PAGE_ONE;
                    if (mOnPageChanged != null)
                        mOnPageChanged.toTop();
                }
            }
        }
        handler.post(new Running());
    }

    private View getDependentView() {
        return dependentView.get();
    }

    public void setScrollY(int scrollY) {
        this.scrollY = scrollY;
    }

    public void backToTop() {
        View dependentView = getDependentView();
        float translationY = dependentView.getTranslationY();
        if (status == PAGE_TWO) {
            if (mode == 0) {
                childView.get().setScaleY(0);
                dependentView.setScrollY(0);
                scroller.startScroll(0, (int) translationY, 0, (int) (-translationY));
                status = PAGE_ONE;
                if (mOnPageChanged != null)
                    mOnPageChanged.toTop();
            }
        }
        handler.post(new Running());
    }

    public void scrollToBottom() {
        View dependentView = getDependentView();
        float translationY = dependentView.getTranslationY();
        dependentView.setScrollY(dependentView.getMeasuredHeight());
        if (status == PAGE_ONE) {
            if (mode == 0) {
                scroller.startScroll(0, (int) translationY, 0, (int) (-dependentView.getMeasuredHeight() - translationY));
                status = PAGE_TWO;
                if (mOnPageChanged != null)
                    mOnPageChanged.toBottom();
            }
        }
        handler.post(new Running());
    }

    private class Running implements Runnable {

        @Override
        public void run() {
            if (scroller.computeScrollOffset()) {
                View dependentView = getDependentView();
                dependentView.setTranslationY(scroller.getCurrY());
                handler.post(this);
            }
        }
    }

    public interface OnPageChanged {

        void toTop();

        void toBottom();
    }
}
