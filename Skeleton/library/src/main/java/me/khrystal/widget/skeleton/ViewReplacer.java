package me.khrystal.widget.skeleton;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/12/18
 * update time:
 * email: 723526676@qq.com
 */

public class ViewReplacer {
    private static final String TAG = ViewReplacer.class.getSimpleName();
    private final View mSourceView;
    private View mTargetView;
    private int mTargetViewResId = -1;
    private View mCurrentView;
    private ViewGroup mSourceParentView;
    private final ViewGroup.LayoutParams mSourceViewLayoutParams;
    private int mSourceViewIndexInParent = 0;
    private final int mSourceViewId;

    public ViewReplacer(View sourceView) {
        mSourceView = sourceView;
        mSourceViewLayoutParams = mSourceView.getLayoutParams();
        mCurrentView = mSourceView;
        mSourceViewId = mSourceView.getId();
    }

    public void replace(int targetViewResId) {
        if (mTargetViewResId == targetViewResId) {
            return;
        }
        if (init()) {
            mTargetViewResId = targetViewResId;
            replace(LayoutInflater.from(mSourceView.getContext()).inflate(mTargetViewResId, mSourceParentView, false));
        }
    }

    public void replace(View targetView) {
        if (mCurrentView == targetView) {
            return;
        }
        if (targetView.getParent() != null) {
            ((ViewGroup) targetView.getParent()).removeView(targetView);
        }
        if (init()) {
            mTargetView = targetView;
            mSourceParentView.removeView(mCurrentView);
            mTargetView.setId(mSourceViewId);
            mSourceParentView.addView(mTargetView, mSourceViewIndexInParent, mSourceViewLayoutParams);
            mCurrentView = mTargetView;
        }
    }

    public void restore() {
        if (mSourceParentView != null) {
            mSourceParentView.removeView(mCurrentView);
            mSourceParentView.addView(mSourceView, mSourceViewIndexInParent, mSourceViewLayoutParams);
            mCurrentView = mSourceView;
            mTargetView = null;
            mTargetViewResId = -1;
        }
    }

    public View getSourceView() {
        return mSourceView;
    }

    public View getTargetView() {
        return mTargetView;
    }

    public View getCurrentView() {
        return mCurrentView;
    }

    private boolean init() {
        if (mSourceParentView == null) {
            mSourceParentView = (ViewGroup) mSourceView.getParent();
            if (mSourceParentView == null) {
                Log.e(TAG, "the source view have not attach to any view");
                return false;
            }
            int count = mSourceParentView.getChildCount();
            for (int index = 0; index < count; index++) {
                if (mSourceView == mSourceParentView.getChildAt(index)) {
                    mSourceViewIndexInParent = index;
                    break;
                }
            }
        }
        return true;
    }
}
