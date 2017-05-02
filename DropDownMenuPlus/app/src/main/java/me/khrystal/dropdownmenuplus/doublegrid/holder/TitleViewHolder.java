package me.khrystal.dropdownmenuplus.doublegrid.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import me.khrystal.dropdownmenuplus.R;

public class TitleViewHolder extends RecyclerView.ViewHolder {

    public TitleViewHolder(Context mContext, ViewGroup parent) {
        super(LayoutInflater.from(mContext).inflate(R.layout.holder_title, parent, false));
    }


    public void bind(String s) {
        ((TextView) itemView).setText(s);
    }
}