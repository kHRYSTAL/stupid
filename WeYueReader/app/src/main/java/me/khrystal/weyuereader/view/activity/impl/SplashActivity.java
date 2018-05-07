package me.khrystal.weyuereader.view.activity.impl;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import me.khrystal.weyuereader.R;
import me.khrystal.weyuereader.view.base.BaseActivity;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/5/7
 * update time:
 * email: 723526676@qq.com
 */
public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            SplashActivity.this.startActivity(new Intent(SplashActivity.this,
                    MainActivity.class));
            finish();
        }, 1000);
    }
}
