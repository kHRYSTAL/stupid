package me.khrystal.suspensionwindow.window;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import me.khrystal.suspensionwindow.R;
import me.khrystal.suspensionwindow.WebViewActivity;
import me.khrystal.suspensionwindow.util.SPUtil;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/10/26
 * update time:
 * email: 723526676@qq.com
 */
public class WindowUtil {

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;
    private View mView;

    private static final String ROM360 = "rom360";
    private static final String HUAWEI = "huawei";
    private static final String MEIZU = "meizu";
    private static final String MIUI = "miui";
    private static final String OPPO = "oppo";
    private static final String COMMON_ROM = "common_rom";

    private OnPermissionListener mOnPermissionListener;

    private WindowUtil() {}

    private static class SingletonInstance {
        @SuppressLint("StaticFieldLeak")
        private static final WindowUtil INSTANCE = new WindowUtil();
    }

    public static WindowUtil getInstance() {
        return SingletonInstance.INSTANCE;
    }

    public void showPermissionWindow(Context context, OnPermissionListener onPermissionListener) {
        if (checkPermission(context)) {
            showWindow(context);
        } else {
            SPUtil.setIntDefault(WebViewActivity.ARTICLE_ID, -1);
            SPUtil.setStringDefault(WebViewActivity.ARTICLE_JUMP_URL, "");
            SPUtil.setStringDefault(WebViewActivity.ARTICLE_IMAGE_URL, "");
            mOnPermissionListener = onPermissionListener;
            applyPermission(context);
        }
    }

    private void showWindow(Context context) {
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mView = LayoutInflater.from(context).inflate(R.layout.article_window, null);
        ImageView ivImage = mView.findViewById(R.id.aw_iv_image);
        String imageUrl = SPUtil.getStringDefault(WebViewActivity.ARTICLE_IMAGE_URL, "");
        RequestOptions requestOptions = RequestOptions.circleCropTransform();
        requestOptions.placeholder(R.mipmap.ic_launcher_round).error(R.mipmap.ic_launcher_round);
        Glide.with(context).load(imageUrl).apply(requestOptions).into(ivImage);
        initListener(context);
        mLayoutParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY;
        } else {
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        mLayoutParams.format = PixelFormat.RGBA_8888; // 窗口透明
        mLayoutParams.gravity = Gravity.LEFT | Gravity.TOP; // 窗口位置
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mLayoutParams.width = 200;
        mLayoutParams.height = 200;
        mLayoutParams.x = mWindowManager.getDefaultDisplay().getWidth() - 200;
        mLayoutParams.y = 0;
        mWindowManager.addView(mView, mLayoutParams);
    }

    public void dismissWindow() {
        if (mWindowManager != null && mView != null) {
            mWindowManager.removeViewImmediate(mView);
            mWindowManager = null;
            mView = null;
        }
    }
    
    private void initListener(final Context context) {
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String jumpUrl = SPUtil.getStringDefault(WebViewActivity.ARTICLE_JUMP_URL, "");
                if (!jumpUrl.isEmpty()) {
                    WebViewActivity.start(context, jumpUrl);
                }
            }
        });
        // 设置触摸滑动事件
        mView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                // TODO: 18/10/29  
                return false;
            }
        });
    }


    interface OnPermissionListener {
        void result(boolean isSuccess);
    }
}
