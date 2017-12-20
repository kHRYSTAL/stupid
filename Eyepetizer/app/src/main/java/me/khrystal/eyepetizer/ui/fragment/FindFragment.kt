package me.khrystal.eyepetizer.ui.fragment

import android.content.Intent
import kotlinx.android.synthetic.main.find_fragment.*
import me.khrystal.eyepetizer.R
import me.khrystal.eyepetizer.adapter.FindAdapter
import me.khrystal.eyepetizer.mvp.model.bean.FindBean
import me.khrystal.eyepetizer.mvp.contract.FindContract
import me.khrystal.eyepetizer.mvp.presenter.FindPresenter
import me.khrystal.eyepetizer.ui.FindDetailActivity

/**
 *
 * usage:
 * author: kHRYSTAL
 * create time: 17/12/19
 * update time:
 * email: 723526676@qq.com
 */
class FindFragment : BaseFragment(), FindContract.View {

    var mPresenter: FindPresenter? = null
    var mAdapter: FindAdapter? = null
    var mList : MutableList<FindBean>? = null

    override fun setData(beans: MutableList<FindBean>) {
        mAdapter?.mList = beans
        mList = beans
        mAdapter?.notifyDataSetChanged()
    }

    override fun getLayoutResources(): Int {
        return R.layout.find_fragment
    }

    override fun initView() {
        mPresenter = FindPresenter(context, this)
        mAdapter = FindAdapter(context, mList)
        mPresenter?.start()
        gv_find.adapter = mAdapter
        gv_find.setOnItemClickListener { parent, view, position, id ->
            var bean = mList?.get(position)
            var name = bean?.name
            TODO("111")
            var intent : Intent = Intent(context, FindDetailActivity::class.java)
            intent.putExtra("name",name)
            startActivity(intent)

        }
    }

}