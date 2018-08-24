package me.khrystal.meituandetailpage.business

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.merchant_page_food_layout.view.*
import me.khrystal.meituandetailpage.R

/**
 *
 * usage:
 * author: kHRYSTAL
 * create time: 18/8/24
 * update time:
 * email: 723526676@qq.com
 */
class MerchantInfoLayout(context: Context) : FrameLayout(context), ScrollableViewProvider {

    override fun getScrollableView(): View {
        return vRecycler
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.merchant_food_list_recommend_cell, this)
        initialData()
    }

    private fun initialData() {

    }

}