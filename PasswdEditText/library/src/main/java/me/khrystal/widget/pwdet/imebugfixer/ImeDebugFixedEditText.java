package me.khrystal.widget.pwdet.imebugfixer;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.widget.EditText;

/**
 * usage: 修复edittext删除按钮回退接收不到问题
 * @see <a href="http://stackoverflow.com/questions/4886858/android-edittext-deletebackspace-key-event">Stack
 * Overflow</a>
 * author: kHRYSTAL
 * create time: 17/8/31
 * update time:
 * email: 723526676@qq.com
 */

public class ImeDebugFixedEditText extends EditText {

    private OnDelKeyEventListener mListener;

    public ImeDebugFixedEditText(Context context) {
        super(context);
    }

    public ImeDebugFixedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImeDebugFixedEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ImeDebugFixedEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return new ZanyInputConnection(super.onCreateInputConnection(outAttrs), true);
    }

    public void setDelKeyEventListener(OnDelKeyEventListener delKeyEventListener) {
        mListener = delKeyEventListener;
    }

    private class ZanyInputConnection extends InputConnectionWrapper {

        public ZanyInputConnection(InputConnection target, boolean mutable) {
            super(target, mutable);
        }

        @Override
        public boolean sendKeyEvent(KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                if (mListener != null) {
                    mListener.onDeleteClick();
                    return true;
                }
            }
            return super.sendKeyEvent(event);
        }

        @Override
        public boolean deleteSurroundingText(int beforeLength, int afterLength) {
            if (beforeLength == 1 && afterLength == 0) {
                return sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL)) &&
                     sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL));
            }
            return super.deleteSurroundingText(beforeLength, afterLength);
        }
    }

    public interface OnDelKeyEventListener {
        void onDeleteClick();
    }
}
