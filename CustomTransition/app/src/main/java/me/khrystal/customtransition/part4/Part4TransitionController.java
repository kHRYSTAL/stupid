package me.khrystal.customtransition.part4;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

import me.khrystal.customtransition.AnimatorBuilder;
import me.khrystal.customtransition.R;
import me.khrystal.customtransition.TransitionController;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/1/11
 * update time:
 * email: 723526676@qq.com
 */

public class Part4TransitionController extends TransitionController {

    private static final int[] VIEW_IDS = {
            R.id.input_view, R.id.input_done, R.id.translation
    };

    public static TransitionController newInstance(Activity activity) {
        WeakReference<Activity> activityWeakReference = new WeakReference<Activity>(activity);
        AnimatorBuilder animatorBuilder = AnimatorBuilder.newInstance(activity);
        return new Part4TransitionController(activityWeakReference, animatorBuilder);
    }

    protected Part4TransitionController(WeakReference<Activity> activityWeakReference, @NonNull AnimatorBuilder animatorBuilder) {
        super(activityWeakReference, animatorBuilder);
    }

    @Override
    protected void enterInputMode(Activity mainActivity) {
        createTransitionAnimator(mainActivity);
        mainActivity.setContentView(R.layout.activity_part4_input);
    }

    @Override
    protected void exitInputMode(Activity mainActivity) {
        createTransitionAnimator(mainActivity);
        mainActivity.setContentView(R.layout.activity_part4);

    }

    private void createTransitionAnimator(Activity mainActivity) {
        ViewGroup parent = (ViewGroup) mainActivity.findViewById(android.R.id.content);
        TransitionAnimator.begin(parent, VIEW_IDS);
    }
}
