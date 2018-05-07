package me.khrystal.weyuereader.view.fragment.impl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gigamole.navigationtabstrip.NavigationTabStrip;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.khrystal.weyuereader.R;
import me.khrystal.weyuereader.view.activity.impl.MainActivity;
import me.khrystal.weyuereader.view.base.BaseFragment;
import me.khrystal.weyuereader.view.base.BaseViewPagerAdapter;
import me.khrystal.weyuereader.viewmodel.BaseViewModel;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/5/7
 * update time:
 * email: 723526676@qq.com
 */

public class BookClassifyFragment extends BaseFragment {

    @BindView(R.id.nts_classify)
    NavigationTabStrip ntsClassify;
    @BindView(R.id.vp_classify)
    ViewPager vpClassify;

    String[] titles = {"男生", "女生", "出版"};
    private List<Fragment> mFragments = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = setContentView(container, R.layout.fragment_book_classify, new BaseViewModel(mContext));
        ButterKnife.bind(this, view);
        return view;
    }

    public static BookClassifyFragment newInstance() {
        BookClassifyFragment fragment = new BookClassifyFragment();
        return fragment;
    }

    @Override
    public void initView() {
        super.initView();
        for (int i = 0; i < titles.length; i++) {
            mFragments.add(ClassifyFragment.newInstance(titles[i]));
        }
        vpClassify.setAdapter(new BaseViewPagerAdapter(getActivity().getSupportFragmentManager(), titles, mFragments));
        vpClassify.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ((MainActivity) getActivity()).setLeftSlide(position == 0);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        vpClassify.setOffscreenPageLimit(4);
        ntsClassify.setTitles(titles);
        ntsClassify.setViewPager(vpClassify);
    }
}
