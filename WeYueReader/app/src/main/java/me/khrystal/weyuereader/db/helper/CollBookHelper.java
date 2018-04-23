package me.khrystal.weyuereader.db.helper;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import me.khrystal.weyuereader.db.entity.CollBookBean;
import me.khrystal.weyuereader.db.gen.CollBookBeanDao;
import me.khrystal.weyuereader.db.gen.DaoSession;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/4/23
 * update time:
 * email: 723526676@qq.com
 */

public class CollBookHelper {
    private static volatile CollBookHelper sInstance;
    private static DaoSession daoSession;
    private static CollBookBeanDao collBookBeanDao;

    public static CollBookHelper getsInstance() {
        if (sInstance == null) {
            synchronized (CollBookHelper.class) {
                if (sInstance == null) {
                    sInstance = new CollBookHelper();
                    daoSession = DaoDbHelper.getsInstance().getSession();
                    collBookBeanDao = daoSession.getCollBookBeanDao();
                }
            }
        }
        return sInstance;
    }

    public void saveBook(CollBookBean collBookBean) {
        collBookBeanDao.insertOrReplace(collBookBean);
    }

    public void saveBooks(List<CollBookBean> collBookBeans) {
        collBookBeanDao.insertOrReplaceInTx(collBookBeans);
    }

    public void saveBookWithAsync(CollBookBean collBookBean) {
        daoSession.startAsyncSession().runInTx(() -> {
            if (collBookBean.getBookChapters() != null) {
                daoSession.getBookChapterBeanDao()
                        .insertOrReplaceInTx(collBookBean.getBookChapters());
            }
            // 存储collbook(确保先后顺序 否则出错)
            collBookBeanDao.insertOrReplace(collBookBean);
        });
    }

    public void saveBooksWithAsync(List<CollBookBean> collBookBeans) {
        daoSession.startAsyncSession()
                .runInTx(() -> {
                    for (CollBookBean bean : collBookBeans) {
                        if (bean.getBookChapters() != null) {
                            //存储BookChapterBean(需要修改，如果存在id相同的则无视)
                            daoSession.getBookChapterBeanDao()
                                    .insertOrReplaceInTx(bean.getBookChapters());
                        }
                    }
                    // 存储collbook(确保先后顺序 否则出错)
                    collBookBeanDao.insertOrReplaceInTx(collBookBeans);
                });
    }

    public Observable<String> removeBookInRx(CollBookBean collBookBean) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                //查看文本中是否存在删除的数据 TODO
//                FileUtils.deleteFile(Constant.BOOK_CACHE_PATH + collBookBean.get_id());
                //删除任务
                BookDownloadHelper.getsInstance().removeDownloadTask(collBookBean.get_id());
                //删除目录
                BookChapterHelper.getsInstance().removeBookChapters(collBookBean.get_id());
                //删除CollBook
                collBookBeanDao.delete(collBookBean);
                e.onNext("删除成功");
            }
        });
    }

    public void removeAllBook() {
        for (CollBookBean collBookBean : findAllBooks()) {
            removeBookInRx(collBookBean);
        }
    }

    /**
     * 查询一本书籍
     */
    public CollBookBean findBookById(String id) {
        CollBookBean bookBean = collBookBeanDao.queryBuilder().where(CollBookBeanDao.Properties._id.eq(id))
                .unique();
        return bookBean;
    }


    /**
     * 查询所有书籍
     */
    public List<CollBookBean> findAllBooks() {
        return collBookBeanDao
                .queryBuilder()
                .orderDesc(CollBookBeanDao.Properties.LastRead)
                .list();
    }

}
