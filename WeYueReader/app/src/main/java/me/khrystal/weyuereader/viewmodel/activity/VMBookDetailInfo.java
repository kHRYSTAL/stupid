package me.khrystal.weyuereader.viewmodel.activity;

import android.content.Context;

import com.allen.library.RxHttpUtils;
import com.allen.library.interceptor.Transformer;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import me.khrystal.weyuereader.api.BookService;
import me.khrystal.weyuereader.api.UserService;
import me.khrystal.weyuereader.db.entity.BookChapterBean;
import me.khrystal.weyuereader.db.entity.CollBookBean;
import me.khrystal.weyuereader.db.helper.CollBookHelper;
import me.khrystal.weyuereader.model.BookBean;
import me.khrystal.weyuereader.model.BookChaptersBean;
import me.khrystal.weyuereader.model.DeleteBookBean;
import me.khrystal.weyuereader.utils.ToastUtils;
import me.khrystal.weyuereader.utils.rxhelper.RxObserver;
import me.khrystal.weyuereader.view.activity.IBookDetail;
import me.khrystal.weyuereader.viewmodel.BaseViewModel;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/4/28
 * update time:
 * email: 723526676@qq.com
 */

public class VMBookDetailInfo extends BaseViewModel {

    IBookDetail iBookDetail;

    public VMBookDetailInfo(Context mContext, IBookDetail iBookDetail) {
        super(mContext);
        this.iBookDetail = iBookDetail;
    }

    public void bookInfo(String bookId) {
        iBookDetail.showLoading();
        RxHttpUtils.getSInstance().addHeaders(tokenMap())
                .createSApi(BookService.class)
                .bookInfo(bookId)
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver<BookBean>() {
                    @Override
                    protected void onError(String errorMsg) {
                        iBookDetail.stopLoading();
                    }

                    @Override
                    protected void onSuccess(BookBean data) {
                        iBookDetail.stopLoading();
                        iBookDetail.getBookInfo(data);
                    }
                });
    }

    public void addBookShelf(CollBookBean collBookBean) {
        iBookDetail.showLoading();
        RxHttpUtils.getSInstance().addHeaders(tokenMap())
                .createSApi(BookService.class)
                .bookChapters(collBookBean.get_id())
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver<BookChaptersBean>() {
                    @Override
                    protected void onError(String errorMsg) {
                        iBookDetail.stopLoading();
                    }

                    @Override
                    protected void onSuccess(BookChaptersBean data) {
                        iBookDetail.stopLoading();
                        List<BookChapterBean> bookChapterList = new ArrayList<>();
                        for (BookChaptersBean.ChapterBean bean: data.getChapters()) {
                            BookChapterBean chapterBean = new BookChapterBean();
                            chapterBean.setBookId(data.getBook());
                            chapterBean.setLink(bean.getLink());
                            chapterBean.setTitle(bean.getTitle());
//                            chapterBean.setTaskName("下载");
                            chapterBean.setUnreadble(bean.isRead());
                            bookChapterList.add(chapterBean);
                        }
                        collBookBean.setBookChapters(bookChapterList);
                        CollBookHelper.getsInstance().saveBookWithAsync(collBookBean);

                        addBookShelfToServer(collBookBean.get_id());
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }
                });
    }

    /**
     * 添加书籍信息到书架
     *
     * @param bookid
     */
    public void addBookShelfToServer(String bookid) {
        RxHttpUtils.getSInstance().addHeaders(tokenMap()).createSApi(UserService.class)
                .addBookShelf(bookid).compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver<String>() {
                    @Override
                    protected void onError(String errorMsg) {

                    }

                    @Override
                    protected void onSuccess(String data) {
                        ToastUtils.show(data);
                    }
                });

    }

    /**
     * 删除书籍信息到书架
     *
     * @param mCollBookBean
     */
    public void deleteBookShelfToServer(CollBookBean mCollBookBean) {
        DeleteBookBean bean=new DeleteBookBean();
        bean.setBookid(mCollBookBean.get_id());
        RxHttpUtils.getSInstance().addHeaders(tokenMap()).createSApi(UserService.class)
                .deleteBookShelf(bean).compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver<String>() {
                    @Override
                    protected void onError(String errorMsg) {

                    }

                    @Override
                    protected void onSuccess(String data) {
                        ToastUtils.show(data);
                        CollBookHelper.getsInstance().removeBookInRx(mCollBookBean);
                    }
                });

    }
}
