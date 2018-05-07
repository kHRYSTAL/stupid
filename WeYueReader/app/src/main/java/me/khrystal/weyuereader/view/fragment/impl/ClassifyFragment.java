package me.khrystal.weyuereader.view.fragment.impl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.weavey.loading.lib.LoadingLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.khrystal.weyuereader.R;
import me.khrystal.weyuereader.model.BookClassifyBean;
import me.khrystal.weyuereader.view.adapter.ClassifyAdapter;
import me.khrystal.weyuereader.view.base.BaseFragment;
import me.khrystal.weyuereader.view.fragment.IClassifyBook;
import me.khrystal.weyuereader.viewmodel.fragment.VMBookClassify;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/5/7
 * update time:
 * email: 723526676@qq.com
 */

public class ClassifyFragment extends BaseFragment implements IClassifyBook {


    @BindView(R.id.rv_classify)
    RecyclerView rvClassify;
    @BindView(R.id.loadinglayout)
    LoadingLayout loadinglayout;

    String tabName;
    ClassifyAdapter mClassifyAdapter;
    private VMBookClassify mModel;
    List<BookClassifyBean.ClassifyBean> mClassifyBeans = new ArrayList<>();
    String getder = "male";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mModel = new VMBookClassify(mContext, this);
        View view = setContentView(container, R.layout.fragment_classify, mModel);
        ButterKnife.bind(this, view);
        return view;
    }

    public static ClassifyFragment newInstance(String tabName) {
        Bundle args = new Bundle();
        args.putString("tabName", tabName);
        ClassifyFragment fragment = new ClassifyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initView() {
        super.initView();
        tabName = getArguments().getString("tabName");
        mModel.bookClassify();
        mClassifyAdapter = new ClassifyAdapter(mClassifyBeans);
        rvClassify.setLayoutManager(new LinearLayoutManager(mContext));
        mClassifyAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        rvClassify.setAdapter(mClassifyAdapter);
        mClassifyAdapter.setOnItemClickListener(((adapter, view, position) -> {
            Bundle bundle = new Bundle();
            bundle.putString("getder", getder);
            bundle.putString("titleName", mClassifyBeans.get(position).getName());
            // 跳转到图书列表页
//            startActivity();
        }));
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void emptyData() {
        loadinglayout.setStatus(LoadingLayout.Empty);
    }

    @Override
    public void errorData(String errorMsg) {
        loadinglayout.setStatus(LoadingLayout.No_Network);
    }

    @Override
    public void NetWorkError() {

    }

    @Override
    public void getBookClassify(BookClassifyBean bookClassifyBean) {
        loadinglayout.setStatus(LoadingLayout.Success);
        mClassifyBeans.clear();
        switch (tabName) {
            case "男生":
                getder = "male";
                mClassifyBeans.addAll(bookClassifyBean.getMale());
                break;
            case "女生":
                getder = "female";
                mClassifyBeans.addAll(bookClassifyBean.getFemale());
                break;
            case "出版":
                getder = "press";
                mClassifyBeans.addAll(bookClassifyBean.getPress());
                break;
        }
        mClassifyAdapter.notifyDataSetChanged();
    }
}
