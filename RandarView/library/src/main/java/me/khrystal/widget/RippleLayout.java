package me.khrystal.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import me.khrystal.widget.mask.MaskPierceView;
import me.khrystal.widget.ripple.RippleView;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/5/8
 * update time:
 * email: 723526676@qq.com
 */

public class RippleLayout extends RelativeLayout {

    private ImageView bgImageView;
    private ImageView iconImageView;
    private MaskPierceView maskView;
    private RippleView rippleView;

    public RippleLayout(Context context) {
        this(context, null);
    }

    public RippleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RippleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        // 添加背景图
        ViewGroup.LayoutParams bgParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        bgImageView = new ImageView(context);
        bgImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        bgImageView.setLayoutParams(bgParams);
        addView(bgImageView);
        // 添加遮罩
        maskView = new MaskPierceView(context);
        maskView.setLayoutParams(bgParams);
        addView(maskView);
        // 添加雷达
        RelativeLayout.LayoutParams radarParams = new LayoutParams(600, 600);
        radarParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        rippleView = new RippleView(context);
        addView(rippleView, radarParams);
        // 添加用户头像
        RelativeLayout.LayoutParams iconParams = new LayoutParams(100, 100);
        iconParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        iconImageView = new ImageView(context);
        iconImageView.setLayoutParams(iconParams);
        addView(iconImageView, iconParams);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (maskView != null) {
            maskView.setPiercePosition(getWidth() / 2, getHeight() / 2, 300);
            maskView.invalidate();
        }
    }

    public void startScan() {
        if (rippleView != null) {
            rippleView.startRipple();
        }
    }

    public void stopScan() {
        if (rippleView != null) {
            rippleView.stopRipple();
        }
    }

    public void setBackgroundRes(int resId) {
        if (bgImageView != null) {
            bgImageView.setImageResource(resId);
        }
    }

    public void setIcon(int resId) {
        if (iconImageView != null) {
            iconImageView.setImageResource(resId);
        }
    }
}
