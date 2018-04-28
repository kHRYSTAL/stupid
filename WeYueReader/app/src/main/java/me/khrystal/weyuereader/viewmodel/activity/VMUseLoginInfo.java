package me.khrystal.weyuereader.viewmodel.activity;

import android.content.Context;

import com.allen.library.RxHttpUtils;
import com.allen.library.interceptor.Transformer;

import me.khrystal.weyuereader.api.UserService;
import me.khrystal.weyuereader.db.entity.UserBean;
import me.khrystal.weyuereader.db.helper.UserHelper;
import me.khrystal.weyuereader.utils.SharedPreUtils;
import me.khrystal.weyuereader.utils.ToastUtils;
import me.khrystal.weyuereader.utils.rxhelper.RxObserver;
import me.khrystal.weyuereader.view.base.BaseActivity;
import me.khrystal.weyuereader.viewmodel.BaseViewModel;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/4/28
 * update time:
 * email: 723526676@qq.com
 */

public class VMUseLoginInfo extends BaseViewModel {

    public VMUseLoginInfo(Context mContext) {
        super(mContext);
    }

    public void login(String username, String password) {
        RxHttpUtils.getSInstance().addHeaders(tokenMap())
                .createSApi(UserService.class)
                .login(username, password)
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver<UserBean>() {
                    @Override
                    protected void onError(String errorMsg) {

                    }

                    @Override
                    protected void onSuccess(UserBean data) {
                        ToastUtils.show("登录成功");
                        UserHelper.getsInstance().saveUser(data);
                        SharedPreUtils.getInstance().putString("token", data.getToken());
                        SharedPreUtils.getInstance().putString("username", data.name);
                        ((BaseActivity) mContext).finish();
                    }
                });
    }
}
