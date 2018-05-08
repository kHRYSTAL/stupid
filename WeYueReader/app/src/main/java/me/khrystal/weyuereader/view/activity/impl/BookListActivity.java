package me.khrystal.weyuereader.view.activity.impl;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.gigamole.navigationtabstrip.NavigationTabStrip;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.khrystal.weyuereader.R;
import me.khrystal.weyuereader.view.base.BaseActivity;
import me.khrystal.weyuereader.view.base.BaseViewPagerAdapter;
import me.khrystal.weyuereader.view.fragment.impl.BooksInfoFragment;
import me.khrystal.weyuereader.viewmodel.BaseViewModel;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/5/7
 * update time:
 * email: 723526676@qq.com
 */

public class BookListActivity extends BaseActivity {

    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    @BindView(R.id.tabStrip)
    NavigationTabStrip mTabStrip;
    private String mTitleName;
    private String mGetder;

    String[] titles = {"热门", "新书", "好评"/*, "完结"*/};
    String[] types = {"hot", "new", "reputation", "over"};
    private List<Fragment> mFragments;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setBinddingView(R.layout.activity_book_list, NO_BINDDING, new BaseViewModel(mContext));
    }

    @Override
    protected void initView() {
        super.initView();
        mTitleName = getIntent().getStringExtra("titleName");
        mGetder = getIntent().getStringExtra("getder");
        initThemeToolBar(mTitleName);

        mFragments = new ArrayList<>();

        for (String type : types) {
            mFragments.add(BooksInfoFragment.newInstance(mTitleName, mGetder, type));
        }

        mViewpager.setAdapter(new BaseViewPagerAdapter(getSupportFragmentManager(), titles, mFragments));
        mViewpager.setOffscreenPageLimit(4);
        mTabStrip.setTitles(titles);
        mTabStrip.setViewPager(mViewpager);
    }
}
