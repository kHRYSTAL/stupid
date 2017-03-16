package me.khrystal.hxdemo.msghandler;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.hyphenate.EMGroupChangeListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

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

public class MessageHandler implements EMMessageListener, EMGroupChangeListener {

    private static final String TAG = MessageHandler.class.getSimpleName();

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
        EMClient.getInstance().groupManager().addGroupChangeListener(getInstance());
    }
    /**
     * 解除消息监听
     */
    public static void unRegistMsgHandler() {
        EMClient.getInstance().chatManager().removeMessageListener(getInstance());
        EMClient.getInstance().groupManager().removeGroupChangeListener(getInstance());
    }

    /**
     * 由接收方自己处理
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
        String notificationContent = message.getBody() instanceof EMTextMessageBody ?
                (message.getBody().toString()) : "不支持的消息类型";

        Intent intent = new Intent(appContext, NotificationBroadCastReceiver.class);
        if (message.getChatType().equals(EMMessage.ChatType.GroupChat)) {
            Log.e(TAG, "" + message.getChatType() + ",send isGroup true");
            intent.putExtra(Constants.IS_GROUPCHAT, true);
        }
        intent.putExtra(Constants.REFERUID, message.getChatType() == EMMessage.ChatType.GroupChat ? message.getTo() : message.getFrom());
        NotificationUtils.showNotification(appContext, "", notificationContent, null, intent);
    }


    /**
     * 接受消息
     * @param messages
     */
    @Override
    public void onMessageReceived(List<EMMessage> messages) {
        Log.e(TAG, "onMessageReceived");
        String clientID = "";
        try {
            clientID = EMClient.getInstance().getCurrentUser();
            for (EMMessage msg : messages) {
                EMConversation conversation = null;
                // 过滤掉自己发送的消息
//                if (!msg.getFrom().equals(clientID)) {
//                    if (msg.getChatType() == EMMessage.ChatType.GroupChat) {
//                        // 群聊发送目标为群
//                        conversation = EMClient.getInstance().chatManager().getConversation(msg.getTo(), null, false);
//                    } else
//                        conversation = EMClient.getInstance().chatManager().getConversation(msg.getFrom(), null, false);
//                    sendEvent(msg, conversation);
//                    if (NotificationUtils.isShowNotification(conversation.conversationId())) {
//                        sendNotification(msg, conversation);
//                    }
//                }

                if (msg.getChatType() == EMMessage.ChatType.GroupChat) { // 群聊
                    Log.e(TAG, msg.getTo() + "," + msg.getFrom());
                    // 群聊消息目标是群组 groupId
                    conversation = EMClient.getInstance().chatManager().getConversation(msg.getTo(), null, false);

                } else if (msg.getChatType() == EMMessage.ChatType.Chat) { // 单聊
                    // 单聊消息目标是自己 来自对方 userId
                    conversation = EMClient.getInstance().chatManager().getConversation(msg.getFrom(), null, false);
                }

                sendEvent(msg, conversation); // 发送eventbus
                if (NotificationUtils.isShowNotification(conversation.conversationId())) {
                    sendNotification(msg, conversation); // 如果不在当前聊天界面 发送通知
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

    //========================GROUP LISTENER START========================

    /**
     * 收到群组邀请
     */
    @Override
    public void onInvitationReceived(String groupId, String groupName, String inviter, String reason) {
        Log.e(TAG, "onInvitationReceived groupId:" + groupId + ",groupName" + groupName);
    }

    /**
     *  用户申请加入群
     */
    @Override
    public void onRequestToJoinReceived(String groupId, String groupName, String applicant, String reason) {
        Log.e(TAG, "onRequestToJoinReceived groupId:" + groupId + ",groupName" + groupName);
    }

    /**
     * 加群申请被对方接受
     */
    @Override
    public void onRequestToJoinAccepted(String groupId, String groupName, String accepter) {
        Log.e(TAG, "onRequestToJoinAccepted groupId:" + groupId + ",groupName" + groupName);
    }

    /**
     * 加群申请被拒绝
     */
    @Override
    public void onRequestToJoinDeclined(String groupId, String groupName, String decliner, String reason) {
        Log.e(TAG, "onRequestToJoinDeclined groupId:" + groupId + ",groupName" + groupName);
    }

    /**
     * 群组邀请被接受
     */
    @Override
    public void onInvitationAccepted(String groupId, String invitee, String reason) {
        Log.e(TAG, "onInvitationAccepted groupId:" + groupId);
    }

    /**
     * 群组邀请被拒绝
     */
    @Override
    public void onInvitationDeclined(String groupId, String invitee, String reason) {
        Log.e(TAG, "onInvitationDeclined groupId:" + groupId);
    }

    /**
     * 当前登录用户被管理员移除出群组
     */
    @Override
    public void onUserRemoved(String groupId, String groupName) {

    }

    /**
     * 群组被解散。
     */
    @Override
    public void onGroupDestroyed(String groupId, String groupName) {

    }

    /**
     * 自动同意加入群组
     */
    @Override
    public void onAutoAcceptInvitationFromGroup(String groupId, String inviter, String inviteMessage) {
        Log.e(TAG, "onAutoAcceptInvitationFromGroup groupId:" + groupId);
    }

    /**
     * 有成员被禁言，此处不同于blacklist
     */
    @Override
    public void onMuteListAdded(String groupId, List<String> mutes, long muteExpire) {

    }

    /**
     * 有成员从禁言列表中移除，恢复发言权限，此处不同于blacklist
     */
    @Override
    public void onMuteListRemoved(String groupId, List<String> mutes) {

    }

    /**
     * 添加成员管理员权限
     */
    @Override
    public void onAdminAdded(String groupId, String administrator) {

    }

    /**
     * 取消某管理员权限
     */
    @Override
    public void onAdminRemoved(String groupId, String administrator) {

    }

    /**
     * 转移群组所有者权限
     */
    @Override
    public void onOwnerChanged(String groupId, String newOwner, String oldOwner) {

    }
}
