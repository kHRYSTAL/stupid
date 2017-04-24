package me.khrystal.widget.menu.typeview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.List;

import me.khrystal.widget.R;
import me.khrystal.widget.menu.adapter.BaseBaseAdapter;
import me.khrystal.widget.menu.adapter.SimpleTextAdapter;
import me.khrystal.widget.menu.util.CommonUtil;

/**
 * usage: 二级列表
 * author: kHRYSTAL
 * create time: 17/4/24
 * update time:
 * email: 723526676@qq.com
 */

public class DoubleListView<LEFTD, RIGHTD> extends LinearLayout implements AdapterView.OnItemClickListener {

    private BaseBaseAdapter<LEFTD> mLeftAdapter;
    private BaseBaseAdapter<RIGHTD> mRightAdapter;
    private ListView lvLeft;
    private ListView lvRight;
    private OnLeftItemClickListener<LEFTD, RIGHTD> mOnLeftItemClickListener;
    private OnRightItemClickListener<LEFTD, RIGHTD> mOnRightItemClickListener;

    public DoubleListView(Context context) {
        this(context, null);
    }

    public DoubleListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DoubleListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setOrientation(VERTICAL);
        inflate(context, R.layout.merge_filter_list, this);
        lvLeft = (ListView) findViewById(R.id.lv_left);
        lvRight = (ListView) findViewById(R.id.lv_right);
        lvLeft.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lvRight.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lvLeft.setOnItemClickListener(this);
        lvRight.setOnItemClickListener(this);
    }

    public DoubleListView<LEFTD, RIGHTD> leftAdapter(SimpleTextAdapter<LEFTD> leftAdapter) {
        mLeftAdapter = leftAdapter;
        lvLeft.setAdapter(leftAdapter);
        return this;
    }

    public DoubleListView<LEFTD, RIGHTD> rightAdapter(SimpleTextAdapter<RIGHTD> rightAdapter) {
        mRightAdapter = rightAdapter;
        lvRight.setAdapter(rightAdapter);
        return this;
    }

    public void setLeftList(List<LEFTD> list, int checkedPosition) {
        mLeftAdapter.setList(list);
        if (checkedPosition != -1) {
            lvLeft.setItemChecked(checkedPosition, true);
        }
    }

    public void setRightList(List<RIGHTD> list, int checkedPosition) {
        mRightAdapter.setList(list);
        if (checkedPosition != -1) {
            lvRight.setItemChecked(checkedPosition, true);
        }
    }

    public interface OnLeftItemClickListener<LEFTD, RIGHTD> {
        /**
         * 通过左侧position 获取右侧列表
         * @return
         */
        List<RIGHTD> provideRightList(LEFTD item, int position);
    }

    public interface OnRightItemClickListener<LEFTD, RIGHTD> {
        void onRightItemClick(LEFTD item, RIGHTD childItem);
    }

    public DoubleListView<LEFTD, RIGHTD> onLeftItemClickListener(OnLeftItemClickListener<LEFTD, RIGHTD> onLeftItemClickListener) {
        this.mOnLeftItemClickListener = onLeftItemClickListener;
        return this;
    }

    public DoubleListView<LEFTD, RIGHTD> onRightItemClickListener(OnRightItemClickListener<LEFTD, RIGHTD> onRightItemClickListener) {
        this.mOnRightItemClickListener = onRightItemClickListener;
        return this;
    }

    public ListView getLeftListView() {
        return lvLeft;
    }

    public ListView getRightListView() {
        return lvRight;
    }



    private int mRightLastChecked;
    private int mLeftLastPosition;
    private int mLeftLastCheckedPosition;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (CommonUtil.isFastDoubleClick()) {
            return;
        }

        if (mLeftAdapter == null || mRightAdapter == null) {
            return;
        }
        // 点击左侧列表
        if (parent == lvLeft) {
            mLeftLastPosition = position;
            if (mOnLeftItemClickListener != null) {
                LEFTD item = mLeftAdapter.getItem(position);
                List<RIGHTD> rightds = mOnLeftItemClickListener.provideRightList(item, position);
                if (CommonUtil.isEmpty(rightds)) {
                    mLeftLastCheckedPosition = -1;
                }
            }
            lvRight.setItemChecked(mRightLastChecked, mLeftLastCheckedPosition == position);
        } else {
            mLeftLastCheckedPosition = mLeftLastPosition;
            mRightLastChecked = position;
            if (mOnRightItemClickListener != null) {
                mOnRightItemClickListener.onRightItemClick(mLeftAdapter.getItem(mLeftLastCheckedPosition),  mRightAdapter.getItem(mRightLastChecked));
            }
        }
    }
}
