package me.khrystal.util.stetho;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/6/11
 * update time:
 * email: 723526676@qq.com
 */

public class ExceptionUtil {

    @SuppressWarnings("unchecked")
    public static <T extends Throwable> void propagateIfInstanceOf(Throwable t, Class<T> type)
            throws T {
        if (type.isInstance(t)) {
            throw (T)t;
        }
    }

    public static RuntimeException propagate(Throwable t) {
        propagateIfInstanceOf(t, Error.class);
        propagateIfInstanceOf(t, RuntimeException.class);
        throw new RuntimeException(t);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Throwable> void sneakyThrow(Throwable t) throws T {
        throw (T)t;
    }
}
