package me.khrystal.gmatorm.greendao;

import android.content.Context;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/4/16
 * update time:
 * email: 723526676@qq.com
 */
public class DaoManager {

    private Context mContext;

    private static final String DB_NAME = "app.db";

    private volatile static DaoManager mInstance;

    private static DaoMaster mDaoMaster;

    private static AppDevOpenHelper mHelper;

    // Dao管理对象
    private static DaoSession mDaoSession;

    public static DaoManager getInstance() {
        if (mInstance == null) {
            synchronized (DaoManager.class) {
                if (mInstance == null) {
                    synchronized (DaoManager.class) {
                        mInstance = new DaoManager();
                    }
                }
            }
        }
        return mInstance;
    }

    /**
     * 注意 改方法建议放置到Application中进行初始化, 因为{@link DaoManager#getDaoMaster()}中,
     * AppDevOpenHelper 会在表结构升级时进行更新
     *
     * @param context
     */
    public void init(Context context) {
        mContext = context;
        // 全局初始化master与session
        getDaoSession();
    }

    public DaoMaster getDaoMaster() {
        if (mDaoMaster == null) {
            mHelper = new AppDevOpenHelper(mContext, DB_NAME, null);
            mDaoMaster = new DaoMaster(mHelper.getWritableDatabase());
        }
        return mDaoMaster;
    }

    public DaoSession getDaoSession() {
        if (mDaoSession == null) {
            if (mDaoMaster == null) {
                mDaoMaster = getDaoMaster();
            }
            mDaoSession = mDaoMaster.newSession();
        }
        return mDaoSession;
    }

    public void closeConnection() {
        closeHelper();
        closeDaoSession();
    }

    public void closeHelper() {
        if (mHelper != null) {
            mHelper.close();
            mHelper = null;
        }
    }

    public void closeDaoSession() {
        if (mDaoSession != null) {
            mDaoSession.clear();
            mDaoSession = null;
        }
    }
}
