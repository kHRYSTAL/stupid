package me.khrystal.weyuereader.widget.page;

import android.support.annotation.Nullable;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.disposables.Disposable;
import me.khrystal.weyuereader.db.entity.BookChapterBean;
import me.khrystal.weyuereader.db.entity.CollBookBean;
import me.khrystal.weyuereader.db.helper.CollBookHelper;
import me.khrystal.weyuereader.utils.Charset;
import me.khrystal.weyuereader.utils.Constant;
import me.khrystal.weyuereader.utils.FileUtils;
import me.khrystal.weyuereader.utils.IOUtils;
import me.khrystal.weyuereader.utils.StringUtils;
import me.khrystal.weyuereader.utils.ToastUtils;
import me.khrystal.weyuereader.utils.rxhelper.RxUtils;
import me.khrystal.weyuereader.model.Void;

/**
 * usage: todo not complete
 * author: kHRYSTAL
 * create time: 18/5/3
 * update time:
 * email: 723526676@qq.com
 */

public class LocalPageLoader extends PageLoader {

    private static final String TAG = LocalPageLoader.class.getSimpleName();
    // 默认从文件中获取数据长度
    private final static int BUFFER_SIZE = 512 * 1024;
    // 没有标题的时候 每个章节的最大长度
    private static final int MAX_LENGTH_WITH_NO_CHAPTER = 10 * 1024;

    // "序(章)|前言"
    private final static Pattern mPreChapterPattern = Pattern.compile("^(\\s{0,10})((\u5e8f[\u7ae0\u8a00]?)|(\u524d\u8a00)|(\u6954\u5b50))(\\s{0,10})$", Pattern.MULTILINE);

    //正则表达式章节匹配模式
    // "(第)([0-9零一二两三四五六七八九十百千万壹贰叁肆伍陆柒捌玖拾佰仟]{1,10})([章节回集卷])(.*)"
    private static final String[] CHAPTER_PATTERNS = new String[]{"^(.{0,8})(\u7b2c)([0-9\u96f6\u4e00\u4e8c\u4e24\u4e09\u56db\u4e94\u516d\u4e03\u516b\u4e5d\u5341\u767e\u5343\u4e07\u58f9\u8d30\u53c1\u8086\u4f0d\u9646\u67d2\u634c\u7396\u62fe\u4f70\u4edf]{1,10})([\u7ae0\u8282\u56de\u96c6\u5377])(.{0,30})$",
            "^(\\s{0,4})([\\(\u3010\u300a]?(\u5377)?)([0-9\u96f6\u4e00\u4e8c\u4e24\u4e09\u56db\u4e94\u516d\u4e03\u516b\u4e5d\u5341\u767e\u5343\u4e07\u58f9\u8d30\u53c1\u8086\u4f0d\u9646\u67d2\u634c\u7396\u62fe\u4f70\u4edf]{1,10})([\\.:\uff1a\u0020\f\t])(.{0,30})$",
            "^(\\s{0,4})([\\(\uff08\u3010\u300a])(.{0,30})([\\)\uff09\u3011\u300b])(\\s{0,2})$",
            "^(\\s{0,4})(\u6b63\u6587)(.{0,20})$",
            "^(.{0,4})(Chapter|chapter)(\\s{0,4})([0-9]{1,4})(.{0,30})$"};


    // 书本的大小
    private long mBookSize;
    // 章节解析模式
    private Pattern mChapterPattern = null;
    // 获取书本的文件
    private File mBookFile;
    // 编码类型
    private Charset mCharset;

    private Disposable mChapterDisp = null;

    public LocalPageLoader(PageView pageView) {
        super(pageView);
        mStatus = STATUS_PARSE;
    }

