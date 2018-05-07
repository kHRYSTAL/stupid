package me.khrystal.weyuereader.view.fragment.impl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.weavey.loading.lib.LoadingLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.khrystal.weyuereader.R;
import me.khrystal.weyuereader.db.helper.CollBookHelper;
import me.khrystal.weyuereader.model.LocalFileBean;
import me.khrystal.weyuereader.utils.FileUtils;
import me.khrystal.weyuereader.utils.LoadingHelper;
import me.khrystal.weyuereader.utils.rxhelper.RxUtils;
import me.khrystal.weyuereader.view.adapter.LocalFileAdapter;
import me.khrystal.weyuereader.view.base.BaseFileFragment;
import me.khrystal.weyuereader.viewmodel.BaseViewModel;
import me.khrystal.weyuereader.widget.DividerItemDecoration;
import me.khrystal.weyuereader.widget.theme.ColorButton;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/5/7
 * update time:
 * email: 723526676@qq.com
 */

public class LocalBookFragment extends BaseFileFragment {

    List<LocalFileBean> mFileBeans = new ArrayList<>();
    @BindView(R.id.btn_scan)
    ColorButton btnScan;
    @BindView(R.id.rv_files)
    RecyclerView rvFiles;
    @BindView(R.id.loadlayout)
    LoadingLayout loadlayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = setContentView(container, R.layout.fragment_local_book, new BaseViewModel(mContext));
        ButterKnife.bind(this, view);
        return view;
    }

    public static LocalBookFragment newInstance() {
        LocalBookFragment fragment = new LocalBookFragment();
        return fragment;
    }

    @Override
    public void initView() {
        super.initView();
        mAdapter = new LocalFileAdapter(mFileBeans);
        rvFiles.setLayoutManager(new LinearLayoutManager(mContext));
        rvFiles.addItemDecoration(new DividerItemDecoration(mContext));
        rvFiles.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(((adapter, view, position) -> {
            //
            String id = mFileBeans.get(position).getFile().getAbsolutePath();
            if (CollBookHelper.getsInstance().findBookById(id) != null) {
                return;
            }
            mAdapter.setCheckedItem(position);
            // 反馈
            if (mListener != null) {
                mListener.onItemCheckedChange(mAdapter.getItemIsChecked(position));
            }
        }));

        btnScan.setOnClickListener(v -> {
            scanFiles();
        });
    }

    /**
     * 搜索文件
     */
    private void scanFiles() {
        LoadingHelper.getInstance().showLoading(mContext);
        addDisposable(FileUtils.getSDTxtFile()
                .compose(RxUtils::toSimpleSingle)
                .subscribe(files -> {
                    LoadingHelper.getInstance().hideLoading();
                    mFileBeans.clear();
                    if (files.size() == 0) {
                        loadlayout.setStatus(LoadingLayout.Empty);
                    } else {
                        loadlayout.setStatus(LoadingLayout.Success);
                        for (File file : files) {
                            LocalFileBean localFileBean = new LocalFileBean();
                            localFileBean.setSelect(false);
                            localFileBean.setFile(file);
                            mFileBeans.add(localFileBean);
                        }
                        mAdapter.notifyDataSetChanged();
                        // 反馈
                        if (mListener != null) {
                            mListener.onCategoryChanged();
                        }
                    }
                }));
    }
}
