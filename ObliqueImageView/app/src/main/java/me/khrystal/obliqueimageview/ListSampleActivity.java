package me.khrystal.obliqueimageview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.khrystal.obliqueimageview.adapter.SampleAdapter;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/5/5
 * update time:
 * email: 723526676@qq.com
 */

public class ListSampleActivity extends AppCompatActivity {

    @BindView(R.id.recycleview)
    RecyclerView recyclerView;
    SampleAdapter sampleAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_sample);
        ButterKnife.bind(this);
        sampleAdapter = new SampleAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(sampleAdapter);
        sampleAdapter.addData();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
