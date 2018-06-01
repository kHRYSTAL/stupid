package me.khrystal.widget;

import android.animation.LayoutTransition;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import me.khrystal.widget.listener.OnBarItemClickListener;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/6/1
 * update time:
 * email: 723526676@qq.com
 */

public class TabBar extends LinearLayout implements View.OnClickListener {

    private final Context context;
    private boolean initialSetup;
    private OnBarItemClickListener onBarItemClickListener;

    public TabBar(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public TabBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public TabBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TabBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        init();
    }

    private void init() {
        LayoutTransition lt = new LayoutTransition();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            // 增加view的移除动画 动画为默认
            lt.disableTransitionType(LayoutTransition.DISAPPEARING);
        }
        setLayoutTransition(lt);
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!initialSetup) {
            // click listener
            for (int i = 0; i < getChildCount(); i++) {
                getChildAt(i).setTag(i);
                getChildAt(i).setOnClickListener(this);
            }
            // add spaces between all the items
            addEmptySpaceBetweenEveryItem();
            initialSetup = true;
        }
    }

    private void addEmptySpaceBetweenEveryItem() {
        // Get Child count
        int childs = getChildCount();

        if (childs == 0) return;

        for (int i = 0; i <= childs * 2; i = i + 2) {
            addView(new EmptySpace(context), i);
        }
        invalidate();
    }

    @Override
    public void onClick(View view) {
        // get clicked position from tag
        int position = Integer.parseInt(view.getTag().toString());
        if (onBarItemClickListener != null) {
            onBarItemClickListener.onBarItemClick((BarItem) view, position);
        }
        // Unselected previous item
        View selectedView = getSelected();
        if (selectedView != null) {
            selectedView.setSelected(false);
        }

        // set new item
        view.setSelected(true);
    }

    /**
     * on BarItem Click listener
     *
     * @param onBarItemClickListener
     */
    public void setOnBarItemClickListener(OnBarItemClickListener onBarItemClickListener) {
        this.onBarItemClickListener = onBarItemClickListener;
    }

    /**
     * get item from the bar by its position
     *
     * @param position
     * @return
     */
    public BarItem getItemAt(int position) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child instanceof BarItem) {
                if (Integer.parseInt(String.valueOf(child.getTag())) == position) {
                    return (BarItem) child;
                }
            }
        }
        return null;
    }

    /**
     * get selected item from the bar
     *
     * @return
     */
    public BarItem getSelected() {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child instanceof BarItem) {
                if (child.isSelected()) {
                    return (BarItem) child;
                }
            }
        }
        return null;
    }

    /**
     * set the item to be selected by its position
     *
     * @param position
     */
    public void setSelected(int position) {
        BarItem shouldSelected = getItemAt(position);
        if (shouldSelected == null)
            return;
        shouldSelected.performClick();
    }


}
