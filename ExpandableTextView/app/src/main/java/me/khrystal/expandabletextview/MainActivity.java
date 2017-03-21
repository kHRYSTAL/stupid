package me.khrystal.expandabletextview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.khrystal.widget.ExpandableTextView;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Bean> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        dataList = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            Bean bean = new Bean();
            if (i % 5 == 0) {
                bean.data = getString(R.string.tips);
                dataList.add(bean);
            } else {
                bean.data = getString(R.string.tips_short);
                dataList.add(bean);
            }
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setAdapter(new A());
    }

    class A extends RecyclerView.Adapter<VH> {

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            VH vh = new VH(LayoutInflater.from(MainActivity.this).inflate(R.layout.item, parent, false));
            return vh;
        }

        //只要在getview时为其赋值为准确的宽度值即可，无论采用何种方法
        private int etvWidth;

        @Override
        public void onBindViewHolder(final VH holder, final int position) {
            if(etvWidth == 0){
                holder.textView.post(new Runnable() {
                    @Override
                    public void run() {
                        etvWidth = holder.textView.getWidth();
                    }
                });
            }
            holder.textView.updateForRecyclerView(dataList.get(position).data,
                    etvWidth,
                    dataList.get(position).isExpand ? ExpandableTextView.STATE_EXPAND : ExpandableTextView.STATE_SHRINK);
            holder.textView.setExpandListener(new ExpandableTextView.OnExpandListener() {
                @Override
                public void onExpand(ExpandableTextView view) {
                    dataList.get(position).isExpand = true;
                }

                @Override
                public void onShrink(ExpandableTextView view) {
                    dataList.get(position).isExpand = false;
                }
            });
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }
    }

    class VH extends RecyclerView.ViewHolder {

        ExpandableTextView textView;

        public VH(View itemView) {
            super(itemView);
            textView = (ExpandableTextView) itemView.findViewById(R.id.tv);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "textView OnClick", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}
