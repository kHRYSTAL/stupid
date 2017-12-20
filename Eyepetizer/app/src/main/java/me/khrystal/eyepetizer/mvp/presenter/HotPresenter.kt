package me.khrystal.eyepetizer.mvp.presenter

import android.content.Context
import io.reactivex.Observable
import me.khrystal.eyepetizer.mvp.contract.HotContract
import me.khrystal.eyepetizer.mvp.model.HotModel
import me.khrystal.eyepetizer.mvp.model.bean.HotBean
import me.khrystal.eyepetizer.utils.applySchedulers

/**
 *
 * usage:
 * author: kHRYSTAL
 * create time: 17/12/20
 * update time:
 * email: 723526676@qq.com
 */
class HotPresenter(context: Context, view: HotContract.View) : HotContract.Presenter{


    var mContext : Context? = null
    var mView : HotContract.View? = null
    val mModel : HotModel by lazy {
        HotModel()
    }
    init {
        mView = view
        mContext = context
    }
    override fun start() {

    }
    override fun requestData(strategy: String) {
        val observable : Observable<HotBean>? = mContext?.let { mModel.loadData(mContext!!,strategy) }
        observable?.applySchedulers()?.subscribe { bean : HotBean ->
            mView?.setData(bean)
        }
    }

}
