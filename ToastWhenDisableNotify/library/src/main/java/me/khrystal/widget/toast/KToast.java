package me.khrystal.widget.toast;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * usage: when disable notification permission use this Toast
 * author: kHRYSTAL
 * create time: 17/6/12
 * update time:
 * email: 723526676@qq.com
 */

public class KToast {

    private static final String TAG = KToast.class.getSimpleName();

    public static final int LENGTH_SHORT = 0;
    public static final int LENGHT_LONG = 1;

    private final int ANIMATION_DURATION = 600;
    private WeakReference<Activity> reference;
    private TextView mTextView;
    private ViewGroup container;
    private View v;
    private LinearLayout mContainer;
    private int HIDE_DELAY = 2000;
    private String TOAST_TAG = "KToast_TAG";
    private AlphaAnimation outAnimation;
    private AlphaAnimation inAnimation;

    private static boolean isShow = false;

    private static String activityName;

    private KToast(Activity activity) {
        reference = new WeakReference<Activity>(activity);
        container = (ViewGroup) activity.findViewById(android.R.id.content);

        View viewWithTag = container.findViewWithTag(TOAST_TAG);
        if (viewWithTag == null) {
            v = activity.getLayoutInflater().inflate(
                    R.layout.ktoast, container);
            v.setTag(TOAST_TAG);
        } else {
            v = viewWithTag;
        }

        mContainer = (LinearLayout) v.findViewById(R.id.mbContainer);
        mTextView = (TextView) v.findViewById(R.id.mbMessage);
        if (!TextUtils.equals(activityName, reference.get().getClass().getName())) {
            activityName = reference.get().getClass().getName();
            isShow = false;
        }
    }


    public static KToast makeText(Context context, CharSequence message, int HIDE_DELAY) {
        if (context instanceof Activity) {
            KToast kToast = new KToast((Activity) context);
            if (HIDE_DELAY == LENGHT_LONG) {
                kToast.HIDE_DELAY = 2500;
            } else {
                kToast.HIDE_DELAY = 1500;
            }
            kToast.setText(message);
            return kToast;
        } else {
            throw new RuntimeException("KToast @param context must instance of Activity");
        }
    }

    public static KToast makeText(Context context, int resId, int HIDE_DELAY) {
        return makeText(context, context.getText(resId), HIDE_DELAY);
    }

    public void show() {
        inAnimation = new AlphaAnimation(0.0f, 1.0f);
        outAnimation = new AlphaAnimation(1.0f, 0.0f);

        inAnimation.setDuration(ANIMATION_DURATION);
        outAnimation.setDuration(ANIMATION_DURATION);

        inAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (!reference.get().isFinishing())
                    mTextView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (!reference.get().isFinishing()) {
                    mContainer.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        outAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (!reference.get().isFinishing())
                    mContainer.setVisibility(View.GONE);
                isShow = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        if (isShow) {
            mContainer.removeCallbacks(oldRun);
            mContainer.postDelayed(mHideRunnable, HIDE_DELAY);
        } else {
            mContainer.startAnimation(inAnimation);
            isShow = true;
        }
        mContainer.postDelayed(mHideRunnable, HIDE_DELAY);
        oldRun = mHideRunnable;
    }

    private static Runnable oldRun;
    private Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            if (reference.get().hasWindowFocus()) {
                mContainer.startAnimation(outAnimation);
            } else {
                if (!reference.get().isFinishing()) {
                    mContainer.setVisibility(View.GONE);
                }
            }
        }
    };

    public void cancel() {
        if (isShow) {
            isShow = false;
            mContainer.setVisibility(View.GONE);
            mContainer.removeCallbacks(mHideRunnable);
        }
    }

    public void setText(CharSequence s) {
        if (v == null)
            throw new RuntimeException("This Toast was not created with makeText");
        mTextView.setText(s);
    }

    public void setText(int resId) {
        setText(reference.get().getText(resId));
    }
}
