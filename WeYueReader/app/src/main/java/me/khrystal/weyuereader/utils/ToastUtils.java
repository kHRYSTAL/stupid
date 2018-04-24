package me.khrystal.weyuereader.utils;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

import me.khrystal.weyuereader.WYApplication;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/4/24
 * update time:
 * email: 723526676@qq.com
 */

public class ToastUtils {
    private static Context context = WYApplication.getAppContext();
    private static Toast toast;

    public void show(@StringRes int resId) {
        show(context.getResources().getString(resId));
    }

    public static void show(CharSequence text) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();
    }
}
