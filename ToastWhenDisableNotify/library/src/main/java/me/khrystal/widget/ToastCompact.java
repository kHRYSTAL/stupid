package me.khrystal.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/6/12
 * update time:
 * email: 723526676@qq.com
 */

public class ToastCompact {
    /**
     * if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
         //解决Android 7.1.1起不能再用Toast的问题（先解决crash）
         if(Build.VERSION.SDK_INT > 24){
         type = WindowManager.LayoutParams.TYPE_PHONE;
         }else{
         type = WindowManager.LayoutParams.TYPE_TOAST;
         }
         } else {
         type = WindowManager.LayoutParams.TYPE_PHONE;
         }
     */
    private Context mContext;
    private WindowManager wm;
    private int mDuration;
    private View mNextView;
    public static final int LENGTH_SHORT = 1500;
    public static final int LENGTH_LONG = 3000;

    public ToastCompact(Context context) {
        mContext = context.getApplicationContext();
        wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
    }

    public static ToastCompact makeText(Context context, CharSequence text,
                                 int duration) {
        ToastCompact result = new ToastCompact(context);
        View view = android.widget.Toast.makeText(context, text, android.widget.Toast.LENGTH_SHORT).getView();
        if (view != null){
            TextView tv = (TextView) view.findViewById(android.R.id.message);
            tv.setText(text);
        }
        result.mNextView = view;
        result.mDuration = duration;
        return result;
    }

    public static ToastCompact makeText(Context context, int resId, int duration)
            throws Resources.NotFoundException {
        return makeText(context, context.getResources().getText(resId),duration);
    }

    public void show() {
        if (mNextView != null) {
            WindowManager.LayoutParams params = new WindowManager.LayoutParams();
            params.gravity = Gravity.CENTER | Gravity.CENTER_HORIZONTAL;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            params.width = WindowManager.LayoutParams.WRAP_CONTENT;
            params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
            params.format = PixelFormat.TRANSLUCENT;
            params.windowAnimations = android.R.style.Animation_Toast;
            params.y = dip2px(mContext, 64);
//            params.type = WindowManager.LayoutParams.TYPE_TOAST;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                //解决Android 7.1.1起不能再用Toast的问题（先解决crash）
                if(Build.VERSION.SDK_INT > 24){
                    params.type = WindowManager.LayoutParams.TYPE_PHONE;
                }else{
                    params.type = WindowManager.LayoutParams.TYPE_TOAST;
                }
            } else {
                params.type = WindowManager.LayoutParams.TYPE_PHONE;
            }
            wm.addView(mNextView, params);
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    if (mNextView != null) {
                        wm.removeView(mNextView);
                        mNextView = null;
                        wm = null;
                    }
                }
            }, mDuration);
        }
    }

    private int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
