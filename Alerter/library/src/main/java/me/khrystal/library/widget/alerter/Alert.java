package me.khrystal.library.widget.alerter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import me.khrystal.library.widget.R;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/2/22
 * update time:
 * email: 723526676@qq.com
 */

public class Alert extends FrameLayout implements View.OnClickListener, Animation.AnimationListener {

    private static final int CLEAN_UP_DELAY_MILLIS = 100;

    private static final long DISPLAY_TIME_IN_SECONDS = 3000;

    private FrameLayout flBackground;
    private TextView tvTitle;
    private TextView tvText;
    private ImageView ivIcon;

    private Animation slideInAnimation;
    private Animation slideOutAnimation;

    private OnShowAlertListener onShowAlertListener;
    private OnHideAlertListener onHideAlertListener;

    private long duration = DISPLAY_TIME_IN_SECONDS;
    // 脉动
    private boolean enableIconPulse = true;

    private boolean marginSet;

    public Alert(@NonNull final Context context) {
        super(context, null, R.attr.alertStyle);
        initView();
    }

    public Alert(@NonNull final Context context, @NonNull final AttributeSet attrs) {
        super(context, attrs, R.attr.alertStyle);
        initView();
    }

    public Alert(@NonNull final Context context, @NonNull final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.alerter_alert_view, this);
        // 使其在触摸的时候有触感反馈,比如震动或按键声音等
        setHapticFeedbackEnabled(true);
        flBackground = (FrameLayout) findViewById(R.id.flAlertBackground);
        ivIcon = (ImageView) findViewById(R.id.ivIcon);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvText = (TextView) findViewById(R.id.tvText);

        flBackground.setOnClickListener(this);
        slideInAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.alerter_slide_in_from_top);
        slideOutAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.alerter_slide_out_to_top);
        slideInAnimation.setAnimationListener(this);

        // set animation to be run when View is added to Window
        setAnimation(slideInAnimation);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!marginSet) {
            marginSet = true;

            final ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) getLayoutParams();
            params.topMargin = getContext().getResources().getDimensionPixelSize(R.dimen.alerter_alert_negative_margin_top);
            requestLayout();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        slideInAnimation.setAnimationListener(null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        performClick();
        return super.onTouchEvent(event);
    }

    @Override
    public void onClick(View v) {
        hide();
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setVisibility(visibility);
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {
        if (!isInEditMode()) {
            performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (enableIconPulse) {
            try {
                ivIcon.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.alerter_pulse));
            } catch (Exception e) {
                Log.e(getClass().getSimpleName(), Log.getStackTraceString(e));
            }
        }
        if (onShowAlertListener != null) {
            onShowAlertListener.onShow();
        }

        postDelayed(new Runnable() {
            @Override
            public void run() {
                hide();
            }
        }, duration);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        // ignore
    }

    private void hide() {
        try {
            slideOutAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    flBackground.setOnClickListener(null);
                    flBackground.setClickable(false);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    removeFromParent();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    // ignore
                }
            });
            startAnimation(slideOutAnimation);
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), Log.getStackTraceString(e));
        }
    }

    private void removeFromParent() {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if (getParent() == null)
                        Log.e(getClass().getSimpleName(), "getParent() return null");
                    else {
                        try {
                            ((ViewGroup) getParent()).removeView(Alert.this);
                            if (onHideAlertListener != null) {
                                onHideAlertListener.onHide();
                            }
                        } catch (Exception e) {
                            Log.e(getClass().getSimpleName(), "Cannot remove from parent");
                        }
                    }
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), Log.getStackTraceString(e));
                }
            }
        }, CLEAN_UP_DELAY_MILLIS);
    }

    public void setAlertBackgroundColor(@ColorInt final int color) {
        flBackground.setBackgroundColor(color);
    }

    @SuppressWarnings("ResourceType")
    public void setTitle(@StyleRes final int titleId) {
        setTitle(getContext().getString(titleId));
    }

    public void setTitle(@NonNull final String title) {
        if (!TextUtils.isEmpty(title)) {
            tvTitle.setVisibility(VISIBLE);
            tvTitle.setText(title);
        }
    }

    @SuppressWarnings("ResourceType")
    public void setText(@StyleRes final int textId) {
        setText(getContext().getString(textId));
    }

    public void setText(@NonNull final String text) {
        if (!TextUtils.isEmpty(text)) {
            tvText.setVisibility(VISIBLE);
            tvText.setText(text);
        }
    }

    public void setIcon(@DrawableRes final int iconId) {
        final Drawable iconDrawable = ContextCompat.getDrawable(getContext(), iconId);
        ivIcon.setImageDrawable(iconDrawable);
    }

    public void setDuration(final long duration) {
        this.duration = duration;
    }

    public void pulseIcon(final boolean shouldPulse) {
        this.enableIconPulse = shouldPulse;
    }

    public void setOnShowAlertListener(@NonNull final OnShowAlertListener listener) {
        this.onShowAlertListener = listener;
    }

    public void setOnHideAlertListener(@NonNull final OnHideAlertListener listener) {
        this.onHideAlertListener = listener;
    }

    public long getDuration() {
        return duration;
    }

    public TextView getTextView() {
        return tvText;
    }

    public FrameLayout getAlertBackgroundView() {
        return flBackground;
    }

    public TextView getTitleView() {
        return tvTitle;
    }
}
