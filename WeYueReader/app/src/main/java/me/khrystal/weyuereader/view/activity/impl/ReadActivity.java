package me.khrystal.weyuereader.view.activity.impl;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;
import me.khrystal.weyuereader.R;
import me.khrystal.weyuereader.db.entity.BookChapterBean;
import me.khrystal.weyuereader.db.entity.CollBookBean;
import me.khrystal.weyuereader.db.helper.BookChapterHelper;
import me.khrystal.weyuereader.model.BookChaptersBean;
import me.khrystal.weyuereader.utils.BrightnessUtils;
import me.khrystal.weyuereader.utils.ReadSettingManager;
import me.khrystal.weyuereader.utils.ScreenUtils;
import me.khrystal.weyuereader.utils.StatusBarUtils;
import me.khrystal.weyuereader.utils.StringUtils;
import me.khrystal.weyuereader.utils.rxhelper.RxUtils;
import me.khrystal.weyuereader.view.activity.IBookChapters;
import me.khrystal.weyuereader.view.adapter.ReadCategoryAdapter;
import me.khrystal.weyuereader.view.base.BaseActivity;
import me.khrystal.weyuereader.viewmodel.activity.VMBookContentInfo;
import me.khrystal.weyuereader.widget.dialog.ReadSettingDialog;
import me.khrystal.weyuereader.widget.page.NetPageLoader;
import me.khrystal.weyuereader.widget.page.PageLoader;
import me.khrystal.weyuereader.widget.page.PageView;
import me.khrystal.weyuereader.widget.page.TxtChapter;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/5/8
 * update time:
 * email: 723526676@qq.com
 */

public class ReadActivity extends BaseActivity implements IBookChapters {

