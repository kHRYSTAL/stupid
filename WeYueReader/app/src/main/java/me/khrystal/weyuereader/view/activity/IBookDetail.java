package me.khrystal.weyuereader.view.activity;

import me.khrystal.weyuereader.model.BookBean;
import me.khrystal.weyuereader.view.base.IBaseLoadView;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/4/26
 * update time:
 * email: 723526676@qq.com
 */

public interface IBookDetail extends IBaseLoadView {
    void addBookCallback();
    void getBookInfo(BookBean bookBean);
}
