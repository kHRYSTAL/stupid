package me.khrystal.util.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Map;

import me.khrystal.util.R;
import me.khrystal.util.oknetworkmonitor.DataPoolImpl;
import me.khrystal.util.oknetworkmonitor.NetworkFeedModel;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/6/12
 * update time:
 * email: 723526676@qq.com
 */

public class NetworkFeedDetailActivity extends AppCompatActivity {
    private NetworkFeedModel mNetworkFeedModel;
    private TextView mRequestHeadersTextView;
    private TextView mResponseHeadersTextView;
    private TextView mBodyTextView;
    private View mBackView;

    public static void start(Context context, String requestId) {
        Intent starter = new Intent(context, NetworkFeedDetailActivity.class);
        starter.putExtra("requestId", requestId);
        context.startActivity(starter);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRequestHeadersTextView = findViewById(R.id.request_headers_textView);
        mResponseHeadersTextView = findViewById(R.id.response_headers_textView);
        mBodyTextView = findViewById(R.id.body_textView);
        mBackView = findViewById(R.id.feed_detail_back_layout);
        mBackView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        initData();
    }

    private void initData() {
        String requestId = getIntent().getStringExtra("requestId");
        if (TextUtils.isEmpty(requestId)) {
            return;
        }
        mNetworkFeedModel = DataPoolImpl.getInstance().getNetworkFeedModel(requestId);
        if (mNetworkFeedModel == null) {
            return;
        }
        setRequestHeaders();
        setResponseHeaders();
        setBody();
    }

    private void setBody() {
        if (mNetworkFeedModel.getContentType().contains("json")) {
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();
            JsonObject bodyJO = new JsonParser().parse(mNetworkFeedModel.getBody()).getAsJsonObject();
            mBodyTextView.setText(gson.toJson(bodyJO));
        } else {
            mBodyTextView.setText(mNetworkFeedModel.getBody());
        }
    }

    private void setResponseHeaders() {
        mResponseHeadersTextView.setText(parseHeadersMapToString(mNetworkFeedModel.getResponseHeaderMap()));
    }

    private void setRequestHeaders() {
        mRequestHeadersTextView.setText(parseHeadersMapToString(mNetworkFeedModel.getRequestHeaderMap()));
    }

    private String parseHeadersMapToString(Map<String, String> headers) {
        if (headers == null || headers.isEmpty()) {
            return "Header is Empty.";
        }
        StringBuilder headersBuilder = new StringBuilder();
        for (String name : headers.keySet()) {
            if (TextUtils.isEmpty(name)) {
                continue;
            }
            headersBuilder
                    .append(name)
                    .append(": ")
                    .append(headers.get(name))
                    .append("\n");
        }
        return headersBuilder.toString();
    }
}
