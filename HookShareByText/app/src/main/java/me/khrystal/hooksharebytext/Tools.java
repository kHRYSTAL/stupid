package me.khrystal.hooksharebytext;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class Tools {

    public static String IMAGE_NAME = "iv_share_";
    public static int i = 0;

    //根据网络图片url路径保存到本地
    public static final File saveImageToSdCard(Context context, String image) {
        boolean success = false;
        File file = null;
        try {
            file = createStableImageFile(context);

            Bitmap bitmap = null;
            URL url = new URL(image);
            HttpURLConnection conn = null;
            conn = (HttpURLConnection) url.openConnection();
            InputStream is = null;
            is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);

            FileOutputStream outStream;

            outStream = new FileOutputStream(file);
            Log.e("Tools", outStream.toString());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (success) {

            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.fromFile(file)));
            Log.e("Tools", "已保存到相册, file:" + file.getAbsolutePath());
            return file;
        } else {
            return null;
        }
    }

    //创建本地保存路径
    public static File createStableImageFile(Context context) throws IOException {
        String path = Environment.getExternalStorageDirectory() + "/lwd/lwd";
        File fileFolder = new File(path);
        if (!fileFolder.exists()) {
            fileFolder.mkdirs();
        }
        String fileName = "IMG_" + System.currentTimeMillis() + ".jpg";
        File file = new File(fileFolder, fileName);
        return file;
    }

    public String saveImageToGallery(Context context, Bitmap bmp) {
        String path = Environment.getExternalStorageDirectory() + "/lwd/lwd";
        File fileFolder = new File(path);
        if (!fileFolder.exists()) {
            fileFolder.mkdirs();
        }
        String fileName = "IMG_" + System.currentTimeMillis() + ".jpg";
        File file = new File(fileFolder, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    //判断是否安装了微信,QQ,QQ空间
    public static boolean isAppAvilible(Context context, String mType) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals(mType)) {
                    return true;
                }
            }
        }
        return false;
    }
}