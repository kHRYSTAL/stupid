package me.khrystal.util.stetho;

import android.support.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.InflaterOutputStream;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/6/11
 * update time:
 * email: 723526676@qq.com
 */

public class RequestBodyHelper {

    static final String GZIP_ENCODING = "gzip";
    static final String DEFLATE_ENCODING = "deflate";


    private final NetworkEventReporter mEventReporter;
    private final String mRequestId;

    private ByteArrayOutputStream mDeflatedOutput;
    private CountingOutputStream mDeflatingOutput;

    public RequestBodyHelper(NetworkEventReporter eventReporter, String requestId) {
        this.mEventReporter = eventReporter;
        this.mRequestId = requestId;
    }

    public OutputStream createBodySink(@Nullable String contentEncoding) throws IOException {
        OutputStream deflatingOutput;
        ByteArrayOutputStream deflatedOutput = new ByteArrayOutputStream();
        if (GZIP_ENCODING.equals(contentEncoding)) {
            deflatingOutput = GunzippingOutputStream.create(deflatedOutput);
        } else if (DEFLATE_ENCODING.equals(contentEncoding)) {
            deflatingOutput = new InflaterOutputStream(deflatedOutput);
        } else {
            deflatingOutput = deflatedOutput;
        }

        mDeflatingOutput = new CountingOutputStream(deflatingOutput);
        mDeflatedOutput = deflatedOutput;

        return mDeflatingOutput;
    }

    public byte[] getDisplayBody() {
        throwIfNoBody();
        return mDeflatedOutput.toByteArray();
    }

    public boolean hasBody() {
        return mDeflatedOutput != null;
    }

    public void reportDataSent() {
        throwIfNoBody();
        mEventReporter.dataSent(
                mRequestId,
                mDeflatedOutput.size(),
                (int)mDeflatingOutput.getCount());
    }

    private void throwIfNoBody() {
        if (!hasBody()) {
            throw new IllegalStateException("No body found; has createBodySink been called?");
        }
    }
}
