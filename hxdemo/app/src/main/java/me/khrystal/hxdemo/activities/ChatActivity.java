package me.khrystal.hxdemo.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import java.util.List;

import me.khrystal.hxdemo.R;
import me.khrystal.hxdemo.callback.EMCallbackAdapter;
import me.khrystal.hxdemo.util.Constants;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/3/10
 * update time:
 * email: 723526676@qq.com
 */

public class ChatActivity extends AppCompatActivity implements EMMessageListener, View.OnClickListener {

    // 聊天信息输入框
    private EditText mInputEdit;
    // 发送按钮
    private Button mSendButton;
    // 显示内容的TextView
    private TextView mContentText;
    // 消息监听器
    private EMMessageListener mMessageListener;
    // 当前聊天对方的uid
    private String referUid;
    // 当前会话对象
    private EMConversation mConversation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        referUid = getIntent().getStringExtra(Constants.REFERUID);
        if (TextUtils.isEmpty(referUid))
            finish();
        mMessageListener = this;
        initView();
        initConversation();
    }

    /**
     * 初始化界面
     */
    private void initView() {
        mInputEdit = (EditText) findViewById(R.id.ec_edit_message_input);
        mSendButton = (Button) findViewById(R.id.ec_btn_send);
        mContentText = (TextView) findViewById(R.id.ec_text_content);
        mContentText.setMovementMethod(new ScrollingMovementMethod());

        // 设置发送按钮的点击时间
        mSendButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ec_btn_send:
                String content = mInputEdit.getText().toString().trim();
                if (!TextUtils.isEmpty(content)) {
                    mInputEdit.setText("");
                    // 创建一条消息 第一个参数为消息内容 第二个为接受者username
                    EMMessage message = EMMessage.createTxtSendMessage(content, referUid);
                    mContentText.setText(mContentText.getText() + "\n" + content + message.getMsgTime());;
                    // 发送消息至环信
                    EMClient.getInstance().chatManager().sendMessage(message);
                    // 消息状态回调
                    message.setMessageStatusCallback(new EMCallbackAdapter() {
                        @Override
                        public void onSuccess() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ChatActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }

                        @Override
                        public void onError(int code, String error) {
                            // 发送失败
                            // TODO  刷新UI
                            Toast.makeText(ChatActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onProgress(int progress, String status) {
                            // 发送图片文件时回调
                        }
                    });

                }
                break;
        }
    }

    /**
     * 初始化会话对象
     */
    private void initConversation() {
        // 参数: 1.当前会话的目标用户或群组id, 2.会话类型 可以为空 3.如果会话不存在 是否创建会话
        mConversation = EMClient.getInstance().chatManager().getConversation(referUid, null, true);
        // 设置当前会话的未读数为 0
        mConversation.markAllMessagesAsRead();
        // 获取当前在内存中对于当前会话所有的消息的数量
        int count = mConversation.getAllMessages().size();
        // 获取当前在手机中(内存+数据库)对于当前会话的所有消息数量
        // 默认一次性显示20条 如果数据库中存在消息 且当前内存中消息数量小于20, 需要在消息上方加载数据库中的消息凑够20
        // 不足20 会加载全部
        if (count < mConversation.getAllMsgCount() && count < 20) {
            // 获取已经在列表中最上边的一条消息id
            String msgId = mConversation.getAllMessages().get(0).getMsgId();
            // 分页加载更多消息，需要传递已经加载的消息的最上边一条消息的id，以及需要加载的消息的条数
            mConversation.loadMoreMsgFromDB(msgId, 20 - count);
        }
        // 打开聊天界面获取内存中最后一条消息(最新)内容并显示
        if (mConversation.getAllMessages().size() > 0) {
            EMMessage message = mConversation.getLastMessage();
            EMTextMessageBody body = (EMTextMessageBody) message.getBody();
            // 显示消息内容和时间
            mContentText.setText(body.getMessage() + " - "
                    + body.getMessage()
                    + " <- " + message.getMsgTime());
        }
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    EMMessage message = (EMMessage) msg.obj;
                    EMTextMessageBody body = (EMTextMessageBody) message.getBody();
                    // 将新的消息内容和时间加入到textView下边
                    mContentText.setText(mContentText.getText()
                            + "\n" + body.getMessage()
                            + "<--" + message.getMsgTime());
                    break;
            }
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        // 添加消息监听
        EMClient.getInstance().chatManager().addMessageListener(mMessageListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 移除消息监听
        EMClient.getInstance().chatManager().removeMessageListener(mMessageListener);
    }

    //================ function handle message start

    /**
     * 接受到消息回调
     * @param messages
     */
    @Override
    public void onMessageReceived(List<EMMessage> messages) {
        for (EMMessage message : messages) {
            if (message.getFrom().equals(referUid)) {
                // 设置消息为已读
                mConversation.markMessageAsRead(message.getMsgId());
                // 因为消息监听回调是异步线程 所以需要handler去更新ui
                Message msg = mHandler.obtainMessage();
                msg.what = 0;
                msg.obj = message;
                mHandler.sendMessage(msg);
            } else {
                // 如果不是当前会话的消息发送通知栏通知
            }
        }
    }

    /**
     * 收到CMD 消息回调
     * @param messages
     */
    @Override
    public void onCmdMessageReceived(List<EMMessage> messages) {
        for (int i = 0; i < messages.size(); i++) {
            // 透传消息
            EMMessage cmdMessage = messages.get(i);
            EMCmdMessageBody body = (EMCmdMessageBody) cmdMessage.getBody();
            Log.i("CMDMsg", body.action());
        }
    }

    /**
     * 收到你发送的消息 对方已读的回执
     * @param messages
     */
    @Override
    public void onMessageRead(List<EMMessage> messages) {

    }

    /**
     * 你发送的消息 发送成功的回执
     * @param messages
     */
    @Override
    public void onMessageDelivered(List<EMMessage> messages) {

    }

    /**
     * 消息状态的改变
     * @param message 发生改变的消息对象
     * @param change 改变的内容
     */
    @Override
    public void onMessageChanged(EMMessage message, Object change) {

    }

    //==================== function handle message end
}
