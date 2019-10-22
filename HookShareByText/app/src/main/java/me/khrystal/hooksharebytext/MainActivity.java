package me.khrystal.hooksharebytext;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Arrays;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String[] stringItem = {
            "https://gd2.alicdn.com/imgextra/i1/2259324182/TB2sdjGm0BopuFjSZPcXXc9EpXa_!!2259324182.jpg",
            "https://gd2.alicdn.com/imgextra/i1/2259324182/TB2sdjGm0BopuFjSZPcXXc9EpXa_!!2259324182.jpg"
    };

    private Button shareBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        shareBtn = findViewById(R.id.shareBtn);
        shareBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        // 应用内更新需要存储权限
        RunTimePermissionMgr.getInstance().requestPermission(this, new RunTimePermissionGrantedListener() {
            @Override
            public void onGranted() {
                ShareUtil.getInstance(MainActivity.this).share(Constants.FRIENDQSHARE, "测试携带文字", Arrays.asList(stringItem));
            }
        }, RunTimePermissionMgr.STORAGE);
    }
}
