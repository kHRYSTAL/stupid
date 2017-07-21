package me.khrystal.widget.sliding;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/7/20
 * update time:
 * email: 723526676@qq.com
 */

public class SlidingUpPanelLayout extends ViewGroup {

    public static final int EXPANDED = 0;
    public static final int COLLAPSED = 1;
    public static final int HIDDEN = 2;
    public static final int DRAGGING = 3;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({EXPANDED, COLLAPSED, HIDDEN, DRAGGING})
    public @interface SlideState {
    }

    private static final int DEFAULT_MIN_FLING_VELOCITY = 400; // dips per second

    private boolean isSlidingEnabled;

    private float mExpandThreshold;

    private float mCollapseThreshold;

    private ISlidingUpPanel mSlidingUpPanel;

    private float mSlidedProgress;
    private boolean isSlidingUp;
    private PanelSlideListener mPanelSlideListener;
    private ViewDragHelper mDragHelper; // core
    private boolean isFirstLayout = true;
    private Adapter mAdapter;

    public SlidingUpPanelLayout(Context context) {
        this(context, null);
    }

    public SlidingUpPanelLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingUpPanelLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SlidingUpPanelLayout, defStyleAttr, 0);
        isSlidingEnabled = !ta.getBoolean(R.styleable.SlidingUpPanelLayout_spl_disableSliding, false);
        mExpandThreshold = ta.getFloat(R.styleable.SlidingUpPanelLayout_spl_expandThreshold, 0.0f);
        mCollapseThreshold = ta.getFloat(R.styleable.SlidingUpPanelLayout_spl_collapseThreshold, 0.7f);
        ta.recycle();

        if (isInEditMode()) {
            mDragHelper = null;
            return;
        }
        mDragHelper = ViewDragHelper.create(this, 1.0f, new DragHelperCallback());
        mDragHelper.setMinVelocity(DEFAULT_MIN_FLING_VELOCITY * getResources().getDisplayMetrics().density);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        isFirstLayout = true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isFirstLayout = true;
        mSlidingUpPanel = null;
        if (mAdapter != null) {
            mAdapter.setSlidingUpPanelLayout(null);
            mAdapter = null;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode != MeasureSpec.EXACTLY) {
            throw new IllegalStateException("Width must has a exact value or MATCH_PARENT");
        } else if (heightMode != MeasureSpec.EXACTLY) {
            throw new IllegalStateException("Height must has a exact value or MATCH_PARENT");
        }

        final int childCount = getChildCount();
        int layoutHeight = heightSize - getPaddingBottom() - getPaddingTop();

        // first pass, Measure based on child LayoutParams width/height
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);
            final LayoutParams lp = (LayoutParams) child.getLayoutParams();
            if (child.getVisibility() == GONE && i == 0) {
                continue;
            }
            int childWidth = widthSize - lp.leftMargin - lp.rightMargin;
            int childWidthSpec;
            if (lp.width == LayoutParams.WRAP_CONTENT) {
                childWidthSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.AT_MOST);
            } else if (lp.width == LayoutParams.MATCH_PARENT) {
                childWidthSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
            } else {
                childWidthSpec = MeasureSpec.makeMeasureSpec(lp.width, MeasureSpec.EXACTLY);
            }

            int childHeight = layoutHeight - lp.topMargin - lp.bottomMargin;
            int childHeightSpec;
            if (lp.height == LayoutParams.WRAP_CONTENT) {
                childHeightSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.AT_MOST);
            } else if (lp.height == LayoutParams.MATCH_PARENT) {
                childHeightSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
            } else {
                childHeightSpec = MeasureSpec.makeMeasureSpec(lp.height, MeasureSpec.EXACTLY);
            }

            child.measure(childWidthSpec, childHeightSpec);
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int paddingLeft = getPaddingLeft();
        final int paddingTop = getPaddingTop();
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);
            if (i == 0 && child instanceof ISlidingUpPanel) {
                throw new IllegalArgumentException("The child view at position 0 can not be instance of ISlidingUpPanel");
            }
            if (i > 0 && !(child instanceof ISlidingUpPanel)) {
                throw new IllegalArgumentException("The child view after position 0 must be instance pf ISlidingUpPanel");
            }

            if (isFirstLayout && child instanceof ISlidingUpPanel) {
                if (mSlidingUpPanel == null) {
                    mSlidingUpPanel = (ISlidingUpPanel) child;
                }
                setOnTouchedInternal(child);
            }

            //always layout the sliding view on the first layout
            if (child.getVisibility() == GONE && (i == 0 || isFirstLayout)) {
                continue;
            }

            int childTop = paddingTop;
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            childTop += lp.topMargin;
            int childBottom = childTop + child.getMeasuredHeight();
            int childLeft = paddingLeft + lp.leftMargin;
            int childRight = childLeft + child.getMeasuredWidth();

            if (child instanceof ISlidingUpPanel) {
                ISlidingUpPanel panel = (ISlidingUpPanel) child;
                childTop = panel.getPanelTopBySlidingState(panel.getSlideState()) + getPaddingTop();
                childBottom = childTop + ((ISlidingUpPanel) child).getPanelExpandHeight();
            }

            child.layout(childLeft, childTop, childRight, childBottom);
        }
        isFirstLayout = false;
    }

    private void setOnTouchedInternal(final View child) {
        child.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mSlidingUpPanel = (ISlidingUpPanel) child;
                return false;
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (h != oldh) {
            isFirstLayout = true;
            mSlidingUpPanel = null;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!isEnabled() || !isSlidingEnabled()) {
            mDragHelper.cancel();
            return super.onInterceptTouchEvent(ev);
        }

        int action = MotionEventCompat.getActionMasked(ev);
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mDragHelper.cancel();
            ;
            return false;
        }
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isSlidingEnabled()) {
            return super.onTouchEvent(event);
        }
        mDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        if (mDragHelper != null && mDragHelper.continueSettling(true)) {
            if (!isSlidingEnabled()) {
                mDragHelper.abort();
                return;
            }
        }
        ViewCompat.postInvalidateOnAnimation(this);
    }

    //region public invoke method
    public boolean expandPanel(@NonNull ISlidingUpPanel panel) {
        mSlidingUpPanel = panel;
        return expandPanel();
    }

    public boolean expandPanel() {
        if (mSlidingUpPanel == null)
            return false;
        if (isFirstLayout) {
            mSlidingUpPanel.setSlideState(EXPANDED);
            return true;
        } else {
            return mSlidingUpPanel.getSlideState() == EXPANDED || isFirstLayout || smoothSlideTo(1.0f);
        }
    }

    public boolean collapsePanel(@NonNull ISlidingUpPanel panel) {
        mSlidingUpPanel = panel;
        return collapsePanel();
    }

    public boolean collapsePanel() {
        if (mSlidingUpPanel == null)
            return false;
        if (isFirstLayout) {
            mSlidingUpPanel.setSlideState(COLLAPSED);
            return false;
        } else {
            return mSlidingUpPanel.getSlideState() == COLLAPSED || isFirstLayout || smoothSlideTo(0.0f);
        }
    }

    public void setmSlidingUpPanel(@NonNull ISlidingUpPanel slidingUpPanel) {
        mSlidingUpPanel = slidingUpPanel;
    }

    public ISlidingUpPanel getSlidingUpPanel() {
        return mSlidingUpPanel;
    }

    public void setSlidingEnabled(boolean enabled) {
        isSlidingEnabled = enabled;
    }

    public boolean isSlidingEnabled() {
        return isSlidingEnabled && mSlidingUpPanel != null;
    }

    public float getExpandThreshold() {
        return mExpandThreshold;
    }

    public void setExpandThreshold(float expandThreshold) {
        mExpandThreshold = expandThreshold;
    }

    public float getCollapseThreshold() {
        return mCollapseThreshold;
    }

    public void setCollapsedThreshold(float collapseThreshold) {
        mCollapseThreshold = collapseThreshold;
    }

    public void setAdapter(@NonNull Adapter adapter) {
        mAdapter = adapter;
        mAdapter.setSlidingUpPanelLayout(this);

        int itemCount = mAdapter.getItemCount();
        if (itemCount <= 0) {
            return;
        }
        if (getChildCount() > 0) {
            View view = getChildAt(0);
            removeAllViews();
            addView(view);
        }

        for (int i = 0; i < itemCount; i++) {
            ISlidingUpPanel panel = mAdapter.onCreateSlidingPanel(i);
            mAdapter.onBindView(panel, i);
            addView(panel.getPanelView());
        }

        isFirstLayout = true;
        mSlidingUpPanel = null;
        requestLayout();
    }

    public void setPanelSlideListener(PanelSlideListener listener) {
        mPanelSlideListener = listener;
    }

    public Adapter getAdapter() {
        return mAdapter;
    }
    //endregion


    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams();
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof MarginLayoutParams ? new LayoutParams((MarginLayoutParams) p)
                : new LayoutParams(p);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof WindowManager.LayoutParams && super.checkLayoutParams(p);
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    private boolean smoothSlideTo(float slideProgress) {
        if (!isSlidingEnabled()) {
            return false;
        }

        int panelTop = computePanelTopPosition(slideProgress);
        if (mDragHelper.smoothSlideViewTo(mSlidingUpPanel.getPanelView(), mSlidingUpPanel.getPanelView().getLeft(), panelTop)) {
            ViewCompat.postInvalidateOnAnimation(this);
            return true;
        }
        return false;
    }

    private int computePanelTopPosition(float slideProgress) {
        return (int) ((mSlidingUpPanel.getPanelExpandHeight() - mSlidingUpPanel.getPanelCollapsedHeight()) * (1 - slideProgress));
    }

    private float computeSlidedProgress(int topPosition) {
        final int collapsedTop = computePanelTopPosition(0);
        return (float) (collapsedTop - topPosition) / (mSlidingUpPanel.getPanelExpandHeight() - mSlidingUpPanel.getPanelCollapsedHeight());
    }

    private class DragHelperCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == mSlidingUpPanel;
        }

        @Override
        public void onViewDragStateChanged(int state) {
            if (mDragHelper == null)
                return;
            if (mDragHelper.getViewDragState() == ViewDragHelper.STATE_IDLE) {
                mSlidedProgress = computeSlidedProgress(mSlidingUpPanel.getPanelView().getTop());
                if (mSlidedProgress == 1) {
                    if (mSlidingUpPanel.getSlideState() != EXPANDED) {
                        mSlidingUpPanel.setSlideState(EXPANDED);
                        if (mPanelSlideListener != null) {
                            mPanelSlideListener.onPanelExpanded(mSlidingUpPanel);
                        }
                    }
                } else if (mSlidedProgress == 0) {
                    if (mSlidingUpPanel.getSlideState() != COLLAPSED) {
                        mSlidingUpPanel.setSlideState(COLLAPSED);
                        if (mPanelSlideListener != null) {
                            mPanelSlideListener.onPanelCollapsed(mSlidingUpPanel);
                        }
                    }
                } else if (mSlidedProgress < 0) {
                    mSlidingUpPanel.setSlideState(HIDDEN);
                    if (mSlidingUpPanel != null) {
                        mPanelSlideListener.onPanelHidden(mSlidingUpPanel);
                    }
                }
            }
        }

        @Override
        public void onViewCaptured(View capturedChild, int activePointerId) {
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            isSlidingUp = dy < 0;
            mSlidingUpPanel.setSlideState(DRAGGING);

            mSlidedProgress = computeSlidedProgress(top);
            if (mPanelSlideListener != null) {
                mPanelSlideListener.onPanelSliding(mSlidingUpPanel, mSlidedProgress);
            }

            for (int i = 1; i < getChildCount(); i++) {
                ISlidingUpPanel view = (ISlidingUpPanel) getChildAt(i);
                view.onSliding(mSlidingUpPanel, top, dy, mSlidedProgress);
            }
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            int target;
            if (isSlidingUp) {
                target = computePanelTopPosition(mSlidedProgress >= mExpandThreshold ? 1.0f : 0.0f);
            } else {
                target = computePanelTopPosition(mSlidedProgress >= mCollapseThreshold ? 1.0f : 0.0f);
            }
            if (mDragHelper != null) {
                mDragHelper.settleCapturedViewAt(releasedChild.getLeft() + getPaddingLeft(), target);
                invalidate();
            }
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            if (mSlidingUpPanel != null) {
                return mSlidingUpPanel.getPanelExpandHeight() - mSlidingUpPanel.getPanelCollapsedHeight();
            }
            return 0;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            final int collapsedTop = computePanelTopPosition(0.0f);
            final int expanedTop = computePanelTopPosition(1.0f);
            return Math.min(Math.max(top, expanedTop), collapsedTop);
        }
    }

    public static class LayoutParams extends MarginLayoutParams {

        public LayoutParams() {
            super(MATCH_PARENT, MATCH_PARENT);
        }

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable parcelable = super.onSaveInstanceState();
        SaveState ss = new SaveState(parcelable);
        ss.isSlidingEnable = isSlidingEnabled;
        ss.expandThreshold = mExpandThreshold;
        ss.collapseThreshold = mCollapseThreshold;
        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SaveState ss = (SaveState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        isSlidingEnabled = ss.isSlidingEnable;
        mExpandThreshold = ss.expandThreshold;
        mCollapseThreshold = ss.collapseThreshold;
    }

    private static class SaveState extends BaseSavedState {

        boolean isSlidingEnable;
        float expandThreshold;
        float collapseThreshold;

        SaveState(Parcelable superState) {
            super(superState);
        }

        public SaveState(Parcel source) {
            super(source);
            isSlidingEnable = source.readByte() == 1;
            expandThreshold = source.readFloat();
            collapseThreshold = source.readFloat();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeByte(isSlidingEnable ? (byte) 1 : (byte) 0);
            out.writeFloat(expandThreshold);
            out.writeFloat(collapseThreshold);
        }

        public static final Creator<SaveState> CREATOR =
                new Creator<SaveState>() {
                    @Override
                    public SaveState createFromParcel(Parcel source) {
                        return new SaveState(source);
                    }

                    @Override
                    public SaveState[] newArray(int size) {
                        return new SaveState[size];
                    }
                };
    }

    public static abstract class Adapter {
        private SlidingUpPanelLayout mSlidingUpPanelLayout;

        private void setSlidingUpPanelLayout(SlidingUpPanelLayout slidingUpPanelLayout) {
            mSlidingUpPanelLayout = slidingUpPanelLayout;
        }

        public abstract int getItemCount();

        @NonNull
        public abstract ISlidingUpPanel onCreateSlidingPanel(int position);

        public abstract void onBindView(ISlidingUpPanel panel, int position);

        public ISlidingUpPanel getItem(int position) {
            if (getItemCount() == 0)
                return null;
            else {
                int childCount = mSlidingUpPanelLayout.getChildCount();
                if (childCount > 1 && position + 1 < childCount) {
                    // position 1 is not outside control childView, we control is [1, childCount - 1]
                    View child = mSlidingUpPanelLayout.getChildAt(position + 1);
                    if (child != null && child instanceof ISlidingUpPanel) {
                        return (ISlidingUpPanel) child;
                    } else {
                        return null;
                    }
                } else {
                    // the max count num is null, child max size is childCount - 1
                    return null;
                }
            }
        }
    }

    public interface PanelSlideListener {
        void onPanelSliding(ISlidingUpPanel panel, float slideProgress);

        void onPanelCollapsed(ISlidingUpPanel panel);

        void onPanelExpanded(ISlidingUpPanel panel);

        void onPanelHidden(ISlidingUpPanel panel);
    }

    public static class PanelSlideListenerAdapter implements PanelSlideListener {

        @Override
        public void onPanelSliding(ISlidingUpPanel panel, float slideProgress) {

        }

        @Override
        public void onPanelCollapsed(ISlidingUpPanel panel) {

        }

        @Override
        public void onPanelExpanded(ISlidingUpPanel panel) {

        }

        @Override
        public void onPanelHidden(ISlidingUpPanel panel) {

        }
    }
}
