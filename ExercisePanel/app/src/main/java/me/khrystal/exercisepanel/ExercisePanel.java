package me.khrystal.exercisepanel;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
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

    private static final String TAG = "ExercisePanel";
    private static final float SCALE_FACTOR = 2.0f;

    public boolean mMagnifierAdded = false;
    private volatile MotionEvent mCurrentMotionEvent;
    private View mGlassView;
    private View mZoomView;
    private int mGlassWidth;
    private int mGlassHeight;
    private int mGlassHeight2;
    private Bitmap mContentBitmap;
    private WordView mWordView;
    private OnWordSelectUpListener upListener;
    private ViewPager viewPager;
    private NestedScrollView nestedScrollView;
    private boolean isShowGlassView;

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
        mGlassHeight2 = getResources().getDimensionPixelOffset(R.dimen.glass_view_height3);
        initMagnifierView(context);
        setOnLongClickListener(this);
        setOnTouchListener(mTouchListener);
    }

    private void initView() {
        mWordView = findExercisePanel();
        mWordView.setOnWordSelectListener(this);
    }

    private WordView findExercisePanel() {
        int allCount = getChildCount();
        for (int i = 0; i < allCount; i++) {
            if (getChildAt(i) instanceof WordView || getChildAt(i) instanceof ExpandableTextView) {
                return (WordView) getChildAt(i);
            }
        }
        return null;
    }

    private void initMagnifierView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_word_magnifier, this, true);
        mGlassView = findViewById(R.id.glass_view);
        mZoomView = findViewById(R.id.zoom_view);
        mGlassView.setVisibility(INVISIBLE);
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
                        mGlassView.setVisibility(INVISIBLE);
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
            mGlassView.setVisibility(INVISIBLE);
            updateGlassViewPosition(event);
            showTouchRegion(event);
        }
    }

    private int getMagnifierLeft(int touchedX) {
        return (touchedX - mGlassWidth / 2);
    }

    private int getMagnifierTop(int touchedY) {
        return (touchedY - mGlassHeight2 * 3);
    }

    private int getDisplayRegionLeft(int touchedX) {
        return (touchedX - mGlassWidth / 2);
    }

    private int getDisplayRegionTop(int touchedY) {
        return (touchedY - mGlassHeight + getResources().getDimensionPixelOffset(R.dimen.height_below_touch_point));
    }

    private void tryHideMagnifier() {
        mGlassView.setVisibility(GONE);
        mWordView.clearSelectedWord();
        isShowGlassView = false;
        mMagnifierAdded = false;
    }


    private void updateGlassViewPosition(MotionEvent motionEvent) {
        if (motionEvent != null) {
            int x = getMagnifierLeft((int) motionEvent.getX());
            int y = getMagnifierTop((int) motionEvent.getY());
            mGlassView.setVisibility(VISIBLE);
            isShowGlassView = true;
            mGlassView.setX(x);
            mGlassView.setY(y);
        }
    }

    private Bitmap wordViewBtimap = null;

    private Bitmap takeScreenShot(View noeView) {
        wordViewBtimap = Bitmap.createBitmap(((View) noeView.getParent()).getWidth(), ((View) noeView.getParent()).getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(wordViewBtimap);
        ((View) noeView.getParent()).draw(canvas);
        return wordViewBtimap;
    }

    private void showTouchRegion(MotionEvent event) {
        if (wordViewBtimap != null && event != null) {
            int x = getDisplayRegionLeft((int) event.getX());
            int y = getDisplayRegionTop((int) event.getY());
            mZoomView.setBackgroundDrawable(getCurrentImage(x, y));
            wordViewBtimap.recycle();
            wordViewBtimap = null;
        }
    }

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
        mWordView.setOnTouchListener(new View.OnTouchListener() {
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
