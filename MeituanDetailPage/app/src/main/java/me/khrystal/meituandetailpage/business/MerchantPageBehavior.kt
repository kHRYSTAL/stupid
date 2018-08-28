package me.khrystal.meituandetailpage.business

import android.animation.Animator
import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet
import android.widget.Scroller
import me.khrystal.meituandetailpage.dp
import me.khrystal.meituandetailpage.view.ViewPager2

/**
 *
 * usage:
 * author: kHRYSTAL
 * create time: 18/8/28
 * update time:
 * email: 723526676@qq.com
 */
class MerchantPageBehavior(context: Context, attrs: AttributeSet) : CoordinatorLayout.Behavior<MerchantPageLayout>(context, attrs) {
    private lateinit var selfView: MerchantPageLayout
    private lateinit var layTitle: MerchantTitleLayout // 商店标题
    private lateinit var vPager: ViewPager2 // 商品菜单所在的pager
    private lateinit var layContent: MerchantContentLayout // 商店详情
    private lateinit var laySettle: MerchantSettleLayout
    private val pagingTouchSlop = dp(5)
    private var horizontalPagingTouch = 0 // 菜单横向列表(推荐商品) 内容的触摸滑动距离
    private var isScrollRecommend = false
    private var simpleTopDistance = 0
    private var isScrollToFullFood = false // 上滑显示商品菜单
    private var isScrollToHideFood = false // 下滑显示商店详情
    private val scroller = Scroller(context)
    private val scrollDuration = 800
    private val handler = android.os.Handler()
    private val flingRunnable = object : Runnable {
        override fun run() {
            if (scroller.computeScrollOffset()) {
                selfView.translationY = scroller.currY.toFloat()
                layContent.effectByOffset(selfView.translationY)
                laySettle.effectByOffset(selfView.translationY)
                handler.post(this)
            } else {
                isScrollToHideFood = false
            }
        }
    }

    private val mAnimListener = object : MerchantContentLayout.AnimatorListenerAdapter1 {
        override fun onAnimationStart(animation: Animator?, toExpanded: Boolean) {
            if (toExpanded) {
                val defaultDisplayHeight = (selfView.height - simpleTopDistance)
                scroller.startScroll(0, selfView.translationY.toInt(), 0,
                        (defaultDisplayHeight - selfView.translationY).toInt(), scrollDuration)
            } else {
                scroller.startScroll(0, selfView.translationY.toInt(), 0, (-selfView.translationY).toInt(), scrollDuration)
            }
            handler.post(flingRunnable)
            isScrollToHideFood = true
        }
    }


}