package me.khrystal.customtransition.part2;

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
        focusChangeListener = Part2TransitionController.newInstance(this);
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                focusHolder.requestFocus();
            }
        };
        setContentView(R.layout.activity_part2);
    }

    /**
     * 同时需要注意到，调用setContentView()的时候view树的结构发生了改变
     * 因此每次布局改变的时候都需要“find the View”。
     * 当然也需要去掉 focus listener ，然后重新把它和新的input view关联。
     * @param layoutResID
     */
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        if (input != null) {
            input.setOnFocusChangeListener(null);
        }
        super.setContentView(layoutResID);
        View inputDone = findViewById(R.id.input_done);
        input = findViewById(R.id.input);
        focusHolder = findViewById(R.id.focus_holder);
        input.setOnFocusChangeListener(focusChangeListener);
        inputDone.setOnClickListener(onClickListener);
    }
}
