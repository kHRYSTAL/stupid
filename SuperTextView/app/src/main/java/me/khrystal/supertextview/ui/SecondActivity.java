package me.khrystal.supertextview.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

import me.khrystal.supertextview.R;
import me.khrystal.supertextview.adjuster.MoveEffectAdjuster;
import me.khrystal.supertextview.adjuster.Ripple2Adjuster;
import me.khrystal.supertextview.adjuster.RippleAdjuster;
import me.khrystal.supertextview.utils.LogUtils;
import me.khrystal.widget.supertextview.SuperTextView;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/6/14
 * update time:
 * email: 723526676@qq.com
 */

public class SecondActivity extends AppCompatActivity {
    private SuperTextView btn;
    private SuperTextView stv2;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        btn = findViewById(R.id.btn);
        btn.setAdjuster(new RippleAdjuster(getResources().getColor(R.color.opacity_5_a58fed)));
        btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                LogUtils.e("onTouch");
                return false;
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogUtils.e("onClick");
            }
        });

        stv2 = findViewById(R.id.stv_2);
        stv2.addAdjuster(new MoveEffectAdjuster().setOpportunity(SuperTextView.Adjuster.Opportunity.BEFORE_TEXT));
        stv2.addAdjuster(new Ripple2Adjuster(getResources().getColor(R.color.opacity_9_a58fed)));
        stv2.setAutoAdjust(true).startAnim();

        findViewById(R.id.stv_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SecondActivity.this, ThirdActivity.class));
            }
        });
    }
}
