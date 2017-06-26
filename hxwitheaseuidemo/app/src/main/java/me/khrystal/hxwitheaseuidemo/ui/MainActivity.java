package me.khrystal.hxwitheaseuidemo.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.hyphenate.EMContactListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.hyphenate.exceptions.HyphenateException;
import com.socks.library.KLog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;
import me.khrystal.hxwitheaseuidemo.R;
import me.khrystal.hxwitheaseuidemo.base.BaseActivity;
import me.khrystal.hxwitheaseuidemo.base.MyConnectionListener;
import me.khrystal.hxwitheaseuidemo.runtimepermissions.PermissionsManager;
import me.khrystal.hxwitheaseuidemo.runtimepermissions.PermissionsResultAction;
import me.khrystal.hxwitheaseuidemo.ui.fragment.PersonFragment;

public class MainActivity extends BaseActivity {

    EaseConversationListFragment conversationListFragment;
    private static EaseContactListFragment contactListFragment;
    private static EMMessageListener emMessageListener;
    private static PersonFragment personFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(this, new PermissionsResultAction() {
            @Override
            public void onGranted() {

            }

            @Override
            public void onDenied(String permission) {

            }
        });
        EMClient.getInstance().addConnectionListener(new MyConnectionListener(this));
        emMessageListener = new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                //收到消息----刷新一下当前页面喽
                conversationListFragment.refresh();
                EMClient.getInstance().chatManager().importMessages(messages);//保存到数据库
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> list) {

            }

            @Override
            public void onMessageReadAckReceived(List<EMMessage> list) {

            }

            @Override
            public void onMessageDeliveryAckReceived(List<EMMessage> list) {

            }

            @Override
            public void onMessageChanged(EMMessage emMessage, Object o) {

            }
        };
        EMClient.getInstance().chatManager().addMessageListener(emMessageListener);
    }

    private void initView() {
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        EMClient.getInstance().contactManager().setContactListener(new EMContactListener() {
            @Override
            public void onContactAdded(String username) {
                //增加了联系人时回调此方法
                KLog.e("添加好友了" + username);

                new Thread() {//需要在子线程中调用
                    @Override
                    public void run() {
                        //需要设置联系人列表才能启动fragment
                        contactListFragment.setContactsMap(getContact());
                        contactListFragment.refresh();
                    }
                }.start();
            }

            @Override
            public void onContactDeleted(String username) {
                KLog.e("好友被删除了" + username);
                //被删除时回调此方法

                new Thread() {//需要在子线程中调用
                    @Override
                    public void run() {
                        //需要设置联系人列表才能启动fragment
                        contactListFragment.setContactsMap(getContact());
                        contactListFragment.refresh();
                    }
                }.start();
            }

            @Override
            public void onContactInvited(String s, String s1) {

            }

            @Override
            public void onContactAgreed(String s) {

            }

            @Override
            public void onContactRefused(String s) {

            }
        });

        personFragment = new PersonFragment();
        contactListFragment = new EaseContactListFragment();
        new Thread() {
            @Override
            public void run() {
                contactListFragment.setContactsMap(getContact());
            }
        }.start();

        // 设置item点击事件
        contactListFragment.setContactListItemClickListener(new EaseContactListFragment.EaseContactListItemClickListener() {
            @Override
            public void onListItemClicked(EaseUser user) {
                startActivity(new Intent(MainActivity.this, ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, user.getUsername()));
            }
        });

        conversationListFragment = new EaseConversationListFragment();
        conversationListFragment.setConversationListItemClickListener(new EaseConversationListFragment.EaseConversationListItemClickListener() {
            @Override
            public void onListItemClicked(EMConversation conversation) {
                //进入聊天页面
                startActivity(new Intent(MainActivity.this, ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, conversation.getUserName()));
            }
        });
        getSupportFragmentManager().beginTransaction().add(R.id.fl_chat, conversationListFragment).commit();
    }

    @OnClick({R.id.tv_chat_list, R.id.tv_contact_list, R.id.tv_persion})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_chat_list:
//                conversationListFragment.onResume();
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_chat, conversationListFragment).commit();
                break;
            case R.id.tv_contact_list:
//                contactListFragment.refresh();
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_chat, contactListFragment).commit();
                break;
            case R.id.tv_persion:
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_chat, personFragment).commit();
                break;
        }
    }

    private Map<String, EaseUser> getContact() {
        Map<String, EaseUser> map = new HashMap<>();
        try {
            List<String> userNames = EMClient.getInstance().contactManager().getAllContactsFromServer();
//            KLog.e("......有几个好友:" + userNames.size());
            for (String userId : userNames) {
//                KLog.e("好友列表中有 : " + userId);
                map.put(userId, new EaseUser(userId));
            }
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
        return map;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatManager().removeMessageListener(emMessageListener);
    }
}
