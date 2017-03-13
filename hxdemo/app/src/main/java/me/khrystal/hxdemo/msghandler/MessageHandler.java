package me.khrystal.hxdemo.msghandler;

import android.content.Context;
import android.content.Intent;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import java.util.List;

import de.greenrobot.event.EventBus;
import me.khrystal.hxdemo.event.ImTypeMessageEvent;
import me.khrystal.hxdemo.util.Constants;
import me.khrystal.hxdemo.util.NotificationUtils;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/3/13
 * update time:
 * email: 723526676@qq.com
 */

public class MessageHandler implements EMMessageListener {

    private MessageHandler(){}
    private static Context appContext;

    public static MessageHandler messageHandler = null;

    /**
     * 获取MsgHandler Singleton
     * @return
     */
    public static MessageHandler getInstance() {
        if (null == messageHandler) {
            synchronized (MessageHandler.class) {
                if (null == messageHandler) {
                    messageHandler = new MessageHandler();
                }
            }
        }
        return messageHandler;
    }

    /**
     * 注册消息监听
     */
    public static void registMsgHandler(Context context) {
        appContext = context;
        EMClient.getInstance().chatManager().addMessageListener(getInstance());
    }
    /**
     * 解除消息监听
     */
    public static void unRegistMsgHandler() {
        EMClient.getInstance().chatManager().removeMessageListener(getInstance());
    }

    /**
     * todo
     * 因为没有 db，所以暂时先把消息广播出去，由接收方自己处理
     * 稍后应该加入 db
     * @param message
     * @param conversation
     */
    private void sendEvent(EMMessage message, EMConversation conversation) {
        ImTypeMessageEvent event = new ImTypeMessageEvent();
        event.message = message;
        event.conversation = conversation;
        EventBus.getDefault().post(event);
    }

    private void sendNotification(EMMessage message, EMConversation conversation) {
        String notificationContent = message instanceof EMMessage ?
                (message.getBody().toString()) : "不支持的消息类型";

        Intent intent = new Intent(appContext, NotificationBroadCastReceiver.class);
        intent.putExtra(Constants.CONVERSATION_ID, conversation.conversationId());
        intent.putExtra(Constants.REFERUID, message.getFrom());
        NotificationUtils.showNotification(appContext, "", notificationContent, null, intent);
    }


    /**
     * 接受消息
     * @param messages
     */
    @Override
    public void onMessageReceived(List<EMMessage> messages) {
        String clientID = "";
        try {
            clientID = EMClient.getInstance().getCurrentUser();
            for (EMMessage msg : messages) {
                // 过滤掉自己发送的消息
                if (!msg.getFrom().equals(clientID)) {
                    EMConversation conversation = EMClient.getInstance().chatManager().getConversation(msg.getFrom(), null, false);
                    sendEvent(msg, conversation);
                    if (NotificationUtils.isShowNotification(conversation.conversationId())) {
                        sendNotification(msg, conversation);
                    }
                }
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    /**
     * 接受透传消息
     * @param messages
     */
    @Override
    public void onCmdMessageReceived(List<EMMessage> messages) {

    }

    /**
     * 收到已读回执
     * @param messages
     */
    @Override
    public void onMessageRead(List<EMMessage> messages) {

    }

    /**
     * 收到已经送达回执
     * @param messages
     */
    @Override
    public void onMessageDelivered(List<EMMessage> messages) {

    }

    /**
     * 收到消息状态改变通知
     * @param message
     * @param change
     */
    @Override
    public void onMessageChanged(EMMessage message, Object change) {

    }
}
