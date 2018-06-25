package me.khrystal.bouncescrollview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.transparentStatusBar(this);
        setContentView(R.layout.activity_main);
        Toolbar mToolbar = findViewById(R.id.mToolbar);
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mToolbar.getLayoutParams();
        lp.topMargin = Util.getStatusBarHeight();
        mToolbar.setLayoutParams(lp);

        findViewById(R.id.mHouseStarkTV).setOnClickListener(this);
        findViewById(R.id.mHouseLannisterTV).setOnClickListener(this);
        findViewById(R.id.mHouseTargaryenTV).setOnClickListener(this);
        findViewById(R.id.mHouseBaratheonTV).setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.mHouseStarkTV:
                intent.setClass(MainActivity.this, HouseStarkActivity.class);
                break;
            case R.id.mHouseTargaryenTV:
                intent.setClass(MainActivity.this, HouseTargaryenActivity.class);
                break;
            case R.id.mHouseLannisterTV:
                intent.setClass(MainActivity.this, HouseLannisterActivity.class);
                break;
            case R.id.mHouseBaratheonTV:
                intent.setClass(MainActivity.this, HouseBaratheonActivity.class);
                break;
        }
        startActivity(intent);
    }
}
