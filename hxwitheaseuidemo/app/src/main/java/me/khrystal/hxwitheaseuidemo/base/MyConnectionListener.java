package me.khrystal.hxwitheaseuidemo.base;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.util.NetUtils;
import com.socks.library.KLog;

import me.khrystal.hxwitheaseuidemo.ui.LoginActivity;

/**
 * usage: 实现ConnectionListener接口-----监听网络状态---在另外一台手机登录----账号被移除
 * author: kHRYSTAL
 * create time: 17/6/26
 * update time:
 * email: 723526676@qq.com
 */

public class MyConnectionListener implements EMConnectionListener {

    Activity context;

    public MyConnectionListener(Activity context) {
        this.context = context;
    }

    @Override
    public void onConnected() {

    }

    @Override
    public void onDisconnected(final int error) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                KLog.e(""+ error);
                if (error == EMError.USER_REMOVED)
                    Toast.makeText(context, "账号已经被移除", Toast.LENGTH_SHORT).show();
                else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                    Toast.makeText(context, "账号在其他设备登录, 被迫下线", Toast.LENGTH_SHORT).show();;
                    EMClient.getInstance().logout(true, new EMCallBack() {
                        @Override
                        public void onSuccess() {
                            Log.e("main", "下线成功");
                        }

                        @Override
                        public void onError(int i, String s) {
                            Log.e("main", "下线失败," + s);
                        }

                        @Override
                        public void onProgress(int i, String s) {

                        }
                    });// 下线
                    context.startActivity(new Intent(context, LoginActivity.class));
                    context.finish();
                } else {
                    if (NetUtils.hasNetwork(context)) {
                        //连接不到聊天服务器
                        Toast.makeText(context, "连接不到聊天服务器", Toast.LENGTH_SHORT).show();
                    } else {
                        //当前网络不可用，请检查网络设置
                        Toast.makeText(context, "当前网络不可用，请检查网络设置", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
