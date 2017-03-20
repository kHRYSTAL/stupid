package me.khrystal.collapsibletextview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import me.khrystal.widget.CollapsibleTextView;


public class ListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Bean> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        initView();
        initData();
        bindData();

    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(ListActivity.this));
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

    private void bindData() {
        recyclerView.setAdapter(new A());
    }



    class A extends RecyclerView.Adapter<VH> {

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            VH vh = new VH(LayoutInflater.from(ListActivity.this).inflate(R.layout.item, parent, false));
            return vh;
        }

        @Override
        public void onBindViewHolder(final VH holder, final int position) {
            Log.e("OnBind","" + position + "," + dataList.get(position).isShrink);
            holder.textView.setOnStateChangeListener(new CollapsibleTextView.OnStateChangeListener() {
                @Override
                public void onStateChange(boolean isShrink) {
                    dataList.get(position).isShrink = isShrink;
                }
            });
            holder.textView.setText(dataList.get(position).data);
            holder.textView.resetState(dataList.get(position).isShrink);

        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }
    }

    class VH extends RecyclerView.ViewHolder {
        CollapsibleTextView textView;
        public VH(View itemView) {
            super(itemView);
            textView = (CollapsibleTextView) itemView.findViewById(R.id.tv);
        }
    }
}
