package me.khrystal.weyuereader.view.base;

import android.app.Service;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/4/26
 * update time:
 * email: 723526676@qq.com
 */

public abstract class BaseService extends Service {

    private CompositeDisposable mDisposable;

    protected void addDisposable(Disposable disposable) {
        if (mDisposable == null) {
            mDisposable = new CompositeDisposable();
        }
        mDisposable.add(disposable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }
}
