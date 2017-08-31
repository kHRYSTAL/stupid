package me.khrystal.widget.pwdet;

import android.text.method.PasswordTransformationMethod;
import android.view.View;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/8/31
 * update time:
 * email: 723526676@qq.com
 */

public class CustomPasswordTransformationMethod extends PasswordTransformationMethod {

    String transformation;

    public CustomPasswordTransformationMethod(String transformation) {
        this.transformation = transformation;
    }

    @Override
    public CharSequence getTransformation(CharSequence source, View view) {
        return new PasswordCharSequence(source);
    }

    private class PasswordCharSequence implements CharSequence {

        private CharSequence mSource;

        public PasswordCharSequence(CharSequence mSource) {
            this.mSource = mSource;
        }

        @Override
        public int length() {
            return mSource.length();
        }

        @Override
        public char charAt(int i) {
            return transformation.charAt(0);
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            return mSource.subSequence(start, end);
        }
    }
}
