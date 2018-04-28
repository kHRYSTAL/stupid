package me.khrystal.weyuereader.viewmodel.activity;

import android.content.Context;

import com.allen.library.RxHttpUtils;
import com.allen.library.interceptor.Transformer;

import me.khrystal.weyuereader.api.UserService;
import me.khrystal.weyuereader.utils.MD5Utils;
import me.khrystal.weyuereader.utils.ToastUtils;
import me.khrystal.weyuereader.utils.rxhelper.RxObserver;
import me.khrystal.weyuereader.view.activity.IUserRegister;
import me.khrystal.weyuereader.viewmodel.BaseViewModel;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/4/28
 * update time:
 * email: 723526676@qq.com
 */

public class VMUserRegisterInfo extends BaseViewModel {

    IUserRegister userRegister;

    public VMUserRegisterInfo(Context mContext, IUserRegister iUserRegister) {
        super(mContext);
        this.userRegister = iUserRegister;
    }

    public void register(String username, String password) {
        // 对密码进行md5加密
        String md5Pass = MD5Utils.encrypt(password);
        RxHttpUtils.getSInstance().addHeaders(tokenMap())
                .createSApi(UserService.class)
                .register(username, md5Pass)
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver<String>() {
                    @Override
                    protected void onError(String errorMsg) {

                    }

                    @Override
                    protected void onSuccess(String data) {
                        ToastUtils.show(data);
                        if (data.equals("注册成功")) {
                            userRegister.registerSuccess();
                        }
                    }
                });
    }
}
