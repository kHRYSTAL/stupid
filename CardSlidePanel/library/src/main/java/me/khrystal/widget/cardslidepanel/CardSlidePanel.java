package me.khrystal.widget.cardslidepanel;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
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

    private static final int[] layerDrawables = new int[]{
            R.drawable.transparent_shade_layer, R.drawable.first_shade_layer,
            R.drawable.second_shade_layer, R.drawable.third_shade_layer,
            R.drawable.third_shade_layer};

    private static final int X_VEL_THRESHOLD = 900;
    private static final int Y_VEL_THRESHOLD = 900;
    private static final int X_DISTANCE_THRESHOLD = 300;
    private static final int Y_DISTANCE_THRESHOLD = 300;

    public static final int VANISH_TYPE_LEFT = 0;
    public static final int VANISH_TYPE_RIGHT = 1;
    public static final int VANISH_TYPE_TOP = 2;

    private Object obj1 = new Object();

    private CardSwitchListener cardSwitchListener;
    private List<CardDataItem> dataList; // 存储的数据列表
    private int isShowingIndex = 0; // 当前正在显示的index

    public CardSlidePanel(Context context) {
        this(context, null);
    }

    public CardSlidePanel(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CardSlidePanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CardSlidePanel);

        bottomMarginTop = (int) ta.getDimension(R.styleable.CardSlidePanel_bottomMarginTop, bottomMarginTop);
        yOffsetStep = (int) ta.getDimension(R.styleable.CardSlidePanel_yOffsetStep, yOffsetStep);
        isCyclic = ta.getBoolean(R.styleable.CardSlidePanel_cyclic, true);
        // 滑动相关类
        mDragHelper = ViewDragHelper.create(this, 0.8f, new DragHelperCallback());
        mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // 渲染完成 初始化卡片view 列表
        viewList.clear();
        int num = getChildCount();
        for (int i = num - 1; i >= 0; i--) {
            View childView = getChildAt(i);
            if (childView.getId() == R.id.card_bottom_layout) {
                bottomLayout = childView;
                initBottomLayout();
            } else {
                CardItemView viewItem = (CardItemView) childView;
                viewItem.setTag(i + 1);
                viewList.add(viewItem);
            }
        }
    }

    private void initBottomLayout() {

    }

    private class DragHelperCallback extends ViewDragHelper.Callback {

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            // 调用 offsetLeftAndRight 导致viewPosition改变 会调到此处 所以此处对index做保护处理
            int index = viewList.indexOf(changedView);
            if (index > 0) {
                return;
            }
            processLinkageView(changedView);
        }

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            // 如果数据List为空, 或者子View 不可见 则不处理
            if (dataList == null || dataList.size() == 0
                    || child.getVisibility() != VISIBLE || child.getScaleX() <= 1.0f - SCALE_STEP) {
                // 一般来讲 如果拖动的是第三层 或者第四层的view 则直接禁止
                // 此处用getScale的用法来巧妙回避
                return false;
            }
            // 只捕获顶部的view(rotation = 0)
            int childIndex = viewList.indexOf(child);
            if (childIndex > 0) {
                return false;
            } else if (child == bottomLayout) {
                return false;
            }
            return true;
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            // 用来控制拖拽过程中松手后 自行滑动的速度
            return 128;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            animToSide(releasedChild, xvel, yvel);
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            return left;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            return top;
        }
    }

    // 对view重新排序
    private void orderViewStack() {
        if (releasedViewList.size() == 0) {
            return;
        }

        synchronized (obj1) {
            CardItemView changedView = (CardItemView) releasedViewList.get(0);
            if (changedView.getLeft() == initCenterViewX) {
                return;
            }

            // 1. 消失的卡片View位置重置
            changedView.offsetLeftAndRight(initCenterViewX - changedView.getLeft());
            changedView.offsetTopAndBottom(initCenterViewY - changedView.getTop() + yOffsetStep * 2);

            float scale = 1.0f - SCALE_STEP * 2;
            changedView.setScaleX(scale);
            changedView.setScaleY(scale);

            updateShaderLayer();

            // 2. 卡片View在ViewGroup中的顺次调整
            int num = viewList.size();
            for (int i = num - 1; i > 0; i--) {
                View tempView = viewList.get(i);
                tempView.bringToFront();
            }
            bottomLayout.bringToFront();

            // 3. changedView填充新数据
            int newIndex = isShowingIndex + viewList.size();
            if (isCyclic) {
                CardDataItem dataItem = dataList.get(newIndex % dataList.size());
                changedView.fillData(dataItem);
                isShowingIndex++;
            } else {
                if (newIndex < dataList.size()) {
                    CardDataItem dataItem = dataList.get(newIndex);
                    changedView.fillData(dataItem);
                } else {
                    changedView.setVisibility(INVISIBLE);
                }
                if (isShowingIndex + 1 < dataList.size()) {
                    isShowingIndex++;
                }
            }

            // 4.viewList中的卡片view的位次调整
            viewList.remove(changedView);
            viewList.add(changedView);
            releasedViewList.remove(0);

            // 接口回调
            if (null != cardSwitchListener) {
                cardSwitchListener.onShow(isShowingIndex);
            }
        }
    }

    private void processLinkageView(View changedView) {
        int changeViewLeft = changedView.getLeft();
        int changeViewTop = changedView.getTop();
        int distance = Math.abs(changeViewTop - initCenterViewY)
                + Math.abs(changeViewLeft - initCenterViewX);
        float rate = distance / (float) MAX_SLIDE_DISTANCE_LINKAGE;

        float rate1 = rate;
        float rate2 = rate - 0.2f;
        float rate3 = rate2 - 0.2f;

        if (rate > 1) {
            rate1 = 1;
        }

        if (rate2 < 0) {
            rate2 = 0;
        } else if (rate2 > 1) {
            rate2 = 1;
        }

        if (rate3 < 0) {
            rate3 = 0;
        } else if (rate3 > 1) {
            rate3 = 1;
        }

        updateShaderLayer();
        adjustLinkageViewItem(changedView, rate1, 1);
        adjustLinkageViewItem(changedView, rate2, 2);
        adjustLinkageViewItem(changedView, rate3, 3);
    }

    // 由index对应view变成index - 1对应的view
    private void adjustLinkageViewItem(View changedView, float rate, int index) {
        int changeIndex = viewList.indexOf(changedView);
        int initPosY = yOffsetStep * index;
        float initScale = 1 - SCALE_STEP * (index - 1);

        int nextPosY = yOffsetStep * (index - 1);
        float nextScale = 1 - SCALE_STEP * (index - 1);

        int offset = (int) (initPosY + (nextPosY - initPosY) * rate);
        float scale = initScale + (nextScale - initScale) * rate;

        CardItemView adjustView = viewList.get(changeIndex + index);
        adjustView.offsetTopAndBottom(offset - adjustView.getTop() + initCenterViewY);
        adjustView.setScaleX(scale);
        adjustView.setScaleY(scale);
    }

    // 松手时处理滑动到边缘的动画
    private void animToSide(View changedView, float xvel, float yvel) {
        int finalX = initCenterViewX;
        int finalY = initCenterViewY;
        int flyType = -1;

        // 通过数学模型计算finalX 与finalY
        int dx = changedView.getLeft() - initCenterViewX;
        int dy = changedView.getTop() - initCenterViewY;
        if (dx == 0) {
            // 由于dx作为坟墓 此处保护处理
            dx = 1;
        }

        if (dy > 0 || yvel > 0) {
            finalX = initCenterViewX;
            finalY = initCenterViewY;
        } else if (xvel > X_VEL_THRESHOLD || yvel > X_DISTANCE_THRESHOLD) {
            finalX = allWidth;
            finalY = dy * (finalX - initCenterViewX) / dx + initCenterViewY;
            flyType = VANISH_TYPE_RIGHT;
            if (dx < 0 && dy < 0) {
                finalX = initCenterViewX;
                finalY = initCenterViewY;
                flyType = -1;
            }
        } else if (xvel < -X_VEL_THRESHOLD || dx < -X_DISTANCE_THRESHOLD) {
            finalX = -childWidth;
            finalY = dy * (childWidth + initCenterViewX) / (-dx) + initCenterViewY;
            flyType = VANISH_TYPE_LEFT;
            if (dx > 0 && dy < 0) {
                finalX = initCenterViewX;
                finalY = initCenterViewY;
                flyType = -1;
            }
        } else if (yvel < -Y_VEL_THRESHOLD || dy < -Y_DISTANCE_THRESHOLD) {
            if (dx > 0) {
                finalX = initCenterViewX + dx;
            } else {
                finalX = -initCenterViewX + dx;
            }
            finalY = -childHeight - getTop();
            flyType = VANISH_TYPE_TOP;
        }

        // 如果斜率太高 就折中处理
        if (finalY > allHeight) {
            finalY = allHeight;
        } else if (finalY < -childHeight - getTop()) {
            finalY = -childHeight - getTop();
        }

        // 如果没有飞向两侧 而是回到中间 需要谨慎处理
        if (finalX != initCenterViewX) {
            releasedViewList.add(changedView);
        }

        // 启动动画
        if (mDragHelper.smoothSlideViewTo(changedView, finalX, finalY)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }

        // 消失动画即将进行 listener回调
        if (flyType >= 0 && cardSwitchListener != null) {
            cardSwitchListener.onCardVanish(isShowingIndex, flyType);
        }
    }

    private void drawShaderLayer() {
        int i = 0;
        for (CardItemView view : viewList) {
            view.setShadeLayer(layerDrawables[i++]);
        }
    }

    private void updateShaderLayer() {
        int i = 0;
        for (CardItemView view : viewList) {
            if (i == 0) {
                view.setShadeLayer(layerDrawables[0]);
            } else {
                view.setShadeLayer(layerDrawables[i - 1]);
            }
            ++i;
        }
    }

    private void vanishOnBtnClick(int type) {
        View animateView = viewList.get(0);
        if (animateView.getVisibility() != VISIBLE) {
            return;
        }

        int finalX = 0;
        if (type == VANISH_TYPE_LEFT) {
            finalX = -childWidth;
        } else if (type == VANISH_TYPE_RIGHT) {
            finalX = allWidth;
        }

        if (finalX != 0) {
            releasedViewList.add(animateView);
            if (mDragHelper.smoothSlideViewTo(animateView, finalX, initCenterViewX + allHeight)) {
                ViewCompat.postInvalidateOnAnimation(this);
            }
        }

        if (type >= 0 && cardSwitchListener != null) {
            cardSwitchListener.onCardVanish(isShowingIndex, type);
        }
    }

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        } else {
            // 动画结束
            if (mDragHelper.getViewDragState() == ViewDragHelper.STATE_IDLE) {
                orderViewStack();
            }
        }
    }

    // touch事件的拦截与处理都交给mDragHelper处理
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean shouldIntercept = mDragHelper.shouldInterceptTouchEvent(ev);
        int action = ev.getActionMasked();
        if (action == MotionEvent.ACTION_DOWN) {
            // 保存初次按下时arrowFlagView的Y坐标
            // action_down时就让mDragHelper开始工作 否则有时会导致异常
            if (ev.getPointerCount() <= 1) {
                mDragHelper.processTouchEvent(ev);
                orderViewStack();
            }
        }
        return shouldIntercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 统一交给mDragHelper处理, 由DragHelperCallback实现拖动效果
        try {
            // 该行代码可能会导致一场, 正式发布时请将这行代码加上try catch
            mDragHelper.processTouchEvent(event);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        int maxHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, 0),
                resolveSizeAndState(maxHeight, heightMeasureSpec, 0));
        allWidth = getMeasuredWidth();
        allHeight = getMeasuredHeight();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        // 布局底部按钮的view
        if (null != bottomLayout) {
            bottomLayout.layout(left, top, bottom, right);
        }
        // 布局卡片view
        int size = viewList.size();
        for (int i = 0; i < size; i++) {
            CardItemView viewItem = viewList.get(i);
            int childHeight = viewItem.getMeasuredHeight();
            left = (getMeasuredWidth() - viewItem.getMeasuredWidth()) / 2;
            viewItem.layout(left, getPaddingTop(), left + viewItem.getMeasuredWidth(), getPaddingTop() + childHeight);
            int offset = yOffsetStep * i;
            float scale = 1 - SCALE_STEP * i;
            if (i > size - 2) {
                // 备用的view
                offset = yOffsetStep * (size - 2);
                scale = 1 - SCALE_STEP * (size - 2);
            }

            viewItem.offsetTopAndBottom(offset);
            viewItem.setScaleX(scale);
            viewItem.setScaleY(scale);
        }

        // 初始化一些中间参数
        initCenterViewX = viewList.get(0).getLeft();
        initCenterViewY = viewList.get(0).getTop();
        childWidth = viewList.get(0).getMeasuredWidth();
        childHeight = viewList.get(0).getMeasuredHeight();
    }

    public static int resolveSizeAndState(int size, int measureSpec, int childMeasuredState) {
        int result = size;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                result = size;
                break;
            case MeasureSpec.AT_MOST:
                if (specSize < size) {
                    result = specSize | MEASURED_STATE_TOO_SMALL;
                } else {
                    result = size;
                }
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
        }
        return result | (childMeasuredState & MEASURED_STATE_MASK);
    }


    public void fillData(List<CardDataItem> dataList) {
        this.dataList = dataList;

        int num = viewList.size();
        for (int i = 0; i < num; i++) {
            CardItemView itemView = viewList.get(i);
            itemView.fillData(dataList.get(i));
            itemView.setVisibility(VISIBLE);
        }
        drawShaderLayer();
        if (null != cardSwitchListener) {
            cardSwitchListener.onShow(0);
        }
    }

    public void appendData(List<CardDataItem> appendList) {
        // TODO: 19/6/21 appendData
        dataList.addAll(appendList);
        int currentIndex = isShowingIndex;
        int num = viewList.size();
        for (int i = 0; i < num; i++) {
            CardItemView itemView = viewList.get(i);
            itemView.setVisibility(View.VISIBLE);
            itemView.fillData(dataList.get(currentIndex++));
        }
    }


    public void setCardSwitchListener(CardSwitchListener cardSwitchListener) {
        this.cardSwitchListener = cardSwitchListener;
    }

    /**
     * 卡片回调接口
     */
    public interface CardSwitchListener {

        // 新卡片显示回调
        public void onShow(int index);

        // 卡片手势操作回调 {@link #VANISH_TYPE_LEFT} 或 {@link #VANISH_TYPE_RIGHT}
        public void onCardVanish(int index, int type);
    }
}
