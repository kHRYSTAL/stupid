package me.khrystal.weyuereader.view.fragment.impl;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import me.khrystal.weyuereader.R;
import me.khrystal.weyuereader.db.entity.CollBookBean;
import me.khrystal.weyuereader.db.entity.DownloadTaskBean;
import me.khrystal.weyuereader.db.helper.BookRecordHelper;
import me.khrystal.weyuereader.db.helper.CollBookHelper;
import me.khrystal.weyuereader.event.DeleteResponseEvent;
import me.khrystal.weyuereader.event.DeleteTaskEvent;
import me.khrystal.weyuereader.event.DownloadMessage;
import me.khrystal.weyuereader.utils.LoadingHelper;
import me.khrystal.weyuereader.utils.ToastUtils;
import me.khrystal.weyuereader.utils.rxhelper.RxBus;
import me.khrystal.weyuereader.utils.rxhelper.RxUtils;
import me.khrystal.weyuereader.view.adapter.BookShelfAdapter;
import me.khrystal.weyuereader.view.base.BaseFragment;
import me.khrystal.weyuereader.view.fragment.IBookShelf;
import me.khrystal.weyuereader.viewmodel.fragment.VMBookShelf;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/5/7
 * update time:
 * email: 723526676@qq.com
 */

public class BookShelfFragment extends BaseFragment implements IBookShelf {

    @BindView(R.id.rv_book_shelf)
    RecyclerView rvBookShelf;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;

