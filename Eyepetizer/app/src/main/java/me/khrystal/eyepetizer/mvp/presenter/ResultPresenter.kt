package me.khrystal.eyepetizer.mvp.presenter

import android.content.Context
import io.reactivex.Observable
import me.khrystal.eyepetizer.mvp.contract.ResultContract
import me.khrystal.eyepetizer.mvp.model.ResultModel
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
class ResultPresenter(context: Context, view: ResultContract.View) : ResultContract.Presenter {


    var mContext: Context? = null
    var mView: ResultContract.View? = null
    val mModel: ResultModel by lazy {
        ResultModel()
    }

    init {
        mView = view
        mContext = context
    }

    override fun start() {

    }

    override fun requestData(query: String, start: Int) {
        val observable: Observable<HotBean>? = mContext?.let { mModel.loadData(mContext!!, query, start) }
        observable?.applySchedulers()?.subscribe { bean: HotBean ->
            mView?.setData(bean)
        }
    }

}