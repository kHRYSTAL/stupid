package me.khrystal.weyuereader.view.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import me.khrystal.weyuereader.R;
import me.khrystal.weyuereader.db.entity.CollBookBean;
import me.khrystal.weyuereader.utils.Constant;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/4/26
 * update time:
 * email: 723526676@qq.com
 */

public class BookShelfAdapter extends BaseQuickAdapter<CollBookBean, BaseViewHolder> {

    public BookShelfAdapter(@Nullable List<CollBookBean> data) {
        super(R.layout.item_book_shelf, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CollBookBean item) {
        if (item.isLocal()) {

            helper.setImageResource(R.id.coll_book_iv_cover, R.drawable.ic_base_local_book)
//                    .setText(R.id.coll_book_tv_lately_update, StringUtils.
//                            dateConvert(item.getUpdated(), Constant.FORMAT_BOOK_DATE)+":")
                    .setVisible(R.id.coll_book_tv_lately_update, true);
        } else {
            Glide.with(mContext).load(Constant.ZHUISHU_IMAGE_URL + item.getCover())
                    .apply(new RequestOptions().placeholder(R.mipmap.ic_book_loading))
                    .into((ImageView) helper.getView(R.id.coll_book_iv_cover));
        }

        helper.setText(R.id.coll_book_tv_name, item.getTitle())
                .setText(R.id.coll_book_tv_chapter, item.getLastChapter());

        if (item.isUpdate()) {
            helper.setVisible(R.id.coll_book_iv_red_rot, true);
        } else {
            helper.setVisible(R.id.coll_book_iv_red_rot, false);

        }
    }
}
