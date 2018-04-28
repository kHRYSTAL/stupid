package me.khrystal.weyuereader.viewmodel.activity;

import android.content.Context;

import com.allen.library.RxHttpUtils;
import com.allen.library.interceptor.Transformer;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.khrystal.weyuereader.api.BookService;
import me.khrystal.weyuereader.db.entity.BookChapterBean;
import me.khrystal.weyuereader.model.ChapterContentBean;
import me.khrystal.weyuereader.utils.BookManager;
import me.khrystal.weyuereader.utils.BookSaveUtils;
import me.khrystal.weyuereader.utils.LogUtils;
import me.khrystal.weyuereader.utils.rxhelper.RxObserver;
import me.khrystal.weyuereader.view.activity.IBookChapters;
import me.khrystal.weyuereader.viewmodel.BaseViewModel;
import me.khrystal.weyuereader.widget.page.TxtChapter;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/4/28
 * update time:
 * email: 723526676@qq.com
 */

public class VMBookContentInfo extends BaseViewModel {

    IBookChapters iBookChapters;
    Disposable mDisposable;
    String title;

    public VMBookContentInfo(Context mContext, IBookChapters iBookChapters) {
        super(mContext);
        this.iBookChapters = iBookChapters;
    }

    public void loadChapters(String bookId) {
        RxHttpUtils.getSInstance().addHeaders(tokenMap())
                .createSApi(BookService.class)
                .bookChapters(bookId)
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver<BookChapterBean>() {
                    @Override
                    protected void onError(String errorMsg) {

                    }

                    @Override
                    protected void onSuccess(BookChapterBean data) {
                        if (iBookChapters != null) {
                            iBookChapters.bookChapters(data);
                        }
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }
                });
    }

    public void loadContent(String bookId, List<TxtChapter> bookChapterList) {
        int size = bookChapterList.size();
        // 取消上次的任务 防止多次加载
        if (mDisposable != null) {
            mDisposable.dispose();
        }

        List<Observable<ChapterContentBean>> chapterContentBeans = new ArrayList<>(bookChapterList.size());
        ArrayDeque<String> titles = new ArrayDeque<>(bookChapterList.size());
        // 首先判断是否Chapter已经存在
        for (int i = 0; i < size; i++) {
            TxtChapter bookChapter = bookChapterList.get(i);
            if (!(BookManager.isChapterCached(bookId, bookChapter.getTitle()))) {
                Observable<ChapterContentBean> contentBeanObservable =
                        RxHttpUtils.createApi(BookService.class)
                        .bookContent(bookChapter.getLink());
                chapterContentBeans.add(contentBeanObservable);
                titles.add(bookChapter.getTitle());
            } else if (i == 0) {
                if (iBookChapters != null) {
                    iBookChapters.finishChapters();
                }
            }
        }
        title = titles.poll();
        Observable.concat(chapterContentBeans)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ChapterContentBean>() {
                    @Override
                    public void accept(ChapterContentBean bean) throws Exception {
                        BookSaveUtils.getsInstance().saveChapterInfo(bookId, title, bean.getChapter().getCpContent());
                        iBookChapters.finishChapters();
                        title = titles.poll();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (bookChapterList.get(0).getTitle().equals(title)) {
                            iBookChapters.errorChapters();
                        }
                        LogUtils.e(throwable.getMessage());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                }, new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mDisposable = disposable;
                    }
                });
    }
}
