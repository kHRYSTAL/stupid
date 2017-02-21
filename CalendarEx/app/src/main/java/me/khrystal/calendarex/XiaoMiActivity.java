package me.khrystal.calendarex;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.khrystal.widget.calendar.CalendarAdapter;
import me.khrystal.widget.calendar.CalendarBean;
import me.khrystal.widget.calendar.CalendarDateView;
import me.khrystal.widget.calendar.CalendarUtil;
import me.khrystal.widget.calendar.CalendarView;

public class XiaoMiActivity extends AppCompatActivity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.calendarDateView)
    CalendarDateView calendarDateView;
    @BindView(R.id.list)
    ListView list;
    @BindView(R.id.activity_main)
    LinearLayout activityMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xiao_mi);
        ButterKnife.bind(this);
        initView();
        initList();
    }

    private void initView() {
        calendarDateView.setAdapter(new CalendarAdapter() {
            @Override
            public View getView(View convertView, ViewGroup parentView, CalendarBean bean) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(parentView.getContext())
                            .inflate(R.layout.item_xiaomi, null);
                }
                TextView chinaText = (TextView) convertView.findViewById(R.id.chinaText);
                TextView text = (TextView) convertView.findViewById(R.id.text);

                text.setText("" + bean.day);
                if (bean.monthFlag != 0) {
                    text.setTextColor(0xff9299a1);
                } else {
                    text.setTextColor(0xff444444);
                }
                chinaText.setText(bean.chinaDay);

                return convertView;
            }
        });

        calendarDateView.setOnItemClickListener(new CalendarView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, CalendarBean bean) {
                title.setText(bean.year + "/" + bean.month + "/" + bean.day);
            }
        });

        int[] data = CalendarUtil.getYMD(new Date());
        title.setText(data[0] + "/" + data[1] + "/" + data[2]);
    }

    private void initList() {
        list.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 100;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(XiaoMiActivity.this)
                            .inflate(android.R.layout.simple_expandable_list_item_1, null);
                }
                TextView textView = (TextView) convertView;
                textView.setText("position: " + position);
                return convertView;
            }
        });

    }

    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }
}
