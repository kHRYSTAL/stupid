package me.khrystal.customtransition.part1;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;

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

public class Part1TransitionController extends TransitionController {

    public static TransitionController newInstance(Activity activity) {
        WeakReference<Activity> mainActivityWeakReference = new WeakReference<Activity>(activity);
        AnimatorBuilder animatorBuilder = AnimatorBuilder.newInstance(activity);
        return new Part1TransitionController(mainActivityWeakReference, animatorBuilder);
    }

    Part1TransitionController(WeakReference<Activity> activityWeakReference, @NonNull AnimatorBuilder animatorBuilder) {
        super(activityWeakReference, animatorBuilder);
    }

    @Override
    protected void enterInputMode(Activity mainActivity) {
        View inputView = mainActivity.findViewById(R.id.input_view);
        View inputDone = mainActivity.findViewById(R.id.input_done);
        View translation = mainActivity.findViewById(R.id.translation_panel);
        View toolbar = mainActivity.findViewById(R.id.toolbar);

        inputDone.setVisibility(View.VISIBLE);

        AnimatorSet animatorSet = new AnimatorSet();
        AnimatorBuilder animatorBuilder = getAnimatorBuilder();
        Animator moveInputView = animatorBuilder.buildTranslationYAnimator(inputView, 0, -toolbar.getHeight());
        Animator showInputDone = animatorBuilder.buildShowAnimator(inputDone);
        Animator hideTranslation = animatorBuilder.buildHideAnimator(translation);
        animatorSet.playTogether(moveInputView, showInputDone, hideTranslation);
        animatorSet.start();
    }

    @Override
    protected void exitInputMode(Activity mainActivity) {
        final View inputView = mainActivity.findViewById(R.id.input_view);
        View inputDone = mainActivity.findViewById(R.id.input_done);
        View translation = mainActivity.findViewById(R.id.translation_panel);
        View toolbar = mainActivity.findViewById(R.id.toolbar);

        AnimatorSet animatorSet = new AnimatorSet();
        AnimatorBuilder animatorBuilder = getAnimatorBuilder();
        Animator moveInputView = animatorBuilder.buildTranslationYAnimator(inputView, -toolbar.getHeight(), 0);
        Animator hideInputDone = animatorBuilder.buildHideAnimator(inputDone);
        Animator showTranslation = animatorBuilder.buildShowAnimator(translation);
        animatorSet.playTogether(moveInputView, hideInputDone, showTranslation);
        // 关闭软键盘
        animatorSet.addListener(new ImeCloseListener(inputDone));
        animatorSet.start();
    }
}
