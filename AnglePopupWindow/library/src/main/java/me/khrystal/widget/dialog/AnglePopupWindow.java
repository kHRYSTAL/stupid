package me.khrystal.widget.dialog;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * usage: 带弹出动画与尖角指向的popupwindow
 *        用于在列表item上显示popupwindow
 * author: kHRYSTAL
 * create time: 17/3/14
 * update time:
 * email: 723526676@qq.com
 */

public class AnglePopupWindow {

    private static final String TAG = AnglePopupWindow.class.getSimpleName();

    private Activity mActivity;
    private WindowManager.LayoutParams params;
    private boolean isShow;
    private WindowManager windowManager;
    private ViewGroup rootView;
    private ViewGroup linearLayout;

    // 动画执行时间
    private final int animDuration = 250;
    // 动画是否在执行
    private boolean isAniming;

    public AnglePopupWindow(Activity activity, List<String> contentList,
                            List<View.OnClickListener> clickListeners) {
        this.mActivity = activity;
        this.windowManager = (WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE);
        initLayout(contentList, clickListeners);
    }

    /**
     * @param contentList 点击item内容的文字
     * @param clickListeners 点击item的事件
     */
    private void initLayout(List<String> contentList, List<View.OnClickListener> clickListeners) {
        // 根布局
        rootView = (ViewGroup) View.inflate(mActivity, R.layout.angle_popup_layout, null);
        linearLayout = (ViewGroup) rootView.findViewById(R.id.linearLayout);

        List<View> list = new ArrayList<>();
        for (int i = 0; i < contentList.size(); i++) {
            View view = View.inflate(mActivity, R.layout.angle_popup_item, null);
            TextView textView = (TextView) view.findViewById(R.id.tv_content);
            View divider = view.findViewById(R.id.v_line);
            textView.setText(contentList.get(i));
            linearLayout.addView(view);
            list.add(view);
            if (i == 0) {
                divider.setVisibility(View.INVISIBLE);
            } else {
                divider.setVisibility(View.VISIBLE);
            }
        }
        if (clickListeners != null) {
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setOnClickListener(clickListeners.get(i));
            }
        }

        // 根布局设置背景透明
        params = new WindowManager.LayoutParams();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.format = PixelFormat.RGBA_8888;
        params.gravity = Gravity.LEFT | Gravity.TOP;

        // 当点击根布局时 隐藏
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissPopupWindow();
            }
        });

        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // 如果是显示状态那么隐藏视图
                if (keyCode == KeyEvent.KEYCODE_BACK && isShow)
                    dismissPopupWindow();
                return isShow;
            }
        });
    }

    public void showPopupWindow(View locationView) {
        if (!isAniming) {
            isAniming = true;
            try {
                // 获取view 相对于屏幕的坐标 (非父布局)
                int[] arr = new int[2];
                locationView.getLocationOnScreen(arr);
                linearLayout.measure(0, 0);
                Rect frame = new Rect();
                // 获取状态栏高度
                mActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
                float x = arr[0] + locationView.getWidth() - linearLayout.getMeasuredWidth();
                float y = 0f;
                
                WindowManager wm = (WindowManager) mActivity
                        .getSystemService(Context.WINDOW_SERVICE);

                int screenH = wm.getDefaultDisplay().getHeight();


                if ((screenH * 2 / 3) < arr[1]) {
                    y = arr[1] - locationView.getHeight() - linearLayout.getMeasuredHeight();
                    // TODO: 17/3/14  设置pop背景图片角标向下
                } else {
                    y = arr[1] - frame.top + locationView.getHeight();
                    // TODO: 17/3/14  
                }
                Log.e(TAG, "screen Y:" + screenH);
                Log.e(TAG, "point Y:" + y);
                linearLayout.setX(x);
                linearLayout.setY(y);

                //这里就是使用WindowManager直接将我们处理好的view添加到屏幕最前端
                windowManager = (WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE);
                windowManager.addView(rootView, params);

                //这一步就是有回弹效果的弹出动画, 我用属性动画写的, 很简单
                showAnim(linearLayout, 0, 1, animDuration, true);

                //视图被弹出来时得到焦点, 否则就捕获不到Touch事件
                rootView.setFocusable(true);
                rootView.setFocusableInTouchMode(true);
                rootView.requestFocus();
                rootView.requestFocusFromTouch();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void dismissPopupWindow(){
        if(!isAniming) {
            isAniming = true;
            isShow = false;
            goneAnim(linearLayout, 0.95f, 1, animDuration / 3, true);
        }
    }

    private void showAnim(final View view, float start, final float end, int duration, final boolean isWhile) {

        ValueAnimator va = ValueAnimator.ofFloat(start, end).setDuration(duration);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                view.setPivotX(view.getWidth());
                view.setPivotY(0);
                view.setScaleX(value);
                view.setScaleY(value);
            }
        });
        va.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (isWhile) {
                    showAnim(view, end, 0.95f, animDuration / 3, false);
                }else{
                    isAniming = false;
                }
            }
        });
        va.start();
    }

    public void goneAnim(final View view, float start, final float end, int duration, final boolean isWhile){

        ValueAnimator va = ValueAnimator.ofFloat(start, end).setDuration(duration);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                view.setPivotX(view.getWidth());
                view.setPivotY(0);
                view.setScaleX(value);
                view.setScaleY(value);
            }
        });
        va.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if(isWhile){
                    alphaAnim(rootView, 1, 0, animDuration);
                    goneAnim(view, end, 0f, animDuration, false);
                }else{
                    try {
                        windowManager.removeViewImmediate(rootView);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    isAniming = false;
                }
            }
        });
        va.start();
    }

    private void alphaAnim(final View view, int start, int end, int duration){

        ValueAnimator va = ValueAnimator.ofFloat(start, end).setDuration(duration);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                view.setAlpha(value);
            }
        });
        va.start();
    }
}
