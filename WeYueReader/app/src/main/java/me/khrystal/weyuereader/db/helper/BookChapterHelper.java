package me.khrystal.weyuereader.db.helper;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import me.khrystal.weyuereader.db.entity.BookChapterBean;
import me.khrystal.weyuereader.db.gen.BookChapterBeanDao;
import me.khrystal.weyuereader.db.gen.DaoSession;

/**
 * usage: 书籍目录数据库操作
 * author: kHRYSTAL
 * create time: 18/4/23
 * update time:
 * email: 723526676@qq.com
 */

public class BookChapterHelper {
    private static volatile BookChapterHelper sInstance;
    private static DaoSession daoSession;
    private static BookChapterBeanDao bookChapterBeanDao;

    public static BookChapterHelper getsInstance() {
        if (sInstance == null) {
            synchronized (BookChapterHelper.class) {
                if (sInstance == null) {
                    sInstance = new BookChapterHelper();
                    daoSession = DaoDbHelper.getsInstance().getSession();
                    bookChapterBeanDao = daoSession.getBookChapterBeanDao();
                }
            }
        }
        return sInstance;
    }

    /**
     * 保存书籍目录
     * @param bookChapterBeans
     */
    public void saveBookChaptersWithAsync(List<BookChapterBean> bookChapterBeans) {
        daoSession.startAsyncSession()
                .runInTx(() -> {
                    daoSession.getBookChapterBeanDao()
                            .insertOrReplaceInTx(bookChapterBeans);
                });
    }

    /**
     * 删除书籍目录
     * @param bookId
     */
    public void removeBookChapters(String bookId) {
        bookChapterBeanDao.queryBuilder().where(BookChapterBeanDao.Properties.BookId.eq(bookId))
                .buildDelete()
                .executeDeleteWithoutDetachingEntities();
    }

    /**
     * 异步Rx查询书籍目录
     * @param bookId
     * @return
     */
    public Observable<List<BookChapterBean>> findBookChaptersInRx(String bookId) {
        return Observable.create(new ObservableOnSubscribe<List<BookChapterBean>>() {
            @Override
            public void subscribe(ObservableEmitter<List<BookChapterBean>> e) throws Exception {
                List<BookChapterBean> chapterBeans = daoSession.getBookChapterBeanDao()
                        .queryBuilder()
                        .where(BookChapterBeanDao.Properties.BookId.eq(bookId))
                        .list();
                e.onNext(chapterBeans);
            }
        });
    }
}
