package khrystal.me.widget.edittext;

import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/7/31
 * update time:
 * email: 723526676@qq.com
 */
public class AddSpaceTextWatcher implements TextWatcher {

    private int beforeTextLength = 0;
    private int onTextLength = 0;
    private boolean isChanged = false;
    private StringBuffer buffer = new StringBuffer();
    // 改变之前text空格数量
    int spaceNumberA = 0;
    private EditText editText;
    private int maxLength;
    private SpaceType spaceType;
    private int location = 0;
    // 是否是主动设置text
    private boolean isSetText = false;

    public AddSpaceTextWatcher(EditText editText, int maxLength) {
        this.editText = editText;
        this.maxLength = maxLength;
        if (null == editText) {
            throw new NullPointerException("EditText must not be null!");
        }
        spaceType = SpaceType.defaultType;
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        editText.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        beforeTextLength = s.length();
        if (buffer.length() > 0) {
            buffer.delete(0, buffer.length());
        }
        spaceNumberA = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == ' ') {
                spaceNumberA++;
            }
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        onTextLength = s.length();
        buffer.append(s.toString());
        if (onTextLength == beforeTextLength || onTextLength > maxLength || isChanged) {
            isChanged = false;
            return;
        }
        isChanged = true;
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (isChanged) {
            location = editText.getSelectionEnd();
            int index = 0;
            while (index < buffer.length()) { // 删掉所有空格
                if (buffer.charAt(index) == ' ') {
                    buffer.deleteCharAt(index);
                } else {
                    index++;
                }
            }

            index = 0;
            int spaceNumberB = 0;
            while (index < buffer.length()) { // 插入所有空格
                spaceNumberB = insertSpace(index, spaceNumberB);
                index++;
            }
            String str = buffer.toString();
            // 计算光标位置
            if (spaceNumberB > spaceNumberA) {
                location += (spaceNumberB - spaceNumberA);
                spaceNumberA = spaceNumberB;
            }

            if (isSetText) {
                location = str.length();
                isSetText = false;
            } else if (location > str.length()) {
                location = str.length();
            } else if (location < 0) {
                location = 0;
            }
            updateContext(s, str);
            isChanged = false;
        }
    }

    private void updateContext(Editable editable, String values) {
        if (spaceType == SpaceType.IDCardNumberType) {
            editable.replace(0, editable.length(), values);
        } else {
            editText.setText(values);
            try {
                editText.setSelection(location);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private int insertSpace(int index, int spaceNumberAfter) {
        switch (spaceType) {
            case defaultType:// 相隔四位空格
                if (index > 3
                        && (index % (4 * (spaceNumberAfter + 1)) == spaceNumberAfter)) {
                    buffer.insert(index, ' ');
                    spaceNumberAfter++;
                }
                break;
            case bankCardNumberType:
                if (index > 3
                        && (index % (4 * (spaceNumberAfter + 1)) == spaceNumberAfter)) {
                    buffer.insert(index, ' ');
                    spaceNumberAfter++;
                }
                break;
            case mobilePhoneNumberType:
                if (index == 3
                        || ((index > 7) && ((index - 3) % (4 * spaceNumberAfter) == spaceNumberAfter))) {
                    buffer.insert(index, ' ');
                    spaceNumberAfter++;
                }
                break;
            case IDCardNumberType:
                if (index == 6
                        || ((index > 10) && ((index - 6) % (4 * spaceNumberAfter) == spaceNumberAfter))) {
                    buffer.insert(index, ' ');
                    spaceNumberAfter++;
                }
                break;
            default:
                if (index > 3
                        && (index % (4 * (spaceNumberAfter + 1)) == spaceNumberAfter)) {
                    buffer.insert(index, ' ');
                    spaceNumberAfter++;
                }
                break;
        }
        return spaceNumberAfter;
    }

    // 计算需要的空格数
    private int computeSpaceCount(CharSequence charSequence) {
        buffer.delete(0, buffer.length());
        buffer.append(charSequence.toString());
        int index = 0;
        int spaceNumberB = 0;
        while (index < buffer.length()) { // 插入所有空格
            spaceNumberB = insertSpace(index, spaceNumberB);
            index++;
        }
        buffer.delete(0, buffer.length());
        return index;
    }

    // 设置空格类型
    public void setSpaceType(SpaceType spaceType) {
        this.spaceType = spaceType;
        if (this.spaceType == SpaceType.IDCardNumberType) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT);
        }
    }

    // 设置输入字符
    public boolean setText(CharSequence charSequence) {
        if (editText != null && !TextUtils.isEmpty(charSequence) && computeSpaceCount(charSequence) <= maxLength) {
            isSetText = true;
            editText.removeTextChangedListener(this);
            editText.setText(charSequence);
            editText.addTextChangedListener(this);
            return true;
        }
        return false;
    }

    public String getTextNotSpace() {
        if (editText != null) {
            return delSpace(editText.getText().toString());
        }
        return null;
    }

    public int getLenghtNotSpace() {
        if (editText != null) {
            return getTextNotSpace().length();
        }
        return 0;
    }

    public int getSpaceCount() {
        return spaceNumberA;
    }

    private String delSpace(String str) {
        if (str != null) {
            str = str.replaceAll("\r", "");
            str = str.replaceAll("\n", "");
            str = str.replace(" ", "");
        }
        return str;
    }


    public enum SpaceType {
        // 默认类型
        defaultType,
        // 银行卡
        bankCardNumberType,
        // 手机号
        mobilePhoneNumberType,
        // 身份证
        IDCardNumberType;
    }
}
