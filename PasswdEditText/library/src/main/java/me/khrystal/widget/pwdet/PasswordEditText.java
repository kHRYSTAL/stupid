package me.khrystal.widget.pwdet;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.khrystal.widget.pwdet.imebugfixer.ImeDebugFixedEditText;

/**
 * usage: 网格输入框 常用语支付密码/口令输入
 *          支持常规字母数字显示样式或密码样式 密码默认为"●";
 * author: kHRYSTAL
 * create time: 17/8/31
 * update time:
 * email: 723526676@qq.com
 */

public class PasswordEditText extends LinearLayout implements PasswordView {

    private static final int DEFAULT_PASSWORD_LENGTH = 6;
    private static final int DEFAULT_TEXTSIZE = 16;
    private static final String DEFAULT_TRANSFORMATION = "●";
    private static final int DEFAULT_LINE_COLOR = 0xaa888888;
    private static final int DEFUALT_GRID_COLOR = 0xffFFFFFF;

    private ColorStateList mTextColor;
    private int mTextSize = DEFAULT_TEXTSIZE;
    private int mLineWidth;
    private int mLineColor;
    private int mGridColor;
    private Drawable mLineDrawable;
    private Drawable mOuterLineDrawable;
    private int mPasswordLength;
    private String mPasswordTransformation;
    private int mPasswordType;

    private String[] mPasswordArr;
    private TextView[] mViewArr;

    private ImeDebugFixedEditText mInputView;
    private OnPasswordChangedListener mListener;
    private PasswordTransformationMethod mTransformationMethod;

    public PasswordEditText(Context context) {
        super(context);
        init(context, null, 0);
    }

