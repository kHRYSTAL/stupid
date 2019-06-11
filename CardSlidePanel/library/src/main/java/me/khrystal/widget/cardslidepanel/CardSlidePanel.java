package me.khrystal.widget.cardslidepanel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/6/11
 * update time:
 * email: 723526676@qq.com
 */
public class CardSlidePanel extends ViewGroup {

    private List<CardItemView> viewList = new ArrayList<>(); // 存放每一层的View 从顶到底
    private List<View> releasedViewList = new ArrayList<>(); // 手指松开后存放的view列表

    private ViewDragHelper mDragHelper; // 修改了interpolator
    private int initCenterViewX = 0, initCenterViewY = 0; // 最初时 中间View的x位置, y位置
    private int allWidth = 0; // 面板的宽度
    private int allHeight = 0; // 面板的高度
    private int childWidth = 0; // 每一个子view对应的宽度
    private int childHeight = 0; // 每一个子view对应的高度

    private static final float SCALE_STEP = 0.08f; // view 叠加缩放的步长
    private static final int MAX_SLIDE_DISTANCE_LINKAGE = 400; // 水平距离+ 垂直距离

    // 超过这个值
    // 则下一层view 完成向上一层view的过度
    private View bottomLayout; // 卡片下边的三个按钮布局

    private int bottomMarginTop = 40;
    private int yOffsetStep = -40; // view叠加垂直偏移量的步长
    private boolean isCyclic = true;





    public CardSlidePanel(Context context) {
        super(context);
    }

    public CardSlidePanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CardSlidePanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}
