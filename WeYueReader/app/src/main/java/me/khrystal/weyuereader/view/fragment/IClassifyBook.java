package me.khrystal.weyuereader.view.fragment;

import me.khrystal.weyuereader.model.BookClassifyBean;
import me.khrystal.weyuereader.view.base.IBaseDataView;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/4/28
 * update time:
 * email: 723526676@qq.com
 */

public interface IClassifyBook extends IBaseDataView {
    void getBookClassify(BookClassifyBean bookClassifyBean);
}
