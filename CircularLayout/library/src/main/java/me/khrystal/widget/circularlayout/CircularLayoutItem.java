package me.khrystal.widget.circularlayout;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/2/13
 * update time:
 * email: 723526676@qq.com
 */
public abstract class CircularLayoutItem<T> extends LinearLayout {

    private int rotationAngle = 0;
    private float mDownX;
    private float mDownY;
    private boolean isPressed;
    private int index;

    protected CircularLayout parent;
    private ImageView background;
    private ImageView foreground;
    private OnClickListener clickListener;
    private OnFocusListener focusListener;


    public CircularLayoutItem(Context context) {
        super(context);
        init();
    }

    public CircularLayoutItem(Context context, CircularLayout circularLayout) {
        super(context);
        parent = circularLayout;
        init();
    }

    public CircularLayoutItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircularLayoutItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getRawX();
                mDownY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                long downTime = SystemClock.uptimeMillis();
                long eventTime = SystemClock.uptimeMillis() + 100;
                float x = ev.getRawX();
                float y = ev.getRawY();
                if ((Math.abs(mDownX) - x > 10) || (Math.abs(mDownY - y) > 10)) {
                    int metaState = 0;
                    // create new motion event
                    MotionEvent motionEvent = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, x, y, metaState);
                    ((CircularLayout) this.getParent()).dispatchTouchEvent(motionEvent); // send event to parent to scroll, not consume
                    return true;
                }
                break;
        }

        return super.dispatchTouchEvent(ev);
    }

    public void onClick() {
        if (clickListener != null) {
            clickListener.onClick();
        }
    }

    public void onFocus() {
        if (focusListener != null) {
            focusListener.onFocus();
        }
    }

    public void onUnFocus() {
        if (focusListener != null) {
            focusListener.onUnFocus();
        }
    }

    // initialize the obj
    public void init() {
        setWillNotDraw(false);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_image_view_layout, this);
        background = findViewById(R.id.background);
        foreground = findViewById(R.id.foreground);

        setLayerType(View.LAYER_TYPE_HARDWARE, null);
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isPressed = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        onClick();
                        if (isPressed) {
                            isPressed = false;
                            return false;
                        }
                        break;
                }
                return true;
            }
        });
    }

    // make item balance in horizontal
    public void balance() {
        Animation animation = new RotateAnimation(0, -1 * parent.getPinndedChildsRotationAngle(), getHeight() / 2, getWidth() / 2);
        animation.setDuration(300);
        animation.setRepeatCount(0); // -1 = infinite repeated;
        animation.setRepeatMode(Animation.REVERSE); // reverses each repeat
        animation.setFillAfter(true);
        this.setAnimation(animation);
    }

    private void rotate() {
        if (background != null) {
            if (!parent.getIsPinnedChilds()) {
                background.setRotation(rotationAngle % 360);
            } else {
                background.setRotation(parent.getPinndedChildsRotationAngle());
            }
        }
        if (foreground != null) {
            if (!parent.getIsPinnedChilds()) {
                foreground.setRotation(rotationAngle % 360);
            } else {
                foreground.setRotation(parent.getPinndedChildsRotationAngle());
            }
        }
    }

    @Override
    public void setBackground(Drawable drawable) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            background.setBackgroundDrawable(drawable);
        } else {
            background.setBackground(drawable);
        }
    }

    @Override
    public void setForeground(Drawable drawable) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            foreground.setBackgroundDrawable(drawable);
        } else {
            foreground.setBackground(drawable);
        }
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    // set the rotation angle of the obj
    public void setRotationAngle(int angle) {
        rotationAngle = angle;
        rotate();
        this.invalidate();
    }

    public void setParent(CircularLayout parent) {
        this.parent = parent;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.clickListener = onClickListener;
    }

    public void setOnFocusListener(OnFocusListener onFocusListener) {
        this.focusListener = onFocusListener;
    }

    public interface OnClickListener {
        void onClick();
    }

    public interface OnFocusListener {
        public void onFocus();

        public void onUnFocus();
    }
}
