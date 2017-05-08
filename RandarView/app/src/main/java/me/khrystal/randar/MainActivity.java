package me.khrystal.randar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import me.khrystal.widget.RadarLayout;
import me.khrystal.widget.RadarView;

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
        radarLayout.startScan();
    }
}
