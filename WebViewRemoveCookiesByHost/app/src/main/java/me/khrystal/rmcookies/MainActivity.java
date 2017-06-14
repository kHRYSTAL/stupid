package me.khrystal.rmcookies;

import android.app.Activity;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {

    WebView mWebView;

    String open = "http://w.mail.qq.com/cgi-bin/loginpage?f=xhtml&kvclick=loginpageapp_pushenterios&ad=false&f=xhtm";
    String finish = "https://w.mail.qq.com/cgi-bin/mobile";

    String domain1 = "http://mail.qq.com";
    String domain2 = "http://ptlogin2.qq.com";
    String domain3 = "http://qq.com";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWebView = (WebView) findViewById(R.id.webview);
        findViewById(R.id.loadQQ).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadUrl(0);
            }
        });

        findViewById(R.id.clearCookies).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearCookies();
            }
        });
        findViewById(R.id.loadBaidu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadUrl(1);
            }
        });
        build();
    }

    private void build() {
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setAppCacheEnabled(false);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setDefaultTextEncodingName("UTF-8");
        mWebView.setWebViewClient(new MyWebViewClient());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

    }

    class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            Log.e("TAG", url);
            return true;
        }
    }



    public void loadUrl(int type) {
        if (type == 0)
            mWebView.loadUrl(open);
        else
            mWebView.loadUrl("http://www.baidu.com");
    }

    public void clearCookies() {
        WebViewUtils.deleteCookiesForDomain(this, domain1);
        WebViewUtils.deleteCookiesForDomain(this, domain2);
        WebViewUtils.deleteCookiesForDomain(this, domain3);
    }
}
