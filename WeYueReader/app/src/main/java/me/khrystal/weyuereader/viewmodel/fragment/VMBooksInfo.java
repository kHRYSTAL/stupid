package me.khrystal.weyuereader.viewmodel.fragment;

import android.content.Context;

import com.allen.library.RxHttpUtils;
import com.allen.library.interceptor.Transformer;

import java.util.List;

import io.reactivex.disposables.Disposable;
import me.khrystal.weyuereader.api.BookService;
import me.khrystal.weyuereader.model.BookBean;
import me.khrystal.weyuereader.utils.ToastUtils;
import me.khrystal.weyuereader.utils.rxhelper.RxObserver;
import me.khrystal.weyuereader.view.fragment.IBookInfo;
import me.khrystal.weyuereader.viewmodel.BaseViewModel;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/4/28
 * update time:
 * email: 723526676@qq.com
 */

public class VMBooksInfo extends BaseViewModel {

    IBookInfo iBookInfo;

    public VMBooksInfo(Context mContext, IBookInfo iBookInfo) {
        super(mContext);
        this.iBookInfo = iBookInfo;
    }

    public void getBooks(String type, String major, int page) {
        RxHttpUtils.getSInstance().addHeaders(tokenMap())
                .createSApi(BookService.class)
                .books(type, major, page)
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver<List<BookBean>>() {
                    @Override
                    protected void onError(String errorMsg) {
                        if (iBookInfo != null) {
                            iBookInfo.stopLoading();
                        }
                    }

                    @Override
                    protected void onSuccess(List<BookBean> data) {
                        if (iBookInfo != null) {
                            iBookInfo.stopLoading();
                        }
                        if (data.size() > 0) {
                            if (iBookInfo != null) {
                                iBookInfo.getBooks(data, page > 1 ? true : false);
                            }
                        } else {
                            ToastUtils.show("无更多书籍");
                        }
                    }

                    @Override
                    public void doOnSubscribe(Disposable disposable) {
                        addDisposable(disposable);
                    }
                });
    }
}
