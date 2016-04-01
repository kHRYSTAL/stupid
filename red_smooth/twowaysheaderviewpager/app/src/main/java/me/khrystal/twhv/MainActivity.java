package me.khrystal.twhv;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import me.khrystal.one.OneActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void firstClick(View view) {
        Intent intent =
                new Intent(MainActivity.this, OneActivity.class);
        startActivity(intent);
    }
}
