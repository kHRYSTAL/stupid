package me.khrystal.weyuereader;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;

import com.allen.library.RxHttpUtils;
import com.ihsanbal.logging.Level;
import com.ihsanbal.logging.LoggingInterceptor;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.concurrent.TimeUnit;

import me.khrystal.weyuereader.utils.Constant;
import me.khrystal.weyuereader.utils.ThemeUtils;
import okhttp3.OkHttpClient;
import okhttp3.internal.platform.Platform;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * usage: TODO
 * author: kHRYSTAL
 * create time: 18/4/23
 * update time:
 * email: 723526676@qq.com
 */

public class WYApplication extends Application {
    private static WYApplication app;

    public static Context getAppContext() {
        return app;
    }

    public static Resources getAppResources() {
        return app.getResources();
    }

    public static PackageInfo packageInfo;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        app = this;
        try {
            packageInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        try {
            packageInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        // 启动下载服务

        // 初始化RxHttpUtil
        initRxHttpUtils();
        // 初始化刷新
        initRefresh();
        // 初始化loading布局
//        initLoadingLayout();
    }

    private void initRefresh() {
//        SmartRefreshLayout.setDefaultRefreshHeaderCreater((context, layout) -> {
//            CircleHeader header = new CircleHeader(context);
//            layout.setPrimaryColorsId(ThemeUtils.getThemeColorId(), R.color.white);
//            return header;
//        });
//        SmartRefreshLayout.setDefaultRefreshFooterCreater((context, layout) -> new BallPulseFooter(context).setSpinnerStyle(SpinnerStyle.Translate));

    }

    private void initRxHttpUtils() {
        RxHttpUtils.init(this);
        OkHttpClient.Builder client = new OkHttpClient().newBuilder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(new LoggingInterceptor.Builder()
                        .setLevel(Level.BASIC)
                        .log(Platform.INFO)
                        .request("Request")
                        .response("Response")
                        .build());

        RxHttpUtils
                .getInstance()
                //开启全局配置
                .config()
                //全局的BaseUrl
                .setBaseUrl(Constant.BASE_URL)
                //全局的请求头信息
//                .setHeaders(map)
                //全局持久话cookie,保存本地每次都会携带在header中
                .setCookie(false)
                //全局ssl证书认证
                //信任所有证书,不安全有风险
//                .setSslSocketFactory()
                //使用预埋证书，校验服务端证书（自签名证书）
                //.setSslSocketFactory(getAssets().open("your.cer"))
                //使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
                //.setSslSocketFactory(getAssets().open("your.bks"), "123456", getAssets().open("your.cer"))
                //全局超时配置
                //全局是否打开请求log日志
                .setOkClient(client.build())
                .setLog(true);
    }
}
