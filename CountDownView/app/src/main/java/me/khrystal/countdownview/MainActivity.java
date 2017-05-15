package me.khrystal.countdownview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import me.khrystal.widget.countdownview.CountdownView;
import me.khrystal.widget.countdownview.DynamicConfig;

public class MainActivity extends AppCompatActivity {

    CountdownView countdownView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        countdownView = (CountdownView) findViewById(R.id.countdownView);
        countdownView.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
            @Override
            public void onEnd(CountdownView cv) {
                Toast.makeText(MainActivity.this, "End", Toast.LENGTH_SHORT).show();
            }
        });

        countdownView.setOnCountdownIntervalListener(1000, new CountdownView.OnCountdownIntervalListener() {

            boolean isConfig;
            @Override
            public void onInterval(CountdownView cv, long remainTime) {
                if (remainTime <= 30 * 1000 && !isConfig) {
                    isConfig = true;
                    DynamicConfig config = new DynamicConfig.Builder().setShowDay(false)
                            .setShowHour(false)
                            .setShowMinute(true)
                            .setConvertHoursToMinutes(true)
                            .setSuffixMinute("分")
                            .setSuffixSecond("秒").build();
                    cv.dynamicShow(config);
                    cv.start(1000 * 60 * 120);

                }
            }
        });
    }

    public void onStart(View view) {
        if (countdownView != null) {
            countdownView.start(1000 * 60);
            DynamicConfig config = new DynamicConfig.Builder().setShowDay(false)
                    .setShowHour(false)
                    .setShowMinute(false)
                    .setSuffixSecond("秒").build();
            countdownView.dynamicShow(config);
        }
    }

    public void onStop(View view) {
        if (countdownView != null) {
            countdownView.stop();
        }
    }

    public void onPause(View view) {
        if (countdownView != null) {
            countdownView.pause();
        }
    }

    public void onRestart(View view) {
        if (countdownView != null) {
            countdownView.restart();
        }
    }
}
