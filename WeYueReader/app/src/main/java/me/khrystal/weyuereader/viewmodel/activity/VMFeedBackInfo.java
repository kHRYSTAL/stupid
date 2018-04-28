package me.khrystal.weyuereader.viewmodel.activity;

import android.content.Context;

import com.allen.library.RxHttpUtils;
import com.allen.library.interceptor.Transformer;

import me.khrystal.weyuereader.api.UserService;
import me.khrystal.weyuereader.utils.ToastUtils;
import me.khrystal.weyuereader.utils.rxhelper.RxObserver;
import me.khrystal.weyuereader.view.activity.IFeedBack;
import me.khrystal.weyuereader.viewmodel.BaseViewModel;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/4/28
 * update time:
 * email: 723526676@qq.com
 */

public class VMFeedBackInfo extends BaseViewModel {

    IFeedBack iFeedBack;

    public VMFeedBackInfo(Context mContext, IFeedBack iFeedBack) {
        super(mContext);
        this.iFeedBack = iFeedBack;
    }

    public void commitFeedBack(String qq, String feedback) {
        RxHttpUtils.getSInstance().addHeaders(tokenMap())
                .createSApi(UserService.class)
                .userFeedBack(qq, feedback)
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver<String>() {
                    @Override
                    protected void onError(String errorMsg) {

                    }

                    @Override
                    protected void onSuccess(String data) {
                        ToastUtils.show("提交反馈成功");
                        iFeedBack.feedBackSuccess();
                    }
                });
    }
}
