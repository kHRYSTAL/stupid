package me.khrystal.widget.textview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/8/24
 * update time:
 * email: 723526676@qq.com
 */
@SuppressLint("AppCompatCustomView")
public class RandomTextView extends TextView {

    // 高位快
    public static final int FASTER_VELOCITY_FIRST = 0;
    // 低位快
    public static final int FASTER_VELOCITY_LAST = 1;
    // 速度相同
    public static final int UNIFORM_VELOCITY_ALL = 2;
    // 自定义速度
    public static final int CUSTOM_VELOCITY = 3;
    // 偏移类型
    private int offsetSpeedType;
    // 滚动总行数
    private int scrollLineCount;



    public RandomTextView(Context context) {
        super(context);
    }

    public RandomTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RandomTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RandomTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
