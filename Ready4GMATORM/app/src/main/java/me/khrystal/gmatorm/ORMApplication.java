package me.khrystal.gmatorm;

import android.app.Application;

import me.khrystal.gmatorm.greendao.DaoManager;
import me.khrystal.gmatorm.greendao.ExternalContext;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/4/16
 * update time:
 * email: 723526676@qq.com
 */
public class ORMApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initGreenDao();
    }

    private void initGreenDao() {
        // 创建DBMaster实例 ExternalContext用于修改在手机存储卡上创建或读取
        DaoManager.getInstance().init(new ExternalContext(this));
    }
}
