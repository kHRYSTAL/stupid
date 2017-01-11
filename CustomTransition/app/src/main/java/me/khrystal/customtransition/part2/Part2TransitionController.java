package me.khrystal.customtransition.part2;

import android.app.Activity;
import android.support.annotation.NonNull;

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

public class Part2TransitionController extends TransitionController {

    public static TransitionController newInstance(Activity activity) {
        WeakReference<Activity> activityWeakReference = new WeakReference<Activity>(activity);
        AnimatorBuilder animatorBuilder = AnimatorBuilder.newInstance(activity);
        return new Part2TransitionController(activityWeakReference, animatorBuilder);
    }

    Part2TransitionController(WeakReference<Activity> activityWeakReference, @NonNull AnimatorBuilder animatorBuilder) {
        super(activityWeakReference, animatorBuilder);
    }

    @Override
    protected void enterInputMode(Activity mainActivity) {
        mainActivity.setContentView(R.layout.activity_part2_input);
    }

    @Override
    protected void exitInputMode(Activity mainActivity) {
        mainActivity.setContentView(R.layout.activity_part2);
    }
}
