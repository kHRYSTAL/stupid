package me.khrystal.weyuereader.widget.animation;

import android.graphics.Canvas;
import android.view.View;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/4/25
 * update time:
 * email: 723526676@qq.com
 */

public class NonePageAnim extends HorizonPageAnim {

    public NonePageAnim(int w, int h, View view, OnPageChangeListener listener) {
        super(w, h, view, listener);
    }

    @Override
    public void drawStatic(Canvas canvas) {
        if (isCancel) {
            canvas.drawBitmap(mCurBitmap, 0, 0, null);
        } else {
            canvas.drawBitmap(mNextBitmap, 0, 0, null);
        }
    }

    @Override
    public void drawMove(Canvas canvas) {
        if (isCancel) {
            canvas.drawBitmap(mCurBitmap, 0, 0, null);
        } else {
            canvas.drawBitmap(mNextBitmap, 0, 0, null);
        }
    }

    @Override
    public void startAnim() {
    }
}
