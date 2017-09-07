package me.khrystal.circleindicator;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.khrystal.widget.viewpager.indicator.CircleIndicator;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private CircleIndicator mCircleIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mCircleIndicator = (CircleIndicator) findViewById(R.id.indicator);
        mViewPager.setAdapter(new Adapter());

        mCircleIndicator.setViewPager(mViewPager);

        mViewPager.setClipToPadding(false);
        mViewPager.setPadding(dip2px(20), 0, dip2px(20), 0);
        mViewPager.setPageMargin(dip2px(8));
    }

    private int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private class Adapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View item = inflater.inflate(R.layout.pager_item, null);
            TextView textView = item.findViewById(R.id.tvItem);
            textView.setText(String.valueOf(position));
            if (position == 0) {
                item.setBackgroundColor(getResources().getColor(R.color.color_ac));
            } else {
                item.setBackgroundColor(getResources().getColor(R.color.color_dc));
            }

            container.addView(item);
            return item;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
