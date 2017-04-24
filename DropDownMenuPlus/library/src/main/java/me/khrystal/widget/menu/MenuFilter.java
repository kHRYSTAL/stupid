package me.khrystal.widget.menu;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import me.khrystal.widget.R;
import me.khrystal.widget.menu.adapter.MenuAdapter;
import me.khrystal.widget.menu.util.DensityUtil;
import me.khrystal.widget.menu.util.SimpleAnimationListener;
import me.khrystal.widget.menu.view.FixedTabIndicator;

/**
 * usage: 筛选器 支持二级 多级 网格 自定义 带title
 * author: kHRYSTAL
 * create time: 17/4/24
 * update time:
 * email: 723526676@qq.com
 */

public class MenuFilter extends RelativeLayout implements View.OnClickListener, FixedTabIndicator.OnItemClickListener {

    private FixedTabIndicator fixedTabIndicator;
    private FrameLayout frameLayoutContainer;

    private View currentView;
    private Animation dismissAnimation;
    private Animation occurAnimation;
    private Animation alphaDismissAnimation;
    private Animation alphaOccurAnimation;

    private MenuAdapter mMenuAdapter;

    public MenuFilter(Context context) {
        this(context, null);
    }

    public MenuFilter(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MenuFilter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setBackgroundColor(Color.WHITE);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setContentView(findViewById(R.id.mFilterContentView));
    }

    public void setContentView(View contentView) {
        removeAllViews();
        // 顶部筛选条
        fixedTabIndicator = new FixedTabIndicator(getContext());
        fixedTabIndicator.setId(R.id.fixedTabIndicator);
        addView(fixedTabIndicator, -1, DensityUtil.dip2px(getContext(), 50));

        // 添加contentView 内容界面
        LayoutParams params = new LayoutParams(-1, -1);
        params.addRule(BELOW, R.id.fixedTabIndicator);
        addView(contentView, params);

        // 添加展开页面 装载筛选器list
        frameLayoutContainer = new FrameLayout(getContext());
        frameLayoutContainer.setBackgroundColor(getResources().getColor(R.color.black_p50));
        addView(frameLayoutContainer, params);

        frameLayoutContainer.setVisibility(GONE);
        initListener();
        initAnimation();
    }

    public void setMenuAdapter(MenuAdapter adapter) {
        verifyContainer();
        mMenuAdapter = adapter;
        verifyMenuAdapter();
        // 设置title
        fixedTabIndicator.setTitles(mMenuAdapter);
        // 添加view
        setPositionView();
    }

    private void setPositionView() {
        int count = mMenuAdapter.getMenuCount();
        for (int i = 0; i < count; ++i) {
            setPositionView(i, findViewAtPosition(i), mMenuAdapter.getBottomMargin(i));
        }
    }

    public View findViewAtPosition(int position) {
        verifyContainer();
        View view = frameLayoutContainer.getChildAt(position);
        if (view == null) {
            view = mMenuAdapter.getView(position, frameLayoutContainer);
        }
        return view;
    }

    private void setPositionView(int position, View view, int bottomMargin) {
        verifyContainer();
        if (view == null || position > mMenuAdapter.getMenuCount() || position < 0) {
            throw new IllegalStateException("the view at " + position + " can not be null");
        }

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-1, -2);
        params.bottomMargin = bottomMargin;
        frameLayoutContainer.addView(view, position, params);
        view.setVisibility(GONE);
    }

    public boolean isShowing() {
        verifyContainer();
        return frameLayoutContainer.isShown();
    }

    public boolean isClosed() {
        return !isShowing();
    }

    public void close() {
        if (isClosed())
            return;
        frameLayoutContainer.startAnimation(alphaDismissAnimation);
        fixedTabIndicator.resetCurrentPos();
        if (currentView != null) {
            currentView.startAnimation(dismissAnimation);
        }
    }

    public void setPositionIndicatorText(int position, String text) {
        verifyContainer();
        fixedTabIndicator.setPositionText(position, text);
    }

    public void setCurrentIndicatorText(String text) {
        verifyContainer();
        fixedTabIndicator.setCurrentText(text);
    }

    private void verifyMenuAdapter() {
        if (mMenuAdapter == null) {
            throw new IllegalStateException("the menu Adapter is null");
        }
    }

    private void verifyContainer() {
        if (frameLayoutContainer == null) {
            throw new IllegalStateException("you must initiation setContentView() before");
        }
    }

    private void initAnimation() {
        occurAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.top_in);
        SimpleAnimationListener listener = new SimpleAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                frameLayoutContainer.setVisibility(GONE);
            }
        };

        dismissAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.top_out);
        dismissAnimation.setAnimationListener(listener);


        alphaDismissAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.alpha_to_zero);
        alphaDismissAnimation.setDuration(300);
        alphaDismissAnimation.setAnimationListener(listener);

        alphaOccurAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.alpha_to_one);
        alphaOccurAnimation.setDuration(300);
    }


    private void initListener() {
        frameLayoutContainer.setOnClickListener(this);
        fixedTabIndicator.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (isShowing()) {
            close();
        }
    }

    @Override
    public void onItemClick(View v, int position, boolean open) {
        if (open) {
            close();
        } else {
            currentView = frameLayoutContainer.getChildAt(position);
            if (currentView == null)
                return;
            frameLayoutContainer.getChildAt(fixedTabIndicator.getLastIndicatorPosition()).setVisibility(GONE);
            frameLayoutContainer.getChildAt(position).setVisibility(VISIBLE);

            if (isClosed()) {
                frameLayoutContainer.setVisibility(VISIBLE);
                frameLayoutContainer.startAnimation(alphaOccurAnimation);
                // 可移出 进行每次展出
                currentView.startAnimation(occurAnimation);
            }
        }
    }

}
