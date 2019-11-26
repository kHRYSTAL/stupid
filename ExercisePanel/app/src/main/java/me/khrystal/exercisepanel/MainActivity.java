package me.khrystal.exercisepanel;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    NestedScrollView nestedScrollView;
    ExercisePanel exercisePanel;
    ExercisePanel belowPanel;
    WordView wordView;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wordView = findViewById(R.id.tvOriginalArticles);
        exercisePanel = findViewById(R.id.exercisePanel);
        belowPanel = findViewById(R.id.belowPanel);
        nestedScrollView = findViewById(R.id.nestedScrollView);

        exercisePanel.supportScroll(nestedScrollView); // 设置textView是否支持滚动
        belowPanel.setNestedScrollView(nestedScrollView); // 设置显示放大镜时 可滑动布局不可滑动
    }
}
