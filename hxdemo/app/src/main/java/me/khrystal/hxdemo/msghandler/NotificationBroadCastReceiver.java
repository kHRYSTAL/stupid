package me.khrystal.hxdemo.msghandler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hyphenate.chat.EMClient;

import me.khrystal.hxdemo.activities.ChatActivity;
import me.khrystal.hxdemo.activities.LoginActivity;
import me.khrystal.hxdemo.util.Constants;

/**
 * usage:  因为 notification 点击时，控制权不在 app，此时如果 app 被 kill 或者上下文改变后，
 * 有可能对 notification 的响应会做相应的变化，所以此处将所有 notification 都发送至此类，
 * 然后由此类做分发。
 * author: kHRYSTAL
 * create time: 17/3/13
 * update time:
 * email: 723526676@qq.com
 */

public class NotificationBroadCastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!EMClient.getInstance().isLoggedInBefore()) {
            gotoLoginActivity(context);
        } else {
            gotoSingleChatActivity(context, intent);
        }
    }

    /**
     * 如果 app 上下文已经缺失，则跳转到登陆页面，走重新登陆的流程
     * @param context
     */
    private void gotoLoginActivity(Context context) {
        Intent startActivityIntent = new Intent(context, LoginActivity.class);
        startActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(startActivityIntent);
    }

    /**
     * 跳转至单聊页面
     * @param context
     * @param intent
     */
    private void gotoSingleChatActivity(Context context, Intent intent) {
        Intent startActivityIntent = new Intent(context, ChatActivity.class);
        startActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityIntent.putExtra(Constants.REFERUID, intent.getStringExtra(Constants.REFERUID));
        context.startActivity(startActivityIntent);
    }
}
