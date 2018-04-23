package me.khrystal.weyuereader.db.helper;

import android.database.sqlite.SQLiteDatabase;

import me.khrystal.weyuereader.WYApplication;
import me.khrystal.weyuereader.db.gen.DaoMaster;
import me.khrystal.weyuereader.db.gen.DaoSession;

/**
 * usage: 数据库base 工具类
 * author: kHRYSTAL
 * create time: 18/4/23
 * update time:
 * email: 723526676@qq.com
 */

public class DaoDbHelper {

    private static final String DB_NAME = "WeYueReader_DB";
    private static volatile DaoDbHelper sInstance;
    private SQLiteDatabase mDb;
    private DaoMaster mDaoMaster;
    private DaoSession mSession;

    private DaoDbHelper() {
        DaoMaster.DevOpenHelper openHelper = new DaoMaster.DevOpenHelper(WYApplication.getAppContext(), DB_NAME, null);
        // 获取数据库
        mDb = openHelper.getWritableDatabase();
        // 封装数据库表的创建、更新、删除
        mDaoMaster = new DaoMaster(mDb);
        // 对表操作的对象
        mSession = mDaoMaster.newSession();
    }

    public static DaoDbHelper getsInstance() {
        if (sInstance == null) {
            synchronized (DaoDbHelper.class) {
                if (sInstance == null) {
                    sInstance = new DaoDbHelper();
                }
            }
        }
        return sInstance;
    }

    public DaoSession getSession() {
        return mSession;
    }

    public SQLiteDatabase getDatabase() {
        return mDb;
    }

    public DaoSession getNewSession() {
        return mDaoMaster.newSession();
    }
}
