package me.khrystal.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.DynamicLayout;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/3/20
 * update time:
 * email: 723526676@qq.com
 */

public class ExpandableTextView extends TextView {

    private static final String TAG = ExpandableTextView.class.getSimpleName();

    public static final int STATE_SHRINK = 0;
    public static final int STATE_EXPAND = 1;

    private static final String CLASS_NAME_VIEW = "android.view.View";
    private static final String CLASS_NAME_LISTENER_INFO = "android.view.View$ListenerInfo";
    private static final String ELLIPSIS_HINT = "...";
    private static final String GAP_TO_EXPAND_HINT = " ";
    private static final String GAP_TO_SHRINK_HINT = " ";
    private static final int MAX_LINES_ON_SHRINK = 4;
    private static final int TO_EXPAND_HINT_COLOR = 0xFF3498DB;
    private static final int TO_SHRINK_HINT_COLOR = 0xFFE74C3C;
    private static final int TO_EXPAND_HINT_COLOR_BG_PRESSED = 0x55999999;
    private static final int TO_SHRINK_HINT_COLOR_BG_PRESSED = 0x55999999;
    private static final boolean TOGGLE_ENABLE = false;
    private static final boolean SHOW_TO_EXPAND_HINT = true;
    private static final boolean SHOW_TO_SHRINK_HINT = true;

    private String mEllipsisHint;
    private String mToExpandHint;
    private String mToShrinkHint;
    private String mGapToExpandHint = GAP_TO_EXPAND_HINT;
    private String mGapToShrinkHint = GAP_TO_SHRINK_HINT;
    private boolean mToggleEnable = TOGGLE_ENABLE;
    private boolean mShowToExpandHint = SHOW_TO_EXPAND_HINT;
    private boolean mShowToShrinkHint = SHOW_TO_SHRINK_HINT;
    private int mMaxLinesOnShrink = MAX_LINES_ON_SHRINK;
    private int mToExpandHintColor = TO_EXPAND_HINT_COLOR;
    private int mToShrinkHintColor = TO_SHRINK_HINT_COLOR;

    private int mToExpandHintColorBgPressed = TO_EXPAND_HINT_COLOR_BG_PRESSED;
    private int mToShrinkHintColorBgPressed = TO_SHRINK_HINT_COLOR_BG_PRESSED;

    private int mCurrState = STATE_SHRINK;

    //  used to add to the tail of modified text, the "shrink" and "expand" text
    private TouchableSpan mTouchableSpan;
    private BufferType mBufferType = BufferType.NORMAL;
    private TextPaint mTextPaint;
    private Layout mLayout;
    private int mTextLineCount = -1;
    private int mLayoutWidth = 0;
    private int mFutureTextViewWidth = 0;

    //  the original text of this view
    private CharSequence mOrigText;

    //  used to judge if the listener of corresponding to the onclick event of ExpandableTextView
    //  is specifically for inner toggle
    private ExpandableClickListener mExpandableClickListener;
    private OnExpandListener mOnExpandListener;
    private boolean isJustSupportOpen;



    public ExpandableTextView(Context context) {
        super(context);
        init();
    }

