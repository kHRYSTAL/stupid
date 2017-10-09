package me.khrystal.widget.tools;

import android.app.Activity;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/10/9
 * update time:
 * email: 723526676@qq.com
 */

public class SwipeBackUtil {

    private static final int DIRECTION_UP_OR_RIGHT = -1;
    private static final int DIRECTION_DOWN_OR_LEFT = 1;

    /**
     * 判断能否手势上滑
     */
    public static boolean canViewScrollUp(View view, float x, float y, boolean defaultValueForNull) {
        // 如果view为空 或手势不再监听的view上 返回设置的默认值
        if (view == null || !contains(view, x, y)) {
            return defaultValueForNull;
        }
        return ViewCompat.canScrollVertically(view, DIRECTION_UP_OR_RIGHT);
    }

    /**
     * 判断能否手势下滑
     */
    public static boolean canViewScrollDown(View mView, float x, float y, boolean defaultValueForNull) {
        if (mView == null || !contains(mView, x, y)) {
            return defaultValueForNull;
        }
        return ViewCompat.canScrollVertically(mView, DIRECTION_DOWN_OR_LEFT);
    }

    /**
     * 判断能否手势右滑
     */
    public static boolean canViewScrollRight(View mView, float x, float y, boolean defaultValueForNull) {
        if (mView == null || !contains(mView, x, y)) {
            return defaultValueForNull;
        }
        return ViewCompat.canScrollHorizontally(mView, DIRECTION_UP_OR_RIGHT);
    }

    /**
     * 判断能否手势左滑
     */
    public static boolean canViewScrollLeft(View mView, float x, float y, boolean defaultValueForNull) {
        if (mView == null || !contains(mView, x, y)) {
            return defaultValueForNull;
        }
        return ViewCompat.canScrollHorizontally(mView, DIRECTION_DOWN_OR_LEFT);
    }

    /**
     * 判断触摸点坐标是否在监听的view上
     */
    public static boolean contains(View view, float x, float y) {
        Rect localRect = new Rect();
        view.getGlobalVisibleRect(localRect);
        return localRect.contains((int) x,  (int) y);
    }

    /**
     * 遍历找到可以滑动的View
     */
    public static View findAllScrollViews(ViewGroup mViewGroup) {
        for (int i = 0; i < mViewGroup.getChildCount(); i++) {
            View mView = mViewGroup.getChildAt(i);
            if (mView.getVisibility() != View.VISIBLE) {
                continue;
            }
            if (isScrollableView(mView)) {
                return mView;
            }
            if (mView instanceof ViewGroup) {
                mView = findAllScrollViews((ViewGroup) mView);
                if (mView != null) {
                    return mView;
                }
            }
        }
        return null;
    }

    /**
     * 判断是否为可滑动的view
     * @param mView
     * @return
     */
    public static boolean isScrollableView(View mView) {
        return mView instanceof ScrollView
                || mView instanceof HorizontalScrollView
                || mView instanceof NestedScrollView
                || mView instanceof AbsListView
                || mView instanceof RecyclerView
                || mView instanceof ViewPager
                || mView instanceof WebView;
    }



    //region 仿微信侧滑 在手势滑动当前页面时 上一个页面在下层时的动画效果
    public static void onPanelSlide(float fraction) {
        // TODO: 17/10/9
//        Activity activity = WxSwipeBackActivityManager.getInstance().getPenultimateActivity();
//        if (activity != null && !activity.isFinishing()) {
//            View decorView = activity.getWindow().getDecorView();
//            ViewCompat.setTranslationX(decorView, -(decorView.getMeasuredWidth() / 3.0f) * (1 - fraction));
//        }
    }

    public static void onPanelReset() {
        // TODO: 17/10/9
//        Activity activity = WxSwipeBackActivityManager.getInstance().getPenultimateActivity();
//        if (activity != null) {
//            View decorView = activity.getWindow().getDecorView();
//            ViewCompat.setTranslationX(decorView, 0);
//        }
    }
    //endregion

}
