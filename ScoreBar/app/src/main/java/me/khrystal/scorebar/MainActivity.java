package me.khrystal.scorebar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import me.khrystal.widget.scorebar.ScoreBarChart;
import me.khrystal.widget.scorebar.ScoreBarView;

public class MainActivity extends AppCompatActivity {

    private ScoreBarView barView;
    private ScoreBarChart chartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button viewTestBtn = (Button) findViewById(R.id.view_test_btn);
        Button chartTestBtn = (Button) findViewById(R.id.chart_test_btn);
        barView = (ScoreBarView) findViewById(R.id.score_bar);
        chartView = (ScoreBarChart) findViewById(R.id.score_bar_chart);
        barView.setName("Test");
        viewTestBtn.setOnClickListener(mOnClickListener);
        chartTestBtn.setOnClickListener(mOnClickListener);
        setChartData();
    }

    // 为 chart 添加数据
    private void setChartData(){
        List<Pair<String,Integer>> dataList = new ArrayList<>();
        dataList.add(new Pair<>("First", 50));
        dataList.add(new Pair<>("Second", 60));
        dataList.add(new Pair<>("Third", 70));
        chartView.setChartData(dataList,1,0);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.view_test_btn:
                    int value = (int) (Math.random() * 100);
                    barView.createValueChangeAnimation(value).start();
                    break;
                case R.id.chart_test_btn:
                    chartView.startAnimator();
                    break;
            }
        }
    };
}
