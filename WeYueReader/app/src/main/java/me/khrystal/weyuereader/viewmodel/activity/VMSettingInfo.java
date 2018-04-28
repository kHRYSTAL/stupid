package me.khrystal.weyuereader.viewmodel.activity;

import android.content.Context;

import com.allen.library.RxHttpUtils;
import com.allen.library.interceptor.Transformer;

import me.khrystal.weyuereader.WYApplication;
import me.khrystal.weyuereader.api.UserService;
import me.khrystal.weyuereader.model.AppUpdateBean;
import me.khrystal.weyuereader.utils.ToastUtils;
import me.khrystal.weyuereader.utils.rxhelper.RxObserver;
import me.khrystal.weyuereader.view.activity.ISetting;
import me.khrystal.weyuereader.viewmodel.BaseViewModel;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/4/28
 * update time:
 * email: 723526676@qq.com
 */

public class VMSettingInfo extends BaseViewModel {

    ISetting iSetting;

    public VMSettingInfo(Context mContext, ISetting iSetting) {
        super(mContext);
        this.iSetting = iSetting;
    }

    public void appUpdate(boolean isTip) {
        iSetting.showLoading();
        RxHttpUtils.getSInstance().addHeaders(tokenMap())
                .createSApi(UserService.class)
                .appUpdate()
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver<AppUpdateBean>() {
                    @Override
                    protected void onError(String errorMsg) {
                        iSetting.stopLoading();
                    }

                    @Override
                    protected void onSuccess(AppUpdateBean data) {
                        iSetting.stopLoading();
                        if (WYApplication.packageInfo.versionCode < data.getVersioncode()) {
                            iSetting.appUpdate(data);
                        } else {
                            if (isTip) {
                                ToastUtils.show("当前是最新版本");
                            }
                        }
                    }
                });
    }
}
