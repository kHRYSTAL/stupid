package me.khrystal.weyuereader.viewmodel.activity;

import android.content.Context;

import com.allen.library.RxHttpUtils;
import com.allen.library.interceptor.Transformer;

import java.util.List;

import io.reactivex.disposables.Disposable;
import me.khrystal.weyuereader.api.BookService;
import me.khrystal.weyuereader.model.BookBean;
import me.khrystal.weyuereader.utils.rxhelper.RxObserver;
import me.khrystal.weyuereader.view.fragment.IBookSearchInfo;
import me.khrystal.weyuereader.viewmodel.BaseViewModel;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/4/28
 * update time:
 * email: 723526676@qq.com
 */

public class VMBookSearchInfo extends BaseViewModel {

    IBookSearchInfo iBookSearchInfo;

    public VMBookSearchInfo(Context mContext, IBookSearchInfo iBookSearchInfo) {
        super(mContext);
        this.iBookSearchInfo = iBookSearchInfo;
    }

    public void searchBooks(String keyword) {
        RxHttpUtils.getSInstance().addHeaders(tokenMap())
                .createSApi(BookService.class)
                .booksSearch(keyword)
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver<List<BookBean>>() {
                    @Override
                    protected void onError(String errorMsg) {

                    }

                    @Override
                    protected void onSuccess(List<BookBean> data) {
                        if (iBookSearchInfo != null) {
                            iBookSearchInfo.getSearchBooks(data);
                        }
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }
                });
    }
}
