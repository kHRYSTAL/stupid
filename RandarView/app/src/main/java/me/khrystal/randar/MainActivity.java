package me.khrystal.randar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import me.khrystal.widget.RadarLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goRipple(View view) {
        Intent intent = new Intent(MainActivity.this, RippleActivity.class);
        startActivity(intent);
    }

    public void goRadar(View view) {
        Intent intent = new Intent(MainActivity.this, RadarActivity.class);
        startActivity(intent);
    }
}
