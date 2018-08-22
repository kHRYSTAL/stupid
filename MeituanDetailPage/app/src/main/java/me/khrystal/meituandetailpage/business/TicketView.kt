package me.khrystal.meituandetailpage.business

import android.animation.ValueAnimator
import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import kotlinx.android.synthetic.main.ticket_view.view.*
import me.khrystal.meituandetailpage.R
import me.khrystal.meituandetailpage.view.*
import me.khrystal.meituandetailpage.text

/**
 *
 * usage:
 * author: kHRYSTAL
 * create time: 18/8/22
 * update time:
 * email: 723526676@qq.com
 */
class TicketView(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs), AnimationUpdateListener {

    private var firstLayout: Boolean = false

    init {
        LayoutInflater.from(context).inflate(R.layout.ticket_view, this)
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        if (!firstLayout) {
            firstLayout = true
            vBorder1.stateSave(R.id.vs1).a(1F)
            vBorder1.stateSave(R.id.vs2).ws(3.8F).a(0F)
            vBorder2.stateSave(R.id.vs1).a(0F)
            vBorder2.stateSave(R.id.vs2).ws(3.8F).a(1F)
            vSimple.stateSave(R.id.vs1)
            vSimple.stateSave(R.id.vs2).a(0F)
            layDetail.stateSave(R.id.vs1)
            layDetail.stateSave(R.id.vs2).sx(1F).sy(1F).a(1F)
        }
    }

    fun set(amount: Int, limit: Int, expireTime: String) {
        vSimple.text("领￥$amount")
        vDetail1.text("￥$amount")
        vDetail2.text("满$limit 可用")
        vDetail3.text("有效期至$expireTime")
    }

    override fun onAnimationUpdate(tag1: Int, tag2: Int, p: Float) {
        arrayOf(vBorder1, vBorder2, vSimple, layDetail).forEach { it.stateRefresh(tag1, tag2, p) }
    }
}