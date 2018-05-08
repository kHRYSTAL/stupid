package me.khrystal.weyuereader.view.activity.impl;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.khrystal.weyuereader.R;
import me.khrystal.weyuereader.db.entity.DownloadTaskBean;
import me.khrystal.weyuereader.utils.LogUtils;
import me.khrystal.weyuereader.view.adapter.BookDownloadAdapter;
import me.khrystal.weyuereader.view.base.BaseActivity;
import me.khrystal.weyuereader.view.service.BookDownloadService;
import me.khrystal.weyuereader.viewmodel.BaseViewModel;
import me.khrystal.weyuereader.widget.MarqueTextView;
import me.khrystal.weyuereader.widget.theme.ColorRelativeLayout;
import me.khrystal.weyuereader.widget.theme.ColorView;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/5/7
 * update time:
 * email: 723526676@qq.com
 */

public class BookDownloadActivity extends BaseActivity implements BookDownloadService.OnDownloadListener {

    @BindView(R.id.status_bar)
    ColorView statusBar;
    @BindView(R.id.iv_toolbar_back)
    AppCompatImageView ivToolbarBack;
    @BindView(R.id.tv_toolbar_title)
    MarqueTextView tvToolbarTitle;
    @BindView(R.id.iv_toolbar_more)
    AppCompatImageView ivToolbarMore;
    @BindView(R.id.crl)
    ColorRelativeLayout crl;
    @BindView(R.id.rv_book_download)
    RecyclerView rvBookDownload;
    private List<DownloadTaskBean> mTaskBeans = new ArrayList<>();
    private BookDownloadAdapter mDownloadAdapter;
    private ServiceConnection mServiceConnection;
    private BookDownloadService.IDownloadManager mService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setBinddingView(R.layout.activity_book_download, NO_BINDDING, new BaseViewModel(this));
    }

    @Override
    protected void initView() {
        super.initView();
        initThemeToolBar("缓存列表");
        // 设置下载列表数据
        mDownloadAdapter = new BookDownloadAdapter(mTaskBeans);
        rvBookDownload.setLayoutManager(new LinearLayoutManager(this));
        rvBookDownload.setAdapter(mDownloadAdapter);

        mDownloadAdapter.setOnItemClickListener(((adapter, view, position) -> {
            DownloadTaskBean taskBean = mTaskBeans.get(position);
            switch (taskBean.getStatus()) {
                // 准备暂停
                case DownloadTaskBean.STATUS_LOADING:
                    mService.setDownloadStatus(taskBean.getTaskName(), DownloadTaskBean.STATUS_PAUSE);
                    break;
                case DownloadTaskBean.STATUS_WAIT:
                    mService.setDownloadStatus(taskBean.getTaskName(), DownloadTaskBean.STATUS_PAUSE);
                    break;
                case DownloadTaskBean.STATUS_PAUSE:
                    mService.setDownloadStatus(taskBean.getTaskName(), DownloadTaskBean.STATUS_WAIT);
                    break;
                case DownloadTaskBean.STATUS_ERROR:
                    mService.setDownloadStatus(taskBean.getTaskName(), DownloadTaskBean.STATUS_WAIT);
                    break;
            }
        }));
        //获取缓存列表数据
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
//                mTaskBeans.clear();
                mService = (BookDownloadService.IDownloadManager) service;
                //添加数据到队列中
                mTaskBeans.addAll(mService.getDownloadTaskList());
                mService.setOnDownloadListener(BookDownloadActivity.this);
                mDownloadAdapter.notifyDataSetChanged();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };

        //绑定
        Intent service = new Intent(this, BookDownloadService.class);
        bindService(service, mServiceConnection, Service.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.print("onDestroy");
    }

    @Override
    public void onDownloadChange(int pos, int status, String msg) {
        DownloadTaskBean taskBean = mDownloadAdapter.getItem(pos);
        taskBean.setStatus(status);
        if (DownloadTaskBean.STATUS_LOADING == status) {
            taskBean.setCurrentChapter(Integer.valueOf(msg));
        }
        mDownloadAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDownloadResponse(int pos, int status) {
        DownloadTaskBean taskBean = mDownloadAdapter.getItem(pos);
        taskBean.setStatus(status);
        mDownloadAdapter.notifyItemChanged(pos);
    }
}
