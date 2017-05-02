package me.khrystal.dropdownmenuplus;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import me.khrystal.dropdownmenuplus.doublegrid.BetterDoubleGridView;
import me.khrystal.dropdownmenuplus.entity.Parent;
import me.khrystal.widget.menu.adapter.MenuAdapter;
import me.khrystal.widget.menu.adapter.SimpleTextAdapter;
import me.khrystal.widget.menu.interfaces.OnFilterDoneListener;
import me.khrystal.widget.menu.interfaces.OnFilterItemClickListener;
import me.khrystal.widget.menu.typeview.DoubleListView;
import me.khrystal.widget.menu.typeview.SingleGridView;
import me.khrystal.widget.menu.typeview.SingleListView;
import me.khrystal.widget.menu.util.DensityUtil;
import me.khrystal.widget.menu.view.FilterCheckedTextView;

public class DropMenuAdapter implements MenuAdapter {
    private final Context mContext;
    private OnFilterDoneListener onFilterDoneListener;
    private String[] titles;

    public DropMenuAdapter(Context context, String[] titles, OnFilterDoneListener onFilterDoneListener) {
        this.mContext = context;
        this.titles = titles;
        this.onFilterDoneListener = onFilterDoneListener;
    }

    @Override
    public int getMenuCount() {
        return titles.length;
    }

    @Override
    public String getMenuTitle(int position) {
        return titles[position];
    }

    @Override
    public int getBottomMargin(int position) {
        if (position == 3) {
            return 0;
        }

        return DensityUtil.dip2px(mContext, 140);
    }

    @Override
    public View getView(int position, FrameLayout parentContainer) {
        View view = parentContainer.getChildAt(position);

        switch (position) {
            case 0:
                view = createSingleListView();
                break;
            case 1:
                view = createDoubleListView();
                break;
            case 2:
                view = createSingleGridView();
                break;
            case 3:
                view = createBetterDoubleGrid();
                break;
        }

        return view;
    }

    private View createSingleListView() {
        List<String> singleList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            singleList.add("aaa" + i);
        }
        SingleListView<String> singleListView = new SingleListView<String>(mContext)
                .adapter(new SimpleTextAdapter<String>(singleList, mContext) {
                    @Override
                    public String provideText(String string) {
                        return string;
                    }

                    @Override
                    protected void initCheckedTextView(FilterCheckedTextView checkedTextView) {
                        int dp = DensityUtil.dip2px(mContext, 15);
                        checkedTextView.setPadding(dp, dp, 0, dp);
                    }
                }).onItemClick(new OnFilterItemClickListener<String>() {
                    @Override
                    public void onItemClick(String item, boolean isCheck) {
                        onFilterDone(0, item, isCheck);
                    }
                });
        singleListView.setList(singleList, -1);
        return singleListView;
    }


    private View createDoubleListView() {
        List<Parent> list = new ArrayList<>();

        //第一项
        Parent parent = new Parent();
        parent.desc = "10";
        list.add(parent);

        //第二项
        parent = new Parent();
        parent.desc = "11";
        List<String> childList = new ArrayList<>();
        for (int i = 0; i < 13; ++i) {
            childList.add("11" + i);
        }
        parent.children = childList;
        list.add(parent);

        //第三项
        parent = new Parent();
        parent.desc = "12";
        childList = new ArrayList<>();
        for (int i = 0; i < 3; ++i) {
            childList.add("12" + i);
        }
        parent.children = childList;
        list.add(parent);

        DoubleListView<Parent, String> comTypeDoubleListView = new DoubleListView<Parent, String>(mContext)
                .leftAdapter(new SimpleTextAdapter<Parent>(list, mContext) {
                    @Override
                    public String provideText(Parent parent1) {
                        return parent1.desc;
                    }

                    @Override
                    protected void initCheckedTextView(FilterCheckedTextView checkedTextView) {
                        checkedTextView.setPadding(DensityUtil.dip2px(mContext, 44), DensityUtil.dip2px(mContext, 15), 0, DensityUtil.dip2px(mContext, 15));
                    }
                })
                .rightAdapter(new SimpleTextAdapter<String>(null, mContext) {
                    @Override
                    public String provideText(String s) {
                        return s;
                    }

                    @Override
                    protected void initCheckedTextView(FilterCheckedTextView checkedTextView) {
                        checkedTextView.setPadding(DensityUtil.dip2px(mContext, 30), DensityUtil.dip2px(mContext, 15), 0, DensityUtil.dip2px(mContext, 15));
                        checkedTextView.setBackgroundResource(android.R.color.white);
                    }
                })
                .onLeftItemClickListener(new DoubleListView.OnLeftItemClickListener<Parent, String>() {
                    @Override
                    public List<String> provideRightList(Parent item, int position) {
                        List<String> child = item.children;
                        return child;
                    }
                })
                .onRightItemClickListener(new DoubleListView.OnRightItemClickListener<Parent, String>() {
                    @Override
                    public void onRightItemClick(Parent item, String string) {

//                        onFilterDone(1, string, string);
                    }
                });




        comTypeDoubleListView.setLeftList(list, -1);
        comTypeDoubleListView.setRightList(list.get(0).children, -1);
        comTypeDoubleListView.getLeftListView().setBackgroundColor(mContext.getResources().getColor(R.color.b_c_fafafa));

        return comTypeDoubleListView;
    }


    private View createSingleGridView() {
        List<String> list = new ArrayList<>();
        for (int i = 20; i < 39; ++i) {
            list.add(String.valueOf(i));
        }

        SingleGridView<String> singleGridView = new SingleGridView<String>(mContext)
                .adapter(new SimpleTextAdapter<String>(list, mContext) {
                    @Override
                    public String provideText(String s) {
                        return s;
                    }

                    @Override
                    protected void initCheckedTextView(FilterCheckedTextView checkedTextView) {
                        checkedTextView.setPadding(0, DensityUtil.dip2px(context, 3), 0, DensityUtil.dip2px(context, 3));
                        checkedTextView.setGravity(Gravity.CENTER);
                        checkedTextView.setBackgroundResource(R.drawable.selector_filter_grid);
                    }
                })
                .onItemClick(new OnFilterItemClickListener<String>() {
                    @Override
                    public void onItemClick(String item, boolean isCheck) {

                        onFilterDone(2, item, isCheck);

                    }
                });

        singleGridView.setList(list, -1);


        return singleGridView;
    }


    private View createBetterDoubleGrid() {

        List<String> phases = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            phases.add("3top" + i);
        }
        List<String> areas = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            areas.add("3bottom" + i);
        }


        return new BetterDoubleGridView(mContext)
                .setmTopGridData(phases)
                .setmBottomGridList(areas)
                .setOnFilterDoneListener(onFilterDoneListener)
                .build();
    }


    private void onFilterDone(int position, String value, boolean isCheck) {
        if (onFilterDoneListener != null) {
            onFilterDoneListener.onFilterDone(position, value, isCheck);
        }
    }

}