package me.khrystal.wallpaper;

import android.hardware.Camera;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import java.io.IOException;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/6/13
 * update time:
 * email: 723526676@qq.com
 */

public class CameraLiveWallpaper extends WallpaperService {

    private static final String TAG = CameraLiveWallpaper.class.getSimpleName();

    @Override
    public Engine onCreateEngine() {
        return new CameraEngine();
    }

    class CameraEngine extends Engine implements Camera.PreviewCallback {

        private Camera camera;

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            startPreview();
            setTouchEventsEnabled(true);
        }

        @Override
        public void onTouchEvent(MotionEvent event) {
            super.onTouchEvent(event);
        }


        @Override
        public void onDestroy() {
            super.onDestroy();
            stopPreview();
            MessageLooping.getInstance(CameraLiveWallpaper.this, camera).stopLooping();
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            if (visible) {
                startPreview();
            } else {
                stopPreview();
            }
        }

        private void stopPreview() {
            if (camera != null) {
                try {
                    camera.stopPreview();
                    camera.setPreviewCallback(null);
                    camera.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                camera = null;
            }
        }

        @SuppressWarnings("NewApi")
        private void startPreview() {
            if (camera != null) {
                camera.release();
                camera = null;
            }
            camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
            camera.setDisplayOrientation(90);

            // 设置预览分辨率
            Camera.Parameters parameters = camera.getParameters();
            Camera.Size size = CameraUtil.getFullScreenSize(parameters.getSupportedPreviewSizes());
            parameters.setPreviewSize(size.width, size.height);
            // 设置自动对焦
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            camera.setParameters(parameters);

            MessageLooping.getInstance(CameraLiveWallpaper.this, camera).startLooping();
            try {
                camera.setPreviewDisplay(getSurfaceHolder());
            } catch (IOException e) {
                e.printStackTrace();
            }
            camera.startPreview();
            camera.cancelAutoFocus();
        }

        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            camera.addCallbackBuffer(data);
        }
    }
}
