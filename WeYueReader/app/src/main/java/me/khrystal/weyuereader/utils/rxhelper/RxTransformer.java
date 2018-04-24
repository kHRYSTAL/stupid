package me.khrystal.weyuereader.utils.rxhelper;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.khrystal.weyuereader.WYApplication;
import me.khrystal.weyuereader.utils.LoadingHelper;
/**
 * usage: 在订阅时执行的事件 如果需要弹出loading 能够在主线程弹出loading
 * doOnSubscribe 可以根据下方subscribeOn指定的线程去执行内部的代码
 * author: kHRYSTAL
 * create time: 18/4/24
 * update time:
 * email: 723526676@qq.com
 */

public class RxTransformer {

    public static <T>ObservableTransformer<T, T> switchSchedulers(boolean isLoading) {
        return upstream -> upstream
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    if (isLoading) {
                        LoadingHelper.getInstance().showLoading(WYApplication.getAppContext());
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
