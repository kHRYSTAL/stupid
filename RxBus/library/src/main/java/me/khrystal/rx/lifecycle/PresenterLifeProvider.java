package me.khrystal.rx.lifecycle;

import android.support.annotation.NonNull;

import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.RxLifecycle;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/7/8
 * update time:
 * email: 723526676@qq.com
 */
public class PresenterLifeProvider implements IPresenterLifecycleProvider {

    private final BehaviorSubject<PresenterEvent> lifecycleSubject = BehaviorSubject.create();

    @Override
    public void onNext(PresenterEvent event) {
        lifecycleSubject.onNext(event);
    }

    @NonNull
    @Override
    public Observable<PresenterEvent> lifecycle() {
        return lifecycleSubject.asObservable();
    }

    @NonNull
    @Override
    public <T> LifecycleTransformer<T> bindUntilEvent(@NonNull PresenterEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }
}
