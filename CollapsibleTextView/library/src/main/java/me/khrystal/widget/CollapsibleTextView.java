package me.khrystal.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * usage: 支持展开折叠 可配置带动画的TextView
 * author: kHRYSTAL
 * create time: 17/3/17
 * update time:
 * email: 723526676@qq.com
 */
public class CollapsibleTextView extends LinearLayout implements View.OnClickListener {

    private TextView textView;
    private TextView tvState;
    private RelativeLayout rlToggleLayout;
    private int textViewStateColor;
    private String textExpand;
    private String textShrink;

    private boolean isExpandNeed = false;
    private boolean isInitTextView = true;
    private boolean isShrink = false;

    private int maxLines;
    private int textLines;

    private CharSequence textContent;
    private int textContentColor;
    private float textContentSize;

    private Thread thread;
    private int sleepTime = 0;

    private OnStateChangeListener onStateChangeListener;

    private final int ANIM = 2;
    private final int ANIM_END = 3;
    private final int ONLY_CHANGE_TOGGLE = 4;

    public CollapsibleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initValue(context, attrs);
        initView(context);
        initClick();
    }

    private void initValue(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CollapsibleTextView);
        maxLines = ta.getInteger(R.styleable.CollapsibleTextView_ctv_maxLines, 4);
        textViewStateColor = ta.getColor(R.styleable.CollapsibleTextView_ctv_textToggleColor, Color.GRAY);

        textShrink = ta.getString(R.styleable.CollapsibleTextView_ctv_textShrink);
        textExpand = ta.getString(R.styleable.CollapsibleTextView_ctv_textExpand);
        if (TextUtils.isEmpty(textShrink)) {
            textShrink = context.getString(R.string.shrink);
        }

        if (TextUtils.isEmpty(textExpand)) {
            textExpand = context.getString(R.string.expand);
        }
        textContentColor = ta.getColor(R.styleable.CollapsibleTextView_ctv_textContentColor, Color.BLACK);
        textContentSize = ta.getDimension(R.styleable.CollapsibleTextView_ctv_textContentSize, 28);

        ta.recycle();

    }

    private void initView(Context context) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_collaps_tv, this);

        rlToggleLayout = (RelativeLayout) findViewById(R.id.collapsible_toggle_layout);

        textView = (TextView) findViewById(R.id.collapsible_tv_content);
        textView.setTextColor(textContentColor);
        textView.getPaint().setTextSize(textContentSize);

        tvState = (TextView) findViewById(R.id.collapsible_toggle_tv);
        tvState.setTextColor(textViewStateColor);

    }

    private void initClick() {
        rlToggleLayout.setOnClickListener(this);
    }

    public void setText(CharSequence charSequence) {

        textContent = charSequence;

        textView.setText(charSequence.toString());

        ViewTreeObserver viewTreeObserver = textView.getViewTreeObserver();
        viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

            @Override
            public boolean onPreDraw() {
                if (!isInitTextView) {
                    return true;
                }
                textLines = textView.getLineCount();
                isExpandNeed = textLines > maxLines;
                isInitTextView = false;
                if (isExpandNeed) {
                    isShrink = true;
                    doAnimation(maxLines, maxLines, ANIM_END);
                } else {
                    isShrink = false;
                    doNotExpand();
                }
                return true;
            }
        });

        if (!isInitTextView) {
            textLines = textView.getLineCount();
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (ANIM == msg.what) {
                textView.setMaxLines(msg.arg1);
                textView.invalidate();
            } else if (ANIM_END == msg.what) {
                setExpandState(msg.arg1);
            } else if (ONLY_CHANGE_TOGGLE == msg.what) {
                changeExpandState(msg.arg1);
            }
            super.handleMessage(msg);
        }
    };

    private void doAnimation(final int startIndex, final int endIndex,
                             final int what) {

        thread = new Thread(new Runnable() {

            @Override
            public void run() {

                if (startIndex < endIndex) {
                    // 如果起止行数小于结束行数，那么往下展开至结束行数
                    // if start index smaller than end index ,do expand action
                    int count = startIndex;
                    while (count++ < endIndex) {
                        Message msg = handler.obtainMessage(ANIM, count, 0);

                        try {
                            Thread.sleep(sleepTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        handler.sendMessage(msg);
                    }
                } else if (startIndex > endIndex) {
                    // 如果起止行数大于结束行数，那么往上折叠至结束行数
                    // if start index bigger than end index ,do shrink action
                    int count = startIndex;
                    while (count-- > endIndex) {
                        Message msg = handler.obtainMessage(ANIM, count, 0);
                        try {
                            Thread.sleep(sleepTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        handler.sendMessage(msg);
                    }
                }

                // 动画结束后发送结束的信号
                // animation end,send signal
                Message msg = handler.obtainMessage(what, endIndex, 0);
                handler.sendMessage(msg);

            }

        });
        thread.start();
    }

    /**
     * 改变折叠状态（仅仅改变折叠与展开状态，不会隐藏折叠/展开图片布局）
     * change shrink/expand state(only change state,but not hide shrink/expand icon)
     *
     * @param endIndex
     */
    @SuppressWarnings("deprecation")
    private void changeExpandState(int endIndex) {
        rlToggleLayout.setVisibility(View.VISIBLE);
        if (endIndex < textLines) {
            tvState.setText(textExpand);
        } else {
            tvState.setText(textShrink);
        }
    }

    @SuppressWarnings("deprecation")
    private void setExpandState(int endIndex) {
        if (endIndex < textLines) {
            isShrink = true;
            rlToggleLayout.setVisibility(View.VISIBLE);
            textView.setOnClickListener(this);
            tvState.setText(textExpand);
        } else {
            isShrink = false;
            rlToggleLayout.setVisibility(View.GONE);
            textView.setOnClickListener(null);
            tvState.setText(textShrink);
        }
    }

    /**
     * 无需折叠
     * do not expand
     */
    private void doNotExpand() {
        textView.setMaxLines(maxLines);
        rlToggleLayout.setVisibility(View.GONE);
        textView.setOnClickListener(null);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.collapsible_toggle_layout) {
            clickImageToggle();
            if (null != onStateChangeListener) onStateChangeListener.onStateChange(isShrink);
        }
    }

    private void clickImageToggle() {
        if (isShrink) {
            // 如果是已经折叠，那么进行非折叠处理
            // do shrink action
            doAnimation(maxLines, textLines, ONLY_CHANGE_TOGGLE);
        } else {
            // 如果是非折叠，那么进行折叠处理
            // do expand action
            doAnimation(textLines, maxLines, ONLY_CHANGE_TOGGLE);
        }
        // 切换状态
        // set flag
        isShrink = !isShrink;
    }

    public void resetState(boolean isShrink) {

        this.isShrink = isShrink;
        if (textLines > maxLines) {
            if (isShrink) {
                rlToggleLayout.setVisibility(View.VISIBLE);
                textView.setOnClickListener(this);
                textView.setMaxLines(maxLines);
                tvState.setText(textExpand);
            } else {
                rlToggleLayout.setVisibility(View.VISIBLE);
                textView.setOnClickListener(this);
                textView.setMaxLines(textLines);
                tvState.setText(textShrink);
            }
        } else {
            doNotExpand();
        }
    }

    public interface OnStateChangeListener {
        void onStateChange(boolean isShrink);
    }

    public void setOnStateChangeListener (OnStateChangeListener onStateChangeListener) {
        this.onStateChangeListener = onStateChangeListener;
    }
}
