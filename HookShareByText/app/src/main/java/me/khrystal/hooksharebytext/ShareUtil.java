package me.khrystal.hooksharebytext;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import java.io.File;
import java.util.List;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 2019-10-22
 * update time:
 * email: 723526676@qq.com
 */
public class ShareUtil {

    private static Object syncObj = new Object();
    private static ShareUtil shareUtil;
    private Context context;

    private ShareUtil() {
    }

    private void setContext(Context context) {
        this.context = context;
    }

    public static ShareUtil getInstance(Context context) {
        synchronized (syncObj) {
            if (shareUtil == null) {
                shareUtil = new ShareUtil();
            }
        }
        shareUtil.setContext(context);
        return shareUtil;
    }

    private int shareType;

    public void share(int type, String text, List<String> imgUrls) {
        this.shareType = type; //用于复制要分享的文字的类
        copy(text);
        if (shareType == Constants.WECHATSHARE) {
            share(0, text, "wchat", imgUrls);
        }
        if (shareType == Constants.FRIENDQSHARE) {
            share(1, text, "wchat", imgUrls);
        }
        if (shareType == Constants.QQSHARE) {
            qqshare(0, text, "qq", imgUrls);
        }
        if (shareType == Constants.QQZONE) {
            share(1, text, "qq_zone", imgUrls);
        }
        if (shareType == Constants.WEBOSHARE) {
            share(2, text, "weibo", imgUrls);
        }
    }

    private void copy(String copy) {
        ClipboardManager clipboard =
                (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText(null, copy);
        clipboard.setPrimaryClip(clipData);
    }

    private void share(int i, String text, String type, List<String> imgUrls) {
        ShareManager shareManager = new ShareManager(context);
        shareManager.setShareImage(i, imgUrls, text, type);
    }

    private void qqshare(int i, String s, String mType, List<String> imgUrls) {
        if (mType.equals("qq") && !Tools.isAppAvilible(context, "com.tencent.mobileqq")) {

            Toast.makeText(context, "您还没有安装QQ", Toast.LENGTH_SHORT).show();
            return;
        } else if (mType.equals("wchat") && !Tools.isAppAvilible(context, "com.tencent.mm")) {

            Toast.makeText(context, "您还没有安装微信", Toast.LENGTH_SHORT).show();
            return;
        }
        ComponentName comp = null;
        if (mType.contains("qq")) {
            comp = new ComponentName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity");
        } else {
            comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");
        }
        Intent intent = new Intent();
        intent.setComponent(comp);
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        Uri uri = Uri.fromFile(new File(imgUrls.get(0)));
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        context.startActivity(intent);
    }

}
