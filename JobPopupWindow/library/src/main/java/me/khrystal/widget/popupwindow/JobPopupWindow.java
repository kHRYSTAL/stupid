package me.khrystal.widget.popupwindow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * usage: 职位模糊匹配PopupWindow
 * author: kHRYSTAL
 * create time: 18/9/4
 * update time:
 * email: 723526676@qq.com
 */
public class JobPopupWindow extends PopupWindowContainer {

    public static final int ANIM_GROW_FROM_LEFT = 1;
    public static final int ANIM_GROW_FROM_RIGHT = 2;
    public static final int ANIM_GROW_FROM_CENTER = 3;
    public static final int ANIM_AUTO = 4;

    private Animation mShowAnim;
    private LayoutInflater inflater;
    private RecyclerView mTrack;
    private OnItemClickListener mItemClickListener;
    private OnVisibleChangeListener mOnVisibleChangeListener;

    private List<JobItem> mJobItemList = new ArrayList<>();
    private JobItemAdapter mJobAdapter;

    // 是否点击
    private boolean mDidPressAction;
    // 是否执行动画
    private boolean mAnimateTrack;
    // anim style
    private int mAnimStyle;

    public JobPopupWindow(Context context) {
        super(context);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mShowAnim = AnimationUtils.loadAnimation(context, R.anim.rail);
        mShowAnim.setInterpolator(new Interpolator() {
            @Override
            public float getInterpolation(float v) {
                final float inner = (v * 1.55f) - 1.1f;
                return 1.2f - inner * inner;
            }
        });
        setRootViewId(R.layout.job_pop_layout);
        mAnimStyle = ANIM_AUTO;
        mAnimateTrack = true;
    }

    // init View
    @SuppressLint({"deprecation", "ObsoleteSdkInt"})
    private void setRootViewId(int id) {
        mRootView = (ViewGroup) inflater.inflate(id, null);
        mTrack = (RecyclerView) mRootView.findViewById(R.id.rvJob);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO) {
            mPopupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        } else {
            mPopupWindow.setWidth(WindowManager.LayoutParams.FILL_PARENT);
        }
        mTrack.setLayoutManager(new LinearLayoutManager(mContext));
        mJobAdapter = new JobItemAdapter();
        mTrack.setAdapter(mJobAdapter);
        setContentView(mRootView);
    }


    //region public method
    public void setAnimateTrack(boolean mAnimateTrack) {
        this.mAnimateTrack = mAnimateTrack;
    }

    public void setAnimStyle(int animStyle) {
        this.mAnimStyle = mAnimStyle;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void show(View anchor) {
        preShow();
        int[] location = new int[2];
        mDidPressAction = false;
        anchor.getLocationOnScreen(location);
        Rect anchorRect = new Rect(location[0], location[1], location[0] + anchor.getWidth(),
                location[1] + anchor.getHeight());
        mPopupWindow.setWidth(dip2px(mContext, 160));
        mPopupWindow.setHeight(dip2px(mContext, 160));
        int xPos = anchorRect.left;
        int yPos = anchorRect.bottom;
//        setAnimationStyle(screenWidth, anchorRect.centerY());
        // TODO 设置popup window 显示位置
        Log.e("Job", ""+ xPos + "," + yPos);
        mPopupWindow.showAsDropDown(anchor, xPos, yPos, Gravity.START);
//        if (mAnimateTrack) {
//            mTrack.startAnimation(mShowAnim);
//        }
    }

    public void setAnimationStyle(int screenWidth, int requestedX) {
        switch (mAnimStyle) {
            case ANIM_GROW_FROM_LEFT:
                mPopupWindow.setAnimationStyle(R.style.Animations_PopDownMenu_Left);
                break;
            case ANIM_GROW_FROM_RIGHT:
                mPopupWindow.setAnimationStyle(R.style.Animations_PopDownMenu_Right);
                break;

            case ANIM_GROW_FROM_CENTER:
                mPopupWindow.setAnimationStyle(R.style.Animations_PopDownMenu_Center);
                break;
            case ANIM_AUTO:
                mPopupWindow.setAnimationStyle(R.style.Animations_PopDownMenu_Center);
                break;
        }
    }

    public void updateList(List<JobItem> jobItems) {
        mJobItemList.clear();
        mJobItemList.addAll(jobItems);
        mJobAdapter.notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mItemClickListener = onItemClickListener;
    }

    public void setOnVisibleChangeListener(OnVisibleChangeListener onVisibleChangeListener) {
        this.mOnVisibleChangeListener = onVisibleChangeListener;
    }
    //endregion

    //region abstract method impl
    @Override
    protected void onDismiss() {
        if (!mDidPressAction && mOnVisibleChangeListener != null) {
            mOnVisibleChangeListener.onDismiss();
        }
    }

    @Override
    protected void onShow() {
        if (mOnVisibleChangeListener != null) {
            mOnVisibleChangeListener.onShow();
        }
    }
    //endregion

    public interface OnItemClickListener {
        void onItemClickListener(JobPopupWindow source, int position, JobItem jobItem);
    }

    public interface OnVisibleChangeListener {
        void onShow();

        void onDismiss();
    }

    // JobItemAdapter
    class JobItemAdapter extends RecyclerView.Adapter<JobItemHolder> {

        @NonNull
        @Override
        public JobItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(mContext)
                    .inflate(R.layout.job_item, parent, false);
            itemView.setFocusable(true);
            itemView.setClickable(true);
            return new JobItemHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull JobItemHolder holder, int position) {
            holder.bind(mJobItemList.get(position), position);
        }

        @Override
        public int getItemCount() {
            return mJobItemList.size();
        }
    }

    // JobItemHolder
    class JobItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private JobItem item;
        private TextView jobText;
        private int position;

        public JobItemHolder(View itemView) {
            super(itemView);
            jobText = (TextView) itemView.findViewById(R.id.jobText);
            itemView.setOnClickListener(this);
        }

        public void bind(JobItem item, int position) {
            this.item = item;
            this.position = position;
            if (item != null)
                jobText.setText(item.getJobText());
            else
                jobText.setText("");

        }

        @Override
        public void onClick(View view) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClickListener(JobPopupWindow.this, this.position, this.item);
                if (!item.isSticky()) {
                    mDidPressAction = true;
                    view.post(new Runnable() {
                        @Override
                        public void run() {
                            dismiss();
                        }
                    });
                }
            }
        }
    }

    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
