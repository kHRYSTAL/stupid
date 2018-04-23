package me.khrystal.weyuereader.db.helper;

import java.util.List;

import me.khrystal.weyuereader.db.entity.DownloadTaskBean;
import me.khrystal.weyuereader.db.gen.DaoSession;
import me.khrystal.weyuereader.db.gen.DownloadTaskBeanDao;

/**
 * usage: 书籍缓存数据库工具类
 * author: kHRYSTAL
 * create time: 18/4/23
 * update time:
 * email: 723526676@qq.com
 */

public class BookDownloadHelper {
    private static volatile BookDownloadHelper sInstance;
    private static DaoSession daoSession;
    private static DownloadTaskBeanDao downloadTaskBeanDao;

    public static BookDownloadHelper getsInstance() {
        if (sInstance == null) {
            synchronized (BookChapterHelper.class) {
                if (sInstance == null) {
                    sInstance = new BookDownloadHelper();
                    daoSession = DaoDbHelper.getsInstance().getSession();
                    downloadTaskBeanDao = daoSession.getDownloadTaskBeanDao();
                }
            }
        }
        return sInstance;
    }

    /**
     * 获取缓存列表所有数据
     * @return
     */
    public List<DownloadTaskBean> getBookDownloadList() {
        return downloadTaskBeanDao.loadAll();
    }

    public void saveBookDownload(DownloadTaskBean taskBean) {
        BookChapterHelper.getsInstance().saveBookChaptersWithAsync(taskBean.getBookChapters());
        downloadTaskBeanDao.insertOrReplace(taskBean);
    }

    public void removeDownloadTask(String bookId) {
        downloadTaskBeanDao.queryBuilder()
                .where(DownloadTaskBeanDao.Properties.BookId.eq(bookId))
                .buildDelete()
                .executeDeleteWithoutDetachingEntities();
    }
}
