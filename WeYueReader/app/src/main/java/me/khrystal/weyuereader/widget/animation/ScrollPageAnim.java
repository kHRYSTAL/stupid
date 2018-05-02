package me.khrystal.weyuereader.widget.animation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * usage: 仿照listView源码实现的上下滑动效果
 * author: kHRYSTAL
 * create time: 18/4/25
 * update time:
 * email: 723526676@qq.com
 */

public class ScrollPageAnim extends PageAnimation {

    private static final String TAG = ScrollPageAnim.class.getSimpleName();

    // 滑动的追踪时间
    private static final int VELOCITY_DURATION = 1000;
    private VelocityTracker mVelocity;

    // 整个Bitmap的背景显示
    private Bitmap mBgBitmap;

    // 滑动到的下一张图片
    private Bitmap mNextBitmap;
    // 被废弃的图片
    private ArrayDeque<BitmapView> mScrapViews;
    // 正在被利用的图片
    private ArrayList<BitmapView> mActiveViews = new ArrayList<>(2);

    // 是否处于刷新阶段
    private boolean isRefresh;

    // 页面加载的方向
    private boolean isNext;

    BitmapView tmpView;

    public ScrollPageAnim(int w, int h, int marginWidth, int marginHeight,
                          View view, OnPageChangeListener listener) {
        super(w, h, marginWidth, marginHeight, view, listener);
        // 创建两个bitmapView
        initWidget();
    }

    private void initWidget() {
        mBgBitmap = Bitmap.createBitmap(mScreenWidth, mScreenHeight, Bitmap.Config.RGB_565);
        mScrapViews = new ArrayDeque<>(2);
        for (int i = 0; i < 2; i++) {
            BitmapView view = new BitmapView();
            view.bitmap = Bitmap.createBitmap(mViewWidth, mViewHeight, Bitmap.Config.RGB_565);
            view.srcRect = new Rect(0, 0, mViewWidth, mViewHeight);
            view.destRect = new Rect(0, 0, mViewWidth, mViewHeight);
            view.top = 0;
            view.bottom = view.bitmap.getHeight();

            mScrapViews.push(view);
        }
        onLayout();
        isRefresh = false;
    }

    // 修改布局 填充内容
    private void onLayout() {
        // 如果还没有开始加载, 则从上到下进行绘制
        if (mActiveViews.size() == 0) {
            fillDown(0, 0);
        } else {
            int offset = (int) (mTouchY - mLastY);
            // 判断是下滑还是上拉
            if (offset > 0) { // 下滑
                int topEdge = mActiveViews.get(0).top;
                fillUp(topEdge, offset);
            } else { // 上拉
                // 底部的距离 ＝ 当前底部的距离 + 滑动的距离(因为上滑，得到的值肯定是负的)
                int bottomEdge = mActiveViews.get(mActiveViews.size() - 1).bottom;
                fillDown(bottomEdge, offset);
            }
        }
    }

    // 底部占位填充
    private Iterator<BitmapView> downIt;

