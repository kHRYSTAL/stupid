package me.khrystal.util.utils;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import me.khrystal.util.oknetworkmonitor.DataPoolImpl;
import me.khrystal.util.oknetworkmonitor.NetworkFeedModel;
import me.khrystal.util.stetho.NetworkEventReporter;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/6/11
 * update time:
 * email: 723526676@qq.com
 */

public class DataTranslator {
    private static final String TAG = "DataTranslator";
    private Map<String, Long> mStartTimeMap = new HashMap<>();

    public void saveInspectorRequest(NetworkEventReporter.InspectorRequest request) {
        String requestId = request.id();
        mStartTimeMap.put(request.id(), System.currentTimeMillis());
        NetworkFeedModel networkFeedModel = DataPoolImpl.getInstance().getNetworkFeedModel(requestId);

        String url = request.url();

        if (!TextUtils.isEmpty(url)) {
            String host = Uri.parse(url).getHost();
            networkFeedModel.setHost(host);
            networkFeedModel.setUrl(url);
        }
        networkFeedModel.setMethod(request.method());
        Map<String, String> headersMap = new HashMap<>();
        for (int i = 0, count = request.headerCount(); i < count; i++) {
            String name = request.headerName(i);
            String value = request.headerValue(i);
            headersMap.put(name, value);
        }
        networkFeedModel.setRequestHeaderMap(headersMap);
    }

    public void saveInspectorResponse(NetworkEventReporter.InspectorResponse response) {
        String requestId = response.requestId();
        long costTime;
        if (mStartTimeMap.containsKey(requestId)) {
            costTime = System.currentTimeMillis() - mStartTimeMap.get(requestId);
            Log.d(TAG, "cost time = " + costTime + "ms");
        } else {
            costTime = -1;
        }

        NetworkFeedModel networkFeedModel = DataPoolImpl.getInstance().getNetworkFeedModel(requestId);
        networkFeedModel.setCostTime(costTime);
        networkFeedModel.setStatus(response.statusCode());
        Map<String, String> headersMap = new HashMap<>();
        for (int i = 0, count = response.headerCount(); i < count; i++) {
            String name = response.headerName(i);
            String value = response.headerValue(i);
            headersMap.put(name, value);
        }
        networkFeedModel.setResponseHeaderMap(headersMap);
    }

    public InputStream saveInterpretResoinseStream(String requestId, String contentType,
                                                   String contentEncoding, InputStream inputStream) {
        NetworkFeedModel networkFeedModel = DataPoolImpl.getInstance().getNetworkFeedModel(requestId);
        networkFeedModel.setContentType(contentType);
        ByteArrayOutputStream byteArrayOutputStream = parseAndSaveBody(inputStream, networkFeedModel);
        InputStream newInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        try {
            byteArrayOutputStream.close();
        } catch (IOException e) {
            Log.e(TAG, TAG, e);
        }
        return newInputStream;
    }

    private ByteArrayOutputStream parseAndSaveBody(InputStream inputStream, NetworkFeedModel networkFeedModel) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buffer)) > -1) {
                byteArrayOutputStream.write(buffer, 0, len);
            }
            byteArrayOutputStream.flush();
            InputStream newStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(newStream));
            StringBuilder bodyBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                bodyBuilder.append(line + '\n');
            }
            String body = bodyBuilder.toString();
            networkFeedModel.setBody(body);
            networkFeedModel.setSize(body.getBytes().length);
        } catch (IOException e) {
            Log.e(TAG, TAG, e);
        }
        return byteArrayOutputStream;
    }
}
