package me.khrystal.weyuereader.view.activity.impl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.OnClick;
import me.khrystal.weyuereader.R;
import me.khrystal.weyuereader.utils.BaseUtils;
import me.khrystal.weyuereader.utils.ToastUtils;
import me.khrystal.weyuereader.view.activity.IFeedBack;
import me.khrystal.weyuereader.view.base.BaseActivity;
import me.khrystal.weyuereader.viewmodel.activity.VMFeedBackInfo;
import me.khrystal.weyuereader.widget.MarqueTextView;
import me.khrystal.weyuereader.widget.theme.ColorRelativeLayout;
import me.khrystal.weyuereader.widget.theme.ColorView;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/5/8
 * update time:
 * email: 723526676@qq.com
 */

public class FeedBackActivity extends BaseActivity implements IFeedBack {


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
    @BindView(R.id.et_qq)
    EditText etQq;
    @BindView(R.id.et_feedback)
    EditText etFeedback;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    private VMFeedBackInfo mModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mModel = new VMFeedBackInfo(this, this);
        setBinddingView(R.layout.activity_feed_back, NO_BINDDING, mModel);
        initThemeToolBar("意见反馈");
    }

    @OnClick(R.id.btn_commit)
    public void onViewClicked() {
        if (TextUtils.isEmpty(etQq.getText())) {
            ToastUtils.show("请输入QQ号码");
            return;
        }
        if (TextUtils.isEmpty(etFeedback.getText())) {
            ToastUtils.show("请描述一下问题或者好的建议哟");
            return;
        }
        mModel.commitFeedBack(etQq.getText().toString(), etFeedback.getText().toString());
        BaseUtils.hideInput(etFeedback);
    }

    @Override
    public void feedBackSuccess() {
        etQq.setText("");
        etFeedback.setText("");
    }
}
