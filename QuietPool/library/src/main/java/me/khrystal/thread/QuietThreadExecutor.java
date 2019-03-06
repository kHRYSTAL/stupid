package me.khrystal.thread;

import android.support.annotation.NonNull;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
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
public interface QuietThreadExecutor {

    /**
     * 立即执行一个任务
     * @param command
     */
    void execute(@NonNull Runnable command);

    /**
     * 延时执行一个任务
     * @param command
     * @param delay
     */
    void delay(@NonNull Runnable command, long delay);

    /**
     * 延时执行一个任务
     * @param command
     * @param delay
     * @param unit
     */
    void delay(@NonNull Runnable command, long delay, TimeUnit unit);

    /**
     * 延时执行一个周期任务
     * @param command
     * @param initialDelay 延迟时间
     * @param delay
     */
    void scheduled(@NonNull Runnable command, long initialDelay, long delay);

    /**
     * 延时执行一个周期任务
     * @param command
     * @param initialDelay
     * @param delay
     * @param unit
     */
    void scheduled(@NonNull Runnable command, long initialDelay, long delay, TimeUnit unit);

    void setGlobalCallback(GlobalCallback callback);

    void awaitTermination(long timeout, TimeUnit timeUnit) throws InterruptedException;

    /**
     * 同步执行一个task
     * @param task
     * @param <T>
     * @return
     */
    <T> Future<T> submit(QuietCallable<T> task);

    /**
     * 同步执行一个task
     * @param runnable
     * @return
     */
    Future<?> submit(Runnable runnable);

    /**
     * 异步执行一个任务
     * @param task
     * @param response 回调
     * @param <T>
     * @return
     */
    <T> Future<T> async(QuietCallable<T> task, Response<T> response);

    /**
     * 异步执行一个任务
     * @param task
     * @param response
     * @param isMainUICallback 是否在主线程回调
     * @param <T>
     * @return
     */
    <T> Future<T> async(QuietCallable<T> task, Response<T> response, boolean isMainUICallback);

    void shutdown();

    List<Runnable> shutdownNow();

    /**
     * 支持callable接口
     * @param callable
     * @param delay
     * @param unit
     * @param <V>
     * @return
     */
    <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit);
}
