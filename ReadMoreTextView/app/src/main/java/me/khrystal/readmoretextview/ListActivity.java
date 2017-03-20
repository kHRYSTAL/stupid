package me.khrystal.readmoretextview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.khrystal.widget.ReadMoreTextView;

public class ListActivity extends AppCompatActivity {

    List<Bean> dataList;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        initData();
        initView();
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(ListActivity.this));
        recyclerView.setAdapter(new A());
    }

    private void initData() {
        dataList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Bean bean = new Bean();
            if (i % 5 == 0) {
                bean.data = getString(R.string.tips);
            } else {
                bean.data = getString(R.string.tips_short);
            }
            dataList.add(bean);
        }
    }


    class A extends RecyclerView.Adapter<VH> {

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            VH vh = new VH(LayoutInflater.from(ListActivity.this).inflate(R.layout.item, parent, false));

            return vh;
        }

        @Override
        public void onBindViewHolder(final VH holder, final int position) {
            holder.textView.setExpandState(dataList.get(position).isExpand); //TODO this method must before setText !!
            holder.textView.setText(dataList.get(position).data, TextView.BufferType.NORMAL);
            holder.textView.setOnExpandStateChangeListener(new ReadMoreTextView.OnExpandStateChangeListener() {
                @Override
                public void onStateChange(boolean isExpand) {
                    dataList.get(position).isExpand = isExpand;
                }
            });
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }
    }

    class VH extends RecyclerView.ViewHolder {
        ReadMoreTextView textView;
        public VH(View itemView) {
            super(itemView);
            textView = (ReadMoreTextView) itemView.findViewById(R.id.rmtv);
        }
    }
}
