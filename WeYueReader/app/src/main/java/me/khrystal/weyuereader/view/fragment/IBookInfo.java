package me.khrystal.weyuereader.view.fragment;

import java.util.List;

import me.khrystal.weyuereader.model.BookBean;
import me.khrystal.weyuereader.view.base.IBaseDataView;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/4/28
 * update time:
 * email: 723526676@qq.com
 */

public interface IBookInfo extends IBaseDataView {
    void getBooks(List<BookBean> bookBeans, boolean isLoadMore);
}
