package me.khrystal.weyuereader.view.activity.impl;

import android.Manifest;
import android.animation.Animator;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;
import me.khrystal.weyuereader.R;
import me.khrystal.weyuereader.db.entity.UserBean;
import me.khrystal.weyuereader.db.helper.UserHelper;
import me.khrystal.weyuereader.model.AppUpdateBean;
import me.khrystal.weyuereader.model.MainMenuBean;
import me.khrystal.weyuereader.utils.AppUpdateUtils;
import me.khrystal.weyuereader.utils.BaseUtils;
import me.khrystal.weyuereader.utils.Constant;
import me.khrystal.weyuereader.utils.SharedPreUtils;
import me.khrystal.weyuereader.utils.SnackBarUtils;
import me.khrystal.weyuereader.utils.ThemeUtils;
import me.khrystal.weyuereader.utils.ToastUtils;
import me.khrystal.weyuereader.view.activity.ISetting;
import me.khrystal.weyuereader.view.adapter.MainMenuAdapter;
import me.khrystal.weyuereader.view.base.BaseActivity;
import me.khrystal.weyuereader.view.fragment.impl.BookClassifyFragment;
import me.khrystal.weyuereader.view.fragment.impl.BookShelfFragment;
import me.khrystal.weyuereader.view.fragment.impl.ScanBookFragment;
import me.khrystal.weyuereader.viewmodel.activity.VMSettingInfo;
import me.khrystal.weyuereader.widget.MarqueTextView;
import me.khrystal.weyuereader.widget.ResideLayout;
import me.khrystal.weyuereader.widget.theme.ColorRelativeLayout;
import me.khrystal.weyuereader.widget.theme.ColorUiUtil;
import me.khrystal.weyuereader.widget.theme.ColorView;
import me.khrystal.weyuereader.widget.theme.Theme;