    /**
     * 创建View填充底部空白部分
     *
     * @param bottomEdge 当前最后一个view的底部， 在整个屏幕上的位置， 即相对于屏幕顶部的距离
     * @param offset     滑动的偏移量
     */
    private void fillDown(int bottomEdge, int offset) {
        // 首先对布局进行调整
        downIt = mActiveViews.iterator();
        BitmapView view;
        while (downIt.hasNext()) {
            view = downIt.next();
            view.top = view.top + offset;
            view.bottom = view.bottom + offset;
            // 设置允许显示的范围
            view.destRect.top = view.top;
            view.destRect.bottom = view.bottom;
            // 判断是否越界了
            if (view.bottom <= 0) {
                // 添加到废弃的View中
                mScrapViews.add(view);
                // 从Active中移除
                downIt.remove();
                // 如果原先是从上加载, 现在变成从下加载，则表示取消
                /**
                 * 滑动造成页面重复的问题：
                 * 这个问题产生的原因是这样的，代码是将下一个显示的页面，设置为 curPage ，即当前页。
                 * 那么如果上下滑动的时候，当前显示的是两个页面，那么第二个页面就是当前页。
                 * 如果这个时候回滚到上一页，我们认为的上一页是第一页的上一页。但是实际上上一页是第二页的上一页，也就是第一页
                 * 所以就造成了，我们滚动到的上一页，实际上是第一页的内容，所以就造成了重复。
                 *
                 * 解决方法就是:
                 * 如果原先是向下滑动的，形成了两个页面的状态。此时又变成向上滑动了，那么第二页就消失了。也就认为是取消显示下一页了
                 * 所以就调用 pageCancel()。
                 */
                if (!isNext) {
                    mListener.pageCancel();
                    isNext = true;
                }
            }
        }
        // 滑动之后的最后一个View距离屏幕顶部的实际位置
        int realEdge = bottomEdge + offset;
        // 进行填充
        while (realEdge < mViewHeight && mActiveViews.size() < 2) {
            // 从废弃的Views中取一个
            view = mScrapViews.getFirst();
            // 擦除其Bitmap()
            if (view == null)
                return;
            Bitmap cancelBitmap = mNextBitmap;
            mNextBitmap = view.bitmap;

            if (!isRefresh) {
                boolean hasNext = mListener.hasNext(); // 如果不成功则无法滑动
                // 如果不存在next 则进行还原
                if (!hasNext) {
                    mNextBitmap = cancelBitmap;
                    for (BitmapView activeView : mActiveViews) {
                        activeView.top = 0;
                        activeView.bottom = mViewHeight;
                        // 设置允许显示的范围
                        activeView.destRect.top = activeView.top;
                        activeView.destRect.bottom = activeView.bottom;
                    }
                    abortAnim();
                    return;
                }
            }

            // 如果加载成功, 那么就将Views从ScrapViews中移除
            mScrapViews.removeFirst();
            // 添加到存活的Bitmap中
            mActiveViews.add(view);
            // 设置Bitmap的范围
            view.top = realEdge;
            view.bottom = realEdge + view.bitmap.getHeight();
            // 设置允许显示的范围
            view.destRect.top = view.top;
            view.destRect.bottom = view.bottom;
            realEdge += view.bitmap.getHeight();
        }
    }

    private Iterator<BitmapView> upIt;

    /**
     * 创建View填充顶部空白部分
     *
     * @param topEdge : 当前第一个View的顶部，到屏幕顶部的距离
     * @param offset  : 滑动的偏移量
     */
    private void fillUp(int topEdge, int offset) {
        //首先进行布局的调整
        upIt = mActiveViews.iterator();
        BitmapView view;
        while (upIt.hasNext()) {
            view = upIt.next();
            view.top = view.top + offset;
            view.bottom = view.bottom + offset;
            //设置允许显示的范围
            view.destRect.top = view.top;
            view.destRect.bottom = view.bottom;

            //判断是否越界了
            if (view.top >= mViewHeight) {
                //添加到废弃的View中
                mScrapViews.add(view);
                //从Active中移除
                upIt.remove();

                //如果原先是下，现在变成从上加载了，则表示取消加载


                if (isNext) {
                    mListener.pageCancel();
                    isNext = false;
                }
            }
        }

        //滑动之后，第一个 View 的顶部距离屏幕顶部的实际位置。
        int realEdge = topEdge + offset;

        //对布局进行View填充
        while (realEdge > 0 && mActiveViews.size() < 2) {
            //从废弃的Views中获取一个
            view = mScrapViews.getFirst();
            if (view == null) return;

            //判断是否存在上一章节
            Bitmap cancelBitmap = mNextBitmap;
            mNextBitmap = view.bitmap;
            if (!isRefresh) {
                boolean hasPrev = mListener.hasPrev(); //如果不成功则无法滑动
                //如果不存在next,则进行还原
                if (!hasPrev) {
                    mNextBitmap = cancelBitmap;
                    for (BitmapView activeView : mActiveViews) {
                        activeView.top = 0;
                        activeView.bottom = mViewHeight;
                        //设置允许显示的范围
                        activeView.destRect.top = activeView.top;
                        activeView.destRect.bottom = activeView.bottom;
                    }
                    abortAnim();
                    return;
                }
            }
            //如果加载成功，那么就将View从ScrapViews中移除
            mScrapViews.removeFirst();
            //加入到存活的对象中
            mActiveViews.add(0, view);
            //设置Bitmap的范围
            view.top = realEdge - view.bitmap.getHeight();
            view.bottom = realEdge;

            //设置允许显示的范围
            view.destRect.top = view.top;
            view.destRect.bottom = view.bottom;
            realEdge -= view.bitmap.getHeight();
        }
    }

