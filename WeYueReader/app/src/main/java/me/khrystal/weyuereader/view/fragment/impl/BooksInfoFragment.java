package me.khrystal.weyuereader.view.fragment.impl;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.weavey.loading.lib.LoadingLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.khrystal.weyuereader.R;
import me.khrystal.weyuereader.model.BookBean;
import me.khrystal.weyuereader.view.activity.impl.BookDetailActivity;
import me.khrystal.weyuereader.view.adapter.BookInfoAdapter;
import me.khrystal.weyuereader.view.base.BaseFragment;
import me.khrystal.weyuereader.view.fragment.IBookInfo;
import me.khrystal.weyuereader.viewmodel.fragment.VMBooksInfo;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/5/7
 * update time:
 * email: 723526676@qq.com
 */

public class BooksInfoFragment extends BaseFragment implements IBookInfo {

    String titleName;
    String getder; // 男生 女生
    String type; // 热门 完结
    @BindView(R.id.rv_bookinfo)
    RecyclerView rvBookinfo;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    @BindView(R.id.loadinglayout)
    LoadingLayout loadinglayout;
    private VMBooksInfo mModel;
    List<BookBean> mBookBeans = new ArrayList<>();
    private BookInfoAdapter mBookInfoAdapter;
    private int loadPage = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mModel = new VMBooksInfo(mContext, this);
        View view = setContentView(container, R.layout.fragment_book_info, mModel);
        ButterKnife.bind(this, view);
        return view;
    }

    public static BooksInfoFragment newInstance(String titleName, String getder, String type) {
        Bundle args = new Bundle();
        args.putString("titleName", titleName);
        args.putString("getder", getder);
        args.putString("type", type);
        BooksInfoFragment fragment = new BooksInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initView() {
        super.initView();
        titleName = getArguments().getString("titleName");
        getder = getArguments().getString("getder");
        type = getArguments().getString("type");

        refresh.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                ++loadPage;
                mModel.getBooks(type, titleName, loadPage);
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                loadPage = 1;
                mModel.getBooks(type, titleName, 1);
            }
        });
        refresh.autoRefresh();
        loadinglayout.setOnReloadListener(v -> {
            mModel.getBooks(type, titleName, 1);
        });
        mBookInfoAdapter = new BookInfoAdapter(mBookBeans);
        rvBookinfo.setLayoutManager(new LinearLayoutManager(mContext));
        mBookInfoAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        rvBookinfo.setAdapter(mBookInfoAdapter);

        mBookInfoAdapter.setOnItemClickListener(((adapter, view, position) -> {
            Intent intent = new Intent();
            intent.setClass(mContext, BookDetailActivity.class);
            intent.putExtra("bookid", mBookBeans.get(position).get_id());
            // 共享元素动画
            if (android.os.Build.VERSION.SDK_INT > 20) {
                ImageView imageView = view.findViewById(R.id.book_brief_iv_portrait);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity(), imageView, "bookImage").toBundle());
            } else {
                startActivity(intent);
            }
        }));
    }

    @Override
    public void getBooks(List<BookBean> bookBeans, boolean isLoadMore) {
        if (!isLoadMore) {
            mBookBeans.clear();
        }
        mBookBeans.addAll(bookBeans);
        mBookInfoAdapter.notifyDataSetChanged();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void stopLoading() {
        refresh.finishRefresh();
        refresh.finishLoadmore();
    }

    @Override
    public void emptyData() {
        loadinglayout.setStatus(LoadingLayout.Empty);
    }

    @Override
    public void errorData(String errorMsg) {
        loadinglayout.setEmptyText(errorMsg).setStatus(LoadingLayout.Error);
    }

    @Override
    public void NetWorkError() {
        loadinglayout.setStatus(LoadingLayout.No_Network);
    }
}
