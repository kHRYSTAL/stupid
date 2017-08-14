package me.khrystal.customnestedscrollview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ScrollView;

public class MainActivity extends AppCompatActivity {
    CustomNestedScrollView scrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scrollView = (CustomNestedScrollView) findViewById(R.id.scrollView);
        scrollView.setOnScrollStateChangeListener(new CustomNestedScrollView.OnScrollStateChangeListener() {
            @Override
            public void onNestedScrollViewStateChanged(int state) {
                Log.e("MainActivity", "" +state);
            }
        });
    }
}
