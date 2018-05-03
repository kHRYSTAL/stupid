package me.khrystal.weyuereader.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.allen.library.RxHttpUtils;
import com.allen.library.download.DownloadObserver;
import com.daimajia.numberprogressbar.NumberProgressBar;

import java.io.File;

import io.reactivex.disposables.Disposable;
import me.khrystal.weyuereader.R;
import me.khrystal.weyuereader.model.AppUpdateBean;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/4/24
 * update time:
 * email: 723526676@qq.com
 */

public class AppUpdateUtils {
    public static AppUpdateUtils mAppUpdateUtils;
    Disposable mDisposable;

    public static AppUpdateUtils getInstance() {
        if (mAppUpdateUtils == null) {
            mAppUpdateUtils = new AppUpdateUtils();
        }
        return mAppUpdateUtils;
    }

    public void appUpdate(Context context, AppUpdateBean appUpdateBean) {
        new MaterialDialog.Builder(context)
                .title("版本更新")
                .content("是否更新到最新版本？")
                .positiveText("立即更新")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        updateDownload(context, appUpdateBean.getDownloadurl());
                    }
                })
                .negativeText("取消")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void updateDownload(Context context, String downloadUrl) {
        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .customView(R.layout.layout_app_update, false)
                .title("更新下载中...")
                .negativeText("取消下载")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (mDisposable != null) {
                            mDisposable.dispose();
                        }
                        ToastUtils.show("取消更新");
                        dialog.dismiss();
                    }
                }).build();

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (mDisposable != null) {
                    mDisposable.dispose();
                }
                ToastUtils.show("取消更新");
                dialogInterface.dismiss();
            }
        });
        NumberProgressBar npbDownload = dialog.getCustomView().findViewById(R.id.npb_download);
        dialog.show();
        String url = Constant.BASE_URL + downloadUrl;
        RxHttpUtils.downloadFile(url)
                .subscribe(new DownloadObserver("WeYue.apk") {
                    @Override
                    protected void getDisposable(Disposable disposable) {
                        mDisposable = disposable;
                    }

                    @Override
                    protected void onError(String s) {

                    }

                    @Override
                    protected void onSuccess(long bytesRead, long contentLength, float progress, boolean done, String filePath) {
                        if (done) {
                            dialog.dismiss();
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                Uri contentUri = FileProvider.getUriForFile(context, "me.khrystal.weyuereader.FileProvider", new File(filePath));
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                            } else {
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.setDataAndType(Uri.fromFile(new File(filePath)), "application/vnd.android.package-archive");
                            }
                            context.startActivity(intent);
                        } else {
                            npbDownload.setProgress((int) progress);
                        }
                    }
                });
    }
}
