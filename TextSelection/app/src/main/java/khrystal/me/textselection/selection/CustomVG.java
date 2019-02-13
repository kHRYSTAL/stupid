package khrystal.me.textselection.selection;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/1/21
 * update time:
 * email: 723526676@qq.com
 */
public class CustomVG extends RelativeLayout {

    public SelectionController sh;

    public CustomVG(Context context) {
        super(context);
    }

    public CustomVG(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomVG(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomVG(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        sh = new SelectionController(this);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (sh != null) sh.checkHandlesPosition();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (sh != null) sh.drawHandles(canvas);
    }

    public void setSelectionCallback(SelectionCallback selectionCallback) {
        if (sh != null) {
            sh.setSelectionCallback(selectionCallback);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (sh != null) {
            if (sh.onTouchEvent(ev)) {
                return true;
            }
        }

        return super.dispatchTouchEvent(ev);
    }

    public void copyTextToClipboard() {
        sh.copyTextToClipboard();
    }

    public String getSelectedText() {
        return sh.getSelectedText();
    }

    public void resetSelection() {
        sh.resetSelection();
    }
}
