package me.khrystal.weyuereader.viewmodel.fragment;

import android.content.Context;

import com.allen.library.RxHttpUtils;
import com.allen.library.interceptor.Transformer;

import io.reactivex.disposables.Disposable;
import me.khrystal.weyuereader.api.BookService;
import me.khrystal.weyuereader.model.BookClassifyBean;
import me.khrystal.weyuereader.utils.NetworkUtils;
import me.khrystal.weyuereader.utils.rxhelper.RxObserver;
import me.khrystal.weyuereader.view.fragment.IClassifyBook;
import me.khrystal.weyuereader.viewmodel.BaseViewModel;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/4/28
 * update time:
 * email: 723526676@qq.com
 */

public class VMBookClassify extends BaseViewModel {

    IClassifyBook iClassifyBook;

    public VMBookClassify(Context mContext, IClassifyBook iClassifyBook) {
        super(mContext);
        this.iClassifyBook = iClassifyBook;
    }

    public void bookClassify() {
        if (NetworkUtils.isConnected()) {
            if (iClassifyBook != null) {
                iClassifyBook.NetWorkError();
            }
            return;
        }
        iClassifyBook.showLoading();
        RxHttpUtils.getSInstance().addHeaders(tokenMap())
                .createSApi(BookService.class)
                .bookClassify()
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver<BookClassifyBean>() {
                    @Override
                    protected void onError(String errorMsg) {
                        if (iClassifyBook != null) {
                            iClassifyBook.stopLoading();
                            iClassifyBook.errorData(errorMsg);
                        }
                    }

                    @Override
                    protected void onSuccess(BookClassifyBean data) {
                        if (iClassifyBook != null) {
                            iClassifyBook.stopLoading();
                            if (data == null) {
                                iClassifyBook.emptyData();
                                return;
                            }
                            iClassifyBook.getBookClassify(data);
                        }
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }
                });
    }
}
