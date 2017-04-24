package me.khrystal.widget.menu.typeview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import me.khrystal.widget.menu.adapter.BaseBaseAdapter;
import me.khrystal.widget.menu.interfaces.OnFilterItemClickListener;
import me.khrystal.widget.menu.util.CommonUtil;

/**
 * usage: 单列表
 * author: kHRYSTAL
 * create time: 17/4/24
 * update time:
 * email: 723526676@qq.com
 */

public class SingleListView<DATA> extends ListView implements AdapterView.OnItemClickListener {

    private BaseBaseAdapter<DATA> mAdapter;
    private OnFilterItemClickListener<DATA> mOnItemClickListener;

    public SingleListView(Context context) {
        this(context, null);
    }

    public SingleListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SingleListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
     setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        setDivider(null);
        setDividerHeight(0);
        setSelector(new ColorDrawable(Color.TRANSPARENT));
        setOnItemClickListener(this);
    }

    public SingleListView<DATA> adapter(BaseBaseAdapter<DATA> adapter) {
        this.mAdapter = adapter;
        setAdapter(adapter);
        return this;
    }

    public SingleListView<DATA> onItemClick(OnFilterItemClickListener<DATA> onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
        return this;
    }

    public void setList(List<DATA> list, int checkedPosition) {
        mAdapter.setList(list);
        if (checkedPosition != -1) {
            setItemChecked(checkedPosition, true);
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (CommonUtil.isFastDoubleClick())
            return;
        DATA item = mAdapter.getItem(position);
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(item);
        }
    }
}
