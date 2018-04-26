package me.khrystal.weyuereader.view.activity;

import me.khrystal.weyuereader.db.entity.UserBean;
import me.khrystal.weyuereader.view.base.IBaseLoadView;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/4/26
 * update time:
 * email: 723526676@qq.com
 */

public interface IUserInfo extends IBaseLoadView {
    void uploadSuccess(String imageUrl);
    void userInfo(UserBean userBean);
}
