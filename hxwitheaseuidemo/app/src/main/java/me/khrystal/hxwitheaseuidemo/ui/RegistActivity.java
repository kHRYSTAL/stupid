package me.khrystal.hxwitheaseuidemo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.socks.library.KLog;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import me.khrystal.hxwitheaseuidemo.R;
import me.khrystal.hxwitheaseuidemo.base.BaseActivity;
import me.khrystal.hxwitheaseuidemo.base.MyConnectionListener;
import me.khrystal.hxwitheaseuidemo.utils.ToastUtils;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/6/26
 * update time:
 * email: 723526676@qq.com
 */

public class RegistActivity extends BaseActivity {
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.et_register_phone)
    EditText etRegisterPhone;
    @InjectView(R.id.et_register_pwd)
    EditText etRegisterPwd;
    @InjectView(R.id.et_register_repwd)
    EditText etRegisterRepwd;

    private static final int REG_SUCCESS = 1;
    private static final int REG_FAILED = 2;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REG_SUCCESS:
                    ToastUtils.showToast(mActivity, "注册成功 请先登录");
                    break;
                case REG_FAILED:
                    if (mActivity == null)
                        KLog.e("Acivity 为空");
                    ToastUtils.showToast(mActivity, "该用户已经注册过了,请换一个号码再试");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        ButterKnife.inject(this);
        // 注册一个监听连接状态的listener
        EMClient.getInstance().addConnectionListener(new MyConnectionListener(this));
    }

    @OnClick(R.id.btn_register)
    public void onClick() {
        String userPhone = etRegisterPhone.getText().toString().trim();
        String pwd = etRegisterPwd.getText().toString().trim();
        String rePwd = etRegisterRepwd.getText().toString().trim();

        if (TextUtils.isEmpty(userPhone))
            ToastUtils.showToast(mActivity, "用户名不能为空");
        if (TextUtils.isEmpty(pwd) || TextUtils.isEmpty(rePwd)) {
            ToastUtils.showToast(mActivity, "密码不能为空");
        }
        if (!pwd.equals(rePwd)) {
            ToastUtils.showToast(mActivity, "两次密码输入不一致, 请重新输入");
            etRegisterPwd.setText("");
            etRegisterRepwd.setFocusable(true);
        }
        regist(userPhone, pwd);
    }

    /**
     * 用户注册(这里只是demo,没有自己写服务器,实际开发中是要通过后台服务器来注册,注册成功之后服务器再注册环信账户,为了简化,这里直接注册[官方都不建议这样做哦])
     *
     * @param userPhone
     * @param pwd
     */
    private void regist(final String userPhone, final String pwd) {
        new Thread() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().createAccount(userPhone, pwd);
                    mHandler.sendEmptyMessage(REG_SUCCESS);
                    startActivity(new Intent(mActivity, LoginActivity.class));
                    finish();
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    KLog.e("错误信息:" + e.getMessage());
                    e.getErrorCode();
                    mHandler.sendEmptyMessage(REG_FAILED);
                }
            }
        }.start();
    }

}