    private static final String TAG = ReadActivity.class.getSimpleName();
    public static final int REQUEST_MORE_SETTING = 1;
    // 注册 Brightness 的 uri
    private final Uri BRIGHTNESS_MODE_URI =
            Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS_MODE);
    private final Uri BRIGHTNESS_URI =
            Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS);
    private final Uri BRIGHTNESS_ADJ_URI =
            Settings.System.getUriFor("screen_auto_brightness_adj");
    public static final String EXTRA_COLL_BOOK = "extra_coll_book";
    public static final String EXTRA_IS_COLLECTED = "extra_is_collected";
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.read_abl_top_menu)
    AppBarLayout readAblTopMenu;
    @BindView(R.id.pv_read_page)
    PageView pvReadPage;
    @BindView(R.id.read_tv_page_tip)
    TextView readTvPageTip;
    @BindView(R.id.read_tv_pre_chapter)
    TextView readTvPreChapter;
    @BindView(R.id.read_sb_chapter_progress)
    SeekBar readSbChapterProgress;
    @BindView(R.id.read_tv_next_chapter)
    TextView readTvNextChapter;
    @BindView(R.id.read_tv_category)
    TextView readTvCategory;
    @BindView(R.id.read_tv_night_mode)
    TextView readTvNightMode;
    @BindView(R.id.read_tv_setting)
    TextView readTvSetting;
    @BindView(R.id.read_ll_bottom_menu)
    LinearLayout readLlBottomMenu;
    @BindView(R.id.rv_read_category)
    RecyclerView rvReadCategory;
    @BindView(R.id.read_dl_slide)
    DrawerLayout readDlSlide;

    private boolean isRegistered = false;

    //region view
    private ReadSettingDialog mSettingDialog;
    private PageLoader mPageLoader;
    private Animation mTopInAnim;
    private Animation mTopOutAnim;
    private Animation mBottomInAnim;
    private Animation mBottomOutAnim;
    //endregion

    private CollBookBean mCollBook;
    // 控制屏幕常亮
    private PowerManager.WakeLock mWakeLock;

    private boolean isCollected = false;
    private boolean isNightMode = false;
    private boolean isFullScreen = false;
    private String mBookId;
    ReadCategoryAdapter mReadCategoryAdapter;
    List<TxtChapter> mTxtChapters = new ArrayList<>();
    private VMBookContentInfo mVmContentInfo;
    List<BookChapterBean> bookChapterList = new ArrayList<>();

    // 电池信息和时间更新的广播
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
                int level = intent.getIntExtra("level", 0);
                mPageLoader.updateBattery(level);
            } else if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
                mPageLoader.updateTime();
            }
        }
    };

    //亮度调节监听
    //由于亮度调节没有 Broadcast 而是直接修改 ContentProvider 的。所以需要创建一个 Observer 来监听 ContentProvider 的变化情况。
    private ContentObserver mBrightObserver = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange);

            //判断当前是否跟随屏幕亮度，如果不是则返回
            if (selfChange || !mSettingDialog.isBrightFollowSystem()) return;

            //如果系统亮度改变，则修改当前 Activity 亮度
            if (BRIGHTNESS_MODE_URI.equals(uri)) {
                Log.d(TAG, "亮度模式改变");
            } else if (BRIGHTNESS_URI.equals(uri) && !BrightnessUtils.isAutoBrightness(ReadActivity.this)) {
                Log.d(TAG, "亮度模式为手动模式 值改变");
                BrightnessUtils.setBrightness(ReadActivity.this, BrightnessUtils.getScreenBrightness(ReadActivity.this));
            } else if (BRIGHTNESS_ADJ_URI.equals(uri) && BrightnessUtils.isAutoBrightness(ReadActivity.this)) {
                Log.d(TAG, "亮度模式为自动模式 值改变");
                BrightnessUtils.setBrightness(ReadActivity.this, BrightnessUtils.getScreenBrightness(ReadActivity.this));
            } else {
                Log.d(TAG, "亮度调整 其他");
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mVmContentInfo = new VMBookContentInfo(mContext, this);
        setBinddingView(R.layout.activity_read, NO_BINDDING, mVmContentInfo);
    }

    @Override
    protected void initView() {
        super.initView();
        mCollBook = (CollBookBean) getIntent().getSerializableExtra(EXTRA_COLL_BOOK);
        isCollected = getIntent().getBooleanExtra(EXTRA_IS_COLLECTED, false);
        isNightMode = ReadSettingManager.getInstance().isNightMode();
        isFullScreen = ReadSettingManager.getInstance().isFullScreen();
        mBookId = mCollBook.get_id();

        tvToolbarTitle.setText(mCollBook.getTitle());
        StatusBarUtils.transparencyBar(this);
        // 获取页面加载器
        mPageLoader = pvReadPage.getPageLoader(mCollBook.isLocal());
        readDlSlide.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        initData();
        // 更多设置dialog
        mSettingDialog = new ReadSettingDialog(this, mPageLoader);
        setCategory();
        toggleNightMode();
        // 注册广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(mReceiver, intentFilter);

        // 设置当前Activity的Brightness
        if (ReadSettingManager.getInstance().isBrightnessAuto()) {
            BrightnessUtils.setBrightness(this, BrightnessUtils.getScreenBrightness(this));
        } else {
            BrightnessUtils.setBrightness(this, ReadSettingManager.getInstance().getBrightness());
        }
        // 初始化屏幕常量类
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
//        mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "keep bright");
        // 隐藏Status bar
        pvReadPage.post(() -> {
            hideSystemBar();
        });
        // 初始化TopMenu
        initTopMenu();
        // 初始化BottomMenu
        initBottomMenu();
        mPageLoader.setOnPageChangeListener(new PageLoader.OnPageChangeListener() {
            @Override
            public void onChapterChange(int pos) {
                setCategorySelect(pos);
            }

            @Override
            public void onLoadChapter(List<TxtChapter> chapters, int pos) {
                mVmContentInfo.loadContent(mBookId, chapters);
                setCategorySelect(mPageLoader.getChapterPos());
                if (mPageLoader.getPageStatus() == NetPageLoader.STATUS_LOADING
                        || mPageLoader.getPageStatus() == NetPageLoader.STATUS_ERROR) {
                    // 冻结使用
                    readSbChapterProgress.setEnabled(false);
                }
                // 隐藏提示
                readTvPageTip.setVisibility(View.GONE);
                readSbChapterProgress.setProgress(0);
            }

            @Override
            public void onCategoryFinish(List<TxtChapter> chapters) {
                mTxtChapters.clear();
                mTxtChapters.addAll(chapters);
                mReadCategoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onPageCountChange(int count) {
                readSbChapterProgress.setEnabled(true);
                readSbChapterProgress.setMax(count - 1);
                readSbChapterProgress.setProgress(0);
            }

            @Override
            public void onPageChange(int pos) {
                readSbChapterProgress.post(() -> {
                    readSbChapterProgress.setProgress(pos);
                });
            }
        });

        readSbChapterProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                if (readLlBottomMenu.getVisibility() == View.VISIBLE) {
                    // 显示标题
                    readTvPageTip.setText((progress + 1) + "/" + (readSbChapterProgress.getMax() + 1));
                    readTvPageTip.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 进行切换
                int pagePos = readSbChapterProgress.getProgress();
                if (pagePos != mPageLoader.getPagePos()) {
                    mPageLoader.skipToPage(pagePos);
                }
                // 隐藏提示
                readTvPageTip.setVisibility(View.GONE);
            }
        });

        pvReadPage.setTouchListener(new PageView.TouchListener() {

            @Override
            public void center() {
                toggleMenu(true);
            }

            @Override
            public boolean onTouch() {
                return !hideReadMenu();
            }

            @Override
            public boolean prePage() {
                return true;
            }

            @Override
            public boolean nextPage() {
                return true;
            }

            @Override
            public void cancel() {

            }
        });
    }

    private void initData() {
        if (mCollBook.isLocal()) {
            mPageLoader.openBook(mCollBook);
        } else {
            // 如果是网络文件
            // 如果是已经收藏的 那么就从数据库中获取目录
            if (isCollected) {
                Disposable disposable = BookChapterHelper.getsInstance().findBookChaptersInRx(mBookId)
                        .compose(RxUtils::toSimpleSingle)
                        .subscribe(beans -> {
                            mCollBook.setBookChapters(beans);
                            mPageLoader.openBook(mCollBook);
                            // 如果是被标记更新的, 重新从网络中获取目录
                            if (mCollBook.isUpdate()) {
                                mVmContentInfo.loadChapters(mBookId);
                            }
                        });
                mVmContentInfo.addDisposable(disposable);
            } else {
                // 加载书籍目录
                mVmContentInfo.loadChapters(mBookId);
            }
        }
    }

    private void setCategory() {
        rvReadCategory.setLayoutManager(new LinearLayoutManager(mContext));
        mReadCategoryAdapter = new ReadCategoryAdapter(mTxtChapters);
        rvReadCategory.setAdapter(mReadCategoryAdapter);
        if (mTxtChapters.size() > 0) {
            setCategorySelect(0);
        }
        mReadCategoryAdapter.setOnItemClickListener(((adapter, view, position) -> {
            setCategorySelect(position);
            readDlSlide.closeDrawer(Gravity.START);
            mPageLoader.skipToPage(position);
        }));
    }

    private void setCategorySelect(int selectPos) {
        for (int i = 0; i < mTxtChapters.size(); i++) {
            TxtChapter chapter = mTxtChapters.get(i);
            if (i == selectPos) {
                chapter.setSelect(true);
            } else {
                chapter.setSelect(false);
            }
        }
        mReadCategoryAdapter.notifyDataSetChanged();
    }

    private void toggleNightMode() {
        if (isNightMode) {
            readTvNightMode.setText(StringUtils.getString(R.string.wy_mode_morning));
            Drawable drawable = ContextCompat.getDrawable(this, R.mipmap.read_menu_morning);
            readTvNightMode.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
        } else {
            readTvNightMode.setText(StringUtils.getString(R.string.wy_mode_night));
            Drawable drawable = ContextCompat.getDrawable(this, R.mipmap.read_menu_night);
            readTvNightMode.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
        }
    }

    private void showSystembar() {
        // 显示
        StatusBarUtils.showUnStableStatusBar(this);
        if (isFullScreen) {
            StatusBarUtils.showUnStableNavBar(this);
        }
    }

    private void hideSystemBar() {
        // 隐藏
        StatusBarUtils.hideStableStatusBar(this);
        if (isFullScreen) {
            StatusBarUtils.hideStableNavBar(this);
        }
    }

    private void initTopMenu() {
        if (Build.VERSION.SDK_INT >= 19) {
            readAblTopMenu.setPadding(0, ScreenUtils.getStatusBarHeight(), 0, 0);
        }
    }

    private void initBottomMenu() {
        // 判断是否全屏
        if (ReadSettingManager.getInstance().isFullScreen()) {
            // 还需要设置mBottomMenu的底部高度
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) readLlBottomMenu.getLayoutParams();
            params.bottomMargin = ScreenUtils.getNavigationBarHeight();
            readLlBottomMenu.setLayoutParams(params);
        } else {
            // 设置mBottomMenu的底部距离
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) readLlBottomMenu.getLayoutParams();
            params.bottomMargin = 0;
            readLlBottomMenu.setLayoutParams(params);
        }
    }

    private boolean hideReadMenu() {
        hideSystemBar();
        if (readAblTopMenu.getVisibility() == View.VISIBLE) {
            toggleMenu(true);
            return true;
        } else if (mSettingDialog.isShowing()) {
            mSettingDialog.dismiss();
            return true;
        }
        return false;
    }

    private void toggleMenu(boolean hideStatusBar) {
        initMenuAnim();
        if (readAblTopMenu.getVisibility() == View.VISIBLE) {
            // 关闭
            readAblTopMenu.startAnimation(mTopOutAnim);
            readLlBottomMenu.startAnimation(mBottomOutAnim);
            readAblTopMenu.setVisibility(View.GONE);
            readLlBottomMenu.setVisibility(View.GONE);
            readTvPageTip.setVisibility(View.GONE);
            if (hideStatusBar) {
                hideSystemBar();
            }
        } else {
            readAblTopMenu.setVisibility(View.VISIBLE);
            readLlBottomMenu.setVisibility(View.VISIBLE);
            readAblTopMenu.startAnimation(mTopInAnim);
            readLlBottomMenu.startAnimation(mBottomInAnim);
            showSystembar();
        }
    }

    private void initMenuAnim() {
        if (mTopInAnim != null) return;

        mTopInAnim = AnimationUtils.loadAnimation(this, R.anim.slide_top_in);
        mTopOutAnim = AnimationUtils.loadAnimation(this, R.anim.slide_top_out);
        mBottomInAnim = AnimationUtils.loadAnimation(this, R.anim.slide_bottom_in);
        mBottomOutAnim = AnimationUtils.loadAnimation(this, R.anim.slide_bottom_out);
        //退出的速度要快
        mTopOutAnim.setDuration(200);
        mBottomOutAnim.setDuration(200);
    }

    @OnClick({R.id.read_tv_pre_chapter, R.id.read_tv_next_chapter, R.id.read_tv_category,
            R.id.read_tv_night_mode, R.id.read_tv_setting, R.id.tv_toolbar_title})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.read_tv_pre_chapter:
                setCategorySelect(mPageLoader.skipPreChapter());
                break;
            case R.id.read_tv_next_chapter:
                setCategorySelect(mPageLoader.skipNextChapter());
                break;
            case R.id.read_tv_category:
                setCategorySelect(mPageLoader.getChapterPos());
                //切换菜单
                toggleMenu(true);
                //打开侧滑动栏
                readDlSlide.openDrawer(Gravity.START);
                break;
            case R.id.read_tv_night_mode:
                if (isNightMode) {
                    isNightMode = false;
                } else {
                    isNightMode = true;
                }
                mPageLoader.setNightMode(isNightMode);
                toggleNightMode();
                break;
            case R.id.read_tv_setting:
                toggleMenu(false);
                mSettingDialog.show();
                break;
            case R.id.tv_toolbar_title:
                finish();
                break;
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void bookChapters(BookChaptersBean bookChaptersBean) {
        bookChapterList.clear();
        for (BookChaptersBean.ChapterBean bean : bookChaptersBean.getChapters()) {
            BookChapterBean chapterBean = new BookChapterBean();
            chapterBean.setBookId(bookChaptersBean.getBook());
            chapterBean.setLink(bean.getLink());
            chapterBean.setTitle(bean.getTitle());
//            chapterBean.setTaskName("下载");
            chapterBean.setUnreadble(bean.isRead());
            bookChapterList.add(chapterBean);
        }
        mCollBook.setBookChapters(bookChapterList);

        //如果是更新加载，那么重置PageLoader的Chapter
        if (mCollBook.isUpdate() && isCollected) {
            mPageLoader.setChapterList(bookChapterList);
            //异步下载更新的内容存到数据库
            BookChapterHelper.getsInstance().saveBookChaptersWithAsync(bookChapterList);

        } else {
            mPageLoader.openBook(mCollBook);
        }
    }

    @Override
    public void finishChapters() {
        if (mPageLoader.getPageStatus() == PageLoader.STATUS_LOADING) {
            pvReadPage.post(() -> {
                mPageLoader.openChapter();
            });
        }
        //当完成章节的时候，刷新列表
        mReadCategoryAdapter.notifyDataSetChanged();
    }

    @Override
    public void errorChapters() {
        if (mPageLoader.getPageStatus() == PageLoader.STATUS_LOADING) {
            mPageLoader.chapterError();
        }
    }

    private void registerBrightObserver() {
        try {
            if (mBrightObserver != null) {
                if (!isRegistered) {
                    final ContentResolver cr = getContentResolver();
                    cr.unregisterContentObserver(mBrightObserver);
                    cr.registerContentObserver(BRIGHTNESS_MODE_URI, false, mBrightObserver);
                    cr.registerContentObserver(BRIGHTNESS_URI, false, mBrightObserver);
                    cr.registerContentObserver(BRIGHTNESS_ADJ_URI, false, mBrightObserver);
                    isRegistered = true;
                }
            }
        } catch (Throwable throwable) {
            Log.e(TAG, "[ouyangyj] register mBrightObserver error! " + throwable);
        }
    }

    private void unregisterBrightObserver() {
        try {
            if (mBrightObserver != null) {
                if (isRegistered) {
                    getContentResolver().unregisterContentObserver(mBrightObserver);
                    isRegistered = false;
                }
            }
        } catch (Throwable throwable) {
            Log.e(TAG, "unregister BrightnessObserver error! " + throwable);
        }
    }

    @Override
    public void onBackPressed() {
        if (readAblTopMenu.getVisibility() == View.VISIBLE) {
            // 非全屏下才收缩 全屏下直接退出
            if (!ReadSettingManager.getInstance().isFullScreen()) {
                toggleMenu(true);
                return;
            }
        } else if (mSettingDialog.isShowing()) {
            mSettingDialog.dismiss();
            return;
        } else if (readDlSlide.isDrawerOpen(Gravity.START)) {
            readDlSlide.closeDrawer(Gravity.START);
        }

        super.onBackPressed();
    }

    private void exit() {
        // 返回给bookdetail
        Intent result = new Intent();
        setResult(Activity.RESULT_OK, result);
        // 退出
        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerBrightObserver();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        mWakeLock.acquire();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWakeLock.release();
        if (isCollected) {
//            mPageLoader.saveRecord();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterBrightObserver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
        mPageLoader.closeBook();
    }
}
