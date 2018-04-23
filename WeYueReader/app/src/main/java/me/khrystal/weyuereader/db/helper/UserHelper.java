package me.khrystal.weyuereader.db.helper;

import me.khrystal.weyuereader.db.entity.UserBean;
import me.khrystal.weyuereader.db.gen.DaoSession;
import me.khrystal.weyuereader.db.gen.UserBeanDao;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/4/23
 * update time:
 * email: 723526676@qq.com
 */

public class UserHelper {
    private static volatile UserHelper sInstance;
    private static DaoSession daoSession;
    private static UserBeanDao userBeanDao;

    public static UserHelper getsInstance() {
        if (sInstance == null) {
            synchronized (UserHelper.class) {
                if (sInstance == null) {
                    sInstance = new UserHelper();
                    daoSession = DaoDbHelper.getsInstance().getSession();
                    userBeanDao = daoSession.getUserBeanDao();
                }
            }
        }
        return sInstance;
    }

    public void saveUser(UserBean userBean) {
        userBeanDao.insertOrReplace(userBean);
    }

    public void updateUser(UserBean userBean) {
        userBeanDao.update(userBean);
    }

    public void removeUser() {
        userBeanDao.deleteAll();
    }

    public UserBean findUserByName(String username) {
        UserBean userBean = userBeanDao.queryBuilder().where(UserBeanDao.Properties.Name.eq(username)).unique();
        return userBean != null ? userBean : null;
    }
}
