package me.khrystal.weyuereader.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import me.khrystal.weyuereader.model.BookBean;

/**
 * usage: todo
 * author: kHRYSTAL
 * create time: 18/4/25
 * update time:
 * email: 723526676@qq.com
 */

public class BookTagDialog extends Dialog {

    Context mConext;
    String tagName;
    private SmartRefreshLayout mRefreshLayout;
    private RecyclerView mRvBooks;
    List<BookBean> mBeans = new ArrayList<>();


    public BookTagDialog(@NonNull Context context) {
        super(context);
    }
}
