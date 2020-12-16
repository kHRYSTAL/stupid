package me.khrystal.dianpinghomemenu;

import androidx.appcompat.app.AppCompatActivity;
import me.khrystal.dianpinghomemenu.bean.Menu;
import me.khrystal.dianpinghomemenu.view.InsideHeaderLayout;
import me.khrystal.dianpinghomemenu.view.OutSideDownFrameLayout;

import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    InsideHeaderLayout insideHeaderLayout;
    OutSideDownFrameLayout outSideDownFrameLayout;
    private List<Menu> menus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initView();
    }

    private void initView() {
        insideHeaderLayout = findViewById(R.id.insideHeader);
        outSideDownFrameLayout = findViewById(R.id.outSideLayout);
        outSideDownFrameLayout.setInsideLayout(insideHeaderLayout);
        insideHeaderLayout.fillPagerData(menus);
    }

    private void initData() {
        menus = new ArrayList<>();
        Menu menu1 = new Menu();
        List<String> items1 = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            items1.add("" + i);
        }
        menu1.setItems(items1);

        Menu menu2 = new Menu();
        List<String> items2 = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            items2.add("" + i);
        }
        menu2.setItems(items2);
        menus.add(menu1);
        menus.add(menu2);
    }

    public void onContentClick(View view) {
        // 目的是拦截viewpager滑动事件
    }
}