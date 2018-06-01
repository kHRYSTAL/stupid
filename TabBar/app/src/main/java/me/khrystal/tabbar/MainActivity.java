package me.khrystal.tabbar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import me.khrystal.widget.BarItem;
import me.khrystal.widget.TabBar;
import me.khrystal.widget.listener.OnBarItemClickListener;

public class MainActivity extends AppCompatActivity {

    private TabBar tabBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabBar = findViewById(R.id.bottomBar);
        tabBar.setOnBarItemClickListener(new OnBarItemClickListener() {
            @Override
            public void onBarItemClick(BarItem barItem, int position) {
                Toast.makeText(MainActivity.this,
                        String.valueOf(position), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
