package me.khrystal.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/3/5
 * update time:
 * email: 723526676@qq.com
 */
public abstract class QuietCallable<V> implements Callable<V> {

    private static AtomicInteger PID = new AtomicInteger(1);
    private static final String DEFAULT_THREAD_NAME = "Quick@" + PID.getAndIncrement();

    public final String mName;

    public QuietCallable() {
        this(DEFAULT_THREAD_NAME);
    }

    public QuietCallable(String name) {
        this.mName = name;
    }

    @Override
    public V call() throws Exception {
        final Thread thread = Thread.currentThread();
        thread.setName(mName);
        return qCall();
    }

    public abstract V qCall() throws Exception;
}
