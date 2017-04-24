package me.khrystal.widget.menu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import me.khrystal.widget.menu.R;
import me.khrystal.widget.menu.genericity.ChildAble;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/4/24
 * update time:
 * email: 723526676@qq.com
 */

public class TextAdapter<T extends ChildAble> extends BaseAdapter {

    public static final int TYPE_PARENT = 0;
    public static final int TYPE_CHILD = 1;
    private Context mContext;
    private List<T> mListData;
    private int selectedPos = -1;
    private T selected;
    private View.OnClickListener onClickListener;
    private OnItemClickListener<T> mOnItemClickListener;
    private int mType;


    public TextAdapter(Context context, List<T> listData, int type) {
        mContext = context;
        mListData = listData;
        mType = type;
        init();
    }

    private void init() {
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPos = (Integer) v.getTag();
                setSelectedPosition(selectedPos);
                if (mOnItemClickListener != null) {
                    if (selectedPos != -1)
                    mOnItemClickListener.onItemClick(v, mListData.get(selectedPos), selectedPos);
                }
            }
        };
    }

    /**
     * 设置选中的position, 并通知列表刷新
     */
    public void setSelectedPosition(int pos) {
        if (mListData != null && pos < mListData.size()) {
            selectedPos = pos;
            selected = mListData.get(pos);
            notifyDataSetChanged();
        }
    }

    /**
     * 设置选中的position 但不通知刷新
     */
    public void setSelectedPositionNoNotify(int pos) {
        selectedPos = pos;
        if (mListData != null && pos < mListData.size()) {
            selected = mListData.get(pos);
        }
    }

    /**
     * 获取选中的position
     */
    public int getSelectedPosition() {
        if (mListData != null && selectedPos < mListData.size()) {
            return selectedPos;
        }
        return -1;
    }

    @Override
    public int getCount() {
        if (mListData != null)
            return mListData.size();
        else
            return 0;
    }

    @Override
    public T getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.menu_sl_item, parent, false);
            holder.textView = (TextView) convertView.findViewById(R.id.tvSlText);
            holder.divider = convertView.findViewById(R.id.slDivider);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textView.setTag(position);
        T data = mListData.get(position);
        if (selected != null && selected.equals(data)) {
            // TODO: 17/4/24 设置选中颜色
        } else {
            // TODO: 17/4/24 设置默认颜色
        }
        // 填充数据
        if(data.isParent() && mType == TYPE_CHILD) {
            holder.textView.setText("全部");
        } else {
            holder.textView.setText(data.toText());
        }
        holder.textView.setOnClickListener(onClickListener);
        return convertView;
    }

    public void setOnItemClickListener(OnItemClickListener<T> l) {
        mOnItemClickListener = l;
    }

    class ViewHolder {
        public TextView textView;
        public View divider;
    }


    public interface OnItemClickListener<T> {
        public void onItemClick(View view, T data, int pos);
    }
}
