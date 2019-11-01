package me.khrystal.exercisepanel;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    NestedScrollView nestedScrollView;
    ExercisePanel exercisePanel;
    WordView wordView;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wordView = findViewById(R.id.tvOriginalArticles);
        exercisePanel = findViewById(R.id.exercisePanel);
        nestedScrollView = findViewById(R.id.nestedScrollView);
        exercisePanel.supportScroll(nestedScrollView);
    }
}
