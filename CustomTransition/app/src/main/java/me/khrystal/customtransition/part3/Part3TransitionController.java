package me.khrystal.customtransition.part3;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;
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

public class Part3TransitionController extends TransitionController {

    public static TransitionController newInstance(Activity activity) {
        WeakReference<Activity> activityWeakReference = new WeakReference<Activity>(activity);
        AnimatorBuilder animatorBuilder = AnimatorBuilder.newInstance(activity);
        return new Part3TransitionController(activityWeakReference, animatorBuilder);
    }

    protected Part3TransitionController(WeakReference<Activity> activityWeakReference, @NonNull AnimatorBuilder animatorBuilder) {
        super(activityWeakReference, animatorBuilder);
    }

    @Override
    protected void enterInputMode(Activity mainActivity) {
        createTransitionAnimator(mainActivity);
        mainActivity.setContentView(R.layout.activity_part2_input);
    }

    @Override
    protected void exitInputMode(Activity mainActivity) {
        createTransitionAnimator(mainActivity);
        mainActivity.setContentView(R.layout.activity_part2);
    }

    private void createTransitionAnimator(Activity activity) {
        ViewGroup parent = (ViewGroup) activity.findViewById(android.R.id.content);
        View inputView = parent.findViewById(R.id.input_view);
        View inputDone = parent.findViewById(R.id.input_done);
        View translation = parent.findViewById(R.id.translation);

        TransitionAnimator.begin(parent, inputView, inputDone, translation);
    }
}
