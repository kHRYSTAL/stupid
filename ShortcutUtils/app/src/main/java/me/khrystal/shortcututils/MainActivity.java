package me.khrystal.shortcututils;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnCreate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ShortUtil.getInstance(MainActivity.this).hasShortcut(getString(R.string.app_name))) {
                    ShortUtil.getInstance(MainActivity.this)
                            .installShortCut(getResources().getString(R.string.app_name),
                                    R.mipmap.ic_launcher, new Intent(MainActivity.this, MainActivity.class));
                }
            }
        });

        findViewById(R.id.btnDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShortUtil.getInstance(MainActivity.this).deleteShortcut(MainActivity.this,
                        getResources().getString(R.string.app_name), new Intent(MainActivity.this, MainActivity.class), true);
            }
        });

        findViewById(R.id.btnHasShortcut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean b = ShortUtil.getInstance(MainActivity.this).hasShortcut(getString(R.string.app_name));
                Toast.makeText(MainActivity.this, "" + b, Toast.LENGTH_SHORT).show();
            }
        });
    }


}
