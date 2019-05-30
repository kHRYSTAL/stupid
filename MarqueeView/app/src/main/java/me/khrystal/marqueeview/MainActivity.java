package me.khrystal.marqueeview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.khrystal.widget.marqueeview.MarqueeView;
import me.khrystal.widget.marqueeview.MarqueeViewAdapter;

public class MainActivity extends AppCompatActivity {

    private MarqueeView marqueeView;
    private List<String> mList;
    private MarqueeViewAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        marqueeView = findViewById(R.id.marqueeView);

        mList = new ArrayList<>();
        mList.add("第一个item");
        mList.add("第二个item");
        mList.add("第三个item");
        mList.add("第四个item");

        mAdapter = new MarqueeViewAdapter<String>(mList) {
            @Override
            protected int getItemLayoutId() {
                return R.layout.item_text;
            }

            @Override
            protected void initView(View view, int position, String item) {
                ((TextView) view).setText(item);
            }
        };

        marqueeView.setAdapter(mAdapter);

        findViewById(R.id.btnAdd).setOnClickListener((view) -> {
            mList.add("追加的item:" + String.valueOf(System.currentTimeMillis()));
            mAdapter.notifyDataSetChanged();
        });

        findViewById(R.id.btnDel).setOnClickListener((view) -> {
            if (mList != null) {
                mList.remove(mList.size() - 1);
                mAdapter.notifyDataSetChanged();
            }
        });
        marqueeView.start();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }


}
