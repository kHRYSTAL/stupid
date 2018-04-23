package me.khrystal.weyuereader.api;

import com.allen.library.bean.BaseData;

import java.util.List;

import io.reactivex.Observable;
import me.khrystal.weyuereader.db.entity.BookChapterBean;
import me.khrystal.weyuereader.model.BookBean;
import me.khrystal.weyuereader.model.BookClassifyBean;
import me.khrystal.weyuereader.model.ChapterContentBean;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * usage: 书籍模块接口
 * author: kHRYSTAL
 * create time: 18/4/23
 * update time:
 * email: 723526676@qq.com
 */

public interface BookService {

    /**
     * 获取所有分类
     */
    @GET(ModelPath.API + "/classify")
    Observable<BaseData<BookClassifyBean>> bookClassify();

    /**
     * 获取分类下的书籍
     */
    @GET(ModelPath.BOOKS)
    Observable<BaseData<List<BookBean>>> books(@Query("type") String type,
                                               @Query("major") String major,
                                               @Query("page") int page);

    /**
     * 获取书籍目录
     */
    @GET(ModelPath.BOOKS + "/{bookId}/chapters")
    Observable<BaseData<BookChapterBean>> bookChapters(@Path("bookId") String bookId);

    /**
     * 根据link获取正文
     */
    @GET("http://chapterup.zhuishushenqi.com/chapter/{link}")
    Observable<ChapterContentBean> bookContent(@Path("link") String link);


    /**
     * 根据tag获取书籍
     */
    @GET(ModelPath.BOOKS + "/tag")
    Observable<BaseData<List<BookBean>>> booksByTag(@Query("bookTag") String bookTag, @Query("page") int page);

    /**
     * 搜索书籍
     */
    @GET(ModelPath.API + "/search")
    Observable<BaseData<List<BookBean>>> booksSearch(@Query("keyword") String keyword);


}
