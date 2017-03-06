package me.khrystal.uninstallserver;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.FileObserver;

/**
 * usage: 文件删除卸载监听
 * author: kHRYSTAL
 * create time: 17/3/6
 * update time:
 * email: 723526676@qq.com
 */

public class SDCardListener extends FileObserver {

    private String path;
    private final Context mContext;
    private String url = "http://www.baidu.com";

    public SDCardListener(String parentPath, Context context) {
        super(parentPath);
        this.path = parentPath;
        this.mContext = context;
    }


    @Override
    public void onEvent(int event, String path) {
        int action = event & FileObserver.ALL_EVENTS; // 与ALL_EVENTS 相与 获取真实的事件
        switch (action) {
            case FileObserver.DELETE: // 卸载事件
                openBrowser(); // 打开浏览器
                break;
        }
    }

    /**
     * 应用卸载后打开网页
     */
    protected void openBrowser() {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }


}
