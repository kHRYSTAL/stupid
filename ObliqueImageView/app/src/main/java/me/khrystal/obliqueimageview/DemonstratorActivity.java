package me.khrystal.obliqueimageview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.khrystal.widget.oblique.ObliqueImageView;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/5/5
 * update time:
 * email: 723526676@qq.com
 */

public class DemonstratorActivity extends AppCompatActivity {

    @BindView(R.id.txt1)
    TextView txt1;
    @BindView(R.id.seek1)
    AppCompatSeekBar seek1;
    @BindView(R.id.txt2)
    TextView txt2;
    @BindView(R.id.seek2)
    AppCompatSeekBar seek2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demonstrator);
        ButterKnife.bind(this);
        final ObliqueImageView imageView = (ObliqueImageView) findViewById(R.id.obliqueView);
        seek1.setMax(180);
        seek1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                imageView.setStartAngle(progress);
                txt1.setText("Starting side Slanting angle " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seek2.setMax(180);
        seek2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                imageView.setEndAngle(progress);
                txt2.setText("Ending side Slanting angle " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
