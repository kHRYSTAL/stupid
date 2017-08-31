package me.khrystal.widget.pwdet;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/8/31
 * update time:
 * email: 723526676@qq.com
 */

interface PasswordView {

    String getPassword();

    void clearPassword();

    void setPassword(String password);

    void setPasswordVisibility(boolean visible);

    void togglePasswordVisibility();

    void setOnPasswordChangeListener(OnPasswordChangedListener listener);

    void setPasswordType(PasswordType passwordType);
}
