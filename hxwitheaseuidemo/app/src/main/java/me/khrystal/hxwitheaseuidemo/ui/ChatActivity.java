package me.khrystal.hxwitheaseuidemo.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseChatFragment;

import me.khrystal.hxwitheaseuidemo.R;
import me.khrystal.hxwitheaseuidemo.base.BaseActivity;
import me.khrystal.hxwitheaseuidemo.base.MyConnectionListener;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/6/26
 * update time:
 * email: 723526676@qq.com
 */

public class ChatActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        EMClient.getInstance().addConnectionListener(new MyConnectionListener(this));

        EaseChatFragment chatFragment = new EaseChatFragment();
        Bundle args = new Bundle();

        args.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
        args.putString(EaseConstant.EXTRA_USER_ID, getIntent().getStringExtra(EaseConstant.EXTRA_USER_ID));
        chatFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().add(R.id.fl_chat_content, chatFragment).commit();
    }
}
