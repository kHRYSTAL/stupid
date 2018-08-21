package me.khrystal.meituandetailpage.business

import android.content.Context
import android.view.LayoutInflater
import android.widget.FrameLayout
import me.khrystal.meituandetailpage.R

/**
 *
 * usage: 店家的商品列表布局
 * author: kHRYSTAL
 * create time: 18/8/20
 * update time:
 * email: 723526676@qq.com
 */
class MerchantFoodLayout(context: Context) : FrameLayout(context) {

    var topHeight: Int = 0
    var recommendHeight: Int = 0

    init {
        LayoutInflater.from(context).inflate(R.layout.merchant_page_food_layout, this)
        initialData()
    }

    private fun initialData() {
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
    }

}