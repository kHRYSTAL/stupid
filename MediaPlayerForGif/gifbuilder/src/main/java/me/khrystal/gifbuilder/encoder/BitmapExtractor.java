package me.khrystal.gifbuilder.encoder;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 2020/11/30
 * update time:
 * email: 723526676@qq.com
 */

public class BitmapExtractor {
    private static final int US_OF_S = 1000 * 1000;
    private List<Bitmap> bitmaps = new ArrayList<>();
    private int width;
    private int height;
    private int begin; // 开始时间
    private int end; // 结束时间
    private int fps = 5;

    public List<Bitmap> createBitmaps(String path) {
        MediaMetadataRetriever m = new MediaMetadataRetriever();
        m.setDataSource(path);
        double inc = US_OF_S / fps; // 间隔帧数
        String dir = Environment.getExternalStorageDirectory() + File.separator + "JzvdTemp" + File.separator;
        for (double i = begin * US_OF_S; i < end * US_OF_S; i += inc) {
            Bitmap frame = m.getFrameAtTime((long) i);
            File dirFile = new File(dir);
            File file = new File(dir + i + ".jpg");
            boolean success = saveBitmap(frame, dirFile, file);
            if (success) {
                // TODO: 2020/11/30 保存帧成功
            }
            if (frame != null) {
                bitmaps.add(scale(frame));
            }
        }
        return bitmaps;
    }

    private boolean saveBitmap(Bitmap bitmap, File dir, File path) {
        boolean success = false;
        byte[] bytes = bitmapToBytes(bitmap, 70);
        OutputStream out = null;
        try {
            if (!dir.exists() && dir.isDirectory()) {
                dir.mkdirs();
            }
            out = new FileOutputStream(path);
            out.write(bytes);
            out.flush();
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return success;
    }

    private byte[] bitmapToBytes(Bitmap bitmap, int quality) {
        if (bitmap == null) {
            return null;
        }
        int size = bitmap.getWidth() * bitmap.getHeight() * 4;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(size);
        try {
            bitmap.compress(Bitmap.CompressFormat.PNG, quality, outputStream);
            outputStream.flush();
            outputStream.close();
            return outputStream.toByteArray();
        } catch (IOException e) {
            return null;
        }
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void setScope(int begin, int end) {
        this.begin = begin;
        this.end = end;
    }

    public void setFps(int fps) {
        this.fps = fps;
    }

    private Bitmap scale(Bitmap bitmap) {
        return Bitmap.createScaledBitmap(bitmap, width > 0 ? width : bitmap.getWidth(), height > 0 ? height : bitmap.getHeight(), true);
    }
}