    // 重置当前位移状态 ==> 将ActiveViews全部删除, 然后重新加载
    public void refreshBitmap() {
        isRefresh = true;
        for (BitmapView view : mActiveViews) {
            mScrapViews.add(view);
        }
        // 清除所有的Active
        mActiveViews.clear();
        // 重新进行布局
        onLayout();
        isRefresh = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        // 初始化追踪器
        if (mVelocity == null) {
            mVelocity = VelocityTracker.obtain();
        }
        mVelocity.addMovement(event);
        setTouchPoint(x, y);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isRunning = false;
                // 设置起始点
                setStartPoint(x, y);
                // 停止动画
                abortAnim();
                break;
            case MotionEvent.ACTION_MOVE:
                mVelocity.computeCurrentVelocity(VELOCITY_DURATION);
                isRunning = true;
                // 进行刷新
                mView.postInvalidate();
                break;
            case MotionEvent.ACTION_UP:
                isRunning = false;
                // 开启动画
                startAnim();
                // 删除追踪器
                mVelocity.recycle();
                mVelocity = null;
                break;
            case MotionEvent.ACTION_CANCEL:
                try {
                    mVelocity.recycle();
                    mVelocity = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
        return true;
    }



    @Override
    public void draw(Canvas canvas) {
        // 进行布局
        onLayout();
        // 绘制背景
        canvas.drawBitmap(mBgBitmap, 0, 0, null);
        // 绘制内容
        canvas.save();
        // 移动位置
        canvas.translate(0, mMarginHeight);
        // 裁剪显示区域
        canvas.clipRect(0, 0, mViewWidth, mViewHeight);
        // 绘制Bitmap
        for (int i = 0; i < mActiveViews.size(); i++) {
            tmpView = mActiveViews.get(i);
            canvas.drawBitmap(tmpView.bitmap, tmpView.srcRect, tmpView.destRect, null);
        }
        canvas.restore();
    }

    @Override
    public synchronized void startAnim() {
        isRunning = true;
        mScroller.fling(0, (int) mTouchY, 0, (int) mVelocity.getYVelocity(),
                0, 0, Integer.MAX_VALUE * -1, Integer.MAX_VALUE);
    }

    @Override
    public void scrollAnim() {
        // Scroller.computeScrollOffset()方法是判断scroller的移动动画是否完成，
        // 当你调用startScroll()方法的时候这个方法返回的值一直都为true，
        // 如果采用其它方式移动视图比如：scrollTo()或 scrollBy时那么这个方法返回false。
        if (mScroller.computeScrollOffset()) {
            int x = mScroller.getCurrX();
            int y = mScroller.getCurrY();
            setTouchPoint(x, y);
            if (mScroller.getFinalX() == x && mScroller.getFinalY() == y) {
                isRunning = false;
            }
            mView.postInvalidate();
        }
    }

    @Override
    public void abortAnim() {
        if (!mScroller.isFinished()) {
            mScroller.abortAnimation();
            isRunning = false;
        }
    }

    @Override
    public Bitmap getBgBitmap() {
        return mBgBitmap;
    }

    @Override
    public Bitmap getNextBitmap() {
        return mNextBitmap;
    }

    private static class BitmapView {
        Bitmap bitmap;
        Rect srcRect;
        Rect destRect;
        int top;
        int bottom;
    }
}
