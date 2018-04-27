package me.khrystal.weyuereader.view.adapter;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import me.khrystal.weyuereader.R;
import me.khrystal.weyuereader.widget.page.TxtChapter;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/4/27
 * update time:
 * email: 723526676@qq.com
 */

public class ReadCategoryAdapter extends BaseQuickAdapter<TxtChapter, BaseViewHolder> {

    public ReadCategoryAdapter(@Nullable List<TxtChapter> data) {
        super(R.layout.item_category, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TxtChapter item) {
        // 首先判断是否该章节已下载
        Drawable drawable = null;
        if (item.getLink() == null) {
            drawable = ContextCompat.getDrawable(mContext, R.drawable.selector_category_load);
        } else {
            if (item.getBookId() != null) {
                drawable = ContextCompat.getDrawable(mContext, R.drawable.selector_category_load);
            } else {
                drawable = ContextCompat.getDrawable(mContext, R.drawable.selector_category_unload);
            }
        }

        TextView category_tv_chapter = helper.getView(R.id.category_tv_chapter);
        category_tv_chapter.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        category_tv_chapter.setSelected(item.isSelect());
        category_tv_chapter.setText(item.getTitle());
        if (item.isSelect()) {
            category_tv_chapter.setTextColor(ContextCompat.getColor(mContext, R.color.color_ec4a48));
        } else {
            category_tv_chapter.setTextColor(ContextCompat.getColor(mContext, R.color.black));
        }
    }
}
