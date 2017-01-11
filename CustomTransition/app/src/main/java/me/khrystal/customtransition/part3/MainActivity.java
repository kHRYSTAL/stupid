package me.khrystal.customtransition.part3;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import me.khrystal.customtransition.R;
import me.khrystal.customtransition.TransitionController;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/1/11
 * update time:
 * email: 723526676@qq.com
 */

public class MainActivity extends AppCompatActivity {

    private View input;
    private TransitionController focusChangeListener;
    private View.OnClickListener onClickListener;
    private View focusHolder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        focusChangeListener = Part3TransitionController.newInstance(this);
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                focusHolder.requestFocus();
            }
        };
        setContentView(R.layout.activity_part2);
    }


    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        if (input != null) {
            input.setOnFocusChangeListener(null);
        }
        super.setContentView(layoutResID);
        input = findViewById(R.id.input);
        View inputDone = findViewById(R.id.input_done);
        focusHolder = findViewById(R.id.focus_holder);
        input.setOnFocusChangeListener(focusChangeListener);
        inputDone.setOnClickListener(onClickListener);
    }
}
