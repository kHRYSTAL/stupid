package me.khrystal.widget.autoscroll;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListAdapter;
import android.widget.ViewFlipper;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/7/24
 * update time:
 * email: 723526676@qq.com
 */
public class VerticalScrollLayout extends ViewFlipper {

    private ListAdapter adapter;
    private boolean isSetAnimDuration = false;
    private int interval = 2000;

    private int animDuration = 500;

    public VerticalScrollLayout(Context context) {
        this(context, null);
    }

    public VerticalScrollLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.VerticalScrollLayout);
        animDuration = ta.getInt(R.styleable.VerticalScrollLayout_vsl_animDuration, animDuration);
        isSetAnimDuration = ta.getBoolean(R.styleable.VerticalScrollLayout_vsl_isCusDuration, false);
        interval = ta.getInt(R.styleable.VerticalScrollLayout_vsl_sleepTime, interval);
        ta.recycle();
        setFlipInterval(interval);
        Animation animIn = AnimationUtils.loadAnimation(getContext(), R.anim.anim_scroll_in);
        Animation animOut = AnimationUtils.loadAnimation(getContext(), R.anim.anim_scroll_out);
        if (isSetAnimDuration) {
            animIn.setDuration(animDuration);
            animOut.setDuration(animDuration);
        }

        setInAnimation(animIn);
        setOutAnimation(animOut);
    }

    private DataSetObserver mDataObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            setupChildren();
        }

        @Override
        public void onInvalidated() {
            setupChildren();
        }
    };

    public void setAdapter(ListAdapter adapter) {
        if (this.adapter != null) {
            this.adapter.unregisterDataSetObserver(mDataObserver);
        }
        this.adapter = adapter;

        if (this.adapter != null) {
            this.adapter.registerDataSetObserver(mDataObserver);
        }
        setupChildren();
    }

    private void setupChildren() {
        if (this.adapter == null || this.adapter.getCount() == 0)
            return;
        for (int i = 0; i < adapter.getCount(); i++) {
            View child = this.adapter.getView(i, null, this);
            if (child == null) {
                throw new NullPointerException("View can not be null!");
            } else {
                addView(child);
            }

        }

        startFlipping();
    }
}
