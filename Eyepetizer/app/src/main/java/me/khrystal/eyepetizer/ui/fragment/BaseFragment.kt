package me.khrystal.eyepetizer.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 *
 * usage:
 * author: kHRYSTAL
 * create time: 17/12/19
 * update time:
 * email: 723526676@qq.com
 */
abstract class BaseFragment : Fragment() {
    var isFirst: Boolean = false
    var rootView: View? = null
    var isFragmentVisiable: Boolean = false
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (rootView == null) {
            rootView = inflater?.inflate(getLayoutResources(), container, false)
        }
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            isFragmentVisiable = true
        }
        if (rootView == null) {
            return
        }
        //可见，并且没有加载过
        if (!isFirst && isFragmentVisiable) {
            onFragmentVisibleChange(true)
            return;
        }
        //由可见——>不可见 已经加载过
        if (isFragmentVisiable) {
            onFragmentVisibleChange(false)
            isFragmentVisiable = false
        }
    }

    open protected fun onFragmentVisibleChange(b: Boolean) {

    }


    abstract fun getLayoutResources(): Int

    abstract fun initView()
}