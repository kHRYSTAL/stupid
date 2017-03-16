package me.khrystal.hxdemo.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import me.khrystal.hxdemo.R;
import me.khrystal.hxdemo.callback.EMCallbackAdapter;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/3/10
 * update time:
 * email: 723526676@qq.com
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    // 进度条
    private ProgressDialog mDialog;
    // username(uid) 输入框
    private EditText mUsernameEt;
    // password(token) 输入框
    private EditText mPasswdEt;

    // register
    private Button mSignUpBtn;
    // login
    private Button mSignInBtn;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        mUsernameEt = (EditText) findViewById(R.id.ec_edit_username);
        mPasswdEt = (EditText) findViewById(R.id.ec_edit_password);

        mSignUpBtn = (Button) findViewById(R.id.ec_btn_sign_up);
        mSignInBtn = (Button) findViewById(R.id.ec_btn_sign_in);
        mSignInBtn.setOnClickListener(this);
        mSignUpBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ec_btn_sign_in:
                signIn();
                break;
            case R.id.ec_btn_sign_up:
                signUp();
                break;
        }
    }

    /**
     * 登录环信操作
     */
    private void signIn() {
        mDialog = new ProgressDialog(this);
        mDialog.setMessage("正在登录, 请稍后");
        mDialog.show();
        String username = mUsernameEt.getText().toString().trim();
        String passwd = mPasswdEt.getText().toString().trim();
        EMClient.getInstance().login(username, passwd, new EMCallbackAdapter() {
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDialog.dismiss();
                        // 加载所有会话到内存
                        EMClient.getInstance().chatManager().loadAllConversations();
                        // 加载所有群组到内存
                        EMClient.getInstance().groupManager().loadAllGroups();
                        // 登录成功后跳转至主界面
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }

            @Override
            public void onError(final int code, final String error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDialog.dismiss();
                        handleError(code, error);
                    }
                });
            }
        });
    }

    /**
     * 注册环信操作
     * todo 接入项目后这步应当在后台登录
     */
    private void signUp() {
        // 注册是耗时操作 要显示一个dialog 提示用户
        if (mDialog == null) {
            mDialog = new ProgressDialog(this);
            mDialog.setMessage("注册中, 请稍后..");
            mDialog.show();
        }
        // 新开一个线程执行异步操作
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String username = mUsernameEt.getText().toString().trim();
                    String passwd = mPasswdEt.getText().toString().trim();
                    // 注册
                    EMClient.getInstance().createAccount(username, passwd);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!LoginActivity.this.isFinishing()) {
                                mDialog.dismiss();
                            }
                            Toast.makeText(LoginActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!LoginActivity.this.isFinishing()) {
                                mDialog.dismiss();
                            }
                            // http://www.easemob.com/apidoc/android/chat3.0/classcom_1_1hyphenate_1_1_e_m_error.html
                            int errorCode = e.getErrorCode();
                            String message = e.getMessage();
                            handleError(errorCode, message);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void handleError(int errorCode, String message) {
        switch (errorCode) {
            // 网络错误
            case EMError.NETWORK_ERROR:
                Toast.makeText(LoginActivity.this, "网络错误 code: " + errorCode + ", message:" + message, Toast.LENGTH_LONG).show();
                break;
            // 用户已存在
            case EMError.USER_ALREADY_EXIST:
                Toast.makeText(LoginActivity.this, "用户已存在 code: " + errorCode + ", message:" + message, Toast.LENGTH_LONG).show();
                break;
            // 参数不合法，一般情况是username 使用了uuid导致，不能使用uuid注册
            case EMError.USER_ILLEGAL_ARGUMENT:
                Toast.makeText(LoginActivity.this, "参数不合法 不能使用uuid注册 code: " + errorCode + ", message:" + message, Toast.LENGTH_LONG).show();
                break;
            case EMError.USER_REG_FAILED:
                Toast.makeText(LoginActivity.this, "账户注册失败 code: " + errorCode + ", message:" + message, Toast.LENGTH_LONG).show();
                break;
            // 无效的用户名 101
            case EMError.INVALID_USER_NAME:
                Toast.makeText(LoginActivity.this, "无效的用户名 code: " + errorCode + ", message:" + message, Toast.LENGTH_LONG).show();
                break;
            // 无效的密码 102
            case EMError.INVALID_PASSWORD:
                Toast.makeText(LoginActivity.this, "无效的密码 code: " + errorCode + ", message:" + message, Toast.LENGTH_LONG).show();
                break;
            // 用户认证失败，用户名或密码错误 202
            case EMError.USER_AUTHENTICATION_FAILED:
                Toast.makeText(LoginActivity.this, "用户认证失败，用户名或密码错误 code: " + errorCode + ", message:" + message, Toast.LENGTH_LONG).show();
                break;
            // 用户不存在 204
            case EMError.USER_NOT_FOUND:
                Toast.makeText(LoginActivity.this, "用户不存在 code: " + errorCode + ", message:" + message, Toast.LENGTH_LONG).show();
                break;
            // 无法访问到服务器 300
            case EMError.SERVER_NOT_REACHABLE:
                Toast.makeText(LoginActivity.this, "无法访问到服务器 code: " + errorCode + ", message:" + message, Toast.LENGTH_LONG).show();
                break;
            // 等待服务器响应超时 301
            case EMError.SERVER_TIMEOUT:
                Toast.makeText(LoginActivity.this, "等待服务器响应超时 code: " + errorCode + ", message:" + message, Toast.LENGTH_LONG).show();
                break;
            // 服务器繁忙 302
            case EMError.SERVER_BUSY:
                Toast.makeText(LoginActivity.this, "服务器繁忙 code: " + errorCode + ", message:" + message, Toast.LENGTH_LONG).show();
                break;
            // 未知 Server 异常 303 一般断网会出现这个错误
            case EMError.SERVER_UNKNOWN_ERROR:
                Toast.makeText(LoginActivity.this, "未知的服务器异常 code: " + errorCode + ", message:" + message, Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(LoginActivity.this, "failed code: " + errorCode + ", message:" + message, Toast.LENGTH_LONG).show();
                break;
        }
    }
}
