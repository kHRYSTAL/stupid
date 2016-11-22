package me.khrystal.swiperecyclerview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.khrystal.widget.swipe.SwipeMenuLayout;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 16/11/14
 * update time:
 * email: 723526676@qq.com
 */

public class Test2Activity extends AppCompatActivity{

    private RecyclerView mRecyclerView;
    private List<String> mData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rv);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(Test2Activity.this));
        mData = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            mData.add("" + i);
        }
        A adapter = new A();
        mRecyclerView.setAdapter(adapter);
    }

    class A extends RecyclerView.Adapter<H> {
        
        @Override
        public H onCreateViewHolder(ViewGroup parent, int viewType) {
            return new H(LayoutInflater.from(Test2Activity.this)
                    .inflate(R.layout.item, parent, false));

        }

        @Override
        public void onBindViewHolder(H holder, final int position) {
            holder.contentView.setText("Number:" + mData.get(position));
            holder.rightView.setText("Del:" + mData.get(position));

            holder.rightView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mData.remove(position);
                    notifyItemRemoved(position);
                }
            });
            holder.swipeMenuLayout.setOnSwipeListener(new SwipeMenuLayout.OnSwipeListener() {
                @Override
                public void OnSwipeOpen(SwipeMenuLayout layout) {

                }

                @Override
                public void OnSwipeClose(SwipeMenuLayout layout) {

                }
            });
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    }

    class H extends RecyclerView.ViewHolder {
        TextView contentView;
        TextView rightView;
        SwipeMenuLayout swipeMenuLayout;
        View itemView;
        public H(View itemView) {
            super(itemView);
            contentView = (TextView) itemView.findViewById(R.id.item_text);
            rightView = (TextView) itemView.findViewById(R.id.right_view);
            swipeMenuLayout = (SwipeMenuLayout) itemView.findViewById(R.id.swipe_layout);
            this.itemView = itemView;
        }
    }
}
