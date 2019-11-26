package me.khrystal.exercisepanel;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;


/**
 * Created by zhangteng on 2016/12/23.
 * 针对放大镜查词
 */
public class ExercisePanel extends RelativeLayout implements View.OnLongClickListener,
        WordView.OnWordSelectListener {

    private static final String TAG = "ExercisePanelNew";
    private static final float SCALE_FACTOR = 2.0f;
    GlassPopupWindow mGlassPopupWindow;

    public boolean mMagnifierAdded = false;
    private MotionEvent mCurrentMotionEvent;
    private int mGlassWidth;
    private int mGlassHeight;
    private Bitmap mContentBitmap;
    private WordView mWordView;
    private OnWordSelectUpListener upListener;
    private ViewPager viewPager;
    private NestedScrollView nestedScrollView;

    public ExercisePanel(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExercisePanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.galss_txtview);
        int resId = a.getResourceId(R.styleable.galss_txtview_g_textview, 0);
        LayoutInflater.from(context).inflate(resId, this, true);
        a.recycle();
        init(context);
    }

    private void init(Context context) {
        initView();
        mGlassWidth = getResources().getDimensionPixelOffset(R.dimen.glass_view_width);
        mGlassHeight = getResources().getDimensionPixelOffset(R.dimen.glass_view_height);
        setOnLongClickListener(this);
        setOnTouchListener(mTouchListener);
    }

    private void initView() {
        mWordView = findExercisePanelNew();
        mWordView.setOnWordSelectListener(this);
    }

    private WordView findExercisePanelNew() {
        int allCount = getChildCount();
        for (int i = 0; i < allCount; i++) {
            if (getChildAt(i) instanceof WordView || getChildAt(i) instanceof ExpandableTextView) {
                return (WordView) getChildAt(i);
            }
        }
        return null;
    }

    /**
     * 在WordView中
     * ScrollingMovementMethodSupport方法后, 以下所有手势处理都被拦截掉了 因此是无用的 需要在{@ScrollingMovementMethodSupport中去处理}
     */
    @Override
    public boolean onLongClick(View v) {
        Log.e(TAG, "长按事件");
        if (viewPager != null) {
            viewPager.requestDisallowInterceptTouchEvent(true);
        }
        if (nestedScrollView != null) {
            nestedScrollView.requestDisallowInterceptTouchEvent(true);
        }
        mMagnifierAdded = true;
        mContentBitmap = takeScreenShot(this);
        return false;
    }

    public OnTouchListener getTouchListener() {
        return mTouchListener;
    }


    /**
     * 在WordView中
     * ScrollingMovementMethodSupport方法后, 以下所有手势处理都被拦截掉了 因此是无用的 需要在{@ScrollingMovementMethodSupport中去处理}
     */
    private OnTouchListener mTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (mMagnifierAdded) {
                        mCurrentMotionEvent = MotionEvent.obtain(event);
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (mMagnifierAdded) {
                        if (nestedScrollView != null)
                            nestedScrollView.requestDisallowInterceptTouchEvent(true);
                        if (viewPager != null)
                            viewPager.requestDisallowInterceptTouchEvent(true);
                        mCurrentMotionEvent = MotionEvent.obtain(event);
                        performSelectWord(event);
                        Log.e(TAG, "选中文字并显示放大镜");
                        tryShowMagnifier(event);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    if (mMagnifierAdded) {

                        if (TextUtils.isEmpty(mWordView.getmSelectedWord())) {
                            mWordView.clearSelectedWord();
                        } else if (upListener != null) {
                            upListener.onWordSelectUp(mWordView.getmSelectedWord());
                        }
                        tryHideMagnifier();
                    }
                    if (viewPager != null) {
                        viewPager.requestDisallowInterceptTouchEvent(false);
                    }
                    if (nestedScrollView != null) {
                        nestedScrollView.requestDisallowInterceptTouchEvent(false);
                    }
                    Log.e(TAG, "清除选择文字");
                    mWordView.clearSelectedWord();
                    break;
                default:
                    break;
            }
            return false;
        }
    };

    @Override
    public void onWordSelect() {
        mContentBitmap = takeScreenShot(this);
        mMagnifierAdded = true;
        tryShowMagnifier(mCurrentMotionEvent);
    }


    private void performSelectWord(MotionEvent motionEvent) {
        int[] location = new int[2];
        mWordView.getLocationOnScreen(location);
        int x = (int) motionEvent.getRawX() - location[0];
        int y = (int) motionEvent.getRawY() - location[1];
        MotionEvent event = MotionEvent.obtain(motionEvent);
        event.setLocation(x, y);
        mWordView.trySelectWord(event);
    }

    private void tryShowMagnifier(MotionEvent event) {
        if (mMagnifierAdded) {
            updateGlassViewPosition(event);
            showTouchRegion(event);
        }
    }

    private int getDisplayRegionLeft(int touchedX) {
        return (touchedX - mGlassWidth / 2);
    }

    private int getDisplayRegionTop(int touchedY) {
        return (touchedY - mGlassHeight + getResources().getDimensionPixelOffset(R.dimen.height_below_touch_point));
    }

    private void tryHideMagnifier() {
        if (mGlassPopupWindow != null) {
            mGlassPopupWindow.hideGlass();
            mGlassPopupWindow = null;
        }
        mWordView.clearSelectedWord();
        mMagnifierAdded = false;
    }


    private void updateGlassViewPosition(MotionEvent motionEvent) {
        if (motionEvent != null) {
            if (mGlassPopupWindow != null) {
                mGlassPopupWindow.moveGlass((int) motionEvent.getRawX(), (int) motionEvent.getRawY());
            } else {
                mGlassPopupWindow = new GlassPopupWindow(getContext());
                mGlassPopupWindow.showGlass(((Activity) getContext()).getWindow().getDecorView(), (int) motionEvent.getRawX(), (int) motionEvent.getRawY());
            }
        }
    }

    private Bitmap wordViewBtimap = null;

    private Bitmap takeScreenShot(View noeView) {
        wordViewBtimap = Bitmap.createBitmap(((View) noeView).getWidth(), ((View) noeView).getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(wordViewBtimap);
        ((View) noeView).draw(canvas);
        return wordViewBtimap;
    }

    private void showTouchRegion(MotionEvent event) {
        if (wordViewBtimap != null && event != null) {
            int x = getDisplayRegionLeft((int) event.getX());
            int y = getDisplayRegionTop((int) event.getY());
            if (mGlassPopupWindow != null) {
                mGlassPopupWindow.setZoomImage(getCurrentImage(x, y));
            }
            wordViewBtimap.recycle();
            wordViewBtimap = null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.DONUT)
    private BitmapDrawable getCurrentImage(int x, int y) {
        Bitmap magnifierBitmap = Bitmap.createBitmap(mGlassWidth, mGlassHeight, mContentBitmap.getConfig());
        Paint paint = new Paint();
        Canvas canvas = new Canvas(magnifierBitmap);
        canvas.scale(SCALE_FACTOR, SCALE_FACTOR,
                mGlassWidth / 2, mGlassHeight / 2);
        canvas.drawBitmap(mContentBitmap,
                -x,
                -y,
                paint);
        BitmapDrawable outputDrawable = new BitmapDrawable(getResources(), magnifierBitmap);
        return outputDrawable;
    }

    public boolean getmMagnifierAdded() {
        return mMagnifierAdded;
    }

    public void setUpListener(OnWordSelectUpListener upListener) {
        this.upListener = upListener;
    }

    public void supportScroll(final ViewGroup viewGroup) {
        mWordView.supportScroll(this);
        mWordView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
                    viewGroup.requestDisallowInterceptTouchEvent(true);
                } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    viewGroup.requestDisallowInterceptTouchEvent(false);
                }
                return false;
            }
        });
    }

    public interface OnWordSelectUpListener {
        public void onWordSelectUp(String selectWord);
    }

    public void clearSelectedWord() {
        mWordView.clearSelectedWord();
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    public void setNestedScrollView(NestedScrollView nestedScrollView) {
        this.nestedScrollView = nestedScrollView;
    }
}