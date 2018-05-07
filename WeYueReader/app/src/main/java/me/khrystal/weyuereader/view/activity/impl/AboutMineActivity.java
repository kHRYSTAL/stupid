package me.khrystal.weyuereader.view.activity.impl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import me.khrystal.weyuereader.R;
import me.khrystal.weyuereader.view.base.BaseActivity;
import me.khrystal.weyuereader.viewmodel.BaseViewModel;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/5/7
 * update time:
 * email: 723526676@qq.com
 */

public class AboutMineActivity extends BaseActivity {
    @BindView(R.id.iv_avatar)
    ImageView mIvAvatar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBinddingView(R.layout.activity_about_mine, NO_BINDDING, new BaseViewModel(this));
        initThemeToolBar("关于作者");
        Glide.with(mContext).load(R.mipmap.avatar).apply(new RequestOptions().transform(new CircleCrop())).into(mIvAvatar);
    }
}
