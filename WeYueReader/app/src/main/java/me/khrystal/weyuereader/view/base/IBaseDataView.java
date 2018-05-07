package me.khrystal.weyuereader.view.base;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/4/26
 * update time:
 * email: 723526676@qq.com
 */

public interface IBaseDataView extends IBaseLoadView {
    void emptyData();
    void errorData(String error);
    void NetWorkError();
}
