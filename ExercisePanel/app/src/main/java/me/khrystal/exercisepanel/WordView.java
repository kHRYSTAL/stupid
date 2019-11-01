package me.khrystal.exercisepanel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zhangteng on 2016/12/21.
 */
@SuppressLint("AppCompatCustomView")
public class WordView extends TextView {

    private static final String TAG = WordView.class.getSimpleName();

    private SpannableString mSpannableString;
    private String mSelectedWord;
    private OnWordSelectListener mOnWordSelectListener;
    private BackgroundColorSpan mForegroundColorSpan = new BackgroundColorSpan(Color.parseColor("#E2E2F6"));

    public WordView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize();
    }

    public WordView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public WordView(Context context) {
        super(context);
        initialize();
    }

    public void setOnWordSelectListener(OnWordSelectListener listener) {
        mOnWordSelectListener = listener;
    }

    private void initialize() {
        setGravity(Gravity.TOP);
    }

    public void trySelectWord(MotionEvent event) {
        Layout layout = getLayout();
        if (layout == null) {
            return;
        }
        int line = layout.getLineForVertical(getScrollY() + (int) event.getY());
        final int index = layout.getOffsetForHorizontal(line, (int) event.getX());
        Word selectedWord = getWord(index);

        if (selectedWord != null) {
            mSpannableString.setSpan(mForegroundColorSpan,
                    selectedWord.getStart(), selectedWord.getEnd(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            setText(mSpannableString);
            mSelectedWord = getText().subSequence(selectedWord.getStart(), selectedWord.getEnd()).toString();
            if (mOnWordSelectListener != null) {
                mOnWordSelectListener.onWordSelect();
            }
        }
    }

    public void clearSelectedWord() {
        clearSpan();
        showSelectedWord(mSelectedWord);
    }

    public String getmSelectedWord() {
        return mSelectedWord;
    }

    private void showSelectedWord(String selectedWord) {
        if (selectedWord != null) {
        }
    }

    private void clearSpan() {
        BackgroundColorSpan[] spans = mSpannableString.getSpans(0, getText().length(), BackgroundColorSpan.class);
        Log.e(TAG, "" + spans.length);
        for (int i = 0; i < spans.length; i++) {
            mSpannableString.removeSpan(spans[i]);
        }
        setText(mSpannableString);
    }

    private Word getWord(final int index) {
        if (mWords == null) {
            return null;
        }

        for (Word w : mWords) {
            if (w.isIn(index)) {
                return w;
            }
        }
        return null;
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        if (!TextUtils.isEmpty(text)) {
            mSpannableString = SpannableString.valueOf(text);
            mWords = getWords(text);
        }
    }

    private List<Word> mWords;

    public List<Word> getWords(CharSequence s) {
        if (s == null) {
            return null;
        }
        List<Word> result = new ArrayList<Word>();
        int start = -1;
        int i = 0;
        for (; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == ' ' || !Character.isLetter(c)) {
                if (start != -1) {
                    result.add(new Word(start, i));// From ( 0, 4 )
                }
                start = -1;
            } else {
                if (start == -1) {
                    start = i;
                }
            }
        }
        if (start != -1) {
            result.add(new Word(start, i));
        }
        return result;
    }

    public void supportScroll(ExercisePanel exercisePanel) {
        ScrollingMovementMethodSupport instance = ScrollingMovementMethodSupport.getInstance();
        instance.setExercisePanel(exercisePanel);
        setMovementMethod(instance);
    }

    private class Word {
        public Word(final int start, final int end) {
            this.mStart = start;
            this.mEnd = end;
        }

        private int mStart;
        private int mEnd;

        public int getStart() {
            return this.mStart;
        }

        public int getEnd() {
            return this.mEnd;
        }

        public boolean isIn(final int index) {
            if (index >= getStart() && index <= getEnd()) {
                return true;
            }
            return false;
        }

        @Override
        public String toString() {
            return "( " + getStart() + ", " + getEnd() + " )";
        }
    }

    public interface OnWordSelectListener {
        public void onWordSelect();
    }
}

