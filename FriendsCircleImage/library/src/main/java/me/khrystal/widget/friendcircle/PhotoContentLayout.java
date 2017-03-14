package me.khrystal.widget.friendcircle;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;

import me.khrystal.widget.friendcircle.adapter.PhotoContentsBaseAdapter;
import me.khrystal.widget.friendcircle.adapter.observer.PhotoBaseDataObserver;
import me.khrystal.widget.friendcircle.flowlayout.FlowLayout;
import me.khrystal.widget.friendcircle.util.ObjectPool;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/3/14
 * update time:
 * email: 723526676@qq.com
 */

public class PhotoContentLayout extends FlowLayout {

    private final int INVALID_POSITION = -1;
    private PhotoContentsBaseAdapter mAdapter;
    private PhotoImageAdapterObserver mAdapterObserver = new PhotoImageAdapterObserver();
    private InnerRecyclerHelper recycler;
    private int mItemCount;
    private boolean mDataChanged;
    private int itemMargin;
    private int multiChildSize;

    private int maxSingleWidth;
    private int maxSingleHeight;

    // 宽高比
    private float singleAspectRatio = 16f / 9f;

    private int mSelectedPosition = INVALID_POSITION;

    private Rect mTouchFrame;

    private Runnable mTouchReset;

    public PhotoContentLayout(Context context) {
        super(context);
        init(context);
    }

    public PhotoContentLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PhotoContentLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        itemMargin = dp2px(4f);
        recycler = new InnerRecyclerHelper();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private int dp2px(float dp) {
        return (int) (dp * getContext().getResources().getDisplayMetrics().density + 0.5f);
    }

    public boolean performItemClick(ImageView view, int position) {
        final boolean result;
        // TODO: 17/3/14
        return false;
    }

    private class PhotoImageAdapterObserver extends PhotoBaseDataObserver {

        @Override
        public void onChanged() {
            super.onChanged();
            updateItemCount();
            mDataChanged = true;
            requestLayout();
        }
        @Override
        public void onInvalidated() {
            super.onInvalidated();
            invalidate();
        }

    }
    private class InnerRecyclerHelper {
        private SparseArray<ImageView> mCachedViews;

        private ObjectPool<ImageView> mSingleCachedViews;

        InnerRecyclerHelper() {
            mCachedViews = new SparseArray<>();
            mSingleCachedViews = new ObjectPool<>(9);
        }

        ImageView getCachedView(int position) {
            final ImageView imageView = mCachedViews.get(position);
            if (null != imageView) {
                mCachedViews.remove(position);
                return imageView;
            }
            return null;
        }

        ImageView getSingleCachedView() {
            return mSingleCachedViews.pop();
        }

        void addCachedView(int position, ImageView view) {
            mCachedViews.put(position, view);
        }

        void addSingleCachedView(ImageView imageView) {
            mSingleCachedViews.put(imageView);
        }
        void clearCache() {
            mCachedViews.clear();
            mSingleCachedViews.clearPool();
        }

    }
}
