package me.khrystal.hwpushdemo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.huawei.android.pushagent.api.PushException;
import com.huawei.android.pushagent.api.PushManager;
import com.huawei.android.pushagent.api.PushManager.PushFeature;


public class TagReportActivity extends Activity {

    public final static String TAG = "PushDemo";

    private EditText mEdTag1Key;
    private EditText mEdTag1Value;
    private EditText mEdTag2Key;
    private EditText mEdTag2Value;
    private EditText mEdDeleteKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tagreport);
        Button tagButton = (Button) findViewById(R.id.tag);
        Button delButton = (Button) findViewById(R.id.delTag);
        Button getButton = (Button) findViewById(R.id.getTag);
        Button openLocationBtn = (Button) findViewById(R.id.openLocation);
        Button closeLocationBtn = (Button) findViewById(R.id.closeLocation);

        mEdTag1Key = (EditText) findViewById(R.id.tag1Key);
        mEdTag1Value = (EditText) findViewById(R.id.tag1Value);
        mEdTag2Key = (EditText) findViewById(R.id.tag2Key);
        mEdTag2Value = (EditText) findViewById(R.id.tag2Value);
        mEdDeleteKey = (EditText) findViewById(R.id.delEdit);

        // 添加标签
        tagButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String tag1KeyStr = mEdTag1Key.getText().toString();
                String tag1ValueStr = mEdTag1Value.getText().toString();
                String tag2KeyStr = mEdTag2Key.getText().toString();
                String tag2ValueStr = mEdTag2Value.getText().toString();
                HashMap<String, String> map = new HashMap<String, String>();
                if (!TextUtils.isEmpty(tag1KeyStr)
                        && !TextUtils.isEmpty(tag1ValueStr)) {
                    map.put(tag1KeyStr, tag1ValueStr);
                }
                if (!TextUtils.isEmpty(tag2KeyStr)
                        && !TextUtils.isEmpty(tag2ValueStr)) {
                    map.put(tag2KeyStr, tag2ValueStr);
                }
                try {
                    PushManager.setTags(TagReportActivity.this, map);
                } catch (PushException e) {
                    Log.e(TAG, e.toString());
                }
            }
        });

        //删除标签
        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick, begin to delete tags");
                String delKeyStr = mEdDeleteKey.getText().toString();
                List<String> list = new ArrayList<String>();
                list.add(delKeyStr);
                try {
                    PushManager.deleteTags(TagReportActivity.this, list);
                } catch (PushException e) {
                    Log.e(TAG, e.toString());
                }
            }
        });

        getButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Map<String, String> tags;
                try {
                    tags = PushManager.getTags(TagReportActivity.this);
                    String tip = "";
                    if (null != tags && !tags.isEmpty()) {
                        for (Map.Entry<String, String> mapEntry : tags.entrySet()) {
                            String key = mapEntry.getKey();
                            String value = mapEntry.getValue();
                            tip +=  ("tagKey:"+ key + ", tagValue:" + value+"; ");
                        }
                        Log.i(TAG, tip);
                        Toast.makeText(TagReportActivity.this, tip, Toast.LENGTH_SHORT).show();
                    }
                } catch (PushException e) {
                    Log.e(TAG, e.toString());
                }
            }
        });

        //上报位置信息
        openLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PushManager.enableFeature(TagReportActivity.this, PushFeature.LOCATION_BASED_MESSAGE, true);
            }
        });

        //关闭位置信息的周期上报
        closeLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PushManager.enableFeature(TagReportActivity.this, PushFeature.LOCATION_BASED_MESSAGE, false);
            }
        });
        
    }
}