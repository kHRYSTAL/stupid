package me.khrystal.weyuereader.viewmodel.activity;

import android.content.Context;

import com.allen.library.RxHttpUtils;
import com.allen.library.interceptor.Transformer;

import java.io.File;

import me.khrystal.weyuereader.api.UserService;
import me.khrystal.weyuereader.db.entity.UserBean;
import me.khrystal.weyuereader.db.helper.UserHelper;
import me.khrystal.weyuereader.utils.SharedPreUtils;
import me.khrystal.weyuereader.utils.ToastUtils;
import me.khrystal.weyuereader.utils.rxhelper.RxObserver;
import me.khrystal.weyuereader.view.activity.IUserInfo;
import me.khrystal.weyuereader.viewmodel.BaseViewModel;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/4/28
 * update time:
 * email: 723526676@qq.com
 */

public class VMUserInfo extends BaseViewModel {

    IUserInfo iUserInfo;

    public VMUserInfo(Context mContext, IUserInfo iUserInfo) {
        super(mContext);
        this.iUserInfo = iUserInfo;
    }

    public void getUserInfo() {
        iUserInfo.showLoading();
        RxHttpUtils.getSInstance().addHeaders(tokenMap())
                .createSApi(UserService.class)
                .getUserInfo()
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver<UserBean>() {
                    @Override
                    protected void onError(String errorMsg) {
                        iUserInfo.stopLoading();
                    }

                    @Override
                    protected void onSuccess(UserBean data) {
                        iUserInfo.stopLoading();
                        iUserInfo.userInfo(data);
                    }
                });
    }

    public void uploadAvatar(String imagePath) {
        iUserInfo.showLoading();
        String username = SharedPreUtils.getInstance().getString("username", "");
        File file = new File(imagePath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("avatar", file.getName(), requestBody);
        RxHttpUtils.getSInstance().addHeaders(tokenMap())
                .createSApi(UserService.class)
                .avatar(body)
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver<String>() {
                    @Override
                    protected void onError(String errorMsg) {
                        iUserInfo.stopLoading();
                    }

                    @Override
                    protected void onSuccess(String data) {
                        iUserInfo.stopLoading();
                        iUserInfo.uploadSuccess(data);
                        UserBean userBean = UserHelper.getsInstance().findUserByName(username);
                        userBean.setIcon(data);
                        UserHelper.getsInstance().updateUser(userBean);
                    }
                });
    }

    public void updatePassword(String password) {
        iUserInfo.showLoading();
        RxHttpUtils.getSInstance().addHeaders(tokenMap())
                .createSApi(UserService.class)
                .updatePassword(password)
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver<String>() {
                    @Override
                    protected void onError(String errorMsg) {
                        iUserInfo.stopLoading();
                    }

                    @Override
                    protected void onSuccess(String data) {
                        iUserInfo.stopLoading();
                        ToastUtils.show(data);
                    }
                });
    }


    public void updateUserInfo(String nickname, String brief) {
        iUserInfo.showLoading();
        String username = SharedPreUtils.getInstance().getString("username", "");
        RxHttpUtils.getSInstance().addHeaders(tokenMap())
                .createSApi(UserService.class)
                .updateUserInfo(nickname, brief)
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver<String>() {
                    @Override
                    protected void onError(String errorMsg) {
                        iUserInfo.stopLoading();
                    }

                    @Override
                    protected void onSuccess(String data) {
                        iUserInfo.stopLoading();
                        ToastUtils.show(data);
                        UserBean userBean = UserHelper.getsInstance().findUserByName(username);
                        userBean.setBrief(brief);
                        userBean.setNickName(nickname);
                        UserHelper.getsInstance().updateUser(userBean);
                    }
                });
    }

}