public class MainActivity extends BaseActivity implements ColorChooserDialog.ColorCallback, ISetting {

    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.tv_desc)
    MarqueTextView tvDesc;
    @BindView(R.id.top_menu)
    LinearLayout topMenu;
    @BindView(R.id.rv_menu)
    RecyclerView rvMenu;
    @BindView(R.id.tv_theme)
    TextView tvTheme;
    @BindView(R.id.tv_setting)
    TextView tvSetting;
    @BindView(R.id.bottom_menu)
    LinearLayout bottomMenu;
    @BindView(R.id.menu)
    ColorRelativeLayout menu;
    @BindView(R.id.status_bar)
    ColorView statusBar;
    @BindView(R.id.iv_toolbar_back)
    AppCompatImageView ivToolbarBack;
    @BindView(R.id.tv_toolbar_title)
    MarqueTextView tvToolbarTitle;
    @BindView(R.id.iv_toolbar_more)
    AppCompatImageView ivToolbarMore;
    @BindView(R.id.crl)
    ColorRelativeLayout crl;
    @BindView(R.id.container)
    FrameLayout container;
    @BindView(R.id.resideLayout)
    ResideLayout resideLayout;
    private MainMenuAdapter mainMenuAdapter;
    private FragmentManager fragmentManager;
    private String currentFragmentTag;
    private List<MainMenuBean> menuBeans = new ArrayList<>();
    private long fristTime = 0;
    private VMSettingInfo mModel;
    private String mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mModel = new VMSettingInfo(this, this);
        setBinddingView(R.layout.activity_main, NO_BINDDING, mModel);
        initThemeToolBar("分类", R.drawable.ic_classify, R.drawable.ic_search,
                view -> {
                    resideLayout.openPane();
                },
                view -> {
                    startActivity(BookSearchActivity.class);
                });
        // 检查更新
        mModel.appUpdate(false);
        fragmentManager = getSupportFragmentManager();
        initMenu();
        switchFragment("分类");
    }

    private void initMenu() {
        tvDesc.setSelected(true);
        BaseUtils.setIconDrawable(tvSetting, R.drawable.ic_setting);
        BaseUtils.setIconDrawable(tvTheme, R.drawable.ic_theme);
        getMenuData();
        rvMenu.setLayoutManager(new LinearLayoutManager(mContext));
        mainMenuAdapter = new MainMenuAdapter(menuBeans);
        rvMenu.setAdapter(mainMenuAdapter);
        mainMenuAdapter.setOnItemClickListener(((adapter, view, position) -> {
            String name = menuBeans.get(position).getName();
            switch (name) {
                case "扫描书籍":
                    RxPermissions rxPermissions = new RxPermissions(this);
                    rxPermissions
                            .requestEach(Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .subscribe(new Consumer<Permission>() {
                                @Override
                                public void accept(Permission permission) throws Exception {
                                    if (permission.granted) {
                                        // 用户已同意该授权
                                        MainActivity.this.switchFragment(name);
                                        tvToolbarTitle.setTag(name);
                                        ivToolbarBack.setImageResource(menuBeans.get(position).getIcon());
                                        resideLayout.closePane();
                                    } else if (permission.shouldShowRequestPermissionRationale) {
                                        // 用户拒绝了该权限 没有选中 不再询问,那么下次再次启动时，还会提示请求权限的对话框
                                        ToastUtils.show("用户拒绝开启读写权限");
                                        resideLayout.closePane();
                                    } else {
                                        // 用户拒绝了该权限, 并且选中不再询问
                                        resideLayout.closePane();
                                        SnackBarUtils.makeShort(MainActivity.this.getWindow().getDecorView(),
                                                "读写权限被禁止,移步到应用管理允许权限")
                                                .show("去设置", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        BaseUtils.getAppDetailSettingIntent(mContext, getPackageName());
                                                    }
                                                });
                                    }
                                }
                            });
                    break;
                case "书架":
                    switchFragment(name);
                    tvToolbarTitle.setText(name);
                    ivToolbarBack.setImageResource(menuBeans.get(position).getIcon());
                    resideLayout.closePane();
                    break;
                case "分类":
                    switchFragment(name);
                    tvToolbarTitle.setText(name);
                    ivToolbarBack.setImageResource(menuBeans.get(position).getIcon());
                    resideLayout.closePane();
                    break;
                case "缓存列表":
                    startActivity(BookDownloadActivity.class);
                    resideLayout.closePane();
                    break;
                case "意见反馈":
                    startActivity(FeedBackActivity.class);
                    resideLayout.closePane();
                    break;
                case "关于作者":
                    startActivity(AboutMineActivity.class);
                    resideLayout.closePane();
                    break;
                default:
                    ToastUtils.show("功能紧急开发中");
                    break;

            }
        }));

    }

    private List<MainMenuBean> getMenuData() {
        menuBeans.clear();
        String[] menuName = getResources().getStringArray(R.array.main_menu_name);
        TypedArray menuIcon = getResources().obtainTypedArray(R.array.main_menu_icon);
        for (int i = 0; i < menuName.length; i++) {
            MainMenuBean menuBean = new MainMenuBean();
            menuBean.setName(menuName[i]);
            menuBean.setIcon(menuIcon.getResourceId(i, 0));
            menuBeans.add(menuBean);
        }
        return menuBeans;
    }

    private void switchFragment(String title) {
        if (currentFragmentTag != null && currentFragmentTag.equals(title)) {
            return;
        }

        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        Fragment currentFragment = fragmentManager.findFragmentByTag(currentFragmentTag);
        if (currentFragment != null) {
            ft.hide(currentFragment);
        }
        Fragment foundFragment = fragmentManager.findFragmentByTag(title);
        if (foundFragment == null) {
            switch (title) {
                case "分类":
                    foundFragment = BookClassifyFragment.newInstance();
                    break;
                case "书架":
                    foundFragment = BookShelfFragment.newInstance();
                    break;
                case "扫描书籍":
                    foundFragment = ScanBookFragment.newInstance();
                    break;
                default:
                    break;
            }
        }
        if (foundFragment == null) {

        } else if (foundFragment.isAdded()) {
            ft.show(foundFragment);
        } else {
            ft.add(R.id.container, foundFragment, title);
        }
        ft.commit();
        currentFragmentTag = title;
    }

    @OnClick({R.id.iv_avatar, R.id.tv_theme, R.id.tv_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_avatar:
                String username = SharedPreUtils.getInstance()
                        .getString("username", "");
                if (username.equals("")) {
                    startActivity(LoginActivity.class);
                } else {
                    startActivity(UserInfoActivity.class);
                }
                break;
            case R.id.tv_theme:
                new ColorChooserDialog.Builder(this, R.string.theme)
                        .customColors(R.array.colors, null)
                        .doneButton(R.string.done)
                        .cancelButton(R.string.cancel)
                        .allowUserColorInput(false)
                        .allowUserColorInputAlpha(false)
                        .show();
                break;
            case R.id.tv_setting:

                if (mUsername.equals("")) {
                    startActivity(LoginActivity.class);
                } else {
                    startActivity(SettingActivity.class);
                }
                break;
        }


    }

    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, int selectedColor) {
        if (selectedColor == ThemeUtils.getThemeColor2Array(this, R.attr.colorPrimary))
            return;
        if (selectedColor == getResources().getColor(R.color.colorBluePrimary)) {
            setTheme(R.style.BlueTheme);
            SharedPreUtils.getInstance().setCurrentTheme(Theme.Blue);

        } else if (selectedColor == getResources().getColor(R.color.colorRedPrimary)) {
            setTheme(R.style.RedTheme);
            SharedPreUtils.getInstance().setCurrentTheme(Theme.Red);

        } else if (selectedColor == getResources().getColor(R.color.colorBrownPrimary)) {
            setTheme(R.style.BrownTheme);
            SharedPreUtils.getInstance().setCurrentTheme(Theme.Brown);

        } else if (selectedColor == getResources().getColor(R.color.colorGreenPrimary)) {
            setTheme(R.style.GreenTheme);
            SharedPreUtils.getInstance().setCurrentTheme(Theme.Green);

        } else if (selectedColor == getResources().getColor(R.color.colorPurplePrimary)) {
            setTheme(R.style.PurpleTheme);
            SharedPreUtils.getInstance().setCurrentTheme(Theme.Purple);

        } else if (selectedColor == getResources().getColor(R.color.colorTealPrimary)) {
            setTheme(R.style.TealTheme);
            SharedPreUtils.getInstance().setCurrentTheme(Theme.Teal);

        } else if (selectedColor == getResources().getColor(R.color.colorPinkPrimary)) {
            setTheme(R.style.PinkTheme);
            SharedPreUtils.getInstance().setCurrentTheme(Theme.Pink);

        } else if (selectedColor == getResources().getColor(R.color.colorDeepPurplePrimary)) {
            setTheme(R.style.DeepPurpleTheme);
            SharedPreUtils.getInstance().setCurrentTheme(Theme.DeepPurple);

        } else if (selectedColor == getResources().getColor(R.color.colorOrangePrimary)) {
            setTheme(R.style.OrangeTheme);
            SharedPreUtils.getInstance().setCurrentTheme(Theme.Orange);

        } else if (selectedColor == getResources().getColor(R.color.colorIndigoPrimary)) {
            setTheme(R.style.IndigoTheme);
            SharedPreUtils.getInstance().setCurrentTheme(Theme.Indigo);

        } else if (selectedColor == getResources().getColor(R.color.colorLightGreenPrimary)) {
            setTheme(R.style.LightGreenTheme);
            SharedPreUtils.getInstance().setCurrentTheme(Theme.LightGreen);

        } else if (selectedColor == getResources().getColor(R.color.colorDeepOrangePrimary)) {
            setTheme(R.style.DeepOrangeTheme);
            SharedPreUtils.getInstance().setCurrentTheme(Theme.DeepOrange);

        } else if (selectedColor == getResources().getColor(R.color.colorLimePrimary)) {
            setTheme(R.style.LimeTheme);
            SharedPreUtils.getInstance().setCurrentTheme(Theme.Lime);

        } else if (selectedColor == getResources().getColor(R.color.colorBlueGreyPrimary)) {
            setTheme(R.style.BlueGreyTheme);
            SharedPreUtils.getInstance().setCurrentTheme(Theme.BlueGrey);

        } else if (selectedColor == getResources().getColor(R.color.colorCyanPrimary)) {
            setTheme(R.style.CyanTheme);
            SharedPreUtils.getInstance().setCurrentTheme(Theme.Cyan);

        }
        final View rootView = getWindow().getDecorView();
        rootView.setDrawingCacheEnabled(true);
        rootView.buildDrawingCache(true);
        // 获取当前屏幕显示的截图
        final Bitmap localBitmap = Bitmap.createBitmap(rootView.getDrawingCache());
        rootView.setDrawingCacheEnabled(false);
        if (null != localBitmap && rootView instanceof ViewGroup) {
            final View tmpView = new View(getApplicationContext());
            tmpView.setBackgroundDrawable(new BitmapDrawable(getResources(), localBitmap));
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            // 向rootView添加一个临时的全屏view
            ((ViewGroup) rootView).addView(tmpView, params);
            tmpView.animate().alpha(0).setDuration(400).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    ColorUiUtil.changeTheme(rootView, getTheme());
                    System.gc();
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    ((ViewGroup) rootView).removeView(tmpView);
                    localBitmap.recycle();
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            }).start();
        }

    }

    @Override
    public void showLoading() {
        // do nothing
    }

    @Override
    public void stopLoading() {
        // do nothing
    }

    @Override
    public void appUpdate(AppUpdateBean appUpdateBean) {
        AppUpdateUtils.getInstance().appUpdate(this, appUpdateBean);
    }

    /**
     * 菜单是否可左滑
     *
     * @param isCanSlide
     */
    public void setLeftSlide(boolean isCanSlide) {
        resideLayout.setCanLeftSlide(isCanSlide);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mUsername = SharedPreUtils.getInstance().getString("username", "");
        try {
            if (!mUsername.equals("")) {
                UserBean userBean = UserHelper.getsInstance().findUserByName(mUsername);
                Glide.with(mContext).load(Constant.BASE_URL + userBean.getIcon())
                        .apply(new RequestOptions().transform(new CircleCrop()).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true))
                        .into(ivAvatar);
                tvDesc.setText(userBean.getBrief());
                tvSetting.setText("设置");
            } else {
                Glide.with(mContext).load(R.mipmap.avatar)
                        .apply(new RequestOptions().transform(new CircleCrop()).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true))
                        .into(ivAvatar);
                tvDesc.setText("未登录");
                tvSetting.setText("登录");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (resideLayout.isOpen()) {
            resideLayout.closePane();
        } else {
            long secondTime = System.currentTimeMillis();
            if (secondTime - fristTime < 2000) {
                finish();
            } else {
                SnackBarUtils.makeShort(getWindow().getDecorView(), "再点击一次退出应用").show();
                fristTime = System.currentTimeMillis();
            }
        }
    }

}
