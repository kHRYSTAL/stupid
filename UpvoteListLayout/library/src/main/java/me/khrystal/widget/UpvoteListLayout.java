package me.khrystal.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/7/4
 * update time:
 * email: 723526676@qq.com
 */

public class UpvoteListLayout extends ViewGroup {

    private Context mContext;

    private List<String> allUrls = new ArrayList<>();

    private boolean LTR = true;

    private int spWidth = 20;

    private List<List<View>> mAllViews = new ArrayList<>();

    private List<Integer> mLineHeight = new ArrayList<>();

    private OnLoadImageListener loadImageListener;

    public UpvoteListLayout(Context context) {
        super(context);
        mContext = context;

    }

    public UpvoteListLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public UpvoteListLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        // wrap_content
        int width = 0;
        int height = 0;

        int lineWidth = 0;
        int lineHeight = 0;

        int cCount = getChildCount();
        for (int i = 0; i < cCount; i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            if (lineWidth + childWidth > sizeWidth - getPaddingLeft() - getPaddingRight()) {
                // judge next line
                height += lineHeight;
                // new line
                lineHeight = childHeight;
                width = Math.max(width, lineWidth);
                lineWidth = childWidth;
            } else {
                // append this line
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight, childHeight);
            }

            if (i == cCount - 1) {
                width = Math.max(lineWidth, width);
                height += lineHeight;
            }
        }

        setMeasuredDimension(modeWidth == MeasureSpec.EXACTLY ? sizeWidth : width,
                modeHeight == MeasureSpec.EXACTLY ? sizeHeight : height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mAllViews.clear();
        mLineHeight.clear();

        int width = getWidth();
        int lineWidth = 0;
        int lineHeight = 0;
        List<View> lineViews = new ArrayList<>();
        int cCount = getChildCount();
        for (int i = 0; i < cCount; i++) {
            View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            if (childWidth + lineWidth + lp.leftMargin + lp.rightMargin > width - getPaddingLeft() - getPaddingRight()) {
                mLineHeight.add(lineHeight);
                mAllViews.add(lineViews);
                lineViews = new ArrayList<>();
                lineWidth = 0;
                lineHeight = childHeight + lp.topMargin + lp.bottomMargin;
            }
            lineWidth += childWidth + lp.leftMargin + lp.rightMargin - spWidth;
            lineHeight = Math.max(lineHeight, childHeight + lp.topMargin + lp.bottomMargin);
            lineViews.add(child);
        }

        mLineHeight.add(lineHeight);
        mAllViews.add(lineViews);

        // set child view position
        int left = getPaddingLeft();
        int top = getPaddingTop();

        // line number
        int lineNum = mAllViews.size();
        for (int i = 0; i < lineNum; i++) {
            lineViews = mAllViews.get(i);
            lineHeight = mLineHeight.get(i);

            // judge LTR
            if (LTR) {
                Collections.reverse(lineViews);
            }

            for (int j = 0; j < lineViews.size(); j++) {
                View child = lineViews.get(j);
                if (child.getVisibility() == GONE) {
                    continue;
                }

                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                int lc = left + lp.leftMargin;
                int tc = top + lp.topMargin;
                int rc = lc + child.getMeasuredWidth();
                int bc = tc + child.getMeasuredHeight();

                child.layout(lc, tc, rc, bc);
                left += child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin - spWidth;
            }

            left = getPaddingLeft();
            top += lineHeight;
        }
    }

    public void setSpWidth(int spWidth) {
        this.spWidth = spWidth;
    }

    public void setLTR(boolean ltr) {
        this.LTR = ltr;
    }

    public void setUrls(List<String> lisVals) {
        allUrls.addAll(lisVals);
        setViews();
    }

    public void setOneUrls(String urlVal) {
        allUrls.add(urlVal);
        setViews();
    }

    public void cancels(String urlVal) {
        allUrls.remove(urlVal);
        setViews();
    }

    private void setViews() {
        removeAllViews();
        for (int i = 0; i < allUrls.size(); i++) {
            ImageView imageView = new ImageView(mContext);
            LayoutParams params = new LayoutParams(DensityUtil.dp2px(36, mContext).intValue(),
                    DensityUtil.dp2px(36, mContext).intValue());
            imageView.setLayoutParams(params);
            if (loadImageListener != null) {
                loadImageListener.onLoadImage(imageView, allUrls.get(i));
            }
            this.addView(imageView);
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attr) {
        return new MarginLayoutParams(getContext(), attr);
    }

    public void setOnLoadImageListener(OnLoadImageListener loadImageListener) {
        this.loadImageListener = loadImageListener;
    }

    public interface OnLoadImageListener {
        void onLoadImage(ImageView view, String url);
    }
}
