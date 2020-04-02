package me.khrystal.rardarchart;

import androidx.appcompat.app.AppCompatActivity;
import me.khrystal.widget.radarchart.RadarData;
import me.khrystal.widget.radarchart.RadarView;

import android.graphics.Color;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RadarView radarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        radarView = findViewById(R.id.radarView);
        radarView.setMaxValue(100);
        radarView.setRotationEnable(false);
        // 雷达图数据
        List<Float> values = new ArrayList<>();
        Collections.addAll(values, 75f, 75f, 25f);
        List<String> verTextList = new ArrayList<>();
        Collections.addAll(verTextList, "做题", "词汇量", "方法技巧");
        RadarData data = new RadarData(values);
        data.setLineWidth(dp2px(3));
        data.setColor(Color.parseColor("#d1c860"));
        radarView.addData(data);
        radarView.setVertexText(verTextList);
    }

    private float dp2px(float dpValue) {
        final float scale = this.getResources().getDisplayMetrics().density;
        return dpValue * scale + 0.5f;
    }
}
