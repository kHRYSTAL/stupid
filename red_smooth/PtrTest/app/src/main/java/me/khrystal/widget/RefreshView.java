package me.khrystal.widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import me.khrystal.ptrtest.R;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 16/4/5
 * update time:
 * email: 723526676@qq.com
 */
public class RefreshView extends LinearLayout implements View.OnTouchListener {


    public static final int STATUS_PULL_TO_REFRESH = 0;

    public static final int STATUS_RELEASE_TO_REFRESH = 1;

    public static final int STATUS_REFRESHING = 2;

    public static final int STATUS_REFRESH_FINISHED = 3;

    public static final int SCROLL_SPEED = -20;

    public static final long ONE_MINUTE = 60 * 1000;

    public static final long ONE_HOUR = 60 * ONE_MINUTE;

    public static final long ONE_DAY = 24 * ONE_HOUR;

    public static final long ONE_MONTH = 30 * ONE_DAY;

    public static final long ONE_YEAR = 12 * ONE_MONTH;

    private static final String UPDATED_AT = "updated_at";

    private PullToRefreshListener mListener;

    private SharedPreferences preferences;

    private View header;

    private ListView listView;

    private ProgressBar progressBar;

    private ImageView arrow;

    private TextView description;

    private TextView updateAt;

    private MarginLayoutParams headerLayoutParams;

    private long lastUpdateTime;

    private int mId;

    private int hideHeaderHeight;

    private int currentStatus = STATUS_REFRESH_FINISHED;

    private int lastStatus = currentStatus;

//    手指按下屏幕的纵坐标
    private float yDown;

//    被判断未滚动之前手指可以移动的最大值
    private int touchSlop;

    private boolean loadOnce;

    private boolean ableToPull;


