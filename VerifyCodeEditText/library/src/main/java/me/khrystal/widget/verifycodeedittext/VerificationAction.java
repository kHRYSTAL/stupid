package me.khrystal.widget.verifycodeedittext;

import android.support.annotation.ColorRes;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/6/29
 * update time:
 * email: 723526676@qq.com
 */

public interface VerificationAction {

    // 设置位数
    void setFigures(int figures);

    // 设置验证码之间的间距
    void setVerCodeMargin(int margin);

    // 设置底部选中状态的颜色
    void setBottomSelectedColor(@ColorRes int bottomSelectedColor);

    // 设置选择的背景色
    void setBottomNormalColor(@ColorRes int bottomNormalColor);

    // 设置选择的背景色
    void setSelectedBackgroundColor(@ColorRes int selectedBackgroundColor);

    // 设置底线的高度
    void setBottomLineHeight(int bottomLineHeight);

    // 设置当验证码变化时的监听器
    void setOnVerificationCodeChangedListener(OnVerificationCodeChangedListener listener);

    interface OnVerificationCodeChangedListener {
        // 当验证码发生变化时回调
        void onVerCodeChanged(CharSequence s, int start, int before, int count);

        // 输入完毕后回调
        void onInputCompleted(CharSequence s);
    }
}
