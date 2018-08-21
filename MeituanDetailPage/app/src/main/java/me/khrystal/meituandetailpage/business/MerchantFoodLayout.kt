package me.khrystal.meituandetailpage.business

import android.content.Context
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
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

class FoodAdapter(data: List<MultiItemEntity>) : BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder>(data) {
    companion object {
        private var TYPE_PRODUCER = 1
        val TYPE_TOP = TYPE_PRODUCER++
        val TYPE_RECOMMEND = TYPE_PRODUCER++
        val TYPE_CONTENT_IMAGE = TYPE_PRODUCER++
        val TYPE_TITLE = TYPE_PRODUCER++
        val TYPE_CONTENT = TYPE_PRODUCER++
    }

    init {
    }

    override fun convert(helper: BaseViewHolder?, item: MultiItemEntity?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}