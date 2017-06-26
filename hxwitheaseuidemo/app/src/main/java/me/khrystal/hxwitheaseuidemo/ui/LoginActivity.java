package me.khrystal.hxwitheaseuidemo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import me.khrystal.hxwitheaseuidemo.R;
import me.khrystal.hxwitheaseuidemo.base.BaseActivity;
import me.khrystal.hxwitheaseuidemo.base.GlobalField;
import me.khrystal.hxwitheaseuidemo.base.MyConnectionListener;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/6/26
 * update time:
 * email: 723526676@qq.com
 */

public class LoginActivity extends BaseActivity {

    private static final int LOGIN_SUCCESS = 1;
    private static final int LOGIN_FAILED = 2;

    @InjectView(R.id.et_main_userName)
    EditText etMainUserName;
    @InjectView(R.id.et_main_pwd)
    EditText etMainPwd;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOGIN_SUCCESS:
                    Toast.makeText(LoginActivity.this, "登录成功了", Toast.LENGTH_SHORT).show();
                    break;
                case LOGIN_FAILED:
                    int code = msg.arg1;
                    switch (code) {
                        case 202:
                            Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        checkIsLogin();
        EMClient.getInstance().addConnectionListener(new MyConnectionListener(this));
    }

    private void checkIsLogin() {
        if (EMClient.getInstance().isLoggedInBefore()) {
            // 不管如何 登录完成之后都要加载所有的组件和聊天信息
            EMClient.getInstance().groupManager().loadAllGroups();
            EMClient.getInstance().chatManager().loadAllConversations();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
            Log.e("main", "已经登录过了 跳转页面");
        }
    }

    @OnClick(R.id.btn_main_login)
    public void onClick() {
        String userName = "" + etMainUserName.getText().toString().trim();
        String pwd = "" + etMainPwd.getText().toString().trim();
        login(userName, pwd);
    }

    private void login(final String userName, String pwd) {
        EMClient.getInstance().login(userName, pwd, new EMCallBack() {
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                // 保存用户名
                getSharedPreferences(GlobalField.USERINFO_FILENAME, MODE_PRIVATE).edit().putString("username", userName).commit();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                Log.e("main", "登录聊天服务器成功!");
                mHandler.sendEmptyMessage(LOGIN_SUCCESS);
                finish();
            }

            @Override
            public void onError(int code, String message) {
                Log.e("main", "登录聊天服务器失败!" + message + " code=" + code);
                Message msg = mHandler.obtainMessage();
                msg.obj = message;
                msg.arg1 = code;
                msg.what = LOGIN_FAILED;
                mHandler.sendMessage(msg);

            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }

    /**
     * 注册按钮
     *
     * @param view
     */
    public void onRegist(View view) {
        startActivity(new Intent(this, RegistActivity.class));
        finish();
    }

    /**
     * 下线按钮,便于测试
     *
     * @param view
     */
    public void onLogout(View view) {
        EMClient.getInstance().logout(true, new EMCallBack() {
            @Override
            public void onSuccess() {
                Log.e("main", "下线成功了");
            }

            @Override
            public void onError(int i, String s) {
                Log.e("main", "下线失败了！" + s);
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });//下线
    }
}
