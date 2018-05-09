package me.khrystal.weyuereader.view.activity.impl;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.BlurTransformation;
import me.khrystal.weyuereader.R;
import me.khrystal.weyuereader.db.entity.UserBean;
import me.khrystal.weyuereader.db.helper.UserHelper;
import me.khrystal.weyuereader.model.BookBean;
import me.khrystal.weyuereader.utils.Constant;
import me.khrystal.weyuereader.utils.GsonUtils;
import me.khrystal.weyuereader.utils.LoadingHelper;
import me.khrystal.weyuereader.utils.LogUtils;
import me.khrystal.weyuereader.utils.SharedPreUtils;
import me.khrystal.weyuereader.utils.ThemeUtils;
import me.khrystal.weyuereader.utils.ToastUtils;
import me.khrystal.weyuereader.view.activity.IUserInfo;
import me.khrystal.weyuereader.view.base.BaseActivity;
import me.khrystal.weyuereader.viewmodel.activity.VMUserInfo;
import me.khrystal.weyuereader.widget.MarqueTextView;
import me.khrystal.weyuereader.widget.dialog.BookTagDialog;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/5/8
 * update time:
 * email: 723526676@qq.com
 */

public class UserInfoActivity extends BaseActivity implements IUserInfo, TakePhoto.TakeResultListener, InvokeListener {

    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.ll_tip)
    LinearLayout llTip;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.et_nickname)
    EditText etNickname;
    @BindView(R.id.tv_name)
    MarqueTextView tvName;
    @BindView(R.id.tv_brief)
    TextView tvBrief;
    @BindView(R.id.et_brief)
    EditText etBrief;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.tv_books)
    TextView tvBooks;
    @BindView(R.id.fl_book_name)
    TagFlowLayout flBookName;
    @BindView(R.id.tv_book_tags)
    TextView tvBookTags;
    @BindView(R.id.fl_book_type)
    TagFlowLayout flBookType;
    @BindView(R.id.cv_like)
    CardView cvLike;
    @BindView(R.id.fab_edit_password)
    FloatingActionButton fabEditPassword;
    @BindView(R.id.fab_edit_userinfo)
    FloatingActionButton fabEditUserinfo;
    @BindView(R.id.fab_menu)
    FloatingActionsMenu fabMenu;
    @BindView(R.id.cl_root)
    CoordinatorLayout clRoot;

    private TakePhoto takePhoto;
    private CropOptions cropOptions;
    private CompressConfig compressConfig; // 压缩参数
    private Uri imageUri; // 图片保存路径

    private InvokeParam invokeParam;
    private VMUserInfo mModel;
    private String mNewPassword;
    private String mUsername;
    private UserBean mUserBean;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        mModel = new VMUserInfo(mContext, this);
        setBinddingView(R.layout.activity_user_info, NO_BINDDING, mModel);
    }

    @Override
    protected void initView() {
        super.initView();
        stopEdit();
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        toolbarLayout.setTitle("用户信息");
        mUsername = SharedPreUtils.getInstance().getString("username", "");
        mUserBean = UserHelper.getsInstance().findUserByName(mUsername);

        mModel.getUserInfo();
        etBrief.setText(mUserBean.getBrief());
        tvName.setText(mUsername);
        etNickname.setText(mUserBean.getNickName());
        Glide.with(mContext).load(Constant.BASE_URL + mUserBean.getIcon())
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(15)).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true))
                .into(ivAvatar);

        // 获取TakePhoto实例
        takePhoto = getTakePhoto();
        // 设置裁剪参数
        cropOptions = new CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(true).create();
        // 设置压缩参数
        compressConfig = new CompressConfig.Builder().setMaxSize(50 * 1024).setMaxPixel(800).create();
        takePhoto.onEnableCompress(compressConfig, false); // 设置为需要压缩
    }

    /**
     * 开启用户信息编辑模式
     */
    private void startEdit() {
        etNickname.setFocusableInTouchMode(true);
        etNickname.setFocusable(true);
        etNickname.requestFocus();
        etBrief.setFocusableInTouchMode(true);
        etBrief.setFocusable(true);
        etBrief.requestFocus();
        tvName.setBackgroundColor(getResources().getColor(R.color.color_ccc));
        llTip.setVisibility(View.VISIBLE);
        btnConfirm.setVisibility(View.VISIBLE);
    }

    /**
     * 关闭用户信息编辑模式
     */
    private void stopEdit() {
        etNickname.setFocusable(false);
        etNickname.setFocusableInTouchMode(false);
        etBrief.setFocusable(false);
        etBrief.setFocusableInTouchMode(false);
        tvName.setBackgroundColor(getResources().getColor(R.color.transparent));
        llTip.setVisibility(View.GONE);
        btnConfirm.setVisibility(View.GONE);
    }

    @OnClick({R.id.fab_edit_password, R.id.fab_edit_userinfo, R.id.iv_avatar, R.id.btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fab_edit_password:
                fabMenu.toggle();
                new MaterialDialog.Builder(this)
                        .title("修改用户密码")
                        .inputRange(2, 20, ThemeUtils.getThemeColor())
//                        .inputType(InputType.TYPE_TEXT_VARIATION_PASSWORD)
                        .input("请输入新密码", null, (dialog, input) -> {
                            dialog.dismiss();
                            mModel.updatePassword(input.toString());
                        })
                        .show();
                break;
            case R.id.fab_edit_userinfo:
                fabMenu.toggle();
                startEdit();
                break;
            case R.id.iv_avatar:
                /**
                 * 设置内容区域为简单列表项
                 */
                final String[] items = {"相册", "拍摄"};
                new MaterialDialog.Builder(this)
                        .title("选择照片方式")
                        .items(items)
                        .itemsCallback((dialog, itemView, position, text) -> {
                            switch (position) {
                                case 0:
                                    dialog.dismiss();
                                    imageUri = getImageCropUri();
                                    //从相册中选取图片并裁剪
                                    takePhoto.onPickFromGalleryWithCrop(imageUri, cropOptions);
                                    //从相册中选取不裁剪
                                    //takePhoto.onPickFromGallery();
                                    break;
                                case 1:
                                    dialog.dismiss();
                                    imageUri = getImageCropUri();
                                    //拍照并裁剪
                                    takePhoto.onPickFromCaptureWithCrop(imageUri, cropOptions);
                                    //仅仅拍照不裁剪
                                    //takePhoto.onPickFromCapture(imageUri);
                                    break;
                            }
                        })
                        .show();
                break;
            case R.id.btn_confirm:
                new MaterialDialog.Builder(this)
                        .title("修改用户信息")
                        .content("是否确认修改?")
                        .negativeText("取消")
                        .onNegative((dialog, which) -> dialog.dismiss())
                        .positiveText("确定")
                        .onPositive((dialog, which) -> {
                            String nickname = etNickname.getText().toString();
                            String brief = etBrief.getText().toString();
                            if (TextUtils.isEmpty(nickname)) {
                                ToastUtils.show("昵称不能为空");
                                return;
                            }
                            if (TextUtils.isEmpty(brief)) {
                                ToastUtils.show("我的格言不能为空");
                                return;
                            }
                            stopEdit();
                            dialog.dismiss();
                            mModel.updateUserInfo(nickname, brief);
                        })
                        .show();
                break;
        }
    }

    /**
     * 获取TakePhoto实例
     *
     * @return
     */
    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return takePhoto;
    }

    //获得照片的输出保存Uri
    private Uri getImageCropUri() {
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        return Uri.fromFile(file);
    }

    @Override
    public void showLoading() {
        LoadingHelper.getInstance().showLoading(mContext);
    }

    @Override
    public void stopLoading() {
        LoadingHelper.getInstance().hideLoading();
    }

    @Override
    public void uploadSuccess(String imageUrl) {
        ToastUtils.show("更换图像成功");

        Glide.with(mContext).load(Constant.BASE_URL + imageUrl)
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(15)).diskCacheStrategy(DiskCacheStrategy.NONE))
                .into(ivAvatar);
    }

    @Override
    public void userInfo(UserBean userBean) {
        LogUtils.print("userbean", GsonUtils.toJson(userBean));
        List<String> likeBooks = new ArrayList<>();
        List<String> bookTags = new ArrayList<>();
        List<BookBean> likebooks = userBean.getLikebooks();
        if (likebooks.size() > 0) {
            cvLike.setVisibility(View.VISIBLE);
            for (BookBean bookBean : likebooks) {
                likeBooks.add(bookBean.getTitle());
                bookTags.addAll(bookBean.getTags());
            }
            // 喜欢的书籍
            flBookName.setAdapter(new TagAdapter<String>(likeBooks) {
                @Override
                public View getView(FlowLayout parent, int position, String s) {
                    TextView tv = (TextView) LayoutInflater.from(mContext).inflate(R.layout.tags_tv,
                            flBookName, false);
                    tv.setText(s);
                    return tv;
                }
            });

            flBookName.setOnTagClickListener((view, position, parent) -> {
                Intent intent = new Intent(mContext, BookDetailActivity.class);
                intent.putExtra("bookid", likebooks.get(position).get_id());
                startActivity(intent);
                return true;
            });
            //喜欢的书籍类型
            if (bookTags.size() > 0) {
                tvBookTags.setVisibility(View.VISIBLE);
                flBookType.setVisibility(View.VISIBLE);
                flBookType.setAdapter(new TagAdapter<String>(bookTags) {
                    @Override
                    public View getView(FlowLayout parent, int position, String s) {
                        TextView tv = (TextView) LayoutInflater.from(mContext).inflate(R.layout.tags_tv,
                                flBookType, false);
                        tv.setText(s);
                        return tv;
                    }
                });

                flBookType.setOnTagClickListener((view, position, parent) -> {
                    String tag = bookTags.get(position);
                    showTagDialog(tag);
                    return true;
                });
            } else {
                tvBookTags.setVisibility(View.GONE);
                flBookType.setVisibility(View.GONE);
            }
        } else {
            cvLike.setVisibility(View.GONE);
        }
    }

    private void showTagDialog(String tag) {
        BookTagDialog bookTagDialog = new BookTagDialog(mContext, tag);
        bookTagDialog.show();
        bookTagDialog.setOnDismissListener(dialog -> hideAnimator());

        long duration = 500;
        Display display = getWindowManager().getDefaultDisplay();
        float[] scale = new float[2];
        scale[0] = 1.0f;
        scale[1] = 0.8f;
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(clRoot, "scaleX", scale).setDuration(duration);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(clRoot, "scaleY", scale).setDuration(duration);

        float[] rotation = new float[]{0, 10, 0};
        ObjectAnimator rotationX = ObjectAnimator.ofFloat(clRoot, "rotationX", rotation).setDuration(duration);
        float[] translation = new float[1];
        translation[0] = -display.getWidth() * 0.2f / 2;
        ObjectAnimator translationY = ObjectAnimator.ofFloat(clRoot
                , "translationY", translation).setDuration(duration);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY, rotationX, translationY);
        animatorSet.setTarget(clRoot);
        animatorSet.start();
    }

    protected void hideAnimator() {
        long duration = 500;
        float[] scale = new float[2];
        scale[0] = 0.8f;
        scale[1] = 1.0f;
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(clRoot
                , "scaleX", scale).setDuration(duration);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(clRoot
                , "scaleY", scale).setDuration(duration);
        float[] rotation = new float[]{0, 10, 0};
        ObjectAnimator rotationX = ObjectAnimator.ofFloat(clRoot
                , "rotationX", rotation).setDuration(duration);

        float[] translation = new float[1];
        translation[0] = 0;
        ObjectAnimator translationY = ObjectAnimator.ofFloat(clRoot
                , "translationY", translation).setDuration(duration);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY, rotationX, translationY);
        animatorSet.setTarget(clRoot);
        animatorSet.start();
    }

    @Override
    public void takeSuccess(TResult result) {
        String mIconPath = result.getImage().getOriginalPath();
        mModel.uploadAvatar(mIconPath);
    }

    @Override
    public void takeFail(TResult result, String msg) {
        ToastUtils.show(msg);
    }

    @Override
    public void takeCancel() {

    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
