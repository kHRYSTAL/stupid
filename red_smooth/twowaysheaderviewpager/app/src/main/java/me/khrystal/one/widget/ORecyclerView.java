package me.khrystal.one.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ScrollingView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 16/4/1
 * update time:
 * email: 723526676@qq.com
 */
public class ORecyclerView extends RecyclerView{

    public ScrollView parentScrollView;
    public int HeaderId;

    public int contentContainerId;
    private int lastScrollDelta = 0;
    int mTop = 10;

    private boolean flag = false;
    private int currentY;


    public ORecyclerView(Context context) {
        super(context);
        setFocusable(false);
    }

    public ORecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setFocusable(false);
    }

    public ORecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFocusable(false);
    }

    public void resume(){
        overScrollBy(0,-lastScrollDelta,0,getScrollY(),0,getScrollRange(),0,0,true);
    }


    private int getScrollRange(){
        int scrollRange = 0;
        if (getChildCount()>0){
            View child = getChildAt(0);
            scrollRange = Math.max(0,child.getHeight()- getHeight());
        }
        return scrollRange;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        if (parentScrollView == null) {
            return super.onInterceptTouchEvent(e);
        } else {
            View parentchild = parentScrollView.getChildAt(0);
            int height2 = parentchild.getMeasuredHeight();
            height2 = height2 - parentScrollView.getMeasuredHeight();


            int scrollY2 = parentScrollView.getScrollY();


            if (e.getAction() == MotionEvent.ACTION_DOWN) {
                if (scrollY2 >= height2) {
                    currentY = (int) e.getY();
                    setParentScrollable(false);
                }

            } else if (e.getAction() == MotionEvent.ACTION_UP) {
                setParentScrollable(true);
            } else if (e.getAction() == MotionEvent.ACTION_MOVE) {
            }
        }
        return super.onInterceptTouchEvent(e);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (parentScrollView != null) {
            if (e.getAction() == MotionEvent.ACTION_MOVE) {

                int y = (int) e.getY();


                View parentchild = parentScrollView.getChildAt(0);
                int height2 = parentchild.getMeasuredHeight();
                height2 = height2 - parentScrollView.getMeasuredHeight();


                int scrollY2 = parentScrollView.getScrollY();


                if (scrollY2 >= height2) {

                    if (currentY < y) {
                        boolean result = false;
                        LayoutManager manager = getLayoutManager();
                        if (manager instanceof GridLayoutManager) {
                            GridLayoutManager gm = (GridLayoutManager) manager;
                            result = gm.findViewByPosition(gm.findFirstVisibleItemPosition()).getTop() == 0 && gm.findFirstVisibleItemPosition() == 0;
                        } else if (manager instanceof LinearLayoutManager) {
                            LinearLayoutManager lm = (LinearLayoutManager) manager;
                            result = lm.findViewByPosition(lm.findFirstVisibleItemPosition()).getTop() == 0 && lm.findFirstVisibleItemPosition() == 0;
                        }
                        if (result) {
                            setParentScrollable(true);
                            return false;
                        } else {
                            setParentScrollable(false);

                        }
                    }
                    else if (currentY > y) {

                        if (scrollY2 >= height2) {
                            setParentScrollable(true);
                            return false;
                        } else {
                            setParentScrollable(false);

                    }

                }
                    currentY = y;
                }else {
                    setParentScrollable(true);
                }
            }
        }
        return super.onTouchEvent(e);
    }

    public void setParentScrollable(boolean scrollable){
        parentScrollView.requestDisallowInterceptTouchEvent(!scrollable);
    }
}
