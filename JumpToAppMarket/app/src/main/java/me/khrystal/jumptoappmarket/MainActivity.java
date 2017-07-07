package me.khrystal.jumptoappmarket;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<String> appList;

    private static final String PACKAGE_NAME = "com.zhisland.android.blog";

    //酷市场 -- 酷安网
    public static final String PACKAGE_COOL_MARKET = "com.coolapk.market";
    //小米应用商店
    public static final String PACKAGE_MI_MARKET = "com.xiaomi.market";
    //应用宝
    public static final String PACKAGE_TENCENT_MARKET = "com.tencent.android.qqdownloader";
    //360手机助手
    public static final String PACKAGE_360_MARKET = "com.qihoo.appstore";
    //豌豆荚
    public static final String PACKAGE_WANDOUJIA_MARKET = "com.wandoujia.phoenix2";
    //魅族应用商店
    public static final String PACKAGE_MEIZU_MARKET = "com.meizu.mstore";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appList = new ArrayList<>();
        initAppList();

    }

    private void initAppList() {
        appList.clear();
        PackageManager packageManager = getPackageManager();
        List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(0);
        for (PackageInfo packageInfo : packageInfoList) {
            appList.add(packageInfo.packageName);
        }
    }

    public void goToXiaomiMarket(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=" + PACKAGE_NAME));
        //跳转小米应用商店
        intent.setClassName(PACKAGE_MI_MARKET, "com.xiaomi.market.ui.AppDetailActivity");
        startActivity(intent);
    }
}
