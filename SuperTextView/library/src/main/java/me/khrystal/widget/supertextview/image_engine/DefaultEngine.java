package me.khrystal.widget.supertextview.image_engine;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Looper;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import me.khrystal.widget.supertextview.ImageEngine;
import me.khrystal.widget.supertextview.SuperTextView;
import me.khrystal.widget.supertextview.utils.ThreadPool;

/**
 * usage:
 * 在调用{@link SuperTextView#setUrlImage(String)}后，
 * 如果没有通过{@link ImageEngine#install(Engine)}配置过图片下载引擎，将使用这个简易版的
 * 图片下载引擎。
 * 建议开发者根据项目情况自行实现{@link Engine}，然后通过{@link ImageEngine#install(Engine)}配置图片下载引擎。
 * author: kHRYSTAL
 * create time: 18/6/12
 * update time:
 * email: 723526676@qq.com
 */

public class DefaultEngine implements Engine {
    @Override
    public void load(final String url, final ImageEngine.Callback callback) {
        ThreadPool.run(new Runnable() {
            @Override
            public void run() {
                try {
                    byte[] bytes = getBytesArrayFromNet(url);
                    final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    final BitmapDrawable drawable = new BitmapDrawable(Resources.getSystem(), bitmap);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onCompleted(drawable);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static byte[] getBytesArrayFromNet(String path) throws Exception {
        // 实例化URL对象 并指定网址
        URL url = new URL(path);
        // 打开连接并返回HttpURLConnection对象
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        // 设置相关参数
        httpURLConnection.setConnectTimeout(5000);
        httpURLConnection.setRequestMethod("GET");
        // 将请求提交到服务器并得到服务器端的响应结果
        int responseCode = httpURLConnection.getResponseCode();
        if (responseCode == 200) {
            InputStream inputStream = null;
            try {
                inputStream = httpURLConnection.getInputStream();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = inputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, len);
                }
                return byteArrayOutputStream.toByteArray();
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                    inputStream = null;
                }
            }
        }
        return null;
    }
}
