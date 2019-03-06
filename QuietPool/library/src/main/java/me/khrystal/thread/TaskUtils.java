package me.khrystal.thread;

import android.os.Handler;
import android.os.Looper;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import me.khrystal.thread.callback.Response;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/3/5
 * update time:
 * email: 723526676@qq.com
 */
public class TaskUtils {

    private static final class Holder {
        private static final TaskUtils INSTANCE = new TaskUtils();
    }

    public static TaskUtils get() {
        return Holder.INSTANCE;
    }

    private ScheduledExecutorService mScheduledExecutorService;
    private Handler mHandler;

    private TaskUtils() {
        mScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        mHandler = new Handler(Looper.getMainLooper());
    }

    public void schedule(Runnable command, long delay, TimeUnit unit) {
        mScheduledExecutorService.schedule(command, delay, unit);
    }

    public void schedule(Runnable command, long delay) {
        mScheduledExecutorService.schedule(command, delay, TimeUnit.MILLISECONDS);
    }

    public void awaitTermination(long timeout, TimeUnit timeUnit) throws InterruptedException {
        mScheduledExecutorService.awaitTermination(timeout, timeUnit);
    }

    public void shutdown() {
        mScheduledExecutorService.shutdown();
    }

    public <T> Future<T> async(Callable<T> task, final Response<T> response, final boolean isMainUICallback) {
        final Future<T> future = mScheduledExecutorService.submit(task);
        mScheduledExecutorService.execute(() -> {
            try {
                final T t = future.get();
                if (response != null) {
                    if (isMainUICallback) {
                        mHandler.post(() -> response.onSuccess(t));
                    } else {
                        response.onSuccess(t);
                    }
                }
            } catch (final Throwable e) {
                if (response != null) {
                    if (isMainUICallback) {
                        mHandler.post(() -> {
                            response.onError(e);
                        });
                    } else {
                        response.onError(e);
                    }
                }
            }
        });
        return future;
    }

    public List<Runnable> shutdownNow() {
        return mScheduledExecutorService.shutdownNow();
    }

    public <V> ScheduledFuture<V> schedule(Callable<V> command, long delay, TimeUnit unit) {
        return mScheduledExecutorService.schedule(command, delay, unit);
    }

    public void scheduleWithFixedDelay(Runnable command, long initialDelay, long delay) {
        mScheduledExecutorService.scheduleWithFixedDelay(command, initialDelay, delay, TimeUnit.MILLISECONDS);
    }

    public void scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        mScheduledExecutorService.scheduleWithFixedDelay(command, initialDelay, delay, unit);
    }
}
