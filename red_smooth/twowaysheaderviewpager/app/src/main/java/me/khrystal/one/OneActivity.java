package me.khrystal.one;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import me.khrystal.twhv.R;
import me.khrystal.twhv.widget.PagerSlidingTabStrip;

/**
 * fuck
 */
public class OneActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private ScrollView mFather;
    private PagerSlidingTabStrip mTabs;
    private ViewPager mViewPager;

    private OFragmentAdapter mAdapter;
    OneFragment[] fragments = new OneFragment[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one);
        mFather = (ScrollView)findViewById(R.id.outer);
        mTabs = (PagerSlidingTabStrip)findViewById(R.id.pager_tabs);
        mViewPager = (ViewPager)findViewById(R.id.view_pager);

        final View view = mFather;
        view.post(new Runnable() {
            @Override
            public void run() {
                ViewGroup.LayoutParams containerparams = mViewPager.getLayoutParams();
                containerparams.height = mFather.getHeight() - mTabs.getHeight();
                mViewPager.setLayoutParams(containerparams);
            }
        });

        mAdapter = new OFragmentAdapter();
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(this);
        mTabs.setViewPager(mViewPager);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mTabs.onPageScrolled(position,positionOffset,positionOffsetPixels);
    }

    @Override
    public void onPageSelected(int position) {
        mTabs.onPageSelected(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        mTabs.onPageScrollStateChanged(state);
    }


    private class OFragmentAdapter extends FragmentPagerAdapter{


        public OFragmentAdapter() {
            super(getSupportFragmentManager());
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "tab1";
                case 1:
                    return "tab2";
                case 2:
                    return "tab3";
                default:
                    return null;
            }
        }

        @Override
        public Fragment getItem(int position) {
            if (fragments[position]==null){
                fragments[position] = OneFragment.newInstance();
                fragments[position].outer = mFather;
            }
            return fragments[position];
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
