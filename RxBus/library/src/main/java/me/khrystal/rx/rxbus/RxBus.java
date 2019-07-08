package me.khrystal.rx.rxbus;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/7/8
 * update time:
 * email: 723526676@qq.com
 */
public class RxBus {

    private static volatile RxBus defaultInstance;
    private final Subject<Object, Object> bus;
    private final Map<Class<?>, Object> stickyEventMap;

    // PublishSubject 只会把在订阅发生的时间点之后来自原始Observable的数据发射给观察者
    public RxBus() {
        bus = new SerializedSubject<>(PublishSubject.create());
        stickyEventMap = new ConcurrentHashMap<>();
    }

    // singleton
    public static RxBus getDefault() {
        return Holder.INSTANCE;
    }

    // 发送一个新的事件
    public void post(Object o) {
        bus.onNext(o);
    }

    public void postDelay(final Object o, long timeMillions) {
        Observable.timer(timeMillions, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        bus.onNext(o);
                    }
                });
    }

    // 发送粘性事件
    public void postSticky(Object o) {
        synchronized (stickyEventMap) {
            stickyEventMap.put(o.getClass(), o);
        }
        post(o);
    }

    // 接受事件
    public <T> Observable<T> tObservable(final Class<T> eventType) {
//        return bus.ofType(eventType);
        return bus.filter(new Func1<Object, Boolean>() {
            @Override
            public Boolean call(Object o) {
                return eventType.isInstance(o);
            }
        }).cast(eventType);
    }

    // 接受粘性事件
    public <T> Observable<T> tObservableSticky(final Class<T> eventType) {
        synchronized (stickyEventMap) {
            Observable<T> observable = bus.ofType(eventType);
            final Object event = stickyEventMap.get(eventType);
            if (null != event) {
                return observable.mergeWith(Observable.create(new Observable.OnSubscribe<T>() {
                    @Override
                    public void call(Subscriber<? super T> subscriber) {
                        subscriber.onNext(eventType.cast(event));
                    }
                }));
            } else {
                return observable;
            }
        }
    }

    // 获取Sticky事件
    public <T> T getStickyEvent(Class<T> eventType) {
        synchronized (stickyEventMap) {
            return eventType.cast(stickyEventMap.get(eventType));
        }
    }

    // 移除指定eventType的Sticky事件
    public <T> T removeStickyEvent(Class<T> eventType) {
        synchronized (stickyEventMap) {
            return eventType.cast(stickyEventMap.remove(eventType));
        }
    }

    // 清空所有Sticky事件
    public void removeAllStickyEvents() {
        synchronized (stickyEventMap) {
            stickyEventMap.clear();
        }
    }

    // 是否有订阅者
    public boolean hasObservers() {
        return bus.hasObservers();
    }

    // 重置singleton
    public void reset() {
        defaultInstance = null;
    }

    private static final class Holder {
        private static RxBus INSTANCE = new RxBus();
    }
}
