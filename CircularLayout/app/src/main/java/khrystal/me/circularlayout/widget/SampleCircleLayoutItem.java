package khrystal.me.circularlayout.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.Toast;

import me.khrystal.widget.circularlayout.CircularLayout;
import me.khrystal.widget.circularlayout.CircularLayoutItem;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/3/5
 * update time:
 * email: 723526676@qq.com
 */
public class SampleCircleLayoutItem extends CircularLayoutItem<Integer> {

    public SampleCircleLayoutItem(Context context) {
        super(context);
    }

    public SampleCircleLayoutItem(Context context, CircularLayout circularLayout) {
        super(context, circularLayout);
        initialize();
    }

    public SampleCircleLayoutItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public SampleCircleLayoutItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private void initialize() {
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick() {
                Toast.makeText(getContext(), "Item number: " + getIndex(), Toast.LENGTH_SHORT).show();
            }
        });

        this.setOnFocusListener(new OnFocusListener() {
            @Override
            public void onFocus() {

            }

            @Override
            public void onUnFocus() {

            }
        });
    }
}
