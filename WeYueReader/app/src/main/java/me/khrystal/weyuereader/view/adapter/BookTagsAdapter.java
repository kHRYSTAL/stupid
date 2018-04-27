package me.khrystal.weyuereader.view.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import me.khrystal.weyuereader.R;
import me.khrystal.weyuereader.model.BookBean;
import me.khrystal.weyuereader.utils.Constant;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/4/26
 * update time:
 * email: 723526676@qq.com
 */

public class BookTagsAdapter extends BaseQuickAdapter<BookBean, BaseViewHolder> {

    public BookTagsAdapter(@Nullable List<BookBean> data) {
        super(R.layout.item_book_tag, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BookBean item) {
        Glide.with(mContext).load(Constant.ZHUISHU_IMAGE_URL + item.getCover())
                .apply(new RequestOptions().placeholder(R.mipmap.ic_book_loading))
                .into((ImageView) helper.getView(R.id.iv_image));
        helper.setText(R.id.tv_book_title, item.getTitle())
                .setText(R.id.tv_book_brief, item.getLongIntro());

        String tags = "";
        for (String tag : item.getTags()) {
            tags += tag + " | ";
        }
        if (item.getTags().size() > 0) {
            tags=tags.substring(0, tags.length()- 2);
        }

        helper.setText(R.id.tv_book_tags, tags);
    }
}
