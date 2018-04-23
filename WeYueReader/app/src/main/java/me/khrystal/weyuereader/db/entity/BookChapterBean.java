package me.khrystal.weyuereader.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Index;

import java.io.Serializable;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/4/23
 * update time:
 * email: 723526676@qq.com
 */
@Entity
public class BookChapterBean implements Serializable {
    private static final long serialVersionUID = 56423411313L;

    private String link;

    private String title;

    @Index
    private String taskName;

    @Index
    private String bookId;

    private boolean unreadble;

    @Generated(hash = 853839616)
    public BookChapterBean() {
    }


    @Generated(hash = 1921381654)
    public BookChapterBean(String link, String title, String taskName, String bookId,
            boolean unreadble) {
        this.link = link;
        this.title = title;
        this.taskName = taskName;
        this.bookId = bookId;
        this.unreadble = unreadble;
    }


    public String getLink() {
        return link;
    }

    public String getTitle() {
        return title;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getBookId() {
        return bookId;
    }

    public boolean isUnreadble() {
        return unreadble;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public void setUnreadble(boolean unreadble) {
        this.unreadble = unreadble;
    }


    public boolean getUnreadble() {
        return this.unreadble;
    }
}
