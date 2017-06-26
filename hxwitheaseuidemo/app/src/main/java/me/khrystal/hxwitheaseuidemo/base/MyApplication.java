package me.khrystal.hxwitheaseuidemo.base;

import android.app.Application;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.controller.EaseUI;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/6/26
 * update time:
 * email: 723526676@qq.com
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        EMOptions options = new EMOptions();
        options.setAcceptInvitationAlways(true);
        EaseUI.getInstance().init(this, options);

        EMClient.getInstance().updateCurrentUserNick(getSharedPreferences(GlobalField.USERINFO_FILENAME, MODE_PRIVATE).getString("username", "hdl"));
    }
}
