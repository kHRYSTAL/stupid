package me.khrystal.flipviewpager.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import java.io.File;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/4/29
 * update time:
 * email: 723526676@qq.com
 */
@SuppressLint("AppCompatCustomView")
public class FontTextView extends TextView {

    public FontTextView(Context context) {
        super(context);
    }

    public FontTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        parseAttributes(context, attrs);
    }

    public FontTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parseAttributes(context, attrs);
    }

    private void parseAttributes(Context context, AttributeSet attrs) {
        setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts" + File.separator + "Roboto-Light.ttf"));
    }
}
