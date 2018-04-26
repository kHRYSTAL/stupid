package me.khrystal.weyuereader.view.activity;

import me.khrystal.weyuereader.model.AppUpdateBean;
import me.khrystal.weyuereader.view.base.IBaseLoadView;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/4/26
 * update time:
 * email: 723526676@qq.com
 */

public interface ISetting extends IBaseLoadView {
    void appUpdate(AppUpdateBean appUpdateBean);
}
