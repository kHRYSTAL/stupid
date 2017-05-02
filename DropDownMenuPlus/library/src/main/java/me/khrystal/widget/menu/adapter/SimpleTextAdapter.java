package me.khrystal.widget.menu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import me.khrystal.widget.R;
import me.khrystal.widget.menu.util.DensityUtil;
import me.khrystal.widget.menu.view.FilterCheckedTextView;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/4/24
 * update time:
 * email: 723526676@qq.com
 */

public abstract class SimpleTextAdapter<T> extends BaseBaseAdapter<T> {

    private final LayoutInflater inflater;

    public SimpleTextAdapter(List<T> list, Context context) {
        super(list, context);
        inflater = LayoutInflater.from(context);
    }

    public static class FilterItemHolder {
        TextView checkedTextView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FilterItemHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.lv_item_filter, parent, false);
            holder = new FilterItemHolder();
            holder.checkedTextView = (FilterCheckedTextView) convertView;
            holder.checkedTextView.setPadding(0, DensityUtil.dip2px(context, 15), 0, DensityUtil.dip2px(context, 15));
            initCheckedTextView(holder.checkedTextView);
            convertView.setTag(holder);
        } else {
            holder = (FilterItemHolder) convertView.getTag();
        }

        T t = list.get(position);
        holder.checkedTextView.setText(provideText(t));

        return convertView;
    }

    public abstract String provideText(T t);

    protected void initCheckedTextView(TextView checkedTextView) {

    }


}
