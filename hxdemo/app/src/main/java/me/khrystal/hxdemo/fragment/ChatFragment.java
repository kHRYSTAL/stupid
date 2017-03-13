package me.khrystal.hxdemo.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import me.khrystal.hxdemo.R;
import me.khrystal.hxdemo.adapter.MultipleItemAdapter;
import me.khrystal.hxdemo.callback.EMCallbackAdapter;
import me.khrystal.hxdemo.event.ImTypeMessageEvent;
import me.khrystal.hxdemo.event.ImTypeMessageResendEvent;
import me.khrystal.hxdemo.event.InputBottomBarTextEvent;
import me.khrystal.hxdemo.util.Constants;
import me.khrystal.hxdemo.util.NotificationUtils;
import me.khrystal.hxdemo.widget.InputBottomBar;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/3/10
 * update time:
 * email: 723526676@qq.com
 */

public class ChatFragment extends Fragment {

    private static final String TAG = "ChatFragment";

    // 会话对象
    protected EMConversation mConversation;
    // 当前聊天对方的uid
    private String referUid;

    protected MultipleItemAdapter itemAdapter;
    protected RecyclerView recyclerView;
    protected LinearLayoutManager layoutManager;
    protected SwipeRefreshLayout refreshLayout;
    protected InputBottomBar inputBottomBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_chat, container, false);
        referUid = getActivity().getIntent().getStringExtra(Constants.REFERUID);
        if (TextUtils.isEmpty(referUid))
            getActivity().finish();

        initConversation();
        initView(view);

        EventBus.getDefault().register(this);
        return view;
    }

    private void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_chat_rv_chat);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_chat_srl_pullrefresh);
        inputBottomBar = (InputBottomBar) view.findViewById(R.id.fragment_chat_inputbottombar);
        inputBottomBar.setTag(referUid);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        itemAdapter = new MultipleItemAdapter();
        recyclerView.setAdapter(itemAdapter);
    }

    /**
     * 初始化会话对象
     */
    private void initConversation() {
        // 参数: 1.当前会话的目标用户或群组id, 2.会话类型 可以为空 3.如果会话不存在 是否创建会话
        mConversation = EMClient.getInstance().chatManager().getConversation(referUid, null, true);
        // 设置当前会话的未读数为 0
        mConversation.markAllMessagesAsRead();

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // 手势加载时执行
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 获取当前在内存中对于当前会话所有的消息的数量
                int count = mConversation.getAllMessages().size();
                // 获取当前在手机中(内存+数据库)对于当前会话的所有消息数量
                // 默认一次性显示20条
                Log.e(TAG, "count in memory:" + mConversation.getAllMessages().size());
                Log.e(TAG, "getAllMsgCount:" + mConversation.getAllMsgCount());
                if (count < mConversation.getAllMsgCount()) {
                    // 获取已经在列表中最上边的一条消息id
                    String msgId = mConversation.getAllMessages().get(0).getMsgId();
                    // 分页加载更多消息，需要传递已经加载的消息的最上边一条消息的id，以及需要加载的消息的条数
                    List<EMMessage> messages = mConversation.loadMoreMsgFromDB(msgId, 20);
                    itemAdapter.addMessageList(messages);
                    itemAdapter.notifyDataSetChanged();
                    // 滚动至加载出来消息的最下方
                    layoutManager.scrollToPositionWithOffset(messages.size() - 1, 0);
                    refreshLayout.setRefreshing(false);
                } else {
                    refreshLayout.setEnabled(false);
                }
            }
        });


        // 进入页面时执行 加载内存中的消息
        if (null != mConversation) {
            // 如果数据库中存在消息 且当前内存中消息数量小于20, 需要在消息上方加载数据库中的消息凑够20
            // 不足20 会加载全部
            List<EMMessage> messages = mConversation.getAllMessages();
            int count = messages.size();
            if (count < mConversation.getAllMsgCount() && count < 20) {
                // 获取已经在列表中最上边的一条消息id
                String msgId = mConversation.getAllMessages().get(0).getMsgId();
                // 分页加载更多消息，需要传递已经加载的消息的最上边一条消息的id，以及需要加载的消息的条数
                messages.addAll(0, mConversation.loadMoreMsgFromDB(msgId, 20 - count));
            }

            itemAdapter.setMessageList(messages);
            recyclerView.setAdapter(itemAdapter);
            itemAdapter.notifyDataSetChanged();
            scrollToBottom();
            refreshLayout.setEnabled(true);
        }
    }

    private void scrollToBottom() {
        layoutManager.scrollToPosition(itemAdapter.getItemCount() - 1);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != mConversation) {
            NotificationUtils.addTag(mConversation.conversationId());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (null != mConversation) {
            NotificationUtils.removeTag(mConversation.conversationId());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    // Event bus start
    /**
     * 输入事件处理，接收后构造成 AVIMTextMessage 然后发送
     * 因为不排除某些特殊情况会受到其他页面过来的无效消息，所以此处加了 tag 判断
     */
    public void onEvent(InputBottomBarTextEvent textEvent) {
        if (null != mConversation && null != textEvent) {
            if (!TextUtils.isEmpty(textEvent.sendContent) && referUid.equals(textEvent.tag)) {
                EMMessage message = EMMessage.createTxtSendMessage(textEvent.sendContent, referUid);
                itemAdapter.addMessage(message);
                itemAdapter.notifyDataSetChanged();
                scrollToBottom();
                EMClient.getInstance().chatManager().sendMessage(message);
                updateMessageStatus(message);
                ArrayList messages = new ArrayList();
                messages.add(message);
            }
        }
    }

    /**
     * 处理推送过来的消息
     * 同理，避免无效消息，此处加了 conversation id 判断
     */
    public void onEventMainThread(ImTypeMessageEvent event) {
        if (null != mConversation && null != event &&
                mConversation.conversationId().equals(event.conversation.conversationId())) {
            itemAdapter.addMessage(event.message);
            itemAdapter.notifyDataSetChanged();
            scrollToBottom();
        }
    }

    /**
     * 重新发送已经发送失败的消息
     */
    public void onEvent(ImTypeMessageResendEvent event) {
        if (null != mConversation && null != event) {
            if (EMMessage.Status.FAIL == event.message.status()
                    && referUid.equals(event.message.getTo())) {
                EMClient.getInstance().chatManager().sendMessage(event.message);
                updateMessageStatus(event.message);
                itemAdapter.notifyDataSetChanged();
            }
        }
    }

    private void updateMessageStatus(EMMessage message) {
        message.setMessageStatusCallback(new EMCallbackAdapter() {
            @Override
            public void onSuccess() {
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (itemAdapter != null) {
                            itemAdapter.notifyDataSetChanged();
                        }
                    }
                });

            }

            @Override
            public void onError(int code, String error) {
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (itemAdapter != null) {
                            itemAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        });
    }
}
