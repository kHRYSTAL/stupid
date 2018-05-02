package me.khrystal.weyuereader.widget.page;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;

import java.lang.ref.WeakReference;
import java.util.List;

import io.reactivex.disposables.Disposable;
import me.khrystal.weyuereader.db.entity.BookRecordBean;
import me.khrystal.weyuereader.db.entity.CollBookBean;
import me.khrystal.weyuereader.utils.ReadSettingManager;
import me.khrystal.weyuereader.utils.ScreenUtils;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/5/2
 * update time:
 * email: 723526676@qq.com
 */

public abstract class PageLoader {
    private static final String TAG = PageLoader.class.getSimpleName();

    public static final int STATUS_LOADING = 1; // 正在加载
    public static final int STATUS_FINISH = 2; // 加载完成
    public static final int STATUS_ERROR = 3; // 加载错误(一般是网络情况)
    public static final int STATUS_EMPTY = 4; // 空数据
    public static final int STATUS_PARSE = 5; // 正在解析(一般用于本地数据加载)

    static final int DEFAULT_MARGIN_HEIGHT = 28;
    static final int DEFAULT_MARGIN_WIDTH = 12;

    // 默认的参数配置
    private static final int DEFAULT_TIP_SIZE = 12;
    private static final int EXTRA_TITLE_SIZE = 4;

    // 当前的章节列表
    protected List<TxtChapter> mChapterList;
    // 书本对象
    protected CollBookBean mCollBook;
    // 监听器
    protected OnPageChangeListener mPageChangeListener;

    // 页面显示类
    private PageView mPageView;
    // 当前显示的页
    private TxtPage mCurPage;
    // 上一章的页面列表缓存
    private WeakReference<List<TxtPage>> mWeakPrePageList;
    // 当前章节的页面列表
    private List<TxtPage> mCurPageList;
    // 下一章的页面列表缓存
    private List<TxtPage> mNextPageList;

    // 下一页绘制缓冲区, 用户缓解卡顿问题
    private Bitmap mNextBitmap;

    // 绘制电池的画笔
    private Paint mBatteryPaint;
    // 绘制提示的画笔
    private Paint mTipPaint;
    // 绘制标题的画笔
    private Paint mTitlePaint;
    // 绘制背景颜色的画笔(用来擦除需要重绘的部分)
    private Paint mBgPaint;
    // 绘制小说内容的画笔
    private TextPaint mTextPaint;
    // 阅读器的配置选项
    private ReadSettingManager mSettingManager;
    // 被遮盖的页, 或者认为被取消显示的页
    private TxtPage mCancelPage;
    // 存储阅读记录类
    private BookRecordBean mBookRecord;

    //region params
    // 当前的状态
    protected int mStatus = STATUS_LOADING;
    // 当前章
    protected int mCurChapterPos = 0;
    // 书本是否打开
    protected boolean isBookOpen = false;

    private Disposable mPreLoadDisp;
    // 上一章的记录
    private int mLastChapter = 0;
    // 书籍绘制区域的宽高
    private int mVisibleWidth;
    private int mVisibleHeight;
    // 应用的宽高
    private int mDisplayWidth;
    private int mDisplayHeight;
    // 间距
    private int mMarginWidth;
    private int mMarginHeight;
    // 字体的颜色
    private int mTextColor;
    // 标题的大小
    private int mTitleSize;
    // 字体的大小
    private int mTextSize;
    // 行间距
    private int mTextInterval;
    // 标题行间距
    private int mTitleInterval;
    // 段落距离(基于行间距的额外距离)
    private int mTextPara;
    private int mTitlePara;
    // 电池的百分比
    private int mBatteryLevel;
    // 页面的翻页效果模式
    private int mPageMode;
    // 加载器的颜色主题
    private int mBgTheme;
    // 当前页面的背景
    private int mPageBg;
    // 当前是否是夜间模式
    private boolean isNightMode;
    //endregion

    public PageLoader(PageView pageView) {
        mPageView = pageView;
        // 初始化数据
        initData();
        // 初始化画笔
        initPaint();
        // 初始化PageView
        initPageView();
    }

    private void initData() {
        mSettingManager = ReadSettingManager.getInstance();
        mTextSize = mSettingManager.getTextSize();
        mTitleSize = mTextSize + ScreenUtils.spToPx(EXTRA_TITLE_SIZE);
        mPageMode = mSettingManager.getPageMode();
        isNightMode = mSettingManager.isNightMode();
        mBgTheme = mSettingManager.getReadBgTheme();
        if (isNightMode) {
            setBgColor(ReadSettingManager.NIGHT_MODE);
        } else {
            setBgColor(mBgTheme);
        }
        // 初始化参数
        mMarginWidth = ScreenUtils.dpToPx(DEFAULT_MARGIN_WIDTH);
        mMarginHeight = ScreenUtils.dpToPx(DEFAULT_MARGIN_HEIGHT);
        mTextInterval = mTextSize / 2;
        mTitleInterval = mTitleSize / 2;
        mTextPara = mTextSize; // 段落间距由text的高度决定
        mTitlePara = mTitleSize;
    }

