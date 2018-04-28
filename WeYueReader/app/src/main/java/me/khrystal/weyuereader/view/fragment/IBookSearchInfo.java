package me.khrystal.weyuereader.view.fragment;

import java.util.List;

import me.khrystal.weyuereader.model.BookBean;
import me.khrystal.weyuereader.view.base.IBaseLoadView;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/4/28
 * update time:
 * email: 723526676@qq.com
 */

public interface IBookSearchInfo extends IBaseLoadView {
    void getSearchBooks(List<BookBean> bookBeans);
}
