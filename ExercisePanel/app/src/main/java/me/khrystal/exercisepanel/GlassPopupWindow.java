package me.khrystal.exercisepanel;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;


import static android.view.View.INVISIBLE;

public class GlassPopupWindow {

    private LayoutInflater inflater;
    protected Context mContext;
    protected PopupWindow mPopupWindow;
    protected View mRootView;

    private View mGlassView;
    private View mZoomView;
    private int mGlassWidth;
    private int mGlassHeight2;

    public GlassPopupWindow(Context context) {
        mContext = context;
        mPopupWindow = new PopupWindow(context);
        mPopupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    mPopupWindow.dismiss();
                    return true;
                }
                return false;
            }
        });
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        initConfig(context);
        setRootViewId(R.layout.view_word_magnifier);
    }

    private void initConfig(Context context) {
        mGlassWidth = context.getResources().getDimensionPixelOffset(R.dimen.glass_view_width);
        mGlassHeight2 = context.getResources().getDimensionPixelOffset(R.dimen.glass_view_height3);
    }

    @SuppressWarnings("deprecation")
    public void setRootViewId(int id) {
        mRootView = (ViewGroup) inflater.inflate(id, null);
        mGlassView = mRootView.findViewById(R.id.glass_view);
        mZoomView = mRootView.findViewById(R.id.zoom_view);
        mGlassView.setVisibility(INVISIBLE);
        mPopupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setContentView(mRootView);
    }

    public void showGlass(View anchor, int motionEventX, int motionEventY) {
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mRootView.setLayoutParams(new ViewGroup.LayoutParams(
                mGlassWidth, mGlassHeight2));
        mPopupWindow.setWidth(mGlassWidth);
        mPopupWindow.setHeight(mGlassHeight2 * 2);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setContentView(mRootView);
        int xPos = getMagnifierLeft(motionEventX);
        int yPos = getMagnifierTop(motionEventY);
        mGlassView.setVisibility(View.VISIBLE);
        mPopupWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, xPos, yPos);
    }

    public void hideGlass() {
        mGlassView.setVisibility(View.GONE);
        mPopupWindow.dismiss();
    }

    public void moveGlass(int motionEventX, int motionEventY) {
        int xPos = getMagnifierLeft(motionEventX);
        int yPos = getMagnifierTop(motionEventY);
        mPopupWindow.update(xPos, yPos, -1, -1, true);
    }

    public void setZoomImage(BitmapDrawable drawable) {
        mZoomView.setBackgroundDrawable(drawable);
    }

    private int getMagnifierLeft(int touchedX) {
        return (touchedX - mGlassWidth / 2);
    }

    private int getMagnifierTop(int touchedY) {
        return (touchedY - mGlassHeight2 * 3);
    }
}
