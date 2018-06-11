package me.khrystal.util.stetho;

import android.support.annotation.NonNull;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/6/11
 * update time:
 * email: 723526676@qq.com
 */

public class CountingOutputStream extends FilterOutputStream {

    private long mCount;

    public CountingOutputStream(@NonNull OutputStream out) {
        super(out);
    }

    public long getCount() {
        return mCount;
    }

    @Override
    public void write(int b) throws IOException {
        super.write(b);
        mCount++;
    }

    @Override
    public void write(@NonNull byte[] b) throws IOException {
        super.write(b, 0, b.length);
    }

    @Override
    public void write(@NonNull byte[] b, int off, int len) throws IOException {
        super.write(b, off, len);
        mCount += len;
    }
}
