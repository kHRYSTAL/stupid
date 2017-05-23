package me.khrystal.widget.slidearcview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/2/28
 * update time:
 * email: 723526676@qq.com
 */

public class SlidingArcView extends ViewGroup {

    private static String TAG = SlidingArcView.class.getSimpleName();

    //region 需要外部 set 的数据
    private List<String> mTitles;
    private List<Integer> mSrcList;
    private List<SignView> views = new ArrayList<>();
    Bitmap mBackgroundBitmap;
    Paint mPaint = new Paint();
    int mDiameter;
    //endregion

    //region 与内部子 view 相关的属性
    private boolean isAnimated = false; // 动画是否在执行
    private int viewTopChange = ScreenUtil.dp2px(80f); // 子view向上偏移的位移
    private VelocityTracker mVelocityTracker; // fling 使用
    private static final int SPEED = 30;
    private SignView leftView; // 屏幕最左边的子view
    private SignView rightView; // 屏幕最右边的子view
    // 外层圆心
    private int mCenterX;
    private int mCenterY;

    private int mRadius; // 半径
    private static final int VX = 50; // 第一个view的x坐标
    private SignView chooseView; // 选中的view;
    private SignView lastChooseView;
    private boolean isClicke = false;
    //endregion

    private boolean supportScroll = true;
    private int lastX;
    private int downPointId;
    private int downX;
    private int downY;


    public SlidingArcView(Context context) {
        this(context, null);
    }

    public SlidingArcView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingArcView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SlidingArcView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // do nothing
    }

    private void init() {
        // TODO 背景图片
        // TODO: 17/5/23  弧形圆心
        mCenterX = ScreenUtil.getScreenW() / 2;
        // TODO: 17/5/23   弧形半径
        mRadius = ScreenUtil.getScreenW() / 2 + 100;
        setBackgroundColor(getResources().getColor(android.R.color.transparent));
        setClickable(true);

        // TODO: 17/5/23 添加子View
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mDiameter = Math.min(getMeasuredWidth(), getMeasuredHeight());
        setMeasuredDimension(mDiameter, mDiameter);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBackgroundBitmap != null)
            drawBackground(canvas);
    }

    /**
     * 绘制弧形背景
     * @param canvas
     */
    private void drawBackground(Canvas canvas) {
        Bitmap bitmap = Bitmap.createScaledBitmap(mBackgroundBitmap, mDiameter, mDiameter, false);
        if (bitmap == null)
            return;
        BitmapShader mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.REPEAT,
                Shader.TileMode.REPEAT);
        Bitmap dest = Bitmap.createBitmap(mBackgroundBitmap.getWidth(), mBackgroundBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        if (dest == null)
            return;
        Canvas c = new Canvas(dest);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(mBitmapShader);
        c.drawCircle(mCenterX, mCenterY - viewTopChange, mRadius, paint);
        canvas.drawBitmap(dest, 0, 0, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isAnimated)
            return super.onTouchEvent(event);
        if (mVelocityTracker == null)
            mVelocityTracker = VelocityTracker.obtain();
        mVelocityTracker.addMovement(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downPointId = event.getPointerId(0);
                downX = lastX = (int) event.getX();
                downY = (int) event.getY();
                return true;
            case MotionEvent.ACTION_MOVE:
                return true;
            case MotionEvent.ACTION_UP:
                return true;

        }
        return super.onTouchEvent(event);
    }

    public interface SignAble {

    }

    private class SignView {

        private int indexInScreen;
        private View view;
        private String title;
        private int centerX;
        private int centerY;
        private int index;
        private int size = 120;
        private int width = (ScreenUtil.getScreenW()) / 5; // 屏幕上半圆内只显示5个元素
        private float normalScale = 1.0f; // 默认大小
        private float maxScale = 0.2f; // 放大倍数
        private boolean stop; // 是否停止手动滚动 + fling滑动 如停止 进行计算滑动到最靠近的指定固定位置
        private boolean isChoose = false;

        public SignView(View view, final int index) {
            this.index = index;
            this.view = view;
            this.title = mTitles.get(index);
            if (index == 0) {
                leftView = this;
            } else if (index == mSrcList.size() - 1) {
                rightView = this;
            }
            // is middle
            if (index == mSrcList.size() / 2) {
                isChoose = true;
                chooseView = this;
            }
            initView();
        }

        /**
         * 计算view的中心点坐标
         */
        private void initView() {
            centerX = (width) / 2 + width * index;
            centerY = mCenterY + (int) Math.sqrt(Math.pow(mRadius, 2) - (int) Math.pow((centerX - mCenterX), 2));
        }

        public void scroll(int scrollX) {
            this.centerX += scrollX;
            centerY = mCenterY + (int) Math.sqrt(Math.pow(mRadius, 2) - Math.pow((centerX - mCenterX), 2));
        }

        public int getCenterX() {
            return centerX;
        }

        public int getCenterY() {
            return centerY;
        }

        public View getView() {
            return view;
        }

        public void flush() {
            clean();
            // 每次计算view的位置
            view.layout(centerX - size / 2,
                    centerY - size / 2 - viewTopChange,
                    centerX + size / 2,
                    centerX + size / 2 - viewTopChange);
            // 以是否靠近中心点 来判断是否变大变小
            if (centerX >= mCenterX && centerX - mCenterX <= width) {
                // 屏幕右半部分移动运动 变小
                float scale = (float) (centerX - width - width / 2) / (float) width - 1.0f;
                ViewCompat.setScaleX(view, normalScale + maxScale - maxScale * scale);
                ViewCompat.setScaleY(view, normalScale + maxScale - maxScale * scale);
                if (scale >= 0.5f) {
                    isChoose = false;
                }
            } else if (centerX <= mCenterX && mCenterX - centerX <= width) {
                // 屏幕左半部分移动 变大
                float scale = (float) (centerX - width - width / 2) / (float) width;
                ViewCompat.setScaleX(view, normalScale + maxScale * scale);
                ViewCompat.setScaleY(view, normalScale + maxScale * scale);
                if (scale >= 0.5f) {
                    isChoose = true;
                }
            } else {
                isChoose = false;
                ViewCompat.setScaleX(view, normalScale);
                ViewCompat.setScaleY(view, normalScale);
            }

            if (isChoose)
                chooseView = this;
        }

        /**
         * 无限循环的判断
         */
        private void clean() {
            if (leftView.noLeftView()) {
                // 最左边没有view了 把最右边的移到最左边
                rightView.centerX = leftView.centerX - width;
                rightView.changeY();
                leftView = rightView;
                rightView = views.get(rightView.index == 0 ? views.size() - 1 : rightView.index - 1);
            }
            if (rightView.noRightView()) {
                // 最右边没有view了 把最左边的移到最右边
                leftView.centerX = rightView.centerX + width;
                leftView.changeY();
                rightView = leftView;
                leftView = views.get(leftView.index == views.size() - 1 ? 0 : leftView.index + 1);
            }
        }

        /**
         * 改变Y点坐标
         */
        public void changeY() {
            centerY = mCenterY + (int) Math.sqrt(Math.pow(mRadius, 2));
        }

        public boolean noLeftView() {
            return centerX - width / 2 > width / 2;
        }

        public boolean noRightView() {
            return centerX + width / 2 + width / 2 < ScreenUtil.getScreenW();
        }
    }

}
