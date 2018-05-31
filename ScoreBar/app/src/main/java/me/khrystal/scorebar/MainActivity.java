package me.khrystal.scorebar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import me.khrystal.widget.scorebar.ScoreBarView;

public class MainActivity extends AppCompatActivity {

    private ScoreBarView barView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button viewTestBtn = (Button) findViewById(R.id.view_test_btn);
        barView = (ScoreBarView) findViewById(R.id.score_bar);
        viewTestBtn.setOnClickListener(mOnClickListener);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.view_test_btn:
                    int value = (int) (Math.random() * 100);
                    barView.createValueChangeAnimation(value).start();
                    break;
            }
        }
    };
}
