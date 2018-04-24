package me.khrystal.weyuereader.utils.rxhelper;

import android.content.Intent;

import com.allen.library.RxHttpUtils;
import com.allen.library.base.BaseDataObserver;
import com.allen.library.bean.BaseData;
import com.allen.library.utils.ToastUtils;

import io.reactivex.disposables.Disposable;
import me.khrystal.weyuereader.WYApplication;
import me.khrystal.weyuereader.utils.LoadingHelper;

/**
 * usage: 网络请求处理基类
 * author: kHRYSTAL
 * create time: 18/4/24
 * update time:
 * email: 723526676@qq.com
 */

public abstract class RxObserver<T> extends BaseDataObserver<T> {
    boolean isLoading;

    public RxObserver() {
    }

    public RxObserver(boolean isLoading) {
        this.isLoading = isLoading;
    }

    /**
     * 失败回调
     */
    protected abstract void onError(String errorMsg);

    /**
     * 成功回调
     */
    protected abstract void onSuccess(T data);

    @Override
    public void doOnSubscribe(Disposable disposable) {
        // 在订阅时添加至RxHttpUtils处理
        // 由RxHttpUtils管理何时中断或取消请求 防止内存泄漏 类似 RxLifeCycle
        RxHttpUtils.addDisposable(disposable);
    }

    @Override
    public void doOnError(String errorMsg) {
        dismissLoading();
        ToastUtils.showToast(errorMsg);
        onError(errorMsg);
    }

    @Override
    public void doOnNext(BaseData<T> data) {
        switch (data.getCode()) {
            case 10000:
                // 请求成功
                onSuccess(data.getData());
                break;
            case 60001:
            case 60002:
                ToastUtils.showToast(data.getMsg());
                // TODO 跳转至登录页面
//                Intent intent = new Intent(WYApplication.getAppContext(), LoginActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                WYApplication.getAppContext().startActivity(intent);
                break;
            case 10005:
            case 40000:
            case 40001:
            case 40003:
            case 40004:
            case 40005:
            case 50000:
            case 50003:
                ToastUtils.showToast(data.getMsg());
                onError(data.getMsg());
                break;
            default:
                onError(data.getMsg());
                break;
        }
    }

    @Override
    public void doOnCompleted() {
        dismissLoading();
    }

    private void dismissLoading() {
        if (isLoading) {
            LoadingHelper.getInstance().hideLoading();
        }
    }
}


