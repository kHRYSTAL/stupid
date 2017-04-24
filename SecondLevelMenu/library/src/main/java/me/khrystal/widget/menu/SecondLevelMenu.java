package me.khrystal.widget.menu;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import me.khrystal.widget.menu.adapter.TextAdapter;
import me.khrystal.widget.menu.genericity.ChildAble;

/**
 * usage: 二级菜单 用于人脉
 * author: kHRYSTAL
 * create time: 17/4/24
 * update time:
 * email: 723526676@qq.com
 */

public class SecondLevelMenu<T extends ChildAble> extends LinearLayout {

    private ListView parentListView;
    private ListView childListView;
    private ArrayList<T> parentDatas = new ArrayList<>();
    // 当前右侧列表数据
    private ArrayList<T> childDatas = new ArrayList<>();
    // 从父列表获取的子列表集合
    private SparseArray<List<T>> children = new SparseArray<>();
    private TextAdapter parentListViewAdapter;
    private TextAdapter childListViewAdapter;
    private T selectData;

    private int parentPosition = -1;
    private int childPosition = -1;

    private OnSelectListener<T> mOnSelectListener;

    public SecondLevelMenu(Context context) {
        super(context);
        init(context);
    }

    public SecondLevelMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SecondLevelMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SecondLevelMenu(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_sl_menu, this, true);
        parentListView = (ListView) findViewById(R.id.parentView);
        childListView = (ListView) findViewById(R.id.childView);
        parentListViewAdapter = new TextAdapter(context, parentDatas, TextAdapter.TYPE_PARENT);
        parentListView.setAdapter(parentListViewAdapter);

        parentListViewAdapter.setOnItemClickListener(new TextAdapter.OnItemClickListener<T>() {
            @Override
            public void onItemClick(View view, T data, int pos) {
                if (pos < children.size()) {
                    childDatas.clear();
                    childDatas.addAll(children.get(pos));
                    childListViewAdapter.notifyDataSetChanged();
                }
            }
        });

        if (parentPosition < children.size()) {
            childDatas.addAll(children.get(parentPosition));
        }

        childListViewAdapter = new TextAdapter(context, childDatas, TextAdapter.TYPE_CHILD);
        childListView.setAdapter(childListViewAdapter);
        childListViewAdapter.setOnItemClickListener(new TextAdapter.OnItemClickListener<T>() {
            @Override
            public void onItemClick(View view, T data, int pos) {
                selectData = childDatas.get(pos);
                if (mOnSelectListener != null) {
                    mOnSelectListener.getValue(data);
                }
            }
        });
        if (parentPosition != -1 && childPosition != -1) {
            parentListViewAdapter.setSelectedPosition(parentPosition);
            childListViewAdapter.setSelectedPosition(childPosition);
        }
    }

    /**
     * 通过对象设置选中
     */
    public void setDefaultSelect(T parent, T child) {
        if (parent == null || child == null) {
            return;
        }
        for (int i = 0; i < parentDatas.size(); i++) {
            if (parentDatas.get(i).equals(parent)) {
                parentListViewAdapter.setSelectedPosition(i);
                childDatas.clear();
                if (i < children.size()) {
                    childDatas.addAll(children.get(i));
                }
                parentPosition = i;
                break;
            }
        }

        for (int i = 0; i < childDatas.size(); i++) {
            if (childDatas.get(i).equals(child)) {
                childListViewAdapter.setSelectedPosition(i);
                childPosition = i;
                break;
            }
        }
    }

    /**
     * 通过position设置选中
     */
    public void setDefaultSelect(int parentPosition, int childPosition) {
        this.parentPosition = parentPosition;
        this.childPosition = childPosition;
        if (parentListViewAdapter != null && parentPosition >= 0)
            parentListViewAdapter.setSelectedPosition(parentPosition);
        if (childListViewAdapter != null && childPosition >= 0)
            childListViewAdapter.setSelectedPosition(childPosition);
    }

    /**
     * 设置左侧 父列表数据 通过父列表数据 配置每个父item的子列表数据
     * @param  insertAll 是否在子列表里插入"全部"header 全部代表parent item数据
     */
    public void setParentData(List<T> data, boolean insertAll) {
        parentDatas.clear();
        parentDatas.addAll(data);
        if (insertAll) {
            for (int i = 0; i < data.size(); i++) {
                ArrayList child = data.get(i).getChild();
                ChildAble parent = data.get(i);
                parent.setIsParent(true);
                child.add(0, parent);
                children.put(i, child);
            }
        } else {
            for (int i = 0; i < data.size(); i++) {
                children.put(i, data.get(i).getChild());
            }
        }
        if (parentListViewAdapter != null)
            parentListViewAdapter.notifyDataSetChanged();
        if (childListViewAdapter != null)
            childListViewAdapter.notifyDataSetChanged();
    }



    public void setOnSelectListener(OnSelectListener<T> onSelectListener) {
        mOnSelectListener = onSelectListener;
    }

    public interface OnSelectListener<T extends ChildAble> {
        public void getValue(T childData);
    }
}
