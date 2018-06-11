package me.khrystal.util.stetho;

import java.io.IOException;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/6/11
 * update time:
 * email: 723526676@qq.com
 */

public class DefaultResponseHandler implements ResponseHandler {

    private final NetworkEventReporter mEventReporter;
    private final String mRequestId;
    private int mBytesRead = 0;
    private int mDecodedBytesRead = -1;

    public DefaultResponseHandler(NetworkEventReporter eventReporter, String requestId) {
        mEventReporter = eventReporter;
        mRequestId = requestId;
    }


    @Override
    public void onRead(int numBytes) {
        mBytesRead += numBytes;
    }

    @Override
    public void onReadDecoded(int numBytes) {
        if (mDecodedBytesRead == -1) {
            mDecodedBytesRead = 0;
        }
        mDecodedBytesRead += numBytes;
    }

    @Override
    public void onEOF() {
        reportDataReceived();
        mEventReporter.responseReadFinished(mRequestId);
    }

    @Override
    public void onError(IOException e) {
        reportDataReceived();
        mEventReporter.responseReadFailed(mRequestId, e.toString());
    }

    private void reportDataReceived() {
        mEventReporter.dataReceived(mRequestId, mBytesRead, mDecodedBytesRead >= 0 ? mDecodedBytesRead : mBytesRead);
    }
}
