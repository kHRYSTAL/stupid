package me.khrystal.hooksharebytext;

/**
 * 动态权限全部状态回调 listener
 */
public interface RunTimePermissionListener extends RunTimePermissionGrantedListener {

    // 取消授权回调
    void onCancel();

}
