package me.khrystal.hxdemo.app;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.hyphenate.chat.BuildConfig;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;

import me.khrystal.hxdemo.util.AppUtil;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/3/9
 * update time:
 * email: 723526676@qq.com
 */

public class MainApplication extends Application {

    private static final String TAG = MainApplication.class.getSimpleName();
    private Context appContext;
    private boolean isHXInit = false;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        initHX();
    }

    private void initHX() {
        // 获取当前id并取得进程名
        int pid = android.os.Process.myPid();
        String processAppName = AppUtil.getAppName(this, pid);
        // 如果APP启用了远程的service，此application:onCreate会被调用2次
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回

        if (processAppName == null ||!processAppName.equalsIgnoreCase(this.getPackageName())) {
            Log.e(TAG, "enter the service process!");

            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }
        if (isHXInit) {
            return;
        }
        /**
         * 环信SDK初始化配置
         * http://www.easemob.com/apidoc/android/chat3.0/classcom_1_1hyphenate_1_1chat_1_1_e_m_options.html
         */
        EMOptions options = new EMOptions();

        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);

        // TODO: 17/3/9 设置是否在第一次登录成功后 下次自动登录
        // 自动登录在以下几种情况下会被取消：

        //  用户调用了 SDK 的登出动作；
        //  用户在别的设备上更改了密码，导致此设备上自动登录失败；
        //  用户的账号被从服务器端删除；
        // 用户从另一个设备登录，把当前设备上登录的用户踢出。
        options.setAutoLogin(true);

        // 设置是否需要发送已读回执
        options.setRequireAck(false);

        // 设置是否需要发送回执
        // TODO: 17/3/10 bug 上层无法接收发送的回执
        options.setRequireDeliveryAck(false);

        // 设置是否自动接受加群邀请 如果设置为true当收到群邀请会自动同意加入
        options.setAutoAcceptGroupInvitation(false);

        // 设置主动或被动退出群组时 是否删除群聊记录
        options.setDeleteMessagesAsExitGroup(true);

        // 设置是否运行聊天室创建者离开并删除聊天室会话
        options.allowChatroomOwnerLeave(true);


        //初始化 环信SDK
        EMClient.getInstance().init(getApplicationContext(), options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(BuildConfig.DEBUG);
        isHXInit = true;
    }

    public Context getContext() {
        return appContext;
    }
}