    private BookShelfAdapter mBookAdapter;
    private List<CollBookBean> mAllBooks = new ArrayList<>();
    private VMBookShelf mModel;
    private boolean isCheck;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mModel = new VMBookShelf(mContext, this);
        View view = setContentView(container, R.layout.fragment_book_shelf, mModel);
        ButterKnife.bind(this, view);
        return view;
    }

    public static BookShelfFragment newInstance() {
        BookShelfFragment fragment = new BookShelfFragment();
        return fragment;
    }

    @Override
    public void initView() {
        super.initView();
        mBookAdapter = new BookShelfAdapter(mAllBooks);
        rvBookShelf.setLayoutManager(new LinearLayoutManager(getContext()));
        mBookAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        rvBookShelf.setAdapter(mBookAdapter);

        refresh.setOnRefreshListener(refreshlayout -> {
            mAllBooks.clear();
            List<CollBookBean> allBooks = CollBookHelper.getsInstance().findAllBooks();
            mAllBooks.addAll(allBooks);
            mBookAdapter.notifyDataSetChanged();
            // 从服务器获取书架上的书 并删除本地数据库已经缓存和收藏的
            mModel.getBookShelf(allBooks);
        });

        mBookAdapter.setOnItemClickListener(((adapter, view, position) -> {
            // 如果是本地文件, 首先判断这个文件是否存在
            CollBookBean collBookBean = mBookAdapter.getItem(position);
            if (collBookBean.isLocal()) {
                // id表示本地文件路径
                String path = collBookBean.get_id();
                File file = new File(path);
                // 判断这个本地文件是否存在
                if (file.exists()) {
                    Bundle bundle = new Bundle();
                    // TODO: 18/5/7 跳转至阅读页
//                    bundle.putSerializable(ReadActivity.EXTRA_COLL_BOOK, collBook);
//                    bundle.putBoolean(ReadActivity.EXTRA_IS_COLLECTED, true);
//                    startActivity(ReadActivity.class, bundle);
                } else {
                    //提示(从目录中移除这个文件)
                    new MaterialDialog.Builder(mContext)
                            .title(BookShelfFragment.this.getResources().getString(R.string.wy_common_tip))
                            .content("文件不存在,是否删除?")
                            .positiveText(BookShelfFragment.this.getResources().getString(R.string.wy_common_sure))
                            .onPositive((dialog, which) -> deleteBook(collBookBean, position))
                            .negativeText(BookShelfFragment.this.getResources().getString(R.string.wy_common_cancel))
                            .onNegative((dialog, which) -> dialog.dismiss())
                            .show();
                }
            } else {
                mModel.setBookInfo(collBookBean);
            }
        }));

        //添加书籍下载任务处理
        Disposable downloadDisp = RxBus.getsInstance()
                .toObservable(DownloadMessage.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(event -> {
                    //使用Toast提示
                    ToastUtils.show(event.message);
                });
        addDisposable(downloadDisp);


        //删除书籍处理
        Disposable deleteDisp = RxBus.getsInstance()
                .toObservable(DeleteResponseEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(event -> {
                    if (event.isDelete) {
                        ProgressDialog progressDialog = new ProgressDialog(mContext);
                        progressDialog.setMessage("正在删除中");
                        progressDialog.show();
                        CollBookHelper.getsInstance().removeBookInRx(event.collBook)
                                .compose(RxUtils::toSimpleSingle)
                                .subscribe(aVoid -> {
                                    progressDialog.dismiss();
                                    mAllBooks.clear();
                                    mAllBooks.addAll(CollBookHelper.getsInstance().findAllBooks());
                                    mBookAdapter.notifyDataSetChanged();
                                });
                    } else {
                        //弹出一个Dialog
                        AlertDialog tipDialog = new AlertDialog.Builder(getContext())
                                .setTitle("您的任务正在加载")
                                .setMessage("先请暂停任务再进行删除")
                                .setPositiveButton("确定", (dialog, which) -> {
                                    dialog.dismiss();
                                }).create();
                        tipDialog.show();
                    }
                });
        addDisposable(deleteDisp);

        mBookAdapter.setOnItemLongClickListener((adapter, view, position) -> {
            openItemDialog(mAllBooks.get(position), position);
            return true;
        });
    }

    private void openItemDialog(CollBookBean collBook, int position) {
        String[] menus;
//        if (collBook.isLocal()) {
        menus = getResources().getStringArray(R.array.wy_menu_local_book);
//        } else {
//            menus = getResources().getStringArray(R.array.wy_menu_net_book);
//        }

        new MaterialDialog.Builder(mContext)
                .title(collBook.getTitle())
                .items(menus)
                .itemsCallback((dialog, itemView, which, text) -> onItemMenuClick(menus[which], collBook, position))
                .show();
    }

    private void onItemMenuClick(String which, CollBookBean collBook, int position) {
        switch (which) {
            //缓存
            case "缓存":
                //2. 进行判断，如果CollBean中状态为未更新。那么就创建Task，加入到Service中去。
                //3. 如果状态为finish，并且isUpdate为true，那么就根据chapter创建状态
                //4. 如果状态为finish，并且isUpdate为false。
                downloadBook(collBook);
                break;
            //删除
            case "删除":
                deleteBook(collBook, position);
                break;
            default:
                break;
        }
    }

    private void downloadBook(CollBookBean collBookBean) {
        //创建任务
//        mPresenter.createDownloadTask(collBook);
        DownloadTaskBean task = new DownloadTaskBean();
        task.setTaskName(collBookBean.getTitle());
        task.setBookId(collBookBean.get_id());
        task.setBookChapters(collBookBean.getBookChapters());
        task.setLastChapter(collBookBean.getBookChapters().size());

        RxBus.getsInstance().post(task);
    }

    /**
     * 默认删除本地文件
     *
     * @param collBook
     */
    private void deleteBook(CollBookBean collBook, int position) {
        if (collBook.isLocal()) {
            new MaterialDialog.Builder(mContext)
                    .title("删除本地书籍")
                    .checkBoxPrompt("同时删除本地文件", false, (buttonView, isChecked) -> isCheck = isChecked)
                    .positiveText(R.string.wy_common_sure)
                    .onPositive((dialog, which) -> {
                        if (isCheck) {
                            LoadingHelper.getInstance().showLoading(mContext);
                            //删除
                            File file = new File(collBook.get_id());
                            if (file.exists()) file.delete();
                            CollBookHelper.getsInstance().removeBookInRx(collBook)
                                    .subscribe(s -> {
                                                ToastUtils.show(s);
                                                BookRecordHelper.getsInstance().removeBook(collBook.get_id());
                                                //从Adapter中删除
                                                mBookAdapter.remove(position);
                                                LoadingHelper.getInstance().hideLoading();
                                            }
                                            , throwable -> {
                                                ToastUtils.show("删除失败");
                                                LoadingHelper.getInstance().hideLoading();
                                            });
                        } else {
                            CollBookHelper.getsInstance().removeBookInRx(collBook);
                            BookRecordHelper.getsInstance().removeBook(collBook.get_id());
                            //从Adapter中删除
                            mBookAdapter.remove(position);
                        }
                        mBookAdapter.notifyDataSetChanged();
                    })
                    .negativeText(R.string.wy_common_cancel)
                    .onNegative((dialog, which) -> {
                        dialog.dismiss();
                    })
                    .show();
        } else {
            RxBus.getsInstance().post(new DeleteTaskEvent(collBook));
            mModel.deleteBookShelfToServer(collBook);
        }
    }

    @Override
    public void onResume() {
        mAllBooks.clear();
        List<CollBookBean> allBooks = CollBookHelper.getsInstance().findAllBooks();
        mAllBooks.addAll(allBooks);
        mBookAdapter.notifyDataSetChanged();
        mModel.getBookShelf(allBooks);
        super.onResume();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void stopLoading() {
        refresh.finishRefresh();
    }

    @Override
    public void bookShelfInfo(List<CollBookBean> beans) {
        mAllBooks.addAll(beans);
        mBookAdapter.notifyDataSetChanged();
    }

    @Override
    public void bookInfo(CollBookBean bean) {
        Bundle bundle = new Bundle();
        // TODO: 18/5/7 跳转至阅读页 读取网络书籍
//        bundle.putSerializable(ReadActivity.EXTRA_COLL_BOOK, bean);
//        bundle.putBoolean(ReadActivity.EXTRA_IS_COLLECTED, true);
//        startActivity(ReadActivity.class, bundle);
    }

    @Override
    public void deleteSuccess() {
        mBookAdapter.notifyDataSetChanged();
    }
}
