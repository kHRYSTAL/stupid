package me.khrystal.widget.supertextview.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/6/12
 * update time:
 * email: 723526676@qq.com
 */

public class ThreadPool {
    private ExecutorService threadPool;

    private ThreadPool() {
        if (threadPool == null) {
            threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
        }
    }

    private static final class Holder {
        private static final ThreadPool instance = new ThreadPool();
    }

    public static final ThreadPool get() {
        return Holder.instance;
    }

    public static void run(Runnable runnable) {
        ThreadPool.get().threadPool.execute(runnable);
    }

    public static <T> Future<T> submit(Callable<T> call) {
        return ThreadPool.get().threadPool.submit(call);
    }

    /**
     * 关闭线程池, 这将导致该线程池立即停止接受新的线程请求,但已经存在的任务仍然会执行, 直到完成
     */
    public synchronized void shutDown() {
        ThreadPool.get().threadPool.shutdownNow();
    }
}
