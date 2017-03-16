package me.khrystal.hxdemo.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import me.khrystal.hxdemo.R;
import me.khrystal.hxdemo.fragment.ChatFragment;
import me.khrystal.hxdemo.util.Constants;

/**
 * usage: 聊天Activity 支持单聊群聊
 * author: kHRYSTAL
 * create time: 17/3/10
 * update time:
 * email: 723526676@qq.com
 */

public class ChatActivity extends AppCompatActivity {

    private String referUid;

    protected ChatFragment chatFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        referUid = getIntent().getStringExtra(Constants.REFERUID);
        if (TextUtils.isEmpty(referUid))
            finish();
        initView();
    }

    /**
     * 初始化界面
     */
    private void initView() {
        chatFragment = (ChatFragment) getFragmentManager().findFragmentById(R.id.fragment_chat);
    }
}
