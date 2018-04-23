package me.khrystal.weyuereader.db.helper;

import me.khrystal.weyuereader.db.entity.BookRecordBean;
import me.khrystal.weyuereader.db.gen.BookRecordBeanDao;
import me.khrystal.weyuereader.db.gen.DaoSession;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/4/23
 * update time:
 * email: 723526676@qq.com
 */

public class BookRecordHelper {
    private static volatile BookRecordHelper sInstance;
    private static DaoSession daoSession;
    private static BookRecordBeanDao bookRecordBeanDao;

    public static BookRecordHelper getsInstance() {
        if (sInstance == null) {
            synchronized (BookRecordHelper.class) {
                if (sInstance == null) {
                    sInstance = new BookRecordHelper();
                    daoSession = DaoDbHelper.getsInstance().getSession();
                    bookRecordBeanDao = daoSession.getBookRecordBeanDao();
                }
            }
        }
        return sInstance;
    }

    public void saveRecordBook(BookRecordBean collBookBean) {
        bookRecordBeanDao.insertOrReplace(collBookBean);
    }

    public void removeBook(String bookId) {
        bookRecordBeanDao.queryBuilder()
                .where(BookRecordBeanDao.Properties.BookId.eq(bookId))
                .buildDelete()
                .executeDeleteWithoutDetachingEntities();
    }

    public BookRecordBean findBookRecordById(String bookId) {
        return bookRecordBeanDao.queryBuilder()
                .where(BookRecordBeanDao.Properties.BookId.eq(bookId)).unique();
    }
}
