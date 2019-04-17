package me.khrystal.gmatorm.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.greenrobot.greendao.database.Database;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/4/16
 * update time:
 * email: 723526676@qq.com
 */
public class AppDevOpenHelper extends DaoMaster.DevOpenHelper {

    public AppDevOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
        // 哪张表结构需要升级 就将对应的Dao传递给MigrationHelper调用用
//        MigrationHelper.getInstance().migrate(db, );
    }
}
