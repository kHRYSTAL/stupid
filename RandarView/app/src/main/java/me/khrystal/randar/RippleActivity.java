package me.khrystal.randar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import me.khrystal.widget.RippleLayout;

public class RippleActivity extends AppCompatActivity {


    RippleLayout rippleLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ripple);
        rippleLayout = (RippleLayout) findViewById(R.id.rippleLayout);
        rippleLayout.setBackgroundRes(R.mipmap.map1);
        rippleLayout.setIcon(R.mipmap.ic_launcher);
    }

    @Override
    protected void onResume() {
        super.onResume();
        rippleLayout.startScan();
    }

    @Override
    protected void onPause() {
        super.onPause();
        rippleLayout.stopScan();
    }
}
