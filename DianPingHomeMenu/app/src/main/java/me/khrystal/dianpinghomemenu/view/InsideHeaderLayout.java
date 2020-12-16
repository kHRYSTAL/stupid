package me.khrystal.dianpinghomemenu.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import me.khrystal.dianpinghomemenu.R;
import me.khrystal.dianpinghomemenu.bean.Menu;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 2020/12/16
 * update time:
 * email: 723526676@qq.com
 */

public class InsideHeaderLayout extends LinearLayout {

    private ViewPager viewPager;

    public InsideHeaderLayout(Context context) {
        this(context, null);
    }

    public InsideHeaderLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_inside_header, null);
        addView(view, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        viewPager = findViewById(R.id.viewpager);
    }

    public void fillPagerData(List<Menu> menus) {
        if (menus == null || menus.isEmpty())
            return;
        List<View> gridViews = new ArrayList<>();
        for (int i = 0; i < menus.size(); i++) {
            GridView gridView = (GridView) LayoutInflater.from(getContext()).inflate(R.layout.item_viewpager, viewPager, false);
            gridView.setNumColumns(4);
            GridViewAdapter adapter = new GridViewAdapter(menus.get(i).getItems());
            gridView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            gridViews.add(gridView);
        }
        ViewPagerAdapter adapter = new ViewPagerAdapter(gridViews);
        viewPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        if (viewPager != null) {
            viewPager.addOnPageChangeListener(listener);
        }
    }


    class GridViewAdapter extends BaseAdapter {

        private List<String> datas;

        public GridViewAdapter(List<String> datas) {
            this.datas = datas;
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int i) {
            return datas.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            ViewHolder vh = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_gridview, viewGroup, false);
                vh = new ViewHolder();
                vh.tv = (TextView) convertView.findViewById(R.id.tvItem);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            vh.tv.setText(datas.get(i));
            return convertView;
        }
    }

    class ViewHolder {
        public TextView tv;
    }

    class ViewPagerAdapter extends PagerAdapter {

        private List<View> mViewList;

        public ViewPagerAdapter(List<View> mViewList) {
            this.mViewList = mViewList;
        }

        @Override
        public int getCount() {
            return mViewList.size();
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView(mViewList.get(position));
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            container.addView(mViewList.get(position));
            return mViewList.get(position);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }
    }
}
