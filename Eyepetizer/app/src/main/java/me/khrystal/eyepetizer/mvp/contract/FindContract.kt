package me.khrystal.eyepetizer.mvp.contract

import me.khrystal.eyepetizer.base.BasePresenter
import me.khrystal.eyepetizer.base.BaseView
import me.khrystal.eyepetizer.mvp.model.bean.FindBean

/**
 *
 * usage:
 * author: kHRYSTAL
 * create time: 17/12/19
 * update time:
 * email: 723526676@qq.com
 */
interface FindContract {
    interface View: BaseView<Presenter> {
        fun setData(beans: MutableList<FindBean>)
    }

    interface Presenter : BasePresenter {
        fun requestData()
    }
}