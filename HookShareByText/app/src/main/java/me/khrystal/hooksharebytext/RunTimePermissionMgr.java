package me.khrystal.hooksharebytext;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;

import java.util.ArrayList;
import java.util.List;

import androidx.core.app.ActivityCompat;

/**
 * Android 动态权限
 */
public class RunTimePermissionMgr {

    // 存储权限
    public static final String[] STORAGE = Permission.Group.STORAGE;
    // 读通讯录权限
    public static final String READ_CONTACTS = Permission.READ_CONTACTS;
    // 写通讯录权限
    public static final String[] WRITE_CONTACTS = Permission.Group.CONTACTS;
    // 拍照权限： CAMERA和STORAGE
    public static final String[] CAMERA = new String[]{Permission.CAMERA,
            Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE};
    // 录音权限： RECORD_AUDIO和STORAGE
    public static final String[] RECORD_AUDIO = new String[]{Manifest.permission.RECORD_AUDIO,
            Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE};

    private static final String TAG = RunTimePermissionMgr.class.getSimpleName();

    // 提醒对话框
    private AlertDialog dialog;

    public static RunTimePermissionMgr getInstance() {
        return DynamicPermissionHolder.INSTANCE;
    }

    /**
     * 判断系统是否有某些权限， 并返回没有权限的列表
     *
     * @param permission 需要判断的权限
     * @return 没有的权限
     */
    public String[] hasPermission(Context context, final String... permission) {
        if (context == null || permission == null || permission.length <= 0) {
            return null;
        }
        List<String> noPermission = new ArrayList<>();
        for (String pm : permission) {
            if (!checkPermission(context, pm)) {
                noPermission.add(pm);
            }
        }
        String[] result = new String[noPermission.size()];
        return noPermission.toArray(result);
    }

    /**
     * 判断系统是否有某个权限
     */
    public boolean checkPermission(Context context, String permission) {
        if (context == null || TextUtils.isEmpty(permission)) {
            return false;
        }
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(context, permission);
    }

    /**
     * 请求权限(默认为非必须)
     *
     * @param permission 需求请求的权限
     * @see Manifest
     */
    public void requestPermission(Context context, RunTimePermissionGrantedListener listener, String... permission) {
        this.requestPermission(context, listener, false, permission);
    }

    /**
     * 请求权限
     *
     * @param permission 需求请求的权限
     * @param force      是否是必须有的权限，如果没有则不能使用App
     * @see Manifest
     */
    public void requestPermission(final Context context, final RunTimePermissionGrantedListener listener, final boolean force, final String... permission) {
        if (context == null || permission == null || permission.length <= 0) {
            if (listener != null) {
                // 如果权限为空，直接返回已授权
                listener.onGranted();
            }
            return;
        }
        AndPermission
                .with(context)
                .permission(permission)
                .onGranted(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        // 用户在系统弹出授权对话框中，点击“允许”授权
                        if (listener != null) {
                            listener.onGranted();
                        }
                    }
                })
                .rationale(new Rationale() {
                    @Override
                    public void showRationale(Context context, List<String> permissions, final RequestExecutor executor) {
                        // 用户点击拒绝授权后，再次请求授权
                        showRationaleDialog(context, permissions, executor, listener, force);
                    }
                })
                .onDenied(new Action() {  // 用户拒绝权限，包括不再显示权限弹窗也在此列
                    @Override
                    public void onAction(List<String> permissions) {
                        if (AndPermission.hasAlwaysDeniedPermission(context, permissions)) {
                            // 用户点击“不再询问”拒绝授权后，再次请求授权
                            showSettingDialog(context, permissions, listener, force);
                        } else {
                            // 用户在系统弹出授权对话框中，点击“拒绝”授权
                            cancelCallBack(listener);
                            // 必须授权，弹关闭App对话框
                            if (force) {
                                showCloseAppWarningDialog(context, permissions, listener);
                            }
                        }
                    }
                })
                .start();
    }

    /**
     * 请求App必须权限 (现在存储权限是必须)
     */
    public void requestAppForcePermission(Context context) {
        requestPermission(context, null, true, RunTimePermissionMgr.STORAGE);
    }

    /**
     * 显示重新授权对话框
     */
    private void showRationaleDialog(final Context context, final List<String> permissions, final RequestExecutor executor, final RunTimePermissionGrantedListener listener, final boolean force) {
        List<String> permissionNames = Permission.transformText(context, permissions);
        String message = String.format("此功能需要使用如下手机权限:\n\n%s",
                TextUtils.join("\n", permissionNames));
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            dialog = new AlertDialog.Builder(context).setCancelable(false)
                    .setMessage(message)
                    .setCancelable(!force)
                    .setPositiveButton("授权", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            executor.execute();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            executor.cancel();
                        }
                    })
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示权限设置对话框
     */
    private void showSettingDialog(final Context context, final List<String> permissions, final RunTimePermissionGrantedListener listener, final boolean force) {
        List<String> permissionNames = Permission.transformText(context, permissions);
        String message = String.format("使用此功能需在手机设置中授予以下权限:\n\n%s",
                TextUtils.join("\n", permissionNames));

        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            dialog = new AlertDialog.Builder(context).setCancelable(false)
                    .setMessage(message)
                    .setCancelable(!force)
                    .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 跳转至应用权限设置页
                            toSystemSetting(context);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cancelCallBack(listener);
                            // 必须授权，弹关闭App对话框
                            if (force) {
                                showCloseAppWarningDialog(context, permissions, listener);
                            }
                        }
                    })
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示关闭App提醒对话框
     */
    private void showCloseAppWarningDialog(final Context context, final List<String> permissions, final RunTimePermissionGrantedListener listener) {
        List<String> permissionNames = Permission.transformText(context, permissions);
        String message = String.format("请在设置中授予以下权限，否则将不能使用应用:\n\n%s",
                TextUtils.join("\n", permissionNames));

        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            dialog = new AlertDialog.Builder(context).setCancelable(false)
                    .setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 跳转至应用权限设置页
                            toSystemSetting(context);
                        }
                    })
                    .setNegativeButton("关闭应用", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cancelCallBack(listener);
                            System.exit(0);
                        }
                    })
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 取消授权回调
     */
    private void cancelCallBack(RunTimePermissionGrantedListener listener) {
        if (listener instanceof RunTimePermissionListener) {
            RunTimePermissionListener cancelListener =
                    (RunTimePermissionListener) listener;
            cancelListener.onCancel();
        }
    }

    /**
     * 跳转到应用权限设置
     */
    public void toSystemSetting(Context context) {
        AndPermission.permissionSetting(context).execute();
    }

    private RunTimePermissionMgr() {

    }

    private static class DynamicPermissionHolder {
        private static RunTimePermissionMgr INSTANCE = new RunTimePermissionMgr();
    }
}