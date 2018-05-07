package me.khrystal.weyuereader.view.activity.impl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.BindView;
import butterknife.OnClick;
import me.khrystal.weyuereader.R;
import me.khrystal.weyuereader.WYApplication;
import me.khrystal.weyuereader.db.helper.UserHelper;
import me.khrystal.weyuereader.model.AppUpdateBean;
import me.khrystal.weyuereader.utils.AppUpdateUtils;
import me.khrystal.weyuereader.utils.LoadingHelper;
import me.khrystal.weyuereader.utils.SharedPreUtils;
import me.khrystal.weyuereader.view.activity.ISetting;
import me.khrystal.weyuereader.view.base.BaseActivity;
import me.khrystal.weyuereader.viewmodel.activity.VMSettingInfo;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/5/7
 * update time:
 * email: 723526676@qq.com
 */

public class SettingActivity extends BaseActivity implements ISetting {

    private VMSettingInfo mModel;

    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.btn_out)
    Button btnOut;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mModel = new VMSettingInfo(this, this);
        setBinddingView(R.layout.activity_setting, NO_BINDDING, mModel);
    }

    @Override
    protected void initView() {
        super.initView();
        initThemeToolBar("设置");
        tvVersion.setText("版本号：v." + WYApplication.packageInfo.versionName);
    }

    @OnClick({R.id.btn_out, R.id.rl_version})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_out:
                new MaterialDialog.Builder(this)
                        .title("退出登录")
                        .content("是否退出登录?")
                        .positiveText("确定")
                        .onPositive((dialog, which) -> {
                            UserHelper.getsInstance().removeUser();
                            SharedPreUtils.getInstance().sharedPreRemove("username");
                            finish();
                        })
                        .negativeText("取消")
                        .onNegative((dialog, which) -> {
                            dialog.dismiss();
                        })
                        .show();
                break;
            case R.id.rl_version:
                mModel.appUpdate(true);
                break;
        }

    }

    @Override
    public void showLoading() {
        LoadingHelper.getInstance().showLoading(mContext);
    }

    @Override
    public void stopLoading() {
        LoadingHelper.getInstance().hideLoading();
    }

    @Override
    public void appUpdate(AppUpdateBean appUpdateBean) {
        AppUpdateUtils.getInstance().appUpdate(this, appUpdateBean);
    }
}
