package me.khrystal.hiveprogressview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import me.khrystal.widget.progress.HiveProgressView;

public class MainActivity extends AppCompatActivity {

    HiveProgressView progressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressView = findViewById(R.id.progressView);
        progressView.setName("Level");
        progressView.setOnProgressListener(new HiveProgressView.OnProgressListener() {
            @Override
            public void onProgress(float currentValue) {
                Log.e("HiveProgress", "" + currentValue);
            }
        });
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (progressView != null) {
                    progressView.startAnim();
                }
            }
        });
    }
}