    public ExpandableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs);
        init();
    }

    public ExpandableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
        init();
    }

    private void initAttr(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ExpandableTextView);
        if (a == null) {
            return;
        }
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.ExpandableTextView_etv_MaxLinesOnShrink) {
                mMaxLinesOnShrink = a.getInteger(attr, MAX_LINES_ON_SHRINK);
            }else if (attr == R.styleable.ExpandableTextView_etv_EllipsisHint){
                mEllipsisHint = a.getString(attr);
            }else if (attr == R.styleable.ExpandableTextView_etv_ToExpandHint) {
                mToExpandHint = a.getString(attr);
            }else if (attr == R.styleable.ExpandableTextView_etv_ToShrinkHint) {
                mToShrinkHint = a.getString(attr);
            }else if (attr == R.styleable.ExpandableTextView_etv_EnableToggle) {
                mToggleEnable = a.getBoolean(attr, TOGGLE_ENABLE);
            }else if (attr == R.styleable.ExpandableTextView_etv_ToExpandHintShow){
                mShowToExpandHint = a.getBoolean(attr, SHOW_TO_EXPAND_HINT);
            }else if (attr == R.styleable.ExpandableTextView_etv_ToShrinkHintShow){
                mShowToShrinkHint = a.getBoolean(attr, SHOW_TO_SHRINK_HINT);
            }else if (attr == R.styleable.ExpandableTextView_etv_ToExpandHintColor){
                mToExpandHintColor = a.getInteger(attr, TO_EXPAND_HINT_COLOR);
            }else if (attr == R.styleable.ExpandableTextView_etv_ToShrinkHintColor){
                mToShrinkHintColor = a.getInteger(attr, TO_SHRINK_HINT_COLOR);
            }else if (attr == R.styleable.ExpandableTextView_etv_ToExpandHintColorBgPressed){
                mToExpandHintColorBgPressed = a.getInteger(attr, TO_EXPAND_HINT_COLOR_BG_PRESSED);
            }else if (attr == R.styleable.ExpandableTextView_etv_ToShrinkHintColorBgPressed){
                mToShrinkHintColorBgPressed = a.getInteger(attr, TO_SHRINK_HINT_COLOR_BG_PRESSED);
            }else if (attr == R.styleable.ExpandableTextView_etv_InitState){
                mCurrState = a.getInteger(attr, STATE_SHRINK);
            }else if (attr == R.styleable.ExpandableTextView_etv_GapToExpandHint){
                mGapToExpandHint = a.getString(attr);
            }else if (attr == R.styleable.ExpandableTextView_etv_GapToShrinkHint){
                mGapToShrinkHint = a.getString(attr);
            }else if (attr == R.styleable.ExpandableTextView_etv_JustSupportOpen){
                isJustSupportOpen = a.getBoolean(attr, false);
            }
        }
        a.recycle();
    }

    private void init() {
        mTouchableSpan = new TouchableSpan();
        setMovementMethod(new LinkTouchMovementMethod());
        if(TextUtils.isEmpty(mEllipsisHint)) {
            mEllipsisHint = ELLIPSIS_HINT;
        }
        if(TextUtils.isEmpty(mToExpandHint)){
            mToExpandHint = getResources().getString(R.string.to_expand_hint);
        }
        if(TextUtils.isEmpty(mToShrinkHint)){
            mToShrinkHint = getResources().getString(R.string.to_shrink_hint);
        }
        if(mToggleEnable){
            mExpandableClickListener = new ExpandableClickListener();
            setOnClickListener(mExpandableClickListener);
        }
        if (isJustSupportOpen) {
            mToShrinkHint = "";
        }
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ViewTreeObserver obs = getViewTreeObserver();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    obs.removeOnGlobalLayoutListener(this);
                } else {
                    obs.removeGlobalOnLayoutListener(this);
                }
                setTextInternal(getNewTextByConfig(), mBufferType);
            }
        });
    }


    public void updateForRecyclerView(CharSequence text, int futureTextViewWidth, int expandState){
        mFutureTextViewWidth = futureTextViewWidth;
        mCurrState = expandState;
        setText(text);
    }

    public void updateForRecyclerView(CharSequence text, BufferType type, int futureTextViewWidth){
        mFutureTextViewWidth = futureTextViewWidth;
        setText(text, type);
    }

    public void updateForRecyclerView(CharSequence text, int futureTextViewWidth){
        mFutureTextViewWidth = futureTextViewWidth;
        setText(text);
    }

    public int getExpandState(){
        return mCurrState;
    }

    private Layout getValidLayout(){
        return mLayout != null ? mLayout : getLayout();
    }

    private CharSequence getNewTextByConfig(){
        if(TextUtils.isEmpty(mOrigText)){
            return mOrigText;
        }

        mLayout = getLayout();
        if(mLayout != null){
            mLayoutWidth = mLayout.getWidth();
        }

        if(mLayoutWidth <= 0){
            if(getWidth() == 0) {
                if (mFutureTextViewWidth == 0) {
                    return mOrigText;
                } else {
                    mLayoutWidth = mFutureTextViewWidth - getPaddingLeft() - getPaddingRight();
                }
            }else{
                mLayoutWidth = getWidth() - getPaddingLeft() - getPaddingRight();
            }
        }

        mTextPaint = getPaint();

        mTextLineCount = -1;
        switch (mCurrState){
            case STATE_SHRINK: {
                mLayout = new DynamicLayout(mOrigText, mTextPaint, mLayoutWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                mTextLineCount = mLayout.getLineCount();
                if (mTextLineCount <= mMaxLinesOnShrink) {
                    return mOrigText;
                }
                int indexEnd = getValidLayout().getLineEnd(mMaxLinesOnShrink - 1);
                int indexStart = getValidLayout().getLineStart(mMaxLinesOnShrink - 1);

                if (mOrigText == null || mOrigText.length() == 0) {
                    return mOrigText;
                }
                int start = getValidLayout().getLineStart(mMaxLinesOnShrink - 1);
                int end = getValidLayout().getLineEnd(mMaxLinesOnShrink - 1);

                CharSequence content = mOrigText.subSequence(start, mOrigText.length());


                float moreWidth = getPaint().measureText(mToExpandHint, 0, mToExpandHint.length());
                float gapWidth = getPaint().measureText(mGapToExpandHint, 0, mGapToExpandHint.length());
                float maxWidth = getValidLayout().getWidth() - moreWidth - gapWidth;
                int len = getPaint().breakText(content, 0, content.length(), true, maxWidth, null);
                if (content.charAt(end - 1) == '\n') {
                    end = end - 1;
                }

                len = Math.min(len, end);

                int endSpaceWidth = getValidLayout().getWidth() -
                        (int) (mTextPaint.measureText(mOrigText.subSequence(indexStart, indexEnd).toString()) + 0.5);
                float widthTailReplaced = mTextPaint.measureText(getContentOfString(mEllipsisHint)
                        + (mShowToExpandHint ? (getContentOfString(mToExpandHint) + getContentOfString(mGapToExpandHint)) + 0.5 : ""));
                if (endSpaceWidth <= widthTailReplaced) {
                    while (endSpaceWidth <= widthTailReplaced) {
                        end = end - 1;
                        if (content.charAt(end - 1) == '\n') {
                            end = end - 1;
                        }
                        endSpaceWidth = getValidLayout().getWidth() -
                                (int) (mTextPaint.measureText(mOrigText.subSequence(indexStart, end - 1).toString()));
                    }
                    len = Math.min(len, end); // 57 62
                    // "limit content... expand"
                    return createShrinkText(mOrigText.subSequence(0, start + len  - 4));
                } else {
                    Log.e(TAG, mOrigText.toString());
                    return createShrinkText(mOrigText.subSequence(0, start + len));
                }

            }
            case STATE_EXPAND: {
                if (!mShowToShrinkHint) {
                    return mOrigText;
                }
                mLayout = new DynamicLayout(mOrigText, mTextPaint, mLayoutWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                mTextLineCount = mLayout.getLineCount();

                if (mTextLineCount <= mMaxLinesOnShrink) {
                    return mOrigText;
                }

                SpannableStringBuilder ssbExpand = new SpannableStringBuilder(mOrigText)
                        .append(mGapToShrinkHint).append(mToShrinkHint);
                ssbExpand.setSpan(mTouchableSpan, ssbExpand.length() - getLengthOfString(mToShrinkHint), ssbExpand.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                return ssbExpand;
            }
        }
        return mOrigText;
    }

    public void setExpandListener(OnExpandListener listener){
        mOnExpandListener = listener;
    }


    private Spanned createShrinkText(CharSequence limitContent) {
        SpannableStringBuilder ssShrink = new SpannableStringBuilder(limitContent)
                .append(mEllipsisHint).append(mGapToExpandHint).append(mToExpandHint);
        ssShrink.setSpan(mTouchableSpan, ssShrink.length() - getLengthOfString(mToExpandHint), ssShrink.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ssShrink;
    }

    private void toggle(){
        switch (mCurrState){
            case STATE_SHRINK:
                mCurrState = STATE_EXPAND;
                if(mOnExpandListener != null){
                    mOnExpandListener.onExpand(this);
                }
                break;
            case STATE_EXPAND:
                // TODO just support open
                if (isJustSupportOpen) {
                    return;
                }
                mCurrState = STATE_SHRINK;
                if(mOnExpandListener != null){
                    mOnExpandListener.onShrink(this);
                }
                break;
        }
        setTextInternal(getNewTextByConfig(), mBufferType);
    }


    @Override
    public void setText(CharSequence text, BufferType type) {
        mOrigText = text;
        mBufferType = type;
        setTextInternal(getNewTextByConfig(), type);
    }

    private void setTextInternal(CharSequence text, BufferType type){
        super.setText(text, type);
    }

    private int getLengthOfString(String string){
        if(string == null)
            return 0;
        return string.length();
    }

    private String getContentOfString(String string){
        if(string == null)
            return "";
        return string;
    }

    public interface OnExpandListener{
        void onExpand(ExpandableTextView view);
        void onShrink(ExpandableTextView view);
    }

    private class ExpandableClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            toggle();
        }
    }

    public View.OnClickListener getOnClickListener(View view) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return getOnClickListenerV14(view);
        } else {
            return getOnClickListenerV(view);
        }
    }

    private View.OnClickListener getOnClickListenerV(View view) {
        View.OnClickListener retrievedListener = null;
        try {
            Field field = Class.forName(CLASS_NAME_VIEW).getDeclaredField("mOnClickListener");
            field.setAccessible(true);
            retrievedListener = (View.OnClickListener) field.get(view);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return retrievedListener;
    }

    private View.OnClickListener getOnClickListenerV14(View view) {
        View.OnClickListener retrievedListener = null;
        try {
            Field listenerField = Class.forName(CLASS_NAME_VIEW).getDeclaredField("mListenerInfo");
            Object listenerInfo = null;

            if (listenerField != null) {
                listenerField.setAccessible(true);
                listenerInfo = listenerField.get(view);
            }

            Field clickListenerField = Class.forName(CLASS_NAME_LISTENER_INFO).getDeclaredField("mOnClickListener");

            if (clickListenerField != null && listenerInfo != null) {
                clickListenerField.setAccessible(true);
                retrievedListener = (View.OnClickListener) clickListenerField.get(listenerInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return retrievedListener;
    }



    private class TouchableSpan extends ClickableSpan {
        private boolean mIsPressed;
        public void setPressed(boolean isSelected) {
            mIsPressed = isSelected;
        }

        @Override
        public void onClick(View widget) {
            if(hasOnClickListeners()
                    && (getOnClickListener(ExpandableTextView.this) instanceof ExpandableClickListener)) {
            }else{
                toggle();
            }
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            switch (mCurrState){
                case STATE_SHRINK:
                    ds.setColor(mToExpandHintColor);
                    ds.bgColor = mIsPressed ? mToExpandHintColorBgPressed : 0;
                    break;
                case STATE_EXPAND:
                    ds.setColor(mToShrinkHintColor);
                    ds.bgColor = mIsPressed ? mToShrinkHintColorBgPressed : 0;
                    break;
            }
            ds.setUnderlineText(false);
        }
    }

    public class LinkTouchMovementMethod extends LinkMovementMethod {
        private TouchableSpan mPressedSpan;

        @Override
        public boolean onTouchEvent(TextView textView, Spannable spannable, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mPressedSpan = getPressedSpan(textView, spannable, event);
                if (mPressedSpan != null) {
                    mPressedSpan.setPressed(true);
                    Selection.setSelection(spannable, spannable.getSpanStart(mPressedSpan),
                            spannable.getSpanEnd(mPressedSpan));
                }
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                TouchableSpan touchedSpan = getPressedSpan(textView, spannable, event);
                if (mPressedSpan != null && touchedSpan != mPressedSpan) {
                    mPressedSpan.setPressed(false);
                    mPressedSpan = null;
                    Selection.removeSelection(spannable);
                }
            } else {
                if (mPressedSpan != null) {
                    mPressedSpan.setPressed(false);
                    super.onTouchEvent(textView, spannable, event);
                }
                mPressedSpan = null;
                Selection.removeSelection(spannable);
            }
            return true;
        }

        private TouchableSpan getPressedSpan(TextView textView, Spannable spannable, MotionEvent event) {

            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= textView.getTotalPaddingLeft();
            y -= textView.getTotalPaddingTop();

            x += textView.getScrollX();
            y += textView.getScrollY();

            Layout layout = textView.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);

            TouchableSpan[] link = spannable.getSpans(off, off, TouchableSpan.class);
            TouchableSpan touchedSpan = null;
            if (link.length > 0) {
                touchedSpan = link[0];
            }
            return touchedSpan;
        }
    }
}
