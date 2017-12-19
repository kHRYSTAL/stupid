package me.khrystal.eyepetizer.base

/**
 *
 * usage:
 * author: kHRYSTAL
 * create time: 17/12/19
 * update time:
 * email: 723526676@qq.com
 */
// 协变 只支持输入 不支持输出 与 ？extends T 等同
interface BaseView<in T> {}