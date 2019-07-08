package me.khrystal.rx.lifecycle;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import com.trello.rxlifecycle.LifecycleTransformer;

import rx.Observable;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/7/8
 * update time:
 * email: 723526676@qq.com
 */
public interface IPresenterLifecycleProvider {

    void onNext(PresenterEvent event);

    @NonNull
    @CheckResult
    Observable<PresenterEvent> lifecycle();

    @NonNull
    @CheckResult
    <T> LifecycleTransformer<T> bindUntilEvent(@NonNull PresenterEvent event);
}
