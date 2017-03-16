package me.khrystal.hxdemo.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.exceptions.HyphenateException;

import me.khrystal.hxdemo.R;
import me.khrystal.hxdemo.callback.EMCallbackAdapter;
import me.khrystal.hxdemo.util.Constants;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    // ============ 单聊 ==============
    // 输入指向对方聊天的uid ,即在环信注册时指明的这个用户唯一标识
    private EditText mReferUserET;
    // 发起单聊会话按钮
    private Button mStartChatBtn;
    // 登出按钮
    private Button mLoginOutBtn;

    // ============ 群聊 ===============
    // 目标群组Id
    private EditText mReferGroupIdET;
    // 创建群组按钮
    private Button mCreateGroupBtn;
    // 邀请该uid 进入群组
    private EditText mInviteUserIdET;
    // 邀请按钮
    private Button mInviteBtn;
    // 发起群聊会话按钮
    private Button mStartGroupChatBtn;
    // 获取我创建的第一个群组id
    private Button mFetchFirstGroupBtn;
    private EMGroup mEMGroup;

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
        // 单聊
        mReferUserET = (EditText) findViewById(R.id.ec_edit_chat_id);
        mStartChatBtn = (Button) findViewById(R.id.ec_btn_start_chat);
        mLoginOutBtn = (Button) findViewById(R.id.ec_btn_sign_out);
        mStartChatBtn.setOnClickListener(this);
        mLoginOutBtn.setOnClickListener(this);
        // 群聊
        mReferGroupIdET = (EditText) findViewById(R.id.ec_edit_group_id);
        mCreateGroupBtn = (Button) findViewById(R.id.ec_btn_create_group);
        mInviteUserIdET = (EditText) findViewById(R.id.ec_invite_user);
        mInviteBtn = (Button) findViewById(R.id.ec_btn_invite);
        mStartGroupChatBtn = (Button) findViewById(R.id.ec_btn_start_group_chat);
        mFetchFirstGroupBtn = (Button) findViewById(R.id.ec_btn_fetch_group);

        mCreateGroupBtn.setOnClickListener(this);
        mInviteBtn.setOnClickListener(this);
        mStartGroupChatBtn.setOnClickListener(this);
        mFetchFirstGroupBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ec_btn_start_chat:
                // 打开单聊
                startSingleChat();
                break;
            case R.id.ec_btn_sign_out:
                // 登出
                logOut();
                break;
            case R.id.ec_btn_create_group:
                // 创建群组
                createGroup();
                break;
            case R.id.ec_btn_invite:
                // 邀请用户进入群组
                inviteUserToGroup();
                break;
            case R.id.ec_btn_start_group_chat:
                // 打开群聊
                startGroupChat();
                break;
            case R.id.ec_btn_fetch_group:
                // 获取创建的群组id

                break;
        }
    }

    /**
     * 打开群聊界面
     */
    private void startGroupChat() {
        // 获取发起聊天指定的 groupId
        String chatId = mReferGroupIdET.getText().toString().trim();
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

    /**
     * 邀请指定uid的人进入群组
     */
    private void inviteUserToGroup() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String inviteUserId = mInviteUserIdET.getText().toString().trim();
                if (!TextUtils.isEmpty(inviteUserId) && mEMGroup != null) {
                    //群主加人调用此方法
                    try {
                        EMClient.getInstance().groupManager().addUsersToGroup(mEMGroup.getGroupId(), new String[] {inviteUserId});
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "邀请成功", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "groupId 或 inviteUserId 不能为空", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        }).start();

    }

    /**
     * 创建群组
     */
    private void createGroup() {
        // 异步
        new Thread(new Runnable() {
            @Override
            public void run() {
                EMGroupManager.EMGroupOptions option = new EMGroupManager.EMGroupOptions();
                option.maxUsers = 10;
                option.style = EMGroupManager.EMGroupStyle.EMGroupStylePrivateMemberCanInvite;
                /**
                 * 创建群组
                 * @param groupName 群组名称
                 * @param desc 群组简介
                 * @param allMembers 群组初始成员，如果只有自己传空数组即可
                 * @param reason 邀请成员加入的reason
                 * @param option 群组类型选项，可以设置群组最大用户数(默认200)及群组类型@see {@link EMGroupStyle}
                 *               option.inviteNeedConfirm表示邀请对方进群是否需要对方同意，默认是需要用户同意才能加群的。
                 *               option.extField创建群时可以为群组设定扩展字段，方便个性化订制。
                 *
                 * @return 创建好的group
                 * @throws HyphenateException
                 *
                 * EMGroupStylePrivateOnlyOwnerInvite——私有群，只有群主可以邀请人；
                 * EMGroupStylePrivateMemberCanInvite——私有群，群成员也能邀请人进群；
                 * EMGroupStylePublicJoinNeedApproval——公开群，加入此群除了群主邀请，只能通过申请加入此群；
                 * EMGroupStylePublicOpenJoin ——公开群，任何人都能加入此群。
                 */
                mEMGroup = null;
                try {
                    mEMGroup = EMClient.getInstance().groupManager().createGroup("testPrivate", "groupDesc", new String[0], "inviteReason", option);
                    final EMGroup tempGroup = mEMGroup;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mReferGroupIdET.setText(tempGroup.getGroupId());
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    /**
     * 退出登录
     */
    private void logOut() {
       EMClient.getInstance().logout(false, new EMCallbackAdapter() {
           @Override
           public void onSuccess() {
               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       Toast.makeText(MainActivity.this, "退出成功", Toast.LENGTH_SHORT).show();
                   }
               });
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
     * 验证后跳转到单聊聊天界面
     */
    private void startSingleChat() {
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
