package me.khrystal.widget;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.List;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/6/15
 * update time:
 * email: 723526676@qq.com
 */

public class NoticeView extends ViewFlipper implements View.OnClickListener {

    private Context mContext;
    private List<String> mNotices;

    public NoticeView(Context context) {
        super(context);
    }

    public NoticeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        // 轮播时间间隔为3s
        setFlipInterval(3000);
        // 内边距为5dp
        setPadding(dp2px(5f), dp2px(5f), dp2px(5f), dp2px(5f));
        // 设置enter和leave动画
        setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.anim_in));
        setOutAnimation(AnimationUtils.loadAnimation(mContext, R.anim.anim_out));
    }

    /**
     * 添加需要轮播展示的公告
     *
     * @param notices
     */
    public void addNotice(List<String> notices) {
        removeAllViews();
        mNotices = notices;
        for (int i = 0; i < notices.size(); i++) {
            // 根据公告内容构建一个TextView
            String notice = notices.get(i);
            TextView textView = new TextView(mContext);
            textView.setSingleLine();
            textView.setText(notice);
            textView.setTextSize(13f);
            textView.setEllipsize(TextUtils.TruncateAt.END);
            textView.setTextColor(Color.parseColor("#666666"));
            textView.setGravity(Gravity.CENTER_VERTICAL);
            // 将公告位置设置为TextView的tag方便点击时回调给外部
            textView.setTag(i);
            textView.setOnClickListener(this);
            // 添加到ViewFlipper
            NoticeView.this.addView(textView,
                    new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        }
    }

    @Override
    public void onClick(View view) {
        int position = (int) view.getTag();
        String notice = mNotices.get(position);
        if (mOnNoticeClickListener != null) {
            mOnNoticeClickListener.onNoticeClick(position, notice);
        }
    }

    public interface OnNoticeClickListener {
        void onNoticeClick(int position, String notice);
    }

    private OnNoticeClickListener mOnNoticeClickListener;

    private int dp2px(float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpValue, mContext.getResources().getDisplayMetrics());
    }
}
