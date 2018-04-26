package me.khrystal.weyuereader.view.activity;

import me.khrystal.weyuereader.db.entity.BookChapterBean;
import me.khrystal.weyuereader.view.base.IBaseLoadView;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/4/26
 * update time:
 * email: 723526676@qq.com
 */

public interface IBookChapters extends IBaseLoadView {
    void bookChapters(BookChapterBean bookChapterBean);
    void finishChapters();
    void errorChapters();
}
