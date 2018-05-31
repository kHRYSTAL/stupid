package me.khrystal.widget.scorebar;

import android.animation.AnimatorSet;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/5/31
 * update time:
 * email: 723526676@qq.com
 */

public class ScoreBarChart extends LinearLayout {

    private int mLineSpace = dp2px(16);
    // animator set
    private AnimatorSet animatorSet;

    public ScoreBarChart(Context context) {
        this(context, null);
    }

    public ScoreBarChart(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScoreBarChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttrs(context, attrs, defStyleAttr);
        initView();
    }

    private void getAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ScoreBarChat,
                defStyleAttr, 0);
        try {
            mLineSpace = ta.getDimensionPixelSize(R.styleable.ScoreBarChat_lineSpace, dp2px(16));
        } finally {
            ta.recycle();
        }
    }

    private void initView() {
        this.setOrientation(LinearLayout.VERTICAL);
    }

    public void setChartData(List<Pair<String, Integer>> data, int colorStyle, int scoreStyle) {
        if (this.getChildCount() > 0)
            updateChartData(data, colorStyle, scoreStyle);
        else
            addChartData(data, colorStyle, scoreStyle);
    }

    public void addChartData(List<Pair<String, Integer>> data, int colorStyle, int scoreStyle) {
        if (data == null || data.size() <= 0)
            return;
        ScoreBarView[] barViews = new ScoreBarView[data.size()];
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, mLineSpace);
        animatorSet = new AnimatorSet();
        AnimatorSet.Builder builder = null;
        for (int i = 0; i < data.size(); i++) {
            barViews[i] = new ScoreBarView(getContext());
            Pair<String, Integer> pair = data.get(i);
            barViews[i].setName(i + 1 + " ." + pair.first);
            barViews[i].setColorStyle(colorStyle);
            barViews[i].setScoreStyle(scoreStyle);
            this.addView(barViews[i], layoutParams);
            if (i == 0) {
                builder = animatorSet.play(barViews[i].createValueChangeAnimation(pair.second));
            } else if (builder != null) {
                builder.with(barViews[i].createValueChangeAnimation(pair.second));
            }
        }
    }

    public void updateChartData(List<Pair<String, Integer>> data, int colorStyle, int scoreStyle) {
        if (data == null || this.getChildCount() != data.size())
            return;
        ScoreBarView[] barViews = new ScoreBarView[data.size()];
        animatorSet = new AnimatorSet();
        AnimatorSet.Builder builder = null;
        for (int i = 0; i < data.size(); i++) {
            barViews[i] = (ScoreBarView) this.getChildAt(i);
            Pair<String, Integer> pair = data.get(i);
            barViews[i].setName(i + 1 + " ." + pair.first);
            barViews[i].setColorStyle(colorStyle);
            barViews[i].setScoreStyle(scoreStyle);
            if (i == 0) {
                builder = animatorSet.play(barViews[i].createValueChangeAnimation(pair.second));
            } else if (builder != null) {
                builder.with(barViews[i].createValueChangeAnimation(pair.second));
            }
        }
    }

    public void startAnimator() {
        if (animatorSet != null) {
            animatorSet.cancel();
            animatorSet.start();
        }
    }

    public boolean isAnimatorRunning() {
        if (animatorSet != null) {
            return animatorSet.isRunning();
        }
        return false;
    }

    private int dp2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
