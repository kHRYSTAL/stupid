package me.khrystal.nighttheme;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/2/24
 * update time:
 * email: 723526676@qq.com
 */
public class SimpleAuthorAdapter extends RecyclerView.Adapter<SimpleAuthorAdapter.SimpleViewHolder> {


    private final View.OnClickListener mSimpleClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(), "toast", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View inflate = inflater.inflate(R.layout.item_layout, parent, false);
        return new SimpleViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {
        holder.itemView.setOnClickListener(mSimpleClickListener);
    }

    @Override
    public int getItemCount() {
        return 20;
    }

    static class SimpleViewHolder extends RecyclerView.ViewHolder {

        public SimpleViewHolder(View itemView) {
            super(itemView);
        }
    }
}
