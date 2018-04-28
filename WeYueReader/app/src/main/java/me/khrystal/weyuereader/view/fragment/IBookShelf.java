package me.khrystal.weyuereader.view.fragment;

import java.util.List;

import me.khrystal.weyuereader.db.entity.CollBookBean;
import me.khrystal.weyuereader.view.base.IBaseLoadView;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/4/28
 * update time:
 * email: 723526676@qq.com
 */

public interface IBookShelf extends IBaseLoadView {
    void bookShelfInfo(List<CollBookBean> beans);

    void bookInfo(CollBookBean bean);

    void deleteSuccess();
}
