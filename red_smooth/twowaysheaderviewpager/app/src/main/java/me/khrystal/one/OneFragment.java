package me.khrystal.one;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

import me.khrystal.one.widget.ORecyclerView;
import me.khrystal.twhv.R;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 16/4/1
 * update time:
 * email: 723526676@qq.com
 */
public class OneFragment extends Fragment{

    private ORecyclerView mRecyclerView;
    public ScrollView outer;
    public int headerId;
    public int contentId;

    public ArrayList<String> dataList;
    public SimpleAdapter mAdapter;

    public static OneFragment newInstance(){
        OneFragment fragment = new OneFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_one,container,false);
        mRecyclerView = (ORecyclerView) root.findViewById(R.id.recycler_view);
        mRecyclerView.HeaderId = headerId;
        mRecyclerView.parentScrollView = outer;
        mRecyclerView.contentContainerId = contentId;
        dataList = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            dataList.add("i = " + i);
        }

        mAdapter = new SimpleAdapter();
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }



    public class SimpleAdapter extends RecyclerView.Adapter<SimpleViewHolder>{


        @Override
        public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item,parent,false);
            SimpleViewHolder sv = new SimpleViewHolder(v);
            return sv;
        }

        @Override
        public void onBindViewHolder(SimpleViewHolder holder, int position) {
            if (position<dataList.size()){
                String text = dataList.get(position);
                holder.bind(text);
            }
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }
    }

    public class SimpleViewHolder extends RecyclerView.ViewHolder{

        TextView textView;


        public SimpleViewHolder(View itemView) {
            super(itemView);
            textView =
                    (TextView) itemView.findViewById(R.id.item_text);
        }


        public void bind(String text){
            if (!TextUtils.isEmpty(text))
                textView.setText(text);
        }
    }
}
