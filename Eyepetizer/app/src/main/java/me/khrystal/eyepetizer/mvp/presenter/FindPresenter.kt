package me.khrystal.eyepetizer.mvp.presenter

import android.content.Context
import io.reactivex.Observable
import me.khrystal.eyepetizer.mvp.contract.FindContract
import me.khrystal.eyepetizer.mvp.model.FindModel
import me.khrystal.eyepetizer.mvp.model.bean.FindBean
import me.khrystal.eyepetizer.utils.applySchedulers

/**
 *
 * usage:
 * author: kHRYSTAL
 * create time: 17/12/20
 * update time:
 * email: 723526676@qq.com
 */
class FindPresenter(context: Context, view : FindContract.View) : FindContract.Presenter{
    var mContext : Context? = null
    var mView : FindContract.View? = null
    val mModel : FindModel by lazy {
        FindModel()
    }
    init {
        mView = view
        mContext = context
    }
    override fun start() {
        requestData()
    }

    override fun requestData() {
        val observable : Observable<MutableList<FindBean>>? = mContext?.let { mModel.loadData(mContext!!) }
        observable?.applySchedulers()?.subscribe { beans : MutableList<FindBean> ->
            mView?.setData(beans)
        }
    }



}