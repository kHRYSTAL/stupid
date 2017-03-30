package me.khrystal.widget.menu;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Space;

/**
 * usage: 下拉菜单容器 类似popupwindow样式
 * author: kHRYSTAL
 * create time: 17/3/21
 * update time:
 * email: 723526676@qq.com
 */

public class DropDownMenu extends LinearLayout {

    // 底部容器, 包含popupMenuViews, maskView;
    private FrameLayout containerView;
    // 弹出菜单父布局
    private FrameLayout popupMenuView;
    // 遮罩半透明View, 点击可关闭DropDownMenu
    private View maskView;
    // 距离顶部视图
    private Space mSpace;

    // popMenu 是否打开
    private boolean popIsOpen = false;

    private OnMenuStateChangeListener mListener;
    private Context context;

    // 遮罩颜色
    private int maskColor = 0x88888888;
    private int mPopContainerIndex;
    private View mPopView;

    public DropDownMenu(Context context) {
        super(context, null);
    }

    public DropDownMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DropDownMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        View root = View.inflate(context, R.layout.drop_down_menu_layout, this);
        mSpace = (Space) root.findViewById(R.id.drop_down_space);
        setOrientation(VERTICAL);
    }

    public DropDownMenu setDropDownMenu(@NonNull View popView) {
        return setDropDownMenu(popView, null, null);
    }

    public DropDownMenu setDropDownMenu(@NonNull View popView, @Nullable ViewGroup.LayoutParams popParams) {
        return setDropDownMenu(popView, null, popParams);
    }

    /**
     * 设置菜单内容
     * @param popView 菜单布局
     * @param contentView 弹出菜单之前的内容布局
     */
    public DropDownMenu setDropDownMenu(@NonNull View popView, @Nullable View contentView, @Nullable ViewGroup.LayoutParams popParams) {
        if (containerView == null) {
            containerView = new FrameLayout(context);
            containerView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            addView(containerView, 1);
        }
        // 解决重复set导致的内部容器增多问题
        containerView.removeAllViews();
        if (contentView != null)
            containerView.addView(contentView, calculateIndex());
        maskView = new View(getContext());
        maskView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        maskView.setBackgroundColor(maskColor);
        maskView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 关闭菜单
                closeMenu();
            }
        });
        containerView.addView(maskView, calculateIndex());
        maskView.setVisibility(GONE);
        popupMenuView = new FrameLayout(getContext());
        popupMenuView.setVisibility(GONE);
        containerView.addView(popupMenuView, calculateIndex());
        if (popView != null) {
            mPopView = popView;
            if (popParams != null)
                popView.setLayoutParams(popParams);
            else
                popView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            popupMenuView.addView(popView);
        }
        return this;
    }

    private int calculateIndex() {
        return  mPopContainerIndex++;
    }

    /**
     * menu switch 开关
     */
    public void toggleMenu() {
        if (popIsOpen) {
            closeMenu();
        } else {
            openMenu();
        }
    }

    /**
     * 打开菜单
     */
    public void openMenu() {
        if (!popIsOpen) {
            popupMenuView.setVisibility(VISIBLE);
            popupMenuView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_menu_in));
            maskView.setVisibility(VISIBLE);
            maskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_mask_in));
            mPopView.setVisibility(VISIBLE);
            popIsOpen = true;
            if (mListener != null) {
                mListener.onShow();
            }
        }
    }

    /**
     * 关闭菜单
     */
    public void closeMenu() {
        if (popIsOpen) {
            popupMenuView.setVisibility(GONE);
            popupMenuView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_menu_out));
            maskView.setVisibility(GONE);
            maskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_mask_out));
            popIsOpen = false;
            mPopContainerIndex = 0;
            if (mListener != null) {
                mListener.onDismiss();
            }
        }
    }

    /**
     * @return 当前菜单是否展开
     */
    public boolean isShowing() {
        return popIsOpen;
    }

    /**
     * 设置space高度 即toolbar高度
     * @param height
     */
    public DropDownMenu setTopSpaceHight(int height) {
        ViewGroup.LayoutParams layoutParams = mSpace.getLayoutParams();
        layoutParams.height = height;
        mSpace.setLayoutParams(layoutParams);
        return this;
    }

    /**
     * 设置监听菜单是否展开的监听器 可以通过回调控制外部某些动画执行
     */
    public DropDownMenu setOnMenuStateChangeListener(OnMenuStateChangeListener listener) {
        mListener = listener;
        return this;
    }

    public interface OnMenuStateChangeListener {
        public void onShow();
        public void onDismiss();
    }

}
