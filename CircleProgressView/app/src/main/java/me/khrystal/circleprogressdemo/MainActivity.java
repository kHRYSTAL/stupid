package me.khrystal.circleprogressdemo;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import me.khrystal.widget.CircleProgressView;

public class MainActivity extends AppCompatActivity implements CircleProgressView.OnProgressListener {

    private CircleProgressView mCircleRingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCircleRingView = (CircleProgressView) findViewById(R.id.circle_view);
        mCircleRingView.setColor(Color.GREEN);
        mCircleRingView.setSecondColor(Color.RED);
        mCircleRingView.setCenterTextSize(40);

        mCircleRingView.setOnProgressListener(this); // 此处有先后顺序 要先设置监听器 在setProgress时才能触发回调设置上文字
        // 也可以手动设置文字
//        mCircleRingView.setCenterText("70%");

        mCircleRingView.setProgress(70);
        mCircleRingView.setSecondaryProgress(80);


    }


    public void startAnim(View view) {
        mCircleRingView.startAnim();
    }

    public void stopAnim(View view) {
        mCircleRingView.stopAnim();
    }

    @Override
    public void onProgress(float currentValue) {
        mCircleRingView.setCenterText(String.valueOf((int) (100 * currentValue)) + "%");
    }
}
