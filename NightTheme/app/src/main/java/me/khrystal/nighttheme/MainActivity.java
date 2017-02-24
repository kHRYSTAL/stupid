package me.khrystal.nighttheme;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private DayNightHelper mDayNightHelper;

    private RecyclerView mRecyclerView;
    private LinearLayout mHeaderLayout;
    private List<RelativeLayout> mLayoutList;
    private List<TextView> mTextViewList;
    private List<CheckBox> mCheckBoxList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        initData();
        initTheme();
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(new SimpleAuthorAdapter());

        mHeaderLayout = (LinearLayout) findViewById(R.id.header_layout);

        mLayoutList = new ArrayList<>();
        mLayoutList.add((RelativeLayout) findViewById(R.id.jianshu_layout));
        mLayoutList.add((RelativeLayout) findViewById(R.id.zhihu_layout));

        mTextViewList = new ArrayList<>();
        mTextViewList.add((TextView) findViewById(R.id.tv_jianshu));
        mTextViewList.add((TextView) findViewById(R.id.tv_zhihu));

        mCheckBoxList = new ArrayList<>();
        CheckBox checkBoxJs = (CheckBox) findViewById(R.id.ckb_jianshu);
        checkBoxJs.setOnCheckedChangeListener(this);
        mCheckBoxList.add(checkBoxJs);
        CheckBox checkBoxZh = (CheckBox) findViewById(R.id.ckb_zhihu);
        checkBoxZh.setOnCheckedChangeListener(this);
        mCheckBoxList.add(checkBoxZh);
    }

    private void initTheme() {
        if (mDayNightHelper.isDay()) {
            setTheme(R.style.DayTheme);
        } else {
            setTheme(R.style.NightTheme);
        }
    }

    private void initData() {
        mDayNightHelper = new DayNightHelper(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int viewId = buttonView.getId();
        if (viewId == R.id.ckb_jianshu) {
            changeThemeByJianShu();
        } else if (viewId == R.id.ckb_zhihu) {
            changeThemeByZhiHu();
        }
    }

    private void changeThemeByZhiHu() {
        showAnimation();
        toggleThemeSetting();
        refreshUI();
    }


    private void changeThemeByJianShu() {
        toggleThemeSetting();
        refreshUI();
    }

    private void showAnimation() {
        final View decorView = getWindow().getDecorView();
        Bitmap cacheBitmap = getCacheBitmapFromView(decorView);
        if (decorView instanceof ViewGroup && cacheBitmap != null) {
            final View view = new View(this);
            view.setBackgroundDrawable(new BitmapDrawable(getResources(), cacheBitmap));
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            ((ViewGroup) decorView).addView(view, layoutParams);
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
            objectAnimator.setDuration(1000);
            objectAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    ((ViewGroup) decorView).removeView(view);
                }
            });
            objectAnimator.start();
        }
    }

    private Bitmap getCacheBitmapFromView(View view) {
        final boolean drawingCacheEnabled = true;
        view.setDrawingCacheEnabled(drawingCacheEnabled);
        view.buildDrawingCache(drawingCacheEnabled);
        final Bitmap drawingCache = view.getDrawingCache();
        Bitmap bitmap;
        if (drawingCache != null) {
            bitmap = Bitmap.createBitmap(drawingCache);
            view.setDrawingCacheEnabled(false);
        } else {
            bitmap = null;
        }
        return bitmap;
    }

    private void refreshUI() {
        TypedValue background = new TypedValue();
        TypedValue textColor = new TypedValue();
        Resources.Theme theme = getTheme();
        theme.resolveAttribute(R.attr.clockBackground, background, true);
        theme.resolveAttribute(R.attr.clockTextColor, textColor, true);

        mHeaderLayout.setBackgroundResource(background.resourceId);
        for (RelativeLayout layout : mLayoutList) {
            layout.setBackgroundResource(background.resourceId);
        }
        for (CheckBox checkBox : mCheckBoxList) {
            checkBox.setBackgroundResource(background.resourceId);
        }
        for (TextView textView : mTextViewList) {
            textView.setBackgroundResource(background.resourceId);
        }

        Resources resources = getResources();
        for (TextView textView : mTextViewList) {
            textView.setTextColor(resources.getColor(textColor.resourceId));
        }

        int childCount = mRecyclerView.getChildCount();
        for (int childIndex = 0; childIndex < childCount; childIndex++) {
            ViewGroup childView = (ViewGroup) mRecyclerView.getChildAt(childIndex);
            childView.setBackgroundResource(background.resourceId);
            View infoLayout = childView.findViewById(R.id.info_layout);
            infoLayout.setBackgroundResource(background.resourceId);
            TextView nickName = (TextView) childView.findViewById(R.id.tv_nickname);
            nickName.setBackgroundResource(background.resourceId);
            nickName.setTextColor(resources.getColor(textColor.resourceId));
            TextView motto = (TextView) childView.findViewById(R.id.tv_motto);
            motto.setBackgroundResource(background.resourceId);
            motto.setTextColor(resources.getColor(textColor.resourceId));
        }

        Class<RecyclerView> recyclerViewClass = RecyclerView.class;
        try {
            Field declaredField = recyclerViewClass.getDeclaredField("mRecycler");
            declaredField.setAccessible(true);
            Method declaredMethod = Class.forName(RecyclerView.Recycler.class.getName())
                    .getDeclaredMethod("clear", (Class<?>[]) new Class[0]);
            declaredMethod.setAccessible(true);
            declaredMethod.invoke(declaredField.get(mRecyclerView), new Object[0]);
            RecyclerView.RecycledViewPool recycledViewPool = mRecyclerView.getRecycledViewPool();
            recycledViewPool.clear();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        refreshStatusBar();
    }

    private void refreshStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = getTheme();
            theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
            getWindow().setStatusBarColor(getResources().getColor(typedValue.resourceId));
        }
    }

    private void toggleThemeSetting() {
        if (mDayNightHelper.isDay()) {
            mDayNightHelper.setMode(DayNight.NIGHT);
            setTheme(R.style.NightTheme);
        } else {
            mDayNightHelper.setMode(DayNight.DAY);
            setTheme(R.style.DayTheme);
        }
    }
}
