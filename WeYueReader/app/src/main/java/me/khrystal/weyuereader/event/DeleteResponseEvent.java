package me.khrystal.weyuereader.event;

import me.khrystal.weyuereader.db.entity.CollBookBean;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/4/23
 * update time:
 * email: 723526676@qq.com
 */

public class DeleteResponseEvent {
    public boolean isDelete;
    public CollBookBean collBook;
    public DeleteResponseEvent(boolean isDelete, CollBookBean collBook) {
        this.isDelete = isDelete;
        this.collBook = collBook;
    }
}
