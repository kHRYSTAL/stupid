package me.khrystal.weyuereader.view.activity.impl;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lcodecore.extextview.ExpandTextView;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import me.khrystal.weyuereader.R;
import me.khrystal.weyuereader.db.entity.CollBookBean;
import me.khrystal.weyuereader.db.helper.CollBookHelper;
import me.khrystal.weyuereader.model.BookBean;
import me.khrystal.weyuereader.utils.BaseUtils;
import me.khrystal.weyuereader.utils.Constant;
import me.khrystal.weyuereader.utils.LoadingHelper;
import me.khrystal.weyuereader.utils.ToastUtils;
import me.khrystal.weyuereader.view.activity.IBookDetail;
import me.khrystal.weyuereader.view.base.BaseActivity;
import me.khrystal.weyuereader.viewmodel.activity.VMBookDetailInfo;
import me.khrystal.weyuereader.widget.MarqueTextView;
import me.khrystal.weyuereader.widget.dialog.BookTagDialog;
import me.khrystal.weyuereader.widget.theme.ColorRelativeLayout;
import me.khrystal.weyuereader.widget.theme.ColorTextView;
import me.khrystal.weyuereader.widget.theme.ColorView;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/5/7
 * update time:
 * email: 723526676@qq.com
 */

public class BookDetailActivity extends BaseActivity implements IBookDetail {

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
    @BindView(R.id.iv_book_image)
    ImageView ivBookImage;
    @BindView(R.id.tv_book_name)
    TextView tvBookName;
    @BindView(R.id.ctv_book_author)
    ColorTextView ctvBookAuthor;
    @BindView(R.id.tv_book_classify)
    TextView tvBookClassify;
    @BindView(R.id.tv_word_updatetime)
    TextView tvWordUpdatetime;
    @BindView(R.id.ctv_score)
    ColorTextView ctvScore;
    @BindView(R.id.tv_evaluate)
    TextView tvEvaluate;
    @BindView(R.id.tv_fow_num)
    TextView tvFowNum;
    @BindView(R.id.tv_good_num)
    TextView tvGoodNum;
    @BindView(R.id.tv_word_count)
    TextView tvWordCount;
    @BindView(R.id.tv_book_brief)
    ExpandTextView tvBookBrief;
    @BindView(R.id.ll_tag)
    LinearLayout llTag;
    @BindView(R.id.fl_tags)
    TagFlowLayout flTags;
    @BindView(R.id.ll_book_copyright)
    LinearLayout llBookCopyright;
    @BindView(R.id.tv_copyright)
    TextView tvCopyright;
    @BindView(R.id.ctv_addbook)
    ColorTextView ctvAddbook;
    @BindView(R.id.ll_fow)
    LinearLayout llFow;
    @BindView(R.id.tv_read)
    TextView tvRead;
    @BindView(R.id.crl_start_read)
    ColorRelativeLayout crlStartRead;
    @BindView(R.id.rl_rootview)
    RelativeLayout rlRootview;
    private CollBookBean mCollBookBean;
    private BookBean mBookBean;
    private VMBookDetailInfo mModel;
    private String mBookId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mModel = new VMBookDetailInfo(mContext, this);
        setBinddingView(R.layout.activity_book_detail, NO_BINDDING, mModel);
        mBookId = getIntent().getStringExtra("bookid");
        mModel.bookInfo(mBookId);
    }

    private void init() {
        initThemeToolBar(mBookBean.getTitle());
        Glide.with(mContext)
                .load(Constant.ZHUISHU_IMAGE_URL + mBookBean.getCover())
                .into(ivBookImage);
        tvBookName.setText(mBookBean.getTitle());
        ctvBookAuthor.setText(mBookBean.getAuthor());
        tvBookClassify.setText("  |  " + mBookBean.getMajorCate());
        tvWordCount.setText(mBookBean.getSerializeWordCount() + "");
        tvFowNum.setText(mBookBean.getLatelyFollower() + "");
        tvGoodNum.setText(mBookBean.getRetentionRatio() + "%");
        tvBookBrief.setText(mBookBean.getLongIntro());
        if (mBookBean.getTags().size() > 0) {
            llTag.setVisibility(View.VISIBLE);
            flTags.setAdapter(new TagAdapter<String>(mBookBean.getTags()) {
                @Override
                public View getView(FlowLayout parent, int position, String s) {
                    TextView tv = (TextView) LayoutInflater.from(mContext).inflate(R.layout.tags_tv,
                            flTags, false);
                    tv.setText(s);
                    return tv;
                }
            });

            flTags.setOnTagClickListener((view, position, parent) -> {
                String tag = mBookBean.getTags().get(position);
                showTagDialog(tag);
                return true;
            });

        } else {
            llTag.setVisibility(View.GONE);
            flTags.setVisibility(View.GONE);
        }
        String wordCount = mBookBean.getWordCount() / 10000 > 0 ? mBookBean.getWordCount() / 10000 + "万字" : mBookBean.getWordCount() + "字";

        if (mBookBean.getRating() != null) {
            ctvScore.setText(BaseUtils.format1Digits(mBookBean.getRating().getScore()));
            tvEvaluate.setText(mBookBean.getRating().getCount() + "人评");
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");//注意格式化的表达式
        try {
            Date d = format.parse(mBookBean.getUpdated().replace("Z", " UTC"));//注意是空格+UTC
            Date nowDate = new Date();
            int day = (int) ((nowDate.getTime() - d.getTime()) / (1000 * 3600 * 24));
            int hour = (int) ((nowDate.getTime() - d.getTime()) / (1000 * 3600));
            String time = day > 0 ? day + "天前" : hour + "小时前";
            tvWordUpdatetime.setText(wordCount + "  |  " + time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //设置书籍
        mCollBookBean = CollBookHelper.getsInstance().findBookById(mBookBean.get_id());

        if (mBookBean.isCollect()) {
            ctvAddbook.setText("移除书架");
        } else {
            ctvAddbook.setText("加入书架");
        }

        if (mCollBookBean == null) {
            mCollBookBean = mBookBean.getCollBookBean();
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
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(rlRootview, "scaleX", scale).setDuration(duration);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(rlRootview, "scaleY", scale).setDuration(duration);
        float[] rotation = new float[]{0, 10, 0};
        ObjectAnimator rotationX = ObjectAnimator.ofFloat(rlRootview, "rotationX", rotation).setDuration(duration);

        float[] translation = new float[1];
        translation[0] = -display.getWidth() * 0.2f / 2;
        ObjectAnimator translationY = ObjectAnimator.ofFloat(rlRootview, "translationY", translation).setDuration(duration);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY, rotationX, translationY);
        animatorSet.setTarget(rlRootview);
        animatorSet.start();
    }

    protected void hideAnimator() {
        long duration = 500;
        float[] scale = new float[2];
        scale[0] = 0.8f;
        scale[1] = 1.0f;
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(rlRootview, "scaleX", scale).setDuration(duration);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(rlRootview, "scaleY", scale).setDuration(duration);
        float[] rotation = new float[]{0, 10, 0};
        ObjectAnimator rotationX = ObjectAnimator.ofFloat(rlRootview, "rotationX", rotation).setDuration(duration);

        float[] translation = new float[1];
        translation[0] = 0;
        ObjectAnimator translationY = ObjectAnimator.ofFloat(rlRootview, "translationY", translation).setDuration(duration);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY, rotationX, translationY);
        animatorSet.setTarget(rlRootview);
        animatorSet.start();
    }

    @OnClick({R.id.ll_fow, R.id.crl_start_read})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_fow:
                String collectStatus = ctvAddbook.getText().toString();
                if (collectStatus.equals("移除书架")) {
                    ctvAddbook.setText("加入书架");
                    mModel.deleteBookShelfToServer(mCollBookBean);
                } else {
                    ctvAddbook.setText("移除书架");
                    mModel.addBookShelf(mCollBookBean);
                }
                break;
            case R.id.crl_start_read:
                Bundle bundle = new Bundle();
                bundle.putSerializable(ReadActivity.EXTRA_COLL_BOOK, mCollBookBean);
                bundle.putBoolean(ReadActivity.EXTRA_IS_COLLECTED, false);
                startActivity(ReadActivity.class, bundle);
                break;
        }
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
    public void addBookCallback() {
        ToastUtils.show("加入书架成功");
    }

    @Override
    public void getBookInfo(BookBean bookBean) {
        mBookBean = bookBean;
        init();
    }
}
