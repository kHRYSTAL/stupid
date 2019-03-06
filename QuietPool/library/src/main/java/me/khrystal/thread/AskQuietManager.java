package me.khrystal.thread;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/3/6
 * update time:
 * email: 723526676@qq.com
 */
public class AskQuietManager {

    private static QuietPool sNetwork;

    private static QuietPool sIO;

    private static QuietPool sCache;

    private static QuietPool sSingle;

    static {
        sNetwork = new QuietPool.Builder()
                .createFixed(10)
                .build();

        sIO = new QuietPool.Builder()
                .createFixed(5)
                .build();

        sCache = new QuietPool.Builder()
                .createCached()
                .build();

        sSingle = new QuietPool.Builder()
                .createSingle()
                .build();
    }

    public static QuietPool getNetwork() {
        return sNetwork;
    }

    public static QuietPool getIO() {
        return sIO;
    }

    public static QuietPool getCache() {
        return sCache;
    }

    public static QuietPool getSingle() {
        return sSingle;
    }
}
