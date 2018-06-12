package me.khrystal.util.reporter;

import android.support.annotation.Nullable;
import android.util.Log;

import java.io.InputStream;
import java.util.concurrent.atomic.AtomicInteger;

import me.khrystal.util.stetho.NetworkEventReporter;
import me.khrystal.util.stetho.ResponseHandler;
import me.khrystal.util.utils.DataTranslator;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/6/12
 * update time:
 * email: 723526676@qq.com
 */

public class OkNetworkReporterImpl implements NetworkEventReporter {

    private static final String TAG = OkNetworkReporterImpl.class.getSimpleName();
    private final AtomicInteger mNextRequestId = new AtomicInteger(0);
    private DataTranslator mDataTranslator;

    private static OkNetworkReporterImpl sInstance;

    public static OkNetworkReporterImpl getInstance() {
        if (sInstance == null) {
            sInstance = new OkNetworkReporterImpl();
        }
        return sInstance;
    }

    public OkNetworkReporterImpl() {
        mDataTranslator = new DataTranslator();
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void requestWillBeSent(InspectorRequest request) {
        Log.d(TAG, "requestWillBeSent");
        mDataTranslator.saveInspectorRequest(request);
    }

    @Override
    public void responseHeadersReceived(InspectorResponse response) {
        Log.d(TAG, "responseHeadersReceived");
        mDataTranslator.saveInspectorResponse(response);
    }

    @Override
    public void httpExchangeFailed(String requestId, String errorText) {
        Log.d(TAG, "httpExchangeFailed");
    }

    @Nullable
    @Override
    public InputStream interpretResponseStream(String requestId, @Nullable String contentType, @Nullable String contentEncoding, @Nullable InputStream inputStream, ResponseHandler responseHandler) {
        Log.d(TAG, "interpretResponseStream");
        return mDataTranslator.saveInterpretResoinseStream(requestId, contentType, contentEncoding, inputStream);
    }

    @Override
    public void responseReadFailed(String requestId, String errorText) {
        Log.d(TAG, "responseReadFailed");
    }

    @Override
    public void responseReadFinished(String requestId) {
        Log.d(TAG, "responseReadFinished");
    }

    @Override
    public void dataSent(String requestId, int dataLength, int encodedDataLength) {
        Log.d(TAG, "dataSent");
    }

    @Override
    public void dataReceived(String requestId, int dataLength, int encodedDataLength) {
        Log.d(TAG, "dataReceived");
    }

    @Override
    public String nextRequestId() {
        Log.d(TAG, "nextRequestId");
        return String.valueOf(mNextRequestId.getAndIncrement());
    }

    @Override
    public void webSocketCreated(String requestId, String url) {
        Log.d(TAG, "webSocketCreated");
    }

    @Override
    public void webSocketClosed(String requestId) {
        Log.d(TAG, "webSocketClosed");
    }

    @Override
    public void webSocketWillSendHandshakeRequest(InspectorWebSocketRequest request) {
        Log.d(TAG, "webSocketWillSendHandshakeRequest");
    }

    @Override
    public void webSocketHandshakeResponseReceived(InspectorWebSocketResponse response) {
        Log.d(TAG, "webSocketHandshakeResponseReceived");
    }

    @Override
    public void webSocketFrameSent(InspectorWebSocketFrame frame) {
        Log.d(TAG, "webSocketFrameSent");
    }

    @Override
    public void webSocketFrameReceived(InspectorWebSocketFrame frame) {
        Log.d(TAG, "webSocketFrameReceived");
    }

    @Override
    public void webSocketFrameError(String requestId, String errorMessage) {
        Log.d(TAG, "webSocketFrameError");
    }
}
