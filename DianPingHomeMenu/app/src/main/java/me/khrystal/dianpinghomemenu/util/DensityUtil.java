package me.khrystal.dianpinghomemenu.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class DensityUtil {

    public static int getWidth(Context context) {
        initScreen(context);
        if (screen != null) {
            return screen.widthPixels;
        }
        return 0;
    }

    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    private static void initScreen(Context context) {
        if (screen == null) {
            DisplayMetrics dm = new DisplayMetrics();
            WindowManager windowManager = (WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getMetrics(dm);
            screen = new Screen(dm.widthPixels, dm.heightPixels);
        }
    }

    private static Screen screen = null;
    private static int STATUS_HEIGHT = 0;

    public static class Screen {
        public int widthPixels;
        public int heightPixels;

        public Screen() {
        }

        public Screen(int widthPixels, int heightPixels) {
            this.widthPixels = widthPixels;
            this.heightPixels = heightPixels;
        }

        @Override
        public String toString() {
            return "(" + widthPixels + "," + heightPixels + ")";
        }

    }
}