    public PasswordEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public PasswordEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PasswordEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        initAttrs(context, attrs, defStyleAttr);
        initViews(context);
    }

    private void initAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PasswordEditText, defStyleAttr, 0);
        mTextColor = ta.getColorStateList(R.styleable.PasswordEditText_pwdetTextColor);
        if (mTextColor == null) {
            mTextColor = ColorStateList.valueOf(getResources().getColor(android.R.color.primary_text_light));
        }
        int testSize = ta.getDimensionPixelSize(R.styleable.PasswordEditText_pedetTextSize, -1) ;
        if (testSize != -1) {
            mTextSize = Util.px2sp(context, testSize);
        }
        mLineWidth = (int) ta.getDimension(R.styleable.PasswordEditText_pwdetLineWidth, Util.dp2px(context, 1));
        mLineColor = ta.getColor(R.styleable.PasswordEditText_pwdetLineColor, DEFAULT_LINE_COLOR);
        mGridColor = ta.getColor(R.styleable.PasswordEditText_pwdetGridColor, DEFUALT_GRID_COLOR);
        mLineDrawable = ta.getDrawable(R.styleable.PasswordEditText_pwdetLineColor);
        if (mLineDrawable == null) {
            mLineDrawable = new ColorDrawable(mLineColor);
        }
        mOuterLineDrawable = generateBackgroundDrawable();

        mPasswordLength = ta.getInt(R.styleable.PasswordEditText_pwdetPasswordLength, DEFAULT_PASSWORD_LENGTH);

        mPasswordTransformation = ta.getString(R.styleable.PasswordEditText_pwdetPasswordTransformation);
        if (TextUtils.isEmpty(mPasswordTransformation)) {
            mPasswordTransformation = DEFAULT_TRANSFORMATION;
        }

        mPasswordType = ta.getInt(R.styleable.PasswordEditText_pwdetPasswordType, 0);
        ta.recycle();

        mPasswordArr = new String[mPasswordLength];
        mViewArr = new TextView[mPasswordLength];
    }

    private void initViews(Context context) {
        super.setBackgroundDrawable(mOuterLineDrawable);
        setShowDividers(SHOW_DIVIDER_NONE);
        setOrientation(HORIZONTAL);

        mTransformationMethod = new CustomPasswordTransformationMethod(mPasswordTransformation);
        inflaterViews(context);
    }

    private void inflaterViews(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.pwd_et, this);
        mInputView = (ImeDebugFixedEditText) findViewById(R.id.inputView);
        mInputView.setMaxEms(mPasswordLength);
        mInputView.addTextChangedListener(textWatcher);
        mInputView.setDelKeyEventListener(onDelKeyEventListener);
        // 移除空格
        InputFilter filter=new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if(source.equals(" "))
                    return "";
                else
                    return null;
            }
        };

        mInputView.setFilters(new InputFilter[]{filter});

        setCustomAttr(mInputView);

        mViewArr[0] = mInputView;
        int index = 1;
        while(index < mPasswordLength) {
            View dividerView = inflater.inflate(R.layout.pwd_divider, null);
            LayoutParams dividerParams = new LayoutParams(mLineWidth, LayoutParams.MATCH_PARENT);
            dividerView.setBackgroundDrawable(mLineDrawable);
            addView(dividerView, dividerParams);

            TextView textView = (TextView) inflater.inflate(R.layout.pwd_tv, null);
            setCustomAttr(textView);
            LayoutParams textViewParams = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1f);
            addView(textView, textViewParams);

            mViewArr[index] = textView;
            index++;
        }
        setOnClickListener(mOnClickListener);
    }

    private void setCustomAttr(TextView view) {
        if (mTextColor != null) {
            view.setTextColor(mTextColor);
        }
        view.setTextSize(mTextSize);

        int inputType;
        switch (mPasswordType) {

            case 1:
                inputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
                mTransformationMethod = new CustomPasswordTransformationMethod(mPasswordTransformation);
                break;
            case 2:
                inputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
                mTransformationMethod = null;
                break;
            case 3:
                inputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD;
                mTransformationMethod = new CustomPasswordTransformationMethod(mPasswordTransformation);
                break;
            default:
                inputType = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD;
                mTransformationMethod = new CustomPasswordTransformationMethod(mPasswordTransformation);
                break;
        }
        view.setInputType(inputType);
        view.setTransformationMethod(mTransformationMethod);
    }

    private OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            forceInputViewGetFocus();
        }
    };

    private GradientDrawable generateBackgroundDrawable() {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(mGridColor);
        drawable.setStroke(mLineWidth, mLineColor);
        return drawable;
    }

    private void forceInputViewGetFocus() {
        mInputView.setFocusable(true);
        mInputView.setFocusableInTouchMode(true);
        mInputView.requestFocus();
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mInputView, InputMethodManager.SHOW_IMPLICIT);
    }

    private ImeDebugFixedEditText.OnDelKeyEventListener onDelKeyEventListener = new ImeDebugFixedEditText.OnDelKeyEventListener() {

        @Override
        public void onDeleteClick() {
            for (int i = mPasswordArr.length - 1; i >= 0; i--) {
                if (mPasswordArr[i] != null) {
                    mPasswordArr[i] = null;
                    mViewArr[i].setText(null);
                    notifyTextChanged();
                    break;
                } else {
                    mViewArr[i].setText(null);
                }
            }
        }
    };

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s == null) {
                return;
            }

            String newStr = s.toString();
            if (newStr.length() == 1) {
                mPasswordArr[0] = newStr;
                notifyTextChanged();
            } else if (newStr.length() == 2) {
                String newNum = newStr.substring(1);
                for (int i = 0; i < mPasswordArr.length; i++) {
                    if (mPasswordArr[i] == null) {
                        mPasswordArr[i] = newNum;
                        mViewArr[i].setText(newNum);
                        notifyTextChanged();
                        break;
                    }
                }
                mInputView.removeTextChangedListener(this);
                mInputView.setText(mPasswordArr[0]);
                if (mInputView.getText().length() >= 1) {
                    mInputView.setSelection(1);
                }
                mInputView.addTextChangedListener(this);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void notifyTextChanged() {
        if (mListener == null)
            return;

        String currentPsw = getPassword();
        mListener.onTextChanged(currentPsw);

        if (currentPsw.length() == mPasswordLength)
            mListener.onInputFinish(currentPsw);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("instanceState", super.onSaveInstanceState());
        bundle.putStringArray("passwordArr", mPasswordArr);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            mPasswordArr = bundle.getStringArray("passwordArr");
            state = bundle.getParcelable("instanceState");
            mInputView.removeTextChangedListener(textWatcher);
            setPassword(getPassword());
            mInputView.addTextChangedListener(textWatcher);
        }
        super.onRestoreInstanceState(state);
    }

    @Override
    public String getPassword() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mPasswordArr.length; i++) {
            if (mPasswordArr[i] != null)
                sb.append(mPasswordArr[i]);
        }
        return sb.toString();
    }

    @Override
    public void clearPassword() {
        for (int i = 0; i < mPasswordArr.length; i++) {
            mPasswordArr[i] = null;
            mViewArr[i].setText(null);
        }
    }

    @Override
    public void setPassword(String password) {
        clearPassword();

        if (TextUtils.isEmpty(password))
            return;

        char[] pswArr = password.toCharArray();
        for (int i = 0; i < pswArr.length; i++) {
            if (i < mPasswordArr.length) {
                mPasswordArr[i] = pswArr[i] + "";
                mViewArr[i].setText(mPasswordArr[i]);
            }
        }
    }

    @Override
    public void setPasswordVisibility(boolean visible) {
        for (TextView textView : mViewArr) {
            textView.setTransformationMethod(visible ? null : mTransformationMethod);
            if (textView instanceof EditText) {
                EditText et = (EditText) textView;
                et.setSelection(et.getText().length());
            }
        }
    }

    @Override
    public void togglePasswordVisibility() {
        boolean currentVisible = getPasswordVisibility();
        setPasswordVisibility(!currentVisible);
    }

    private boolean getPasswordVisibility() {
        return mViewArr[0].getTransformationMethod() == null;
    }

    @Override
    public void setOnPasswordChangeListener(OnPasswordChangedListener listener) {
        mListener = listener;
    }

    @Override
    public void setPasswordType(PasswordType passwordType) {
        boolean visible = getPasswordVisibility();
        int inputType = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD;
        switch (passwordType) {

            case TEXT:
                inputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
                break;

            case TEXTVISIBLE:
                inputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
                break;

            case TEXTWEB:
                inputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD;
                break;
        }

        for (TextView textView : mViewArr)
            textView.setInputType(inputType);

        setPasswordVisibility(visible);
    }
}
