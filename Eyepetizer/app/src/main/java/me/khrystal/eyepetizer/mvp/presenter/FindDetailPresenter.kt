package me.khrystal.eyepetizer.mvp.presenter

import android.content.Context
import io.reactivex.Observable
import me.khrystal.eyepetizer.mvp.contract.FindDetailContract
import me.khrystal.eyepetizer.mvp.model.FindDetailModel
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
class FindDetailPresenter(context: Context, view: FindDetailContract.View) : FindDetailContract.Presenter {

    var mContext: Context? = null
    var mView: FindDetailContract.View? = null
    val mModel: FindDetailModel by lazy {
        FindDetailModel()
    }

    init {
        mView = view
        mContext = context
    }

    override fun start() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun requestData(categoryName: String, strategy: String) {
        val observable: Observable<HotBean>? = mContext?.let { mModel.loadData(mContext!!, categoryName, strategy) }
        observable?.applySchedulers()?.subscribe { bean: HotBean ->
            mView?.setData(bean)
        }
    }

    override fun requestMoreData(start: Int, categoryName: String, strategy: String) {val observable: Observable<HotBean>? = mContext?.let { mModel.loadMoreData(mContext!!, start, categoryName, strategy) }
        observable?.applySchedulers()?.subscribe { bean: HotBean ->
            mView?.setData(bean)
        }
    }

}