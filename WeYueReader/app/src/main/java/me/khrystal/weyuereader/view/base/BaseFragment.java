package me.khrystal.weyuereader.view.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import me.khrystal.weyuereader.utils.LogUtils;
import me.khrystal.weyuereader.viewmodel.BaseViewModel;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 18/4/26
 * update time:
 * email: 723526676@qq.com
 */

public class BaseFragment extends Fragment {
    private String TAG;
    protected BaseViewModel mModel;
    protected Context mContext;
    private View mBindView;
    private View mView;
    protected CompositeDisposable mDisposable;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    /**
     * Databinding设置布局绑定
     */
    public View setBinddingView(LayoutInflater inflater, ViewGroup container,
                                @LayoutRes int resId, int brVariavle, BaseViewModel mModel) {
        if (mBindView == null) {
            TAG = this.getClass().getSimpleName();
            ViewDataBinding dataBinding = DataBindingUtil.inflate(inflater, resId, container, false);
            dataBinding.setVariable(brVariavle, mModel);
            mBindView = dataBinding.getRoot();
            ButterKnife.bind(this, mBindView);
            this.mModel = mModel;
        }
        return mBindView;
    }

    public View setContentView(ViewGroup container, @LayoutRes int resId, BaseViewModel mModel) {
        if (mView == null) {
            TAG = this.getClass().getSimpleName();
            mView = LayoutInflater.from(getActivity()).inflate(resId, container, false);
            ButterKnife.bind(this, mView);
            this.mModel = mModel;
            initView();
        }
        return mView;
    }

    public void initView() {

    }

    protected void addDisposable(Disposable d) {
        if (mDisposable == null) {
            mDisposable = new CompositeDisposable();
        }
        mDisposable.add(d);
    }

    public void startActivity(Class<? extends Activity> className) {
        Intent intent = new Intent(mContext, className);
        startActivity(intent);
    }

    public void startActivity(Class<? extends Activity> className, Bundle bundle) {
        Intent intent = new Intent(mContext, className);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mModel != null) {
            LogUtils.d(TAG, "onDestroy");
            mModel.onDestroy();
        }
    }
}