    @Override
    public void openBook(CollBookBean collBook) {
        super.openBook(collBook);
        mBookFile = new File(collBook.get_id());
        // 这里id表示本地文件的路径
        // 判断是否文件存在
        if (!mBookFile.exists())
            return;
        // 获取文件的大小
        mBookSize = mBookFile.length();
        // 文件内容为空
        if (mBookSize == 0) {
            mStatus = STATUS_EMPTY;
            return;
        }
        isBookOpen = false;
        //通过RxJava异步处理分章事件
        Single.create(new SingleOnSubscribe<Void>() {
            @Override
            public void subscribe(SingleEmitter<Void> e) throws Exception {
                loadBook(mBookFile);
                e.onSuccess(new Void());
            }
        }).compose(RxUtils::toSimpleSingle)
                .subscribe(new SingleObserver<Void>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mChapterDisp = d;
                    }

                    @Override
                    public void onSuccess(Void value) {
                        mChapterDisp = null;
                        //提示目录加载完成
                        if (mPageChangeListener != null) {
                            mPageChangeListener.onCategoryFinish(mChapterList);
                        }
                        //打开章节，并加载当前章节
                        openChapter();
                    }

                    @Override
                    public void onError(Throwable e) {
                        //数据读取错误(弄个文章解析错误的Tip,会不会好一点)
                        mStatus = STATUS_ERROR;
                        ToastUtils.show("数据解析错误");
                    }
                });
    }

    private void loadBook(File bookFile) throws IOException {
        // 获取文件编码
        mCharset = FileUtils.getCharset(bookFile.getAbsolutePath());
        // 查找章节, 分配章节
        loadChapters();
    }

    private void loadChapters() throws IOException {
        // TODO: 18/5/3
    }

    @Nullable
    @Override
    protected List<TxtPage> loadPageList(int chapterPos) {
        if (mChapterList == null)
            throw new IllegalArgumentException("Chapter list must not null");
        TxtChapter chapter = mChapterList.get(chapterPos);
        // 从文件中获取数据
        byte[] content = getChapterContent(chapter);
        ByteArrayInputStream bais = new ByteArrayInputStream(content);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(bais, mCharset.getName()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return loadPages(chapter, br);
    }

    /**
     * 从文件中提取一章的内容
     *
     * @param chapter
     * @return
     */
    private byte[] getChapterContent(TxtChapter chapter) {
        RandomAccessFile bookStream = null;
        try {
            bookStream = new RandomAccessFile(mBookFile, "r");
            bookStream.seek(chapter.start);
            int extent = (int) (chapter.end - chapter.start);
            byte[] content = new byte[extent];
            bookStream.read(content, 0, extent);
            return content;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(bookStream);
        }
        return new byte[0];
    }

    /**
     * 1. 检查文件中是否存在章节名
     * 2. 判断文件中使用的章节名类型的正则表达式
     *
     * @return 是否存在章节名
     */
    private boolean checkChapterType(RandomAccessFile bookStream) throws IOException {
        //首先获取128k的数据
        byte[] buffer = new byte[BUFFER_SIZE / 4];
        int length = bookStream.read(buffer, 0, buffer.length);
        //进行章节匹配
        for (String str : CHAPTER_PATTERNS) {
            Pattern pattern = Pattern.compile(str, Pattern.MULTILINE);
            Matcher matcher = pattern.matcher(new String(buffer, 0, length, mCharset.getName()));
            //如果匹配存在，那么就表示当前章节使用这种匹配方式
            if (matcher.find()) {
                mChapterPattern = pattern;
                //重置指针位置
                bookStream.seek(0);
                return true;
            }
        }
        //重置指针位置
        bookStream.seek(0);
        return false;
    }

    @Override
    boolean prevChapter() {
        if (mStatus == STATUS_PARSE_ERROR)
            return false;
        return super.prevChapter();
    }

    @Override
    boolean nextChapter() {
        if (mStatus == STATUS_PARSE_ERROR)
            return false;
        return super.nextChapter();
    }

    @Override
    public void skipToChapter(int pos) {
        super.skipToChapter(pos);
        // 加载章节
        openChapter();
    }

    @Override
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        super.setOnPageChangeListener(listener);
        if (mChapterList != null && mChapterList.size() != 0) {
            mPageChangeListener.onCategoryFinish(mChapterList);
        }
    }

    @Override
    public void setChapterList(List<BookChapterBean> bookChapters) {
        // do nothing
    }

    @Override
    public void saveRecord() {
        super.saveRecord();
        // 修改当前collbook记录
        if (mCollBook != null && isBookOpen) {
            // 表示当前CollBook已经阅读
            mCollBook.setIsUpdate(false);
            mCollBook.setLastChapter(mChapterList.get(mCurChapterPos).getTitle());
            mCollBook.setLastRead(StringUtils.dateConvert(System.currentTimeMillis(), Constant.FORMAT_BOOK_DATE));
            // 直接更新
            CollBookHelper.getsInstance().saveBook(mCollBook);
        }
    }

    @Override
    public void closeBook() {
        super.closeBook();
        if (mChapterDisp != null) {
            mChapterDisp.dispose();
            mChapterDisp = null;
        }
    }
}
