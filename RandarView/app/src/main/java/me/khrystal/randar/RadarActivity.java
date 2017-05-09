package me.khrystal.randar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import me.khrystal.widget.RadarLayout;

public class RadarActivity extends AppCompatActivity {


    RadarLayout radarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radar);
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

}
