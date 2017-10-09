package me.khrystal.h5videodemo;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {

    WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webview = (WebView) findViewById(R.id.webView);

        webview.loadUrl("http://mp.weixin.qq.com/s/K4fJUzKT_WHSIIP6E0nngg");

        webview.setHorizontalScrollBarEnabled(false);
        webview.setVerticalScrollBarEnabled(true);
        webview.setWebViewClient(new WebViewClient());

        WebSettings setting = webview.getSettings();
        // 编码
        setting.setDefaultTextEncodingName("UTF-8");
        // 屏幕
        setting.setSupportZoom(true);
        setting.setBuiltInZoomControls(false);
        setting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        setting.setLoadWithOverviewMode(true);
        // 存储
        setting.setDomStorageEnabled(true);
        setting.setDatabaseEnabled(true);
        // 默认不开启缓存
        setting.setAppCacheEnabled(false);
        // 默认缓存模式为不取缓存
        setting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        setting.setAppCacheMaxSize(1024 * 1024 * 5 * 1L);
        // js
        setting.setJavaScriptEnabled(true);
    }
}
