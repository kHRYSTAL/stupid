package me.khrystal.appbarlayoutdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/8/7
 * update time:
 * email: 723526676@qq.com
 */

public class ListFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<String> datas;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initData();
        bindData();
    }

    private void bindData() {
        A adapter = new A();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    private void initData() {
        datas = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            datas.add("position:" + i);
        }
    }

    private void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
    }

    class A extends RecyclerView.Adapter<VH> {

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            VH h = new VH(LayoutInflater.from(getContext())
                    .inflate(R.layout.item, parent, false));

            return h;
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {
            holder.bind(datas.get(position));
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }
    }

    class VH extends RecyclerView.ViewHolder {

        TextView textView;

        public VH(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tvItem);
        }

        public void bind(String text) {
            if (!TextUtils.isEmpty(text)) {
                textView.setText(text);
            }
        }
    }


}
