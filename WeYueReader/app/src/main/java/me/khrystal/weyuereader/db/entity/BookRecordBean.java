package me.khrystal.weyuereader.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/4/23
 * update time:
 * email: 723526676@qq.com
 */
@Entity
public class BookRecordBean {

    @Id
    private String bookId;

    // 阅读到了第几章
    private int chapter;

    // 当前的页码
    private int pagePos;
    @Generated(hash = 340380968)
    public BookRecordBean(String bookId, int chapter, int pagePos) {
        this.bookId = bookId;
        this.chapter = chapter;
        this.pagePos = pagePos;
    }
    @Generated(hash = 398068002)
    public BookRecordBean() {
    }
    public String getBookId() {
        return bookId;
    }

    public int getChapter() {
        return chapter;
    }

    public int getPagePos() {
        return pagePos;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public void setChapter(int chapter) {
        this.chapter = chapter;
    }

    public void setPagePos(int pagePos) {
        this.pagePos = pagePos;
    }
}
