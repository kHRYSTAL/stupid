package me.khrystal.thread;

import android.support.annotation.NonNull;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import me.khrystal.thread.callback.GlobalCallback;
import me.khrystal.thread.callback.Response;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/3/5
 * update time:
 * email: 723526676@qq.com
 */
public class QuietPool implements QuietThreadExecutor {

    private ExecutorService mThreadPool;
    private TaskUtils mTaskUtils;
    private GlobalCallback mGlobalCallback;

    public QuietPool(Builder builder) {
        mThreadPool = builder.mThreadPool;
        mTaskUtils = TaskUtils.get();
    }

    @Override
    public void execute(@NonNull Runnable command) {
        mThreadPool.execute(command);
    }

    @Override
    public void delay(@NonNull Runnable command, long delay) {
        mTaskUtils.schedule(command, delay);
    }

    @Override
    public void delay(@NonNull Runnable command, long delay, TimeUnit unit) {
        mTaskUtils.schedule(command, delay, unit);
    }

    @Override
    public void scheduled(@NonNull Runnable command, long initialDelay, long delay) {
        mTaskUtils.scheduleWithFixedDelay(command, initialDelay, delay);
    }

    @Override
    public void scheduled(@NonNull Runnable command, long initialDelay, long delay, TimeUnit unit) {
        mTaskUtils.scheduleWithFixedDelay(command, initialDelay, delay, unit);
    }

    @Override
    public void setGlobalCallback(GlobalCallback callback) {
        mGlobalCallback = callback;
    }

    @Override
    public void awaitTermination(long timeout, TimeUnit timeUnit) throws InterruptedException {
        mTaskUtils.awaitTermination(timeout, timeUnit);
    }

    @Override
    public <T> Future<T> submit(QuietCallable<T> task) {
        final CallableWrapper<T> callableWrapper = new CallableWrapper<>(task.mName, task, mGlobalCallback);
        return mThreadPool.submit(callableWrapper);
    }

    @Override
    public Future<?> submit(Runnable runnable) {
        return mThreadPool.submit(runnable);
    }

    @Override
    public <T> Future<T> async(QuietCallable<T> task, Response<T> response) {
        final CallableWrapper<T> callableWrapper = new CallableWrapper<>(task.mName, task, mGlobalCallback);
        return mTaskUtils.async(callableWrapper, response, false);
    }

    @Override
    public <T> Future<T> async(QuietCallable<T> task, Response<T> response, boolean isMainUICallback) {
        final CallableWrapper<T> callableWrapper = new CallableWrapper<>(task.mName, task, mGlobalCallback);
        return mTaskUtils.async(callableWrapper, response, isMainUICallback);
    }

    @Override
    public void shutdown() {
        mTaskUtils.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        return mTaskUtils.shutdownNow();
    }

    @Override
    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
        return mTaskUtils.schedule(callable, delay, unit);
    }

    public static class Builder {

        private ExecutorService mThreadPool;

        public Builder() {
        }

        public Builder createCached() {
            mThreadPool = Executors.newCachedThreadPool();
            return this;
        }

        public Builder createFixed(int sThread) {
            mThreadPool = Executors.newFixedThreadPool(sThread);
            return this;
        }

        public Builder createFixed(int sThread, ThreadFactory factory) {
            mThreadPool = Executors.newScheduledThreadPool(sThread, factory);
            return this;
        }

        public Builder createScheduled(int coreThreadSize) {
            mThreadPool = Executors.newScheduledThreadPool(coreThreadSize);
            return this;
        }

        public Builder createScheduled(int coreThreadSize, ThreadFactory factory) {
            mThreadPool = Executors.newScheduledThreadPool(coreThreadSize, factory);
            return this;
        }

        public Builder createSingle() {
            mThreadPool = Executors.newSingleThreadExecutor();
            return this;
        }

        public Builder createSingle(ThreadFactory factory) {
            mThreadPool = Executors.newSingleThreadExecutor(factory);
            return this;
        }

        public QuietPool build() {
            return new QuietPool(this);
        }
    }
}
