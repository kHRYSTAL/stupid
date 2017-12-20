package me.khrystal.eyepetizer.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import kotlinx.android.synthetic.main.hot_fragment.*
import me.khrystal.eyepetizer.R
import me.khrystal.eyepetizer.adapter.HotAdapter

/**
 *
 * usage:
 * author: kHRYSTAL
 * create time: 17/12/20
 * update time:
 * email: 723526676@qq.com
 */
class HotFragment : BaseFragment() {
    var mTabs = listOf<String>("周排行", "月排行", "总排行").toMutableList()
    lateinit var mFragments: ArrayList<Fragment>
    val STRATEGY = arrayOf("weekly", "monthly", "historical")
    override fun getLayoutResources(): Int {
        return R.layout.hot_fragment
    }

    override fun initView() {
        var weekFragment: RankFragment = RankFragment()
        var weekBundle = Bundle()
        weekBundle.putString("strategy", STRATEGY[0])
        weekFragment.arguments = weekBundle
        var monthFragment: RankFragment = RankFragment()
        var monthBundle = Bundle()
        monthBundle.putString("strategy", STRATEGY[1])
        monthFragment.arguments = monthBundle
        var allFragment: RankFragment = RankFragment()
        var allBundle = Bundle()
        allBundle.putString("strategy", STRATEGY[2])
        allFragment.arguments = allBundle
        mFragments = ArrayList()
        mFragments.add(weekFragment as Fragment)
        mFragments.add(monthFragment as Fragment)
        mFragments.add(allFragment as Fragment)
        vp_content.adapter = HotAdapter(fragmentManager, mFragments, mTabs)
        tabs.setupWithViewPager(vp_content)
    }

}