package me.khrystal.widget.slidearcview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/2/28
 * update time:
 * email: 723526676@qq.com
 */

public class SlidingArcView extends ViewGroup {

    private static String TAG = SlidingArcView.class.getSimpleName();

    // 需要外部 set 的数据
    private List<String> mTitles;
    private List<Integer> mSrcList;

    // 与内部子 view 相关的属性
    private boolean isAnimated = false; // 动画是否在执行
    private int viewTopChange = ScreenUtil.dp2px(80f); // 子view向上偏移的位移
    private VelocityTracker mVelocityTracker; // fling 使用
    private static final int SPEED = 30;
    private SignView leftView; // 屏幕最左边的子view
    private SignView rightView; // 屏幕最右边的子view
    // 外层圆心
    private int CentX;
    private int CentY;

    private int RADIUS; // 半径
    private static final int VX = 50; // 第一个view的x坐标
    private SignView chooseView; // 选中的view;
    private SignView lastChooseView;
    private boolean isClicke = false;



    private List<SignView> views = new ArrayList<>();




    public SlidingArcView(Context context) {
        super(context);
    }

    public SlidingArcView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlidingArcView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SlidingArcView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    private class SignView {

        private int indexInScreen;
        private View view;
        private String title;
        private int centerX;
        private int centerY;
        private int index;
        private int size = 120;
        private int width = (ScreenUtil.getScreenW()) / 5; // 屏幕上半圆内只显示5个元素
        private float normalScale = 1.0f; // 默认大小
        private float maxScale = 0.2f; // 放大倍数
        private boolean stop; // 是否停止手动滚动 + fling滑动 如停止 进行计算滑动到最靠近的指定固定位置
        private boolean isChoose = false;

        public SignView(View view, final int index) {
            this.index = index;
            this.view = view;
            this.title = mTitles.get(index);
            if (index == 0) {

            }
        }
    }
}
