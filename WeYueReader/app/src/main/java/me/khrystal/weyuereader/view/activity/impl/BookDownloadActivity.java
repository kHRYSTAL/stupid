package me.khrystal.weyuereader.view.activity.impl;

import android.content.ServiceConnection;

import java.util.ArrayList;
import java.util.List;

import me.khrystal.weyuereader.db.entity.DownloadTaskBean;
import me.khrystal.weyuereader.view.adapter.BookDownloadAdapter;
import me.khrystal.weyuereader.view.base.BaseActivity;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/5/7
 * update time:
 * email: 723526676@qq.com
 */

public class BookDownloadActivity extends BaseActivity {

    private List<DownloadTaskBean> mTaskBeans = new ArrayList<>();
    private BookDownloadAdapter mDownloadAdapter;
    private ServiceConnection mServiceConnection;
//    private BookDown.IDownloadManager mService;
}
