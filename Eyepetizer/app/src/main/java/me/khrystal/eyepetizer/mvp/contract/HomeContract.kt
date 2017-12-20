package me.khrystal.eyepetizer.mvp.contract

import me.khrystal.eyepetizer.base.BasePresenter
import me.khrystal.eyepetizer.base.BaseView
import me.khrystal.eyepetizer.mvp.model.bean.HomeBean

/**
 *
 * usage:
 * author: kHRYSTAL
 * create time: 17/12/19
 * update time:
 * email: 723526676@qq.com
 */
interface HomeContract{
    interface View : BaseView<Presenter> {
        fun setData(bean : HomeBean)
    }
    interface Presenter : BasePresenter {
        fun requestData()
    }
}