    public RefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        header = LayoutInflater.from(context).inflate(R.layout.refresh_header,null,true);
        progressBar = (ProgressBar)header.findViewById(R.id.progress_bar);
        arrow = (ImageView)header.findViewById(R.id.arrow);
        description = (TextView)header.findViewById(R.id.description);
        updateAt = (TextView)header.findViewById(R.id.updated_at);
        //TODO
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        refreshUpdatedAtValue();
        setOrientation(VERTICAL);
        addView(header,0);
    }

    private void refreshUpdatedAtValue() {
        lastUpdateTime = preferences.getLong(UPDATED_AT + mId, -1);
        long currentTime = System.currentTimeMillis();
        long timePassed = currentTime - lastUpdateTime;
        long timeIntoFormat;
        String updateAtValue;
        if (lastUpdateTime == -1) {
            updateAtValue = getResources().getString(R.string.not_updated_yet);
        } else if (timePassed < 0) {
            updateAtValue = getResources().getString(R.string.time_error);
        } else if (timePassed < ONE_MINUTE) {
            updateAtValue = getResources().getString(R.string.updated_just_now);
        } else if (timePassed < ONE_HOUR) {
            timeIntoFormat = timePassed / ONE_MINUTE;
            String value = timeIntoFormat + "分钟";
            updateAtValue = String.format(getResources().getString(R.string.updated_at), value);
        } else if (timePassed < ONE_DAY) {
            timeIntoFormat = timePassed / ONE_HOUR;
            String value = timeIntoFormat + "小时";
            updateAtValue = String.format(getResources().getString(R.string.updated_at), value);
        } else if (timePassed < ONE_MONTH) {
            timeIntoFormat = timePassed / ONE_DAY;
            String value = timeIntoFormat + "天";
            updateAtValue = String.format(getResources().getString(R.string.updated_at), value);
        } else if (timePassed < ONE_YEAR) {
            timeIntoFormat = timePassed / ONE_MONTH;
            String value = timeIntoFormat + "个月";
            updateAtValue = String.format(getResources().getString(R.string.updated_at), value);
        } else {
            timeIntoFormat = timePassed / ONE_YEAR;
            String value = timeIntoFormat + "年";
            updateAtValue = String.format(getResources().getString(R.string.updated_at), value);
        }
        updateAt.setText(updateAtValue);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed && !loadOnce){
            hideHeaderHeight = -header.getHeight();
            headerLayoutParams = (MarginLayoutParams)header.getLayoutParams();
            headerLayoutParams.topMargin = hideHeaderHeight;
            listView = (ListView)getChildAt(1);
            listView.setOnTouchListener(this);
            loadOnce = true;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        setIsAbleToPull(event);
        if (ableToPull){
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    yDown = event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float yMove = event.getRawY();
                    int distance = (int)(yMove-yDown);
                    if (distance <= 0 && headerLayoutParams.topMargin<=hideHeaderHeight){
                        return false;
                    }
                    if (distance < touchSlop){
                        return false;
                    }
                    if (currentStatus != STATUS_REFRESHING){
                        if (headerLayoutParams.topMargin > 0){
                            currentStatus = STATUS_RELEASE_TO_REFRESH;
                        } else {
                            currentStatus = STATUS_PULL_TO_REFRESH;
                        }
//                        TODO
                        headerLayoutParams.topMargin = (distance/2)+hideHeaderHeight;
                        header.setLayoutParams(headerLayoutParams);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                default:
                    if (currentStatus == STATUS_RELEASE_TO_REFRESH){
                        new RefreshingTask().execute();
                    }else if (currentStatus == STATUS_PULL_TO_REFRESH){
                        new HideHeaderTask().execute();
                    }
                    break;
            }
            if (currentStatus == STATUS_PULL_TO_REFRESH
                    || currentStatus == STATUS_RELEASE_TO_REFRESH){
                updateHeaderView();
                listView.setPressed(false);
                listView.setFocusable(false);
                listView.setFocusableInTouchMode(false);
                lastStatus = currentStatus;

                return true;
            }
        }
        return false;
    }


    private void setIsAbleToPull(MotionEvent event){
        View firstChild = listView.getChildAt(0);
        if (firstChild!=null){
            int firstVisiblePos = listView.getFirstVisiblePosition();
            if (firstVisiblePos==0 && firstChild.getTop()==0){
                if (!ableToPull){
                    yDown = event.getRawY();
                }
                ableToPull = true;
            }else {
                if (headerLayoutParams.topMargin!=hideHeaderHeight){
                    headerLayoutParams.topMargin = hideHeaderHeight;
                    header.setLayoutParams(headerLayoutParams);
                }
                ableToPull = false;
            }
        }else {
            ableToPull = true;
        }
    }

    class RefreshingTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            int topMargin = headerLayoutParams.topMargin;
            while (true) {
                topMargin = topMargin + SCROLL_SPEED;
                if (topMargin <= 0) {
                    topMargin = 0;
                    break;
                }
                publishProgress(topMargin);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            currentStatus = STATUS_REFRESHING;
            publishProgress(0);
            if (mListener != null) {
                mListener.onRefresh();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... topMargin) {
            updateHeaderView();
            headerLayoutParams.topMargin = topMargin[0];
            header.setLayoutParams(headerLayoutParams);
        }

    }

    private void rotateArrow() {
        float pivotX = arrow.getWidth() / 2f;
        float pivotY = arrow.getHeight() / 2f;
        float fromDegrees = 0f;
        float toDegrees = 0f;
        if (currentStatus == STATUS_PULL_TO_REFRESH) {
            fromDegrees = 180f;
            toDegrees = 360f;
        } else if (currentStatus == STATUS_RELEASE_TO_REFRESH) {
            fromDegrees = 0f;
            toDegrees = 180f;
        }
        RotateAnimation animation = new RotateAnimation(fromDegrees, toDegrees, pivotX, pivotY);
        animation.setDuration(100);
        animation.setFillAfter(true);
        arrow.startAnimation(animation);
    }


    private void updateHeaderView() {
        if (lastStatus != currentStatus) {
            if (currentStatus == STATUS_PULL_TO_REFRESH) {
                description.setText(getResources().getString(R.string.pull_to_refresh));
                arrow.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                rotateArrow();
            } else if (currentStatus == STATUS_RELEASE_TO_REFRESH) {
                description.setText(getResources().getString(R.string.release_to_refresh));
                arrow.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                rotateArrow();
            } else if (currentStatus == STATUS_REFRESHING) {
                description.setText(getResources().getString(R.string.refreshing));
                progressBar.setVisibility(View.VISIBLE);
                arrow.clearAnimation();
                arrow.setVisibility(View.GONE);
            }
            refreshUpdatedAtValue();
        }
    }

    class HideHeaderTask extends AsyncTask<Void, Integer, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {
            int topMargin = headerLayoutParams.topMargin;
            while (true) {
                topMargin = topMargin + SCROLL_SPEED;
                if (topMargin <= hideHeaderHeight) {
                    topMargin = hideHeaderHeight;
                    break;
                }
                publishProgress(topMargin);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return topMargin;
        }

        @Override
        protected void onProgressUpdate(Integer... topMargin) {
            headerLayoutParams.topMargin = topMargin[0];
            header.setLayoutParams(headerLayoutParams);
        }

        @Override
        protected void onPostExecute(Integer topMargin) {
            headerLayoutParams.topMargin = topMargin;
            header.setLayoutParams(headerLayoutParams);
            currentStatus = STATUS_REFRESH_FINISHED;
        }
    }


    private void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public interface PullToRefreshListener {
        void onRefresh();

    }

}
