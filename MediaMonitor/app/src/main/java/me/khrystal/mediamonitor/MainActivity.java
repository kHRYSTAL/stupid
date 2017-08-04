package me.khrystal.mediamonitor;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.util.List;
import java.util.concurrent.TimeUnit;

import me.khrystal.util.mediamonitor.MediaMonitor;
import me.khrystal.util.mediamonitor.MediaType;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 100;

    private TextView mScreenshotPathTv;
    private Subscription mDetectorSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mScreenshotPathTv = (TextView) findViewById(R.id.tv_screenshot_path);

        if (AndPermission.hasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            startDetectScreenshot();
        } else {
            requestReadExternalStoragePermission();
        }
    }

    private void requestReadExternalStoragePermission() {
        AndPermission.with(this)
                .requestCode(REQUEST_CODE_READ_EXTERNAL_STORAGE)
                .permission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .callback(new PermissionListener() {
                    @Override
                    public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
                        if (REQUEST_CODE_READ_EXTERNAL_STORAGE == requestCode) {
                            startDetectScreenshot();
                        }
                    }

                    @Override
                    public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {

                    }
                })
                .rationale(new RationaleListener() {
                    @Override
                    public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                        AndPermission.rationaleDialog(MainActivity.this, rationale)
                                .show();
                    }
                })
                .start();
    }

    private void startDetectScreenshot() {
        mDetectorSubscription = MediaMonitor
                .listen(this, MediaType.ALL)
                .debounce(100, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String path) {
                        mScreenshotPathTv.setText(path);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDetectorSubscription != null && !mDetectorSubscription.isUnsubscribed()) {
            mDetectorSubscription.unsubscribe();
        }
    }
}
