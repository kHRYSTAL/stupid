package me.khrystal.hxdemo.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;

import me.khrystal.hxdemo.R;
import me.khrystal.hxdemo.callback.EMCallbackAdapter;
import me.khrystal.hxdemo.util.Constants;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    // 输入指向对方聊天的uid ,即在环信注册时指明的这个用户唯一标识
    private EditText mReferUserET;
    // 发起会话按钮
    private Button mStartChatBtn;
    // 登出按钮
    private Button mLoginOutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 判断环信sdk是否之前登录成功过
        // 由于在application设置了不需要每次都重新登录 因此登录成功过后
        // 除非被服务器踢出 , 主动退出 , 被动退出(自己代码处理登出) 否则不需要跳转至登录页面重新登录
        if (!EMClient.getInstance().isLoggedInBefore()) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.activity_main);
        initView();
    }

    /**
     * 初始化界面
     */
    private void initView() {
        mReferUserET = (EditText) findViewById(R.id.ec_edit_chat_id);
        mStartChatBtn = (Button) findViewById(R.id.ec_btn_start_chat);
        mLoginOutBtn = (Button) findViewById(R.id.ec_btn_sign_out);
        mStartChatBtn.setOnClickListener(this);
        mLoginOutBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ec_btn_start_chat:
                startChat();
                break;
            case R.id.ec_btn_sign_out:
                logOut();
                break;
        }
    }

    /**
     * 退出登录
     */
    private void logOut() {
       EMClient.getInstance().logout(false, new EMCallbackAdapter() {
           @Override
           public void onSuccess() {
               Toast.makeText(MainActivity.this, "退出成功", Toast.LENGTH_SHORT).show();
               finish();
           }

           @Override
           public void onError(int code, String error) {
               Toast.makeText(MainActivity.this, "退出失败 code:"
                       + Integer.toString(code) + ","
                       + error,
                       Toast.LENGTH_SHORT)
                       .show();
           }
       });
    }

    /**
     * 验证后跳转到聊天界面
     */
    private void startChat() {
        // 获取发起聊天指定的uid
        String chatId = mReferUserET.getText().toString().trim();
        if (!TextUtils.isEmpty(chatId)) {
            // 获取当前登录用户的username
            String currUserName = EMClient.getInstance().getCurrentUser();
            if (chatId.equals(currUserName)) {
                Toast.makeText(MainActivity.this, "不能和自己聊天", Toast.LENGTH_SHORT).show();
                return;
            }
            // 跳转到聊天界面 开始聊天
            Intent intent = new Intent(MainActivity.this, ChatActivity.class);
            intent.putExtra(Constants.REFERUID, chatId);
            startActivity(intent);
        } else {
            Toast.makeText(MainActivity.this, "UserName 不能为空", Toast.LENGTH_SHORT).show();
        }
    }
}
