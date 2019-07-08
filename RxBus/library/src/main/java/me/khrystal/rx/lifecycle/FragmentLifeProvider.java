package me.khrystal.rx.lifecycle;

import android.support.annotation.NonNull;

import com.trello.rxlifecycle.FragmentEvent;
import com.trello.rxlifecycle.FragmentLifecycleProvider;
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
public class FragmentLifeProvider implements FragmentLifecycleProvider {

    private final BehaviorSubject<FragmentEvent> lifecyceSubject = BehaviorSubject.create();

    @NonNull
    @Override
    public Observable<FragmentEvent> lifecycle() {
        return lifecyceSubject.asObservable();
    }

    @NonNull
    @Override
    public <T> LifecycleTransformer<T> bindUntilEvent(@NonNull FragmentEvent event) {
        return RxLifecycle.bindUntilEvent(lifecyceSubject, event);
    }

    @NonNull
    @Override
    public <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycle.bindFragment(lifecyceSubject);
    }

    public void onNext(FragmentEvent event) {
        lifecyceSubject.onNext(event);
    }
}
