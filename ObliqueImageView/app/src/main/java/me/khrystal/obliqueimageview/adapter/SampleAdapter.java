package me.khrystal.obliqueimageview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.ButterKnife;
import me.khrystal.obliqueimageview.R;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/5/5
 * update time:
 * email: 723526676@qq.com
 */

public class SampleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<String> mDataset = new ArrayList<>();
    public Context context;

    public SampleAdapter(Context context) {
        this.context = context;
    }

    public void addData() {
        for (int i = 0; i < 8; i++) {
            mDataset.add("Sample txt " + i);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = null;
        if (viewType % 4 == 0) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_one, parent, false);
        } else if (viewType % 4 == 1) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_two, parent, false);
        } else if (viewType % 4 == 2) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_three, parent, false);
        } else if (viewType % 4 == 3) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_four, parent, false);
        }
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
