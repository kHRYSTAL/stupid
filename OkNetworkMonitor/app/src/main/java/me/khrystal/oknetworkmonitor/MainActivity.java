package me.khrystal.oknetworkmonitor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;

import me.khrystal.util.ui.NetworkFeedActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.test_button)
                .setOnClickListener(view ->
                        {
                            NetworkFeedActivity.start(this);
                        }
                );
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Thread() {
            @Override
            public void run() {
                super.run();
                OkHttpManager.getInstance().get("http://192.168.2.103:14020/impressions/6145788201744203780", new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e(TAG, "e = " + e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String body = response.body().string();
                        Log.d(TAG, body);
                    }
                });
            }
        }.run();
    }
}
