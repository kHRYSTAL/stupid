package me.khrystal.weyuereader.utils.rxhelper;


import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/4/24
 * update time:
 * email: 723526676@qq.com
 */

public class RxBus {
    private static volatile RxBus sInstance;
    private final PublishSubject<Object> mEventBus = PublishSubject.create();

    public static RxBus getsInstance() {
        if (sInstance == null) {
            synchronized (RxBus.class) {
                if (sInstance == null) {
                    sInstance = new RxBus();
                }
            }
        }
        return sInstance;
    }

    /**
     * 发送事件
     *
     * @param event
     */
    public void post(Object event) {
        mEventBus.onNext(event);
    }

    public void post(int code, Object event) {
        Message msg = new Message(code, event);
        mEventBus.onNext(msg);
    }

    /**
     * 返回event管理者, 进行对事件的接受
     *
     * @return
     */
    public Observable toObservable() {
        return mEventBus;
    }

    /**
     * 保证接收指定的类型
     */
    public <T> Observable<T> toObservable(Class<T> cls) {
        // ofType 起到过滤作用, 确定接受的类型
        return mEventBus.ofType(cls);
    }

    /**
     * 保证接收到指定的类型
     */
    public <T> Observable<T> tObservable(int code, Class<T> cls) {
        return mEventBus.ofType(Message.class)
                .filter(msg -> msg.code == code && cls.isInstance(msg.event))
                .map(msg -> (T) msg.event);
    }


    class Message {
        int code;
        Object event;

        public Message(int code, Object event) {
            this.code = code;
            this.event = event;
        }
    }
}
