package me.khrystal.wallpaper;

import android.app.Activity;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.util.DisplayMetrics;

import java.util.List;
import java.util.Map;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/6/13
 * update time:
 * email: 723526676@qq.com
 */

public class CameraUtil {

    private static final String TAG = CameraUtil.class.getSimpleName();

    public static void setFullScreenSize(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        //获取屏幕信息
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        setKeyValue("width", screenWidth);
        setKeyValue("height", screenHeight);

        int statusBarHeight = -1;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        setKeyValue("status", statusBarHeight);
    }

    public static Camera.Size getFullScreenSize(List<Camera.Size> preSizeList) {
        int width = getByKey("width", 1080);
        int height = getByKey("height", 1920);
        return getCloselyPreSize(true, width, height, preSizeList);
    }

    /**
     * set key and valueint
     */
    public static  <T> void setKeyValue(String key, T i) {
        SharedPreferences.Editor editor = Application.APP_PREFERENCE.edit();
        if (i instanceof Integer) {
            editor.putInt(key, (Integer) i);
        } else if (i instanceof String) {
            editor.putString(key, (String) i);
        } else if (i instanceof Long) {
            editor.putLong(key, (Long) i);
        } else if (i instanceof Boolean) {
            editor.putBoolean(key, (Boolean) i);
        }
        editor.commit();
    }

    /**
     * 获取value
     *
     * @param key
     * @param defaultValue 当不存在时返回默认值
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getByKey(String key, T defaultValue) {
        Map<String, ?> maps = Application.APP_PREFERENCE.getAll();
        if (!maps.containsKey(key))
            return defaultValue;

        Object value = maps.get(key);
        return (T) value;
    }

    /**
     * 获取近似的摄像头预览尺寸
     * @return
     */
    public static Camera.Size getCloselyPreSize(boolean isPortrait, int surfaceWidth, int surfaceHeight,
                                            List<Camera.Size> preSizeList) {
        int ReqTmpWidth;
        int ReqTmpHeight;
        // 当屏幕为垂直的时候需要把宽高值进行调换，保证宽大于高
        if (isPortrait) {
            ReqTmpWidth = surfaceHeight;
            ReqTmpHeight = surfaceWidth;
        } else {
            ReqTmpWidth = surfaceWidth;
            ReqTmpHeight = surfaceHeight;
        }
        //先查找preview中是否存在与surfaceview相同宽高的尺寸
        for(Camera.Size size : preSizeList){
            if((size.width == ReqTmpWidth) && (size.height == ReqTmpHeight)){
                return size;
            }
        }

        // 得到与传入的宽高比最接近的size
        float reqRatio = ((float) ReqTmpWidth) / ReqTmpHeight;
        float curRatio, deltaRatio;
        float deltaRatioMin = Float.MAX_VALUE;
        Camera.Size retSize = null;
        for (Camera.Size size : preSizeList) {
            curRatio = ((float) size.width) / size.height;
            deltaRatio = Math.abs(reqRatio - curRatio);
            if (deltaRatio < deltaRatioMin) {
                deltaRatioMin = deltaRatio;
                retSize = size;
            }
        }
        return retSize;
    }
}
