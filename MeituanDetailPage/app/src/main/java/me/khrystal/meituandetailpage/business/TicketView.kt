package me.khrystal.meituandetailpage.business

import android.animation.Animator
import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import kotlinx.android.synthetic.main.ticket_view.view.*
import me.khrystal.meituandetailpage.R

/**
 *
 * usage:
 * author: kHRYSTAL
 * create time: 18/8/22
 * update time:
 * email: 723526676@qq.com
 */
class TicketView(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs), Animator.AnimatorListener {

    private var firstLayout: Boolean = false

    init {
        LayoutInflater.from(context).inflate(R.layout.ticket_view, this)
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        if (!firstLayout) {
            firstLayout = true
//            TODO()
        }
    }

    override fun onAnimationCancel(p0: Animator?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onAnimationEnd(p0: Animator?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onAnimationStart(p0: Animator?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onAnimationRepeat(p0: Animator?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}