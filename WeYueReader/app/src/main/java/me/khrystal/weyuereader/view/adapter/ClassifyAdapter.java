package me.khrystal.weyuereader.view.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import me.khrystal.weyuereader.R;
import me.khrystal.weyuereader.model.BookClassifyBean;
import me.khrystal.weyuereader.utils.Constant;

public class ClassifyAdapter extends BaseQuickAdapter<BookClassifyBean.ClassifyBean, BaseViewHolder> {


    public ClassifyAdapter(@Nullable List<BookClassifyBean.ClassifyBean> data) {
        super(R.layout.item_classify, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BookClassifyBean.ClassifyBean item) {
        helper.setText(R.id.tv_name, item.getName())
                .setText(R.id.tv_count, item.getBookCount() + "本");

        Glide.with(mContext).load(Constant.BASE_URL+item.getIcon())
                .apply(new RequestOptions().placeholder(R.drawable.ic_default))
                .into((ImageView) helper.getView(R.id.iv_icon));
    }
}