package me.khrystal.randar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import me.khrystal.widget.RadarLayout;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    RadarLayout radarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        radarLayout = (RadarLayout) findViewById(R.id.radarLayout);
        radarLayout.setBackgroundRes(R.mipmap.map1);
        radarLayout.setIcon(R.mipmap.ic_launcher);
    }

    @Override
    protected void onResume() {
        super.onResume();
        radarLayout.startScan();
    }

    @Override
    protected void onPause() {
        super.onPause();
        radarLayout.stopScan();
    }

    public void goRipple(View view) {
        Intent intent = new Intent(MainActivity.this, RippleActivity.class);
        startActivity(intent);
    }
}
