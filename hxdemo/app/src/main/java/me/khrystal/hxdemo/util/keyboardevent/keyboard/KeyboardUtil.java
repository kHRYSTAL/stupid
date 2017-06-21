package me.khrystal.hxdemo.util.keyboardevent.keyboard;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/5/25
 * update time:
 * email: 723526676@qq.com
 */

public final class KeyboardUtil {

    private KeyboardUtil() {
        throw new AssertionError();
    }

    public static void showKeyboard(Context context, EditText target) {
        if (context == null || target == null) {
            return;
        }
        InputMethodManager imm = getInputMethodManager(context);
        imm.showSoftInput(target, InputMethodManager.SHOW_IMPLICIT);
    }

    public static void showKeyboardInDialog(Dialog dialog, EditText target) {
        if (dialog == null || target == null) {
            return;
        }
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        target.requestFocus();
    }

    public static void hideKeyboard(Context context, View target) {
        if (context == null || target == null) {
            return;
        }
        InputMethodManager imm = getInputMethodManager(context);
        imm.hideSoftInputFromWindow(target.getWindowToken(), 0);
    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.getWindow().getDecorView();
        if (view != null) {
            hideKeyboard(activity, view);
        }
    }

    private static InputMethodManager getInputMethodManager(Context context) {
        return (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    }
}