    private void initPaint() {
        // 绘制提示的画笔
        mTipPaint = new Paint();
        mTipPaint.setColor(mTextColor);
        mTipPaint.setTextAlign(Paint.Align.LEFT); // 绘制的起始点
        mTipPaint.setTextSize(ScreenUtils.spToPx(DEFAULT_TIP_SIZE)); // tip默认的字体大小
        mTipPaint.setAntiAlias(true);
        mTipPaint.setSubpixelText(true);
        // 绘制页面内容的画笔
        mTextPaint = new TextPaint();
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setAntiAlias(true);
        // 绘制标题的画笔
        mTitlePaint = new TextPaint();
        mTitlePaint.setColor(mTextColor);
        mTitlePaint.setTextSize(mTitleSize);
        mTitlePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mTitlePaint.setTypeface(Typeface.DEFAULT_BOLD);
        mTitlePaint.setAntiAlias(true);
        // 绘制背景的画笔
        mBgPaint = new Paint();
        mBgPaint.setColor(mPageBg);

        mBatteryPaint = new Paint();
        mBatteryPaint.setAntiAlias(true);
        mBatteryPaint.setDither(true);
        if (isNightMode) {
            mBatteryPaint.setColor(Color.WHITE);
        } else {
            mBatteryPaint.setColor(Color.BLACK);
        }
    }

    private void initPageView() {
        // 配置参数
        mPageView.setPageMode(mPageMode);
        mPageView.setBgColor(mPageBg);
    }

    //region public method
    // 跳转至上一章
    public int skipPreChapter() {
        if (!isBookOpen) {
            return mCurChapterPos;
        }
        // 载入上一章
        if (prevChapter()) {
            mCurPage = getCurPage(0);
            mPageView.refreshPage();
        }
        return mCurChapterPos;
    }

    // 跳转至下一章
    public int skipNextChapter() {
        if (!isBookOpen) {
            return mCurChapterPos;
        }
        // 判断是否达到章节的终止点
        if (nextChapter()) {
            mCurPage = getCurPage(0);
            mPageView.refreshPage();
            ;
        }
        return mCurChapterPos;
    }

    // 跳转到指定章节
    public void skipToChapter(int pos) {
        // 正在加载
        mStatus = STATUS_LOADING;
        // 绘制当前的状态
        mCurChapterPos = pos;
        // 将上一章的缓存设置为null
        mWeakPrePageList = null;
        // 如果当前下一章缓存正在执行, 则取消缓存操作
        if (mPreLoadDisp != null) {
            mPreLoadDisp.dispose();
        }
        // 将下一章缓存设置为null
        mNextPageList = null;

        if (mPageChangeListener != null) {
            mPageChangeListener.onChapterChange(mCurChapterPos);
        }
        if (mCurPage != null) {
            // 重置position的位置, 防止正在加载的时候退出存储的位置为上一章的页码
            mCurPage.position = 0;
        }

        // 需要对ScrollAnimation进行重新布局
        mPageView.refreshPage();
    }

    // 跳转到具体的页
    public void skipToPage(int pos) {
        mCurPage = getCurPage(pos);
        mPageView.refreshPage();
    }

    // 自动翻到上一章
    public boolean autoPrevPage() {
        if (!isBookOpen)
            return false;
        return mPageView.autoPrevPage();
    }

    // 自动翻到下一章
    public boolean autoNextPage() {
        if (!isBookOpen)
            return false;
        return mPageView.autoNextPage();
    }

    // 更新时间
    public void updateTime() {
        if (mPageView.isPrepare() && !mPageView.isRunning()) {
            mPageView.drawCurPage(true);
        }
    }

    // 更新电量
    public void updateBattery(int level) {
        mBatteryLevel = level;
        if (mPageView.isPrepare() && !mPageView.isRunning()) {
            mPageView.drawCurPage(true);
        }
    }

    // 设置文字大小
    public void setTextSize(int textSize) {
        if (!isBookOpen) {
            return;
        }
        // 设置TextSize
        mTextSize = textSize;
        mTextInterval = mTextSize / 2;
        mTextPara = mTextSize;

        mTitleSize = mTextSize + ScreenUtils.spToPx(EXTRA_TITLE_SIZE);
        mTitleInterval = mTitleInterval / 2;
        mTitlePara = mTitleSize;
        // 设置画笔的字体大小
        mTextPaint.setTextSize(mTextSize);
        // 设置标题的字体大小
        mTitlePaint.setTextSize(mTitleSize);
        // 存储状态
        mSettingManager.setTextSize(mTextSize);
        // 取消缓存
        mWeakPrePageList = null;
        mNextPageList = null;
        // 如果当前为完成状态
        if (mStatus == STATUS_FINISH) {
            // 重新计算页面
            mCurPageList = loadPageList(mCurChapterPos);
            // 防止在最后一页, 通过修改字体, 以至于页面数减少导致的崩溃问题
            if (mCurPage.position >= mCurPageList.size()) {
                mCurPage.position = mCurPageList.size() - 1;
            }
        }
        // 重新设置文章指针的位置
        mCurPage = getCurPage(mCurPage.position);
        // 绘制
        mPageView.refreshPage();
    }

    // TODO: 18/5/2  
    //endregion


    public interface OnPageChangeListener {
        void onChapterChange(int pos);

        // 请求加载回调
        void onLoadChapter(List<TxtChapter> chapters, int pos);

        // 当目录加载完成的回调(必须要在创建的时候, 就要存在了)
        void onCategoryFinish(List<TxtChapter> chapters);

        // 页码改变
        void onPageCountChange(int count);

        // 页面改变
        void onPageChange(int pos);
    }
}
