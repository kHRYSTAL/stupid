package me.khrystal.hooksharebytext;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.core.content.FileProvider;

public class ShareManager {

    private Context mContext;
    private List<File> files = new ArrayList<>();
    ArrayList<Uri> imageUris = new ArrayList<Uri>();

    public ShareManager(Context mContext) {
        this.mContext = mContext;
    }

    public void setShareImage(final int flag, final List<String> stringList, final String description, final String mType) {

        if (mType.equals("qq") && !Tools.isAppAvilible(mContext, "com.tencent.mobileqq")) {

            Toast.makeText(mContext, "您还没有安装QQ", Toast.LENGTH_SHORT).show();
            return;
        } else if (mType.equals("wchat") && !Tools.isAppAvilible(mContext, "com.tencent.mm")) {

            Toast.makeText(mContext, "您还没有安装微信", Toast.LENGTH_SHORT).show();
            return;
        } else if (mType.equals("qq_zone") && !Tools.isAppAvilible(mContext, "com.qzone")) {
            Toast.makeText(mContext, "您还没有安装QQ空间", Toast.LENGTH_SHORT).show();
            return;
        } else if (mType.equals("weibo") && !Tools.isAppAvilible(mContext, "com.sina.weibo")) {
            Toast.makeText(mContext, "您还没有安装新浪微博", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < stringList.size(); i++) {
                    File file = Tools.saveImageToSdCard(mContext, stringList.get(i));
                    Log.e("ShareManager", file.getName());
                    files.add(file);
                }
                Intent intent = new Intent();
                ComponentName comp = null;
                if (mType.contains("qq")) {
                    if (flag == 0) {
                        comp = new ComponentName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity");
                    } else {
                        comp = new ComponentName("com.qzone", "com.qzonex.module.operation.ui.QZonePublishMoodActivity");
                    }
                } else if (mType.contains("weibo")) {
                    if (flag == 2) {
                        comp = new ComponentName("com.sina.weibo", "com.sina.weibo.composerinde.ComposerDispatchActivity");
                    }
                } else {
                    if (flag == 0) {
                        comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");
                    } else {
                        comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");
                    }
                }
                intent.setComponent(comp);
                intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                intent.setType("image/*");
                for (File f : files) {
                    imageUris.add(getImageContentUri(mContext, f));
                }
                if (mType.equals("wchat") && flag == 1)
                    intent.setAction(Intent.ACTION_SEND);
                else
                    intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
                intent.putExtra("Kdescription" + System.currentTimeMillis() + 1000, description); // 携带的文字, 大概率不显示
                mContext.startActivity(intent);
            }
        }).start();
    }

    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        Uri uri = null;

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
                Uri baseUri = Uri.parse("content://media/external/images/media");
                uri = Uri.withAppendedPath(baseUri, "" + id);
            }

            cursor.close();
        }

        if (uri == null) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DATA, filePath);
            uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        }

        return uri;
    }

}