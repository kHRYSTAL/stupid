package me.khrystal.meituandetailpage

/**
 *
 * usage:
 * author: kHRYSTAL
 * create time: 18/8/20
 * update time:
 * email: 723526676@qq.com
 */
class BusEvent {
    var act: Int = 0
    // 类型后加? 表示可以为空 不加表示不能为空
    var obj: Any? = null
    var obj2: Any? = null
    var obj3: Any? = null

    constructor(a: Int) {
        act = a
    }

    constructor(a: Int, o: Any) : this(a) {
        obj = o
    }

    constructor(a: Int, o: Any, o2: Any): this(a, o) {
        obj2 = o2
    }

    constructor(a: Int, o: Any, o2: Any, o3: Any): this(a, o, o2) {
        obj3 = o3
    }
}