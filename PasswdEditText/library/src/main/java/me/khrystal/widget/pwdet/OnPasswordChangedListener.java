package me.khrystal.widget.pwdet;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/8/31
 * update time:
 * email: 723526676@qq.com
 */

public interface OnPasswordChangedListener {

        /**
         * Invoked when the password changed.
         * @param psw new text
         */
        void onTextChanged(String psw);

        /**
         * Invoked when the password is at the maximum length.
         * @param psw complete text
         */
        void onInputFinish(String psw);

    }