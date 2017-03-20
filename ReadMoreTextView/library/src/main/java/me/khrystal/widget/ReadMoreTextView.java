package me.khrystal.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/3/20
 * update time:
 * email: 723526676@qq.com
 */

public class ReadMoreTextView extends TextView {

    private int mMaxLines;
    private BufferType mBufferType = BufferType.NORMAL;
    private CharSequence mText;
    private String mMoreText;
    private String mLessText;
    private int mMoreColor;
    private int mLessColor;
    private float mToggleSize;
    private boolean ellipsizeEnd;
    private boolean expand = false;
    private OnExpandStateChangeListener mStateListener;

    private ViewTreeObserver.OnPreDrawListener mListener = new ViewTreeObserver.OnPreDrawListener() {
        @Override
        public boolean onPreDraw() {
            if (getLineCount() < mMaxLines) {
                return true;
            }
            if (expand) { // 初始化时判断是否已经展开
                final CharSequence content = createContent();
                setTextInternal(content);
                setOnClickListener(new OnClick(content));
            } else {
                final CharSequence summary = createSummary();
                setTextInternal(summary);
                setOnClickListener(new OnClick(summary));
            }
            return true;
        }
    };

    public ReadMoreTextView(Context context) {
        super(context);
        setUp();
    }

    public ReadMoreTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
        setUp();
    }

    public ReadMoreTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
        setUp();
    }

    private void initView(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ReadMoreTextView);
        mMoreText = ta.getString(R.styleable.ReadMoreTextView_rmtMoreText);
        mLessText = ta.getString(R.styleable.ReadMoreTextView_rmtLessText);
        mMoreColor = ta.getInteger(R.styleable.ReadMoreTextView_rmtMoreColor, Color.GRAY);
        mLessColor = ta.getInteger(R.styleable.ReadMoreTextView_rmtLessColor, Color.GRAY);
        ellipsizeEnd = ta.getBoolean(R.styleable.ReadMoreTextView_rmtEllipsizeEnd, false);
        mToggleSize = ta.getDimension(R.styleable.ReadMoreTextView_rmtToggleSize, 14);
        ta.recycle();
        if (TextUtils.isEmpty(mMoreText)) {
            mMoreText = context.getString(R.string.default_more);
        }
        if (TextUtils.isEmpty(mLessText)) {
            mLessText = context.getString(R.string.default_less);
        }
        // 设置末尾省略号 多行情况下失效
        setEllipsize(TextUtils.TruncateAt.END);
    }

    public void setMoreText(String more) {
        mMoreText = more;
    }

    public void setLessText(String less) {
        mLessText = less;
    }

    public void setExpandState(boolean isExpand) {
        this.expand = isExpand;
    }

    public void setOnExpandStateChangeListener(OnExpandStateChangeListener listener) {
        mStateListener = listener;
    }

    @Override
    public void setMaxLines(int maxlines) {
        mMaxLines = maxlines;
        setUp();
//        requestLayout();
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        mText = text;
        mBufferType = type;
        getViewTreeObserver().removeOnPreDrawListener(mListener);
        setUp();
        super.setText(text, type);
    }

    public void setUp() {
        if (mListener == null || mMaxLines < 1 || mText == null) {
            return;
        }
        getViewTreeObserver().addOnPreDrawListener(mListener);
    }

    /**
     * 设置文字 不带标签
     * @param text
     */
    public void setTextInternal(CharSequence text) {
        super.setText(text, mBufferType);
    }

    /**
     * 创建展开时显示的文字
     * @return
     */
    private CharSequence createContent() {
        if (mLessText == null || mLessText.length() == 0) {
            return mText;
        }
        return create(mText, mLessText, mLessColor);
    }

    /**
     * 创建折叠时显示的文字
     * @return
     */
    private CharSequence createSummary() {
        if (mMoreText == null || mMoreText.length() == 0) {
            return mText;
        }
        Layout layout = getLayout();
        int start = layout.getLineStart(mMaxLines - 1);
        int end = layout.getLineEnd(mMaxLines - 1) - start;
        CharSequence content = mText.subSequence(start, mText.length());

        float moreWidth = getPaint().measureText(mMoreText, 0, mMoreText.length());
        float maxWidth = layout.getWidth() - moreWidth;
        int len = getPaint().breakText(content, 0, content.length(), true, maxWidth, null);
        if (content.charAt(end - 1) == '\n') {
            end = end - 1;
        }
        len = Math.min(len, end);
        return create(mText.subSequence(0, start + len), mMoreText, mMoreColor);
    }

    /**
     * 通过StringBuilder 创建文字
     * @return
     */
    private Spannable create(CharSequence content, String label, int color) {
        // 如果是折叠状态且设置了末尾省略 增加省略号
        if (ellipsizeEnd && !expand) {
            content = content.subSequence(0, content.length() - 3);
            content = content + "...";
        }
        SpannableStringBuilder builder = new SpannableStringBuilder(label);
        builder.setSpan(new ForegroundColorSpan(color), 0, label.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return new SpannableStringBuilder(content).append(builder);
    }

    private class OnClick implements View.OnClickListener {
        CharSequence summary;
        CharSequence content;

        OnClick(CharSequence s) {
            this.summary = s;
        }

        @Override
        public void onClick(View v) {
            if (expand) {
                if (content == null) {
                    content = createContent();
                }
                setTextInternal(content);
            } else {
                setTextInternal(summary);
            }
            expand = !expand;
            if (mStateListener != null) {
                mStateListener.onStateChange(expand);
            }
        }
    }

    public interface OnExpandStateChangeListener {
        public void onStateChange(boolean isExpand);
    }

}
