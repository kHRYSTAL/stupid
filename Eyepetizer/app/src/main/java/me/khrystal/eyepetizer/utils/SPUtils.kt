package me.khrystal.eyepetizer.utils

import android.content.Context
import android.content.SharedPreferences
import java.util.*

/**
 *
 * usage: JvmOverloads 自动重载函数 通常用于定义默认值 且可以修改默认值
 * author: kHRYSTAL
 * create time: 17/12/18
 * update time:
 * email: 723526676@qq.com
 */
class SPUtils private constructor(context: Context, spName: String) {

    private val sp: SharedPreferences

    init {
        sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE)
    }

    fun put(key: String, value: String) {
        sp.edit().putString(key, value).apply()
    }

    /**
     * 如getString 函数 调用时 可以为getString(key, defaultValue)
     * 也可以为getString(key)
     */
    @JvmOverloads fun getString(key: String, defaultValue: String = ""): String {
        return sp.getString(key, defaultValue)
    }

    fun put(key: String, value: Int) {
        sp.edit().putInt(key, value).apply()
    }

    @JvmOverloads fun getInt(key: String, defaultValue: Int = -1): Int {
        return sp.getInt(key, defaultValue)
    }

    fun put(key: String, value: Long) {
        sp.edit().putLong(key, value).apply()
    }

    @JvmOverloads fun getLong(key: String, defaultValue: Long = -1L): Long {
        return sp.getLong(key, defaultValue)
    }

    fun put(key: String, value: Float) {
        sp.edit().putFloat(key, value).apply()
    }

    @JvmOverloads fun getFloat(key: String, defaultValue: Float = -1f): Float {
        return sp.getFloat(key, defaultValue)
    }

    fun put(key: String, value: Boolean) {
        sp.edit().putBoolean(key, value).apply()
    }

    @JvmOverloads fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return sp.getBoolean(key, defaultValue)
    }

    fun put(key: String, values: Set<String>) {
        sp.edit().putStringSet(key, values).apply()
    }

    @JvmOverloads fun getStringSet(key: String, defaultValue: Set<String> = Collections.emptySet()): Set<String> {
        return sp.getStringSet(key, defaultValue)
    }

    /**
     * SP中获取所有键值对
     * 变量为all 当调用xxx = ObjectSaveUtils.all时 自动调用这个变量的get函数
     * @return Map对象
     */
    val all: Map<String, *>
        get() = sp.all

    fun remove(key: String) {
        sp.edit().remove(key).apply()
    }

    /**
     * 重载操作符 contains方法
     */
    operator fun contains(key: String): Boolean {
        return sp.contains(key)
    }

    fun clear() {
        sp.edit().clear().apply()
    }

    /**
     * 当类需要单例且构造函数不为空时 需要使用伴生函数去构造单例
     */
    companion object {

        private val sSPMap = HashMap<String,SPUtils>()

        /**
         * 获取SP实例
         * @param spName sp名
         * *
         * @return [SPUtils]
         */
        fun getInstance(context: Context,spName: String): SPUtils {
            var spName = spName
            if (isSpace(spName)) spName = "spUtils"
            var sp: SPUtils? = sSPMap[spName]
            if (sp == null) {
                sp = SPUtils(context,spName)
                sSPMap.put(spName, sp)
            }
            return sp
        }

        private fun isSpace(s: String?): Boolean {
            if (s == null) return true
            var i = 0
            val len = s.length
            while (i < len) {
                if (!Character.isWhitespace(s[i])) {
                    return false
                }
                ++i
            }
            return true
        }
    }
